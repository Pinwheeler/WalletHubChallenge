package Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "blockedIP")
public class BlockedIP {
    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "ipAddress", nullable = false)
    private String ipAddress;

    @Column(name = "comments", nullable = true)
    private String comments;
}
