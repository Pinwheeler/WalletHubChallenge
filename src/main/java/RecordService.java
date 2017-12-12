import Models.Record;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class RecordService {

    public static String SQL_INSERT = "INSERT INTO records (date, ipAddress, httpMethod, responseStatus, userAgent) VALUES(?,?,?,?,?);";

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
}
