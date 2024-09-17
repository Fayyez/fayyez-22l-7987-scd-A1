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
    public static Date add7Days(Date date) {
        Date newDate = new Date(date.getTime() + 7 * 24 * 60 * 60 * 1000);
        return newDate;
    }
    public static boolean isInPast(Date date) {
        return date.before(new Date());
    }
    public static boolean isInFuture(Date date) {
        return date.after(new Date());
    }
    public static boolean lessThan30DaysDifference(Date expiry_date) throws Exception {
        if (expiry_date == null) {
            throw new Exception("Expiry date is null in Date Builder");
        }
        else if (expiry_date.before(new Date())) {
            return false;// already expired
        }
        return (expiry_date.getTime() - new Date().getTime()) / (24 * 60 * 60 * 1000) < 30;
    }
}
