package de.liga.util;

import junit.framework.TestCase;

/**
 * Description: <br/>
 * User: roman <br/>
 * Date: 26.01.2009 <br/>
 * Time: 10:13:22 <br/>
 * Copyright: Agimatec GmbH
 */
public class StringUtilsTest extends TestCase {
    public void testWordWrap() {
        String s = "Gasthaus zur Eiche";
        String[] a = StringUtils.wordWrap(s, 15);
        assertEquals("Gasthaus zur", a[0].trim());
        assertEquals("Eiche", a[1]);

        s = "Bei Wolfgang Pub-Gaststätte";
        a = StringUtils.wordWrap(s, 20);
        assertEquals("Bei Wolfgang Pub-", a[0].trim());
        assertEquals("Gaststätte", a[1]);

        a = StringUtils.wordWrap(s, 25);
        assertEquals("Bei Wolfgang Pub-", a[0].trim());
        assertEquals("Gaststätte", a[1]);
        assertEquals(2, a.length);
    }
}
