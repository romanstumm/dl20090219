package de.liga.util;

import junit.framework.TestCase;

import java.io.IOException;
import java.net.URL;

/**
 * Description:   <br/>
 * User: roman
 * Date: 11.02.2008, 22:07:17
 */
public class ResourceUtilsTest extends TestCase {


    public ResourceUtilsTest(String string) {
        super(string);
    }

    public void testListResources() throws IOException {
        URL[] urls =
                ResourceUtils.listFileResources(getClass().getClassLoader().getResource("upgrade"));
        for(URL each : urls) {
            System.out.println(each);
        }
    }

    public void testListFiles() throws IOException {
        URL[] urls =
                ResourceUtils.listFileResources(new URL("file://database/upgrade"));
        for(URL each : urls) {
            System.out.println(each);
        }
    }
}
