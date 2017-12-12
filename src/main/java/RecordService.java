import Models.BlockedIP;
import Models.Duration;
import Models.Record;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class RecordService {

    public static String SQL_INSERT = "INSERT INTO records (date, ipAddress, httpMethod, responseStatus, userAgent) VALUES(?,?,?,?,?);";
    public static String SQL_SELECT_TIME_BOUNDED_LIMITED = "SELECT ipAddress, COUNT(*) as count FROM records WHERE id IN (\n" +
            "\tSELECT id FROM records WHERE (records.`date` < ? AND records.`date` < ?)\n" +
            ") GROUP BY ipAddress\n" +
            "HAVING count > ?;";

    private Connection connection;

    RecordService(Connection connection) {
        this.connection = connection;
    }

    void persist(List<Record> recordList) throws SQLException {
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
            throw e;
        }
    }

    public List<BlockedIP> findViolatingIPAddresses(Date startDate, Duration duration, int limit) {
        return null;
    }
}
