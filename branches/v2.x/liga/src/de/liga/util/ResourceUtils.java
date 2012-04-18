package de.liga.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Description:   <br/>
 * User: roman
 * Date: 11.02.2008, 22:00:59
 */
public class ResourceUtils {
    /**
     * experimental - 
     * can list all resources for a directory or jar file
     *
     * @param url  - url of a package in the classpath or directory
     * @return
     */
    public static URL[] listFileResources(URL url) throws IOException {
        if ("jar".equalsIgnoreCase(url.getProtocol())) {
            return listJarResources(url);
        } else if ("file".equalsIgnoreCase(url.getProtocol())) {
            return listFiles(url);
        }
        return null;
    }

    private static URL[] listJarResources(URL url) throws IOException {
        String ext = url.toExternalForm();
        int idx = ext.lastIndexOf('!');
        String path = ext.substring(ext.indexOf(':') + 1, idx);
        String prefix = ext.substring(idx + 2) + "/";
        ZipInputStream in = new ZipInputStream(new URL(path).openStream());
        ZipEntry e = in.getNextEntry();
        List<URL> urls = new ArrayList();
        while (e != null) {
            if (!e.isDirectory()) {
                if (e.getName().startsWith(prefix)) {
                    urls.add(ResourceUtils.class.getClassLoader().getResource(e.getName()));
                }
            }
            e = in.getNextEntry();
        }
        return urls.toArray(new URL[urls.size()]);
    }

    private static URL[] listFiles(URL url) throws MalformedURLException {
        /*String path = url.toExternalForm();
        int len = 3;
        int idx = path.indexOf("://");
        if (idx < 0) {
            len = 2;
            idx = path.indexOf(":/");
            if (idx < 0) return null;
        }*/
//        File file = new File(path.substring(idx + len));
        File file = new File(url.getFile());
        File[] list = file.listFiles();
        if (list == null) return null;
        List<URL> urls = new ArrayList(list.length);
        for (File each : list) {
            if(!each.isDirectory()) {
            urls.add(each.toURI().toURL());
            }
        }
        return urls.toArray(new URL[urls.size()]);
    }
}
