import Models.BlockedIP;
import Models.Duration;
import Models.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sun.jvm.hotspot.opto.Block;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RecordServiceTest {

    private RecordService subject;

    @Mock
    private Connection connectionMock;
    @Mock
    private PreparedStatement insertStatement;
    @Mock
    private PreparedStatement selectStatement;
    @Mock
    private ResultSet resultSet;

    @Before
    public void init() {

        try {
            given(resultSet.next()).willReturn(true).willReturn(false);
            given(selectStatement.executeQuery()).willReturn(resultSet);
            given(connectionMock.prepareStatement(RecordService.SQL_INSERT)).willReturn(insertStatement);
            given(connectionMock.prepareStatement(RecordService.SQL_SELECT_TIME_BOUNDED_LIMITED)).willReturn(selectStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        subject = new RecordService(connectionMock);
    }

    @Test
    public void testPersistingAListOfRecords() {
        List<Record> records = new ArrayList<Record>();
        Date dt = new Date();

        Record record0 = new Record(dt, "112", "GOTCHA", 99, "James Bond");
        Record record1 = new Record(dt, "303", "PROCEED", 401, "Smith");

        records.add(record0);
        records.add(record1);

        try {
            subject.persist(records);

            verify(insertStatement, times(2)).setObject(1, dt);

            verify(insertStatement).setString(2, "112");
            verify(insertStatement).setString(3, "GOTCHA");
            verify(insertStatement).setInt(4,99);
            verify(insertStatement).setString(5,"James Bond");

            verify(insertStatement).setString(2, "303");
            verify(insertStatement).setString(3, "PROCEED");
            verify(insertStatement).setInt(4,401);
            verify(insertStatement).setString(5,"Smith");

            verify(insertStatement, times(2)).addBatch();
            verify(insertStatement).executeBatch();
            verify(connectionMock).commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testFindingDailyViolatingIPAddresses() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add( Calendar.DATE, 1);
        Date tomorrow = cal.getTime();
        subject.findViolatingIPAddresses(date, Duration.daily, 50);

        try {
            verify(selectStatement).setObject(1, date);
            verify(selectStatement).setObject(2, tomorrow);
            verify(selectStatement).setInt(3, 50);
            verify(selectStatement).executeQuery();
            verify(connectionMock).commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindingHourlyViolatingIPAddresses() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add( Calendar.HOUR, 1);
        Date hourLater = cal.getTime();
        subject.findViolatingIPAddresses(date, Duration.hourly, 50);

        try {
            verify(selectStatement).setObject(1, date);
            verify(selectStatement).setObject(2, hourLater);
            verify(selectStatement).setInt(3, 50);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
