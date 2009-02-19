package de.liga.util;

import java.util.List;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Description:   <br/>
 * User: roman
 * Date: 02.03.2008, 13:26:54
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {
    public static String fixTimeString(String str) {
        int i = str.indexOf(':');
        if (i < 0) i = str.indexOf('.');
        if (i < 0) {
            if (str.length() < 2) {
                str = org.apache.commons.lang.StringUtils.leftPad(str, 2, '0') + ":00";
            } else {
                str = str.substring(0, 2) + ":" + str.substring(2);
                str = org.apache.commons.lang.StringUtils.rightPad(str, 5, '0');
            }
            if (str.length() > 5) str = str.substring(0, 5);
        } else {
            String oldStr = str.substring(i + 1);
            if (i < 2) {
                str = org.apache.commons.lang.StringUtils
                      .leftPad(str.substring(i), 2, '0');
            } else if (i >= 2) {
                str = str.substring(0, 2);
            }
            str += ':';
            if (oldStr.length() < 2) {
                oldStr = org.apache.commons.lang.StringUtils.rightPad(oldStr, 2, '0');
            } else if (oldStr.length() > 2) {
                oldStr = oldStr.substring(0, 2);
            }
            str += oldStr;
        }
        return str;
    }

    public static String[] wordWrap(String str, int length) {
        if (str == null || str.length() == 0) return new String[]{""};
        Pattern wrapRE = Pattern.compile(".{0," + length + "}(?:\\S(?:-| |$)|$)");
        List list = new LinkedList();
        Matcher m = wrapRE.matcher(str);
        while (m.find()) {
            if (m.group().length() > 0) {
                list.add(m.group());
            }
        }
        return (String[]) list.toArray(new String[list.size()]);
    }
}
