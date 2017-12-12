import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "record")
public class Record {

    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "date", nullable = false)
    public LocalDateTime date;

    @Column(name = "ipAddress", nullable = false)
    public String ipAddress;

    @Column(name = "httpMethod", nullable = false)
    public String httpMethod;

    @Column(name = "responseStatus", nullable = false)
    public String responseStatus;

    @Column(name = "userAgent", nullable = false)
    public String userAgent;

    public Record(LocalDateTime date, String ipAddress, String httpMethod, String responseStatus, String userAgent) {
        this.date = date;
        this.ipAddress = ipAddress;
        this.httpMethod = httpMethod;
        this.responseStatus = responseStatus;
        this.userAgent = userAgent;
    }

    public boolean equals(Record other) {
        return (
                date == other.date
                && ipAddress == other.ipAddress
                && httpMethod == other.httpMethod
                && responseStatus == other.responseStatus
                && userAgent == other.userAgent
        );
    }
}
