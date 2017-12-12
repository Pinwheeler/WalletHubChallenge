import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RecordParser {

    private Scanner scanner;
    private DateTimeFormatter formatter;

    public RecordParser(Scanner scanner) {
        this.scanner = scanner;
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
    }

    public List<Record> parse() {
        ArrayList<Record> records = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] substrings = line.split("\\|");
            LocalDateTime dateTime = LocalDateTime.parse(substrings[0], formatter);
            Record record = new Record(dateTime, substrings[1], substrings[2].replaceAll("^\"|\"$", ""), substrings[3], substrings[4].replaceAll("^\"|\"$", ""));
            records.add(record);
        }
        return records;
    }
}
