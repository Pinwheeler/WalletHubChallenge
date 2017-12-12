import Models.BlockedIP;
import Models.Duration;
import Models.Record;
import sun.jvm.hotspot.opto.Block;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecordService {

    public static String SQL_INSERT = "INSERT INTO records (date, ipAddress, httpMethod, responseStatus, userAgent) VALUES(?,?,?,?,?);";
    public static String SQL_SELECT_TIME_BOUNDED_LIMITED = "SELECT ipAddress, COUNT(*) as count FROM records WHERE id IN (\n" +
            "\tSELECT id FROM records WHERE (records.`date` > ? AND records.`date` < ?)\n" +
            ") GROUP BY ipAddress\n" +
            "HAVING count > ?;";

    private Connection connection;

    RecordService(Connection connection) {
        this.connection = connection;
    }

    void persist(List<Record> recordList) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT);
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
                String comment = String.format("Requests Made: %d, Threshold: %d, Interval Start: %s, Interval End: %s",
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
