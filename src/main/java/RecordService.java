import java.sql.Connection;

public class RecordService {

    private Connection connection;

    RecordService(Connection connection) {
        this.connection = connection;
    }
}
