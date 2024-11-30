package Test;

import org.junit.jupiter.api.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import utils.DateBuilder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DateBuilderTest {

    @Test
    public void testGetDateobjValidDate() {
        String dateStr = "15/11/2024";
        Date date = DateBuilder.getDateobj(dateStr);

        Assertions.assertNotNull(date);
        SimpleDateFormat formatter = DateBuilder.getDateFormator();
        Assertions.assertEquals(dateStr, formatter.format(date));
    }

    @Test
    public void testGetDateobjInvalidDate() {
        String dateStr = "invalid_date";

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            DateBuilder.getDateobj(dateStr);
        });
    }

    @Test
    public void testGetDateStr() {
        String dateStr = "01/12/2024";
        Date date = DateBuilder.getDateobj(dateStr);

        String formattedDate = DateBuilder.getDateStr(date);

        Assertions.assertEquals(dateStr, formattedDate);
    }

    @Test
    public void testAdd7Days() {
        String dateStr = "25/11/2024";
        Date date = DateBuilder.getDateobj(dateStr);

        Date newDate = DateBuilder.add7Days(date);

        String expectedDateStr = "02/12/2024";
        SimpleDateFormat formatter = DateBuilder.getDateFormator();
        Assertions.assertEquals(expectedDateStr, formatter.format(newDate));
    }

    @Test
    public void testIsInPast() {
        Date pastDate = DateBuilder.getDateobj("01/01/2020");

        Assertions.assertTrue(DateBuilder.isInPast(pastDate));

        Date futureDate = DateBuilder.add7Days(new Date());

        Assertions.assertFalse(DateBuilder.isInPast(futureDate));
    }

    @Test
    public void testIsInFuture() {
        Date futureDate = DateBuilder.add7Days(new Date());

        Assertions.assertTrue(DateBuilder.isInFuture(futureDate));

        Date pastDate = DateBuilder.getDateobj("01/01/2020");

        Assertions.assertFalse(DateBuilder.isInFuture(pastDate));
    }

    @Test
    public void testLessThan30DaysDifference() throws Exception {
        Date today = new Date();
        Date lessThan30Days = DateBuilder.add7Days(today);
        Date moreThan30Days = DateBuilder.getDateobj("01/01/2025");
        Date pastDate = DateBuilder.getDateobj("01/01/2020");

        Assertions.assertTrue(DateBuilder.lessThan30DaysDifference(lessThan30Days));
        Assertions.assertFalse(DateBuilder.lessThan30DaysDifference(moreThan30Days));
        Assertions.assertFalse(DateBuilder.lessThan30DaysDifference(pastDate));
    }

    @Test
    public void testLessThan30DaysDifferenceNullDate() {
        Assertions.assertThrows(Exception.class, () -> {
            DateBuilder.lessThan30DaysDifference(null);
        });
    }

    @Test
    public void testGetMonth() {
        Date date = DateBuilder.getDateobj("15/11/2024");

        int month = DateBuilder.getMonth(date);

        // Months are 0-based in Java, so 10 represents November.
        Assertions.assertEquals(10, month);
    }
}
