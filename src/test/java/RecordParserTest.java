import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import static junit.framework.TestCase.assertEquals;

public class RecordParserTest {

    RecordParser subject;

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
        List<Record> result = subject.parse();
        Record firstResult = result.get(0);
        Record secondResult = result.get(1);

        String dateString1 = "2017-01-01 23:58:55.837";
        String dateString2 = "2017-01-01 23:58:55.921";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);

        assertEquals(LocalDateTime.parse(dateString1, formatter), firstResult.date);
        assertEquals("192.168.142.23", firstResult.ipAddress);
        assertEquals("GET / HTTP/1.1", firstResult.httpMethod);
        assertEquals(200, firstResult.responseStatus);
        assertEquals("swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0", firstResult.userAgent);

        assertEquals(LocalDateTime.parse(dateString2, formatter), secondResult.date);
        assertEquals("192.168.31.166", secondResult.ipAddress);
        assertEquals("GET / HTTP/1.1", secondResult.httpMethod);
        assertEquals(200, secondResult.responseStatus);
        assertEquals("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36 Edge/15.15063", secondResult.userAgent);
    }
}
