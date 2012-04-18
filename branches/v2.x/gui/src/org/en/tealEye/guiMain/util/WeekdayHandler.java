package org.en.tealEye.guiMain.util;

import de.liga.util.CalendarUtils;
import de.liga.util.Value;
import de.liga.util.ValueHandler;

/**
 * Description:   <br/>
 * User: roman
 * Date: 09.11.2007, 20:41:28
 */
public class WeekdayHandler implements ValueHandler {
    public static void register(Value value) {
        value.registerHandler("weekday", new WeekdayHandler());
    }

    public Object getValue(Object value, String params) {
        if (value != null) {
            return CalendarUtils.getWeekdayName(((Number) value).intValue());
        } else {
            return "";
        }
    }
}
