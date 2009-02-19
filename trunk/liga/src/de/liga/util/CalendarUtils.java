package de.liga.util;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Description:   <br/>
 * User: roman
 * Date: 02.11.2007, 13:43:41
 */
public class CalendarUtils {
    /**
     * @return a time with the given hour:minute (seconds = 0)
     */
    public static Time createTime(int hour, int minute) {
        Calendar gc = Calendar.getInstance();
        gc.clear();
        gc.set(0, 0, 0, hour, minute, 0);
        return new Time(gc.getTimeInMillis());
    }

    public static String timeToString(Time time) {
        if (time == null) return null;
        SimpleDateFormat timeFormHHMM = new SimpleDateFormat("HH:mm");
        timeFormHHMM.setLenient(false);
        return timeFormHHMM.format(time);
    }

    public static Time stringToTime(String time) {
        if (StringUtils.isEmpty(time)) return null;
        SimpleDateFormat timeFormHHMM = new SimpleDateFormat("HH:mm");
        timeFormHHMM.setLenient(false);
        try {
            return new Time(timeFormHHMM.parse(time).getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getWeekdayName(int dayOfWeek) {
        DateFormatSymbols symbols = DateFormatSymbols.getInstance();
        return symbols.getWeekdays()[dayOfWeek];
    }

    public static Integer getWeekday(String dayOfWeek) {
        DateFormatSymbols symbols = DateFormatSymbols.getInstance();
        int index = ArrayUtils.indexOf(symbols.getWeekdays(), dayOfWeek);
        return index <= 0 ? null : Integer.valueOf(index);
    }

    public static String[] getWeekdays() {
        DateFormatSymbols symbols = DateFormatSymbols.getInstance();
        return symbols.getWeekdays();
    }

    public static Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * konvertiert in Daten der altanwendung
     *
     * @param time
     * @return
     */
    public static int toOldLongValue(Time time) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(time);
        int h = gc.get(Calendar.HOUR_OF_DAY);
        int m = gc.get(Calendar.MINUTE);
        return (h * 60 * 60) + (m * 60);
    }
}
