package org.en.tealEye.guiMain.util;

import de.liga.util.CalendarUtils;
import de.liga.util.Value;
import de.liga.util.ValueHandler;

import java.sql.Time;

/**
 * Description:   <br/>
 * User: roman
 * Date: 09.11.2007, 20:41:34
 */
public class TimeHandler implements ValueHandler {
    public static void register(Value value) {
        value.registerHandler("time", new TimeHandler());
    }

    public Object getValue(Object value, String params) {
        if (value != null) {
            return CalendarUtils.timeToString((Time) value);
        } else {
            return "";
        }
    }
}
