import Converters.DateConverter;
import Converters.DurationConverter;
import Converters.FileConverter;
import Models.BlockedIP;
import Models.Duration;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Parameters(separators = "=")
public class Main {

    @Parameter(names = {"--accesslog"}, converter = FileConverter.class)
    File logFile;
    @Parameter(names = "--startDate", converter = DateConverter.class)
    Date startDate;
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
            DatabaseService databaseService = ServiceProvider.defaultRecordService();
            boolean uploadLogfile = (logFile != null);
            boolean findLimitViolators = (startDate != null && duration != null && threshold > 0);
            if (uploadLogfile || findLimitViolators) {
                if (uploadLogfile) {

                    Scanner testScanner = new Scanner(logFile);
                    RecordParser recordParser = new RecordParser(testScanner);

                    databaseService.persistRecords(recordParser.parse());

                } if (findLimitViolators) {
                    List<BlockedIP> blockedIPs = databaseService.findViolatingIPAddresses(startDate, duration, threshold);
                    databaseService.persistBlockedIPs(blockedIPs);
                    for (BlockedIP ip : blockedIPs) {
                        System.out.print(ip);
                        System.out.println();
                    }
                }
            } else {
                System.out.print("\nInvalid usage. You must specify either a file or a " +
                        "start-time, duration, threshold combination\n\n" +
                        "Valid Usage:\n " +
                        "\t--accesslog=\t\tspecify a path to the log file to build the database\n" +
                        "\t--startDate=\t\tspecify a start time for audit in form yyyy-MM-dd.HH:mm:ss\n" +
                        "\t--duration=\t\tspecify a duration for audit. Either 'daily' or 'hourly defaults to daily\n" +
                        "\t--threshold=\t\tspecify a threshold for audit.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
