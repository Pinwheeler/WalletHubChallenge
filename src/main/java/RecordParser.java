import Models.Record;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class RecordParser {

    private Scanner scanner;
    private SimpleDateFormat formatter;

    public RecordParser(Scanner scanner) {
        this.scanner = scanner;
        this.formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    public List<Record> parse() throws ParseException {
        ArrayList<Record> records = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] substrings = line.split("\\|");
            Date date = formatter.parse(substrings[0]);
            Record record = new Record(
                    date,
                    substrings[1],
                    substrings[2].replaceAll("^\"|\"$", ""),
                    Integer.parseInt(substrings[3]),
                    substrings[4].replaceAll("^\"|\"$", "")
            );
            records.add(record);
        }
        return records;
    }
}
