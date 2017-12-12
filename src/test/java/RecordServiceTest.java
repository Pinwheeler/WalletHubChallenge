import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RecordServiceTest {

    private RecordService subject;

    @Captor
    ArgumentCaptor<Record> captor;

    @Mock
    private Connection connectionMock;
    @Mock
    private EntityManager entityManagerMock;
    @Mock
    private EntityTransaction entityTransactionMock;


    @Before
    public void init() {
        given(entityManagerMock.getTransaction()).willReturn(entityTransactionMock);
        subject = new RecordService(connectionMock, entityManagerMock);
    }

    @Test
    public void testPersistingAListOfRecords() {
        List<Record> records = new ArrayList<Record>();
        LocalDateTime dt0 = LocalDateTime.now();
        LocalDateTime dt1 = LocalDateTime.now();

        Record record0 = new Record(dt0, "112", "GOTCHA", "99", "James Bond");
        Record record1 = new Record(dt1, "303", "PROCEED", "401k", "Smith");

        records.add(record0);
        records.add(record1);

        subject.persist(records);
        verify(entityManagerMock).getTransaction();
        verify(entityTransactionMock).begin();
        verify(entityManagerMock).persist(captor.capture());
        List<Record> persistedRecords = captor.getAllValues();

        assertEquals(record0, persistedRecords.get(0));
        assertEquals(record1, persistedRecords.get(1));

        verify(entityTransactionMock).commit();
        verify(entityManagerMock).close();
    }
}
