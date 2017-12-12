package Models;

import java.time.LocalDateTime;

public class Record {

    public LocalDateTime date;
    public String ipAddress;
    public String httpMethod;
    public int responseStatus;
    public String userAgent;

    public Record(LocalDateTime date, String ipAddress, String httpMethod, int responseStatus, String userAgent) {
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
