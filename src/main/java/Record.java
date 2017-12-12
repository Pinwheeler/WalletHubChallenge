import java.time.LocalDateTime;
import java.util.Date;

public class Record {

    public LocalDateTime date;
    public String ipAddress;
    public String httpMethod;
    public String responseStatus;
    public String userAgent;

    public Record(LocalDateTime date, String ipAddress, String httpMethod, String responseStatus, String userAgent) {
        this.date = date;
        this.ipAddress = ipAddress;
        this.httpMethod = httpMethod;
        this.responseStatus = responseStatus;
        this.userAgent = userAgent;
    }
}
