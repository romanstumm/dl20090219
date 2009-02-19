package de.liga.dart.database;

import junit.framework.TestCase;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Description:   <br/>
 * User: roman
 * Date: 08.02.2008, 00:05:21
 */
public class DBVersionTest extends TestCase {

    public DBVersionTest(String string) {
        super(string);
    }

    public void testCompare() {
        DBVersion v1 = new DBVersion("2.0.0");
        DBVersion v2 = new DBVersion("2.0.1");
        DBVersion v3 = new DBVersion("2.0.0.1");
        DBVersion v3too = new DBVersion("2.0.0.1");
        assertEquals(v3, v3too);
        assertTrue(v2.isHigherThan(v1));
        assertTrue(v2.isHigherThan(v3));
        assertTrue(v2.isHigherThan(v1));
        assertTrue(v1.isLowerThan(v3));
    }

    public void testReadPackage() throws IOException {
        URL url = getClass().getClassLoader().getResource("upgrade/up-2.0.1.sql");
        System.out.println("file: " + url.getFile());
        System.out.println("path: " + url.getPath());
        System.out.println("prot: " + url.getProtocol());
        System.out.println("content: "+ url.getContent());
        JarInputStream in = new JarInputStream(new FileInputStream("target/dart-2.0.jar"));
        JarEntry e = in.getNextJarEntry();
        while (e != null) {
            System.out.println(e);
            e = in.getNextJarEntry();
        }
        System.out.println("** END **");
    }
}
