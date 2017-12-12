import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RecordServiceTest {

    private RecordService subject;

    @Captor
    ArgumentCaptor<Record> captor;

    @Mock
    private Connection connectionMock;
    @Mock
    private PreparedStatement statementMock;

    @Before
    public void init() {
        try {
            given(connectionMock.prepareStatement(RecordService.SQL_INSERT)).willReturn(statementMock);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        subject = new RecordService(connectionMock);
    }

    @Test
    public void testPersistingAListOfRecords() {
        List<Record> records = new ArrayList<Record>();
        LocalDateTime dt0 = LocalDateTime.now();
        LocalDateTime dt1 = LocalDateTime.now();

        Record record0 = new Record(dt0, "112", "GOTCHA", 99, "James Bond");
        Record record1 = new Record(dt1, "303", "PROCEED", 401, "Smith");

        records.add(record0);
        records.add(record1);

        try {
            subject.persist(records);

            verify(statementMock).setObject(0, dt0);
            verify(statementMock).setString(1, "112");
            verify(statementMock).setString(2, "GOTCHA");
            verify(statementMock).setInt(3,99);
            verify(statementMock).setString(4,"James Bond");

            verify(statementMock).setObject(0, dt1);
            verify(statementMock).setString(1, "303");
            verify(statementMock).setString(2, "PROCEED");
            verify(statementMock).setInt(3,401);
            verify(statementMock).setString(4,"Smith");

            verify(statementMock, times(2)).addBatch();
            verify(statementMock).executeBatch();
            verify(connectionMock).commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
