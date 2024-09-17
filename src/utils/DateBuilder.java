package utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateBuilder {
    public static Date getDateobj(String datestr) {
        SimpleDateFormat formatter = DateBuilder.getDateFormator();
        try {
            return (Date) formatter.parse(datestr);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format");
        }
    }
    public static String getDateStr(Date date) {
        Format formatter = DateBuilder.getDateFormator();
        return formatter.format(date);
    }
    public static SimpleDateFormat getDateFormator() {
        return new SimpleDateFormat("dd/MM/yyyy");
    }
}
