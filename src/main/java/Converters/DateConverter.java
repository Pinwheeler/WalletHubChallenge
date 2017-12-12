package Converters;

import com.beust.jcommander.IStringConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter implements IStringConverter<Date> {
    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");

    @Override
    public Date convert(String value) {
        try {
            return formatter.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
