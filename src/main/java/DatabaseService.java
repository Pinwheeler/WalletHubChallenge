import Models.BlockedIP;
import Models.Duration;
import Models.Record;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatabaseService {

    public static String SQL_DELETE_ALL_RECORDS = "TRUNCATE TABLE records";
    public static String SQL_INSERT_RECORDS = "INSERT INTO records (date, ipAddress, httpMethod, responseStatus, userAgent) VALUES(?,?,?,?,?);";
    public static String SQL_SELECT_TIME_BOUNDED_LIMITED = "SELECT ipAddress, COUNT(*) as count FROM records WHERE id IN (\n" +
            "\tSELECT id FROM records WHERE (records.`date` > ? AND records.`date` < ?)\n" +
            ") GROUP BY ipAddress\n" +
            "HAVING count > ?;";

    public static String SQL_INSERT_BLOCKED_IPS = "REPLACE INTO blockedIPs (address, `comment`) values (?, ?)";

    private Connection connection;

    DatabaseService(Connection connection) {
        this.connection = connection;
    }

    void persistRecords(List<Record> recordList) {
        try {
            // Delete all records before rebuilding the table (this is as opposed to creating some kind of
            // hash for the records to avoid duplication. It is considerably faster and works considering we
            // only have one logfile from which we are drawing our information
            PreparedStatement delete = connection.prepareStatement(SQL_DELETE_ALL_RECORDS);
            delete.execute();
            connection.commit();

            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_RECORDS);
            int i = 0;

            for (Record record: recordList) {
                statement.setObject(1, record.date);
                statement.setString(2, record.ipAddress);
                statement.setString(3, record.httpMethod);
                statement.setInt(4, record.responseStatus);
                statement.setString(5, record.userAgent);

                statement.addBatch();
                i++;

                if (i % 1000 == 0 || i == recordList.size()) {
                    statement.executeBatch();
                }
            }

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void persistBlockedIPs(List<BlockedIP> blockedIPList) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_BLOCKED_IPS);
            int i = 0;

            for (BlockedIP blockedIP: blockedIPList) {
                statement.setString(1, blockedIP.ipAddress);
                statement.setString(2, blockedIP.comments);

                statement.addBatch();
                i++;

                if (i % 1000 == 0 || i == blockedIPList.size()) {
                    statement.executeBatch();
                }
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<BlockedIP> findViolatingIPAddresses(Date startDate, Duration duration, int threshold) {
        ArrayList<BlockedIP> blockedIPs = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT_TIME_BOUNDED_LIMITED);
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            switch (duration) {
                case daily:
                    cal.add(Calendar.DATE, 1);
                    break;
                case hourly:
                    cal.add(Calendar.HOUR, 1);
                    break;
            }
            Date endDate = cal.getTime();

            statement.setObject(1, startDate);
            statement.setObject(2, endDate);
            statement.setInt(3, threshold);
            ResultSet ips = statement.executeQuery();
            connection.commit();

            while (ips.next()) {
                String address = ips.getString("ipAddress");
                int count = ips.getInt("count");
                String comment = String.format("| Requests Made: %d | Threshold: %d | Interval Start: %s | Interval End: %s |",
                        count,
                        threshold,
                        startDate,
                        endDate);
                BlockedIP ip = new BlockedIP(address, comment);
                blockedIPs.add(ip);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return blockedIPs;
    }
}
