import javax.persistence.EntityManager;
import java.sql.Connection;
import java.util.List;

public class RecordService {

    private Connection connection;
    private EntityManager em;

    RecordService(Connection connection, EntityManager manager) {
        this.connection = connection;
        this.em = manager;
    }

    void persist(List<Record> recordList) {
        em.getTransaction().begin();
        for (Record record: recordList) {
            em.persist(record);
        }
        em.close();
    }
}
