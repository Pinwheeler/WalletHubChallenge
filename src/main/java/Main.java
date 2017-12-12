import Converters.DateTimeConverter;
import Converters.DurationConverter;
import Converters.FileConverter;
import Models.Args;
import Models.Duration;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Scanner;

import static com.sun.javafx.scene.control.skin.Utils.getResource;

public class Main {

    @Parameter(names = {"--file", "-f"}, converter = FileConverter.class)
    File logFile;
    @Parameter(names = "--startDate", converter = DateTimeConverter.class)
    LocalDateTime startDate;
    @Parameter(names = {"--duration"}, converter = DurationConverter.class)
    Duration duration;
    @Parameter(names = {"--threshold"})
    int threshold;

    public static void main(String[] argv) {
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(argv);
        main.run();
    }

    public void run() {
        try {
            RecordService recordService = ServiceProvider.defaultRecordService();
            Scanner testScanner = new Scanner(logFile);
            RecordParser recordParser = new RecordParser(testScanner);

            recordService.persist(recordParser.parse());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
