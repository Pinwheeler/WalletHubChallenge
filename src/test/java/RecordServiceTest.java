import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RecordServiceTest {
    @Mock
    Driver driverMock;

    @InjectMocks
    RecordService subject;

    @Test
    public void testAttemptsDatabaseConnectionOnInit() {
        Properties properties = new Properties();
        try {
            verify(driverMock).connect("127.0.0.1", properties);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
