import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.*;

public class Main {

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/wallethub",
                    "root",
                    null);
            EntityManagerFactory factory = Persistence.createEntityManagerFactory("Record");
            EntityManager manager = factory.createEntityManager();
            RecordService recordService = new RecordService(connection, manager);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
