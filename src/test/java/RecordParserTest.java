import Models.Record;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.format;

public class RecordParserTest {

    RecordParser subject;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Before
    public void init() {
        try {
            File file = new File(getClass().getResource("subset.log").getFile());
            Scanner testScanner = new Scanner(file);
            subject = new RecordParser(testScanner);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParserReturnsRecordObjects() {
        List<Record> result = null;
        try {
            result = subject.parse();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Record firstResult = result.get(0);
        Record secondResult = result.get(1);

        String dateString1 = "2017-01-01 23:58:55.837";
        String dateString2 = "2017-01-01 23:58:55.921";

        try {
            assertEquals(formatter.parse(dateString1), firstResult.date);
            assertEquals("192.168.142.23", firstResult.ipAddress);
            assertEquals("GET / HTTP/1.1", firstResult.httpMethod);
            assertEquals(200, firstResult.responseStatus);
            assertEquals("swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0", firstResult.userAgent);

            assertEquals(formatter.parse(dateString2), secondResult.date);
            assertEquals("192.168.31.166", secondResult.ipAddress);
            assertEquals("GET / HTTP/1.1", secondResult.httpMethod);
            assertEquals(200, secondResult.responseStatus);
            assertEquals("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36 Edge/15.15063", secondResult.userAgent);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
