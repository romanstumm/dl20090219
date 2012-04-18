package de.liga.util;

import junit.framework.TestCase;

import java.sql.Time;
import java.util.Calendar;

/**
 * Description:   <br/>
 * User: roman
 * Date: 03.11.2007, 17:10:20
 */
public class CalendarUtilsTest extends TestCase {

    public CalendarUtilsTest(String string) {
        super(string);
    }

    public void testWochentag() {
        String tag = CalendarUtils.getWeekdayName(Calendar.MONDAY);
        assertEquals(Calendar.MONDAY, CalendarUtils.getWeekday(tag).intValue());       
        tag = CalendarUtils.getWeekdayName(Calendar.SUNDAY);
        assertEquals(Calendar.SUNDAY, CalendarUtils.getWeekday(tag).intValue());
    }

    public void testTimeToString() {
        Time t = CalendarUtils.createTime(22, 58);
        assertEquals("22:58", CalendarUtils.timeToString(t));
        assertEquals("22:58", CalendarUtils.timeToString(
                CalendarUtils.stringToTime("22:58")));
    }
}
