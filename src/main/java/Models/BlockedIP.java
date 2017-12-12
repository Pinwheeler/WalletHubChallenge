package Models;

public class BlockedIP {
    public String ipAddress;
    public String comments;

    public BlockedIP(String ipAddress, String comments) {
        this.ipAddress = ipAddress;
        this.comments = comments;
    }

    @Override
    public String toString() {
        return ipAddress + " -- " + comments;
    }
}
