package Converters;

import com.beust.jcommander.IStringConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeConverter implements IStringConverter<LocalDateTime> {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss", Locale.ENGLISH);

    @Override
    public LocalDateTime convert(String value) {
        return LocalDateTime.parse(value, formatter);
    }
}
