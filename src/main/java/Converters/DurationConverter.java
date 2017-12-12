package Converters;

import Models.Duration;
import com.beust.jcommander.IStringConverter;

public class DurationConverter implements IStringConverter<Duration> {
    @Override
    public Duration convert(String value) {
        try {
            return Duration.valueOf(value);
        } catch (IllegalArgumentException e) {
            return Duration.daily; //Default fallback
        }
    }
}
