import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ServiceProvider {
    static String dbURL = "jdbc:mysql://localhost:3306/wallethub";
    static String dbUser = "root";
    static String dbPassword = null;

    public static DatabaseService defaultRecordService() throws ServiceProviderException {
        try {
            Connection connection = DriverManager.getConnection( dbURL, dbUser, dbPassword);
            connection.setAutoCommit(false);
            return new DatabaseService(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServiceProviderException("Failed to create a DatabaseService");
        }
    }
}

class ServiceProviderException extends Exception {
    public ServiceProviderException(String message){
        super(message);
    }
}