import java.sql.*;

public class Main {

    public static void main(String[] args) {
        try {
            RecordService recordService =
                    new RecordService(DriverManager.getConnection("jdbc:mysql://localhost:3306/wallethub",
                            "root",
                            null));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
