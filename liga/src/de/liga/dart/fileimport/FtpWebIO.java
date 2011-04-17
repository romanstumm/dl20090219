package de.liga.dart.fileimport;

import de.liga.dart.exception.DartException;
import de.liga.dart.model.Liga;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * viaboxx GmbH, (c) 2010
 * User: roman.stumm
 * Date: 17.04.2011
 * Time: 20:31:49
 */
public class FtpWebIO {
    // target files
    protected static final Map<String, String> WEB_DIRS = new HashMap();
    protected static String rangSuffix = "_rang.html", planSuffix = "_plan.html";

    public static void setRangSuffix(String rangSuffix) {
        FtpWebIO.rangSuffix = rangSuffix;
    }

    public static void setPlanSuffix(String planSuffix) {
        FtpWebIO.planSuffix = planSuffix;
    }

    public static void putLigaWeb(String liga, String dir) {
        WEB_DIRS.put(liga, dir);
    }

    public static Map<String, String> getWebDirs() {
        return WEB_DIRS;
    }

    public static void clear() {
        WEB_DIRS.clear();
    }

    public void copyRangHtmlToWebdir(Liga liga) throws DartException, IOException {
        // dbfdir/HTARCHIV = source dir
        String syncdir = DbfIO.getSyncDir(liga.getLigaName());
        File sourcedir = new File(syncdir, "HTARCHIV");

        // webdir = target dir
        String webdir = getWebdir(liga);
        File targetdir = new File(webdir, "rangliste");
        copyFiles(sourcedir, targetdir, rangSuffix.toLowerCase());
    }

    private String getWebdir(Liga liga) {
        final String path = WEB_DIRS.get(liga.getLigaName());
        if (path == null) throw new DartException("webdir." + liga.getLigaName() +
                " nicht bekannt (bitte settings.properties pruefen)");
        return path;
    }

    public void copyPlanHtmlToWebdir(Liga liga) throws IOException {
        String syncdir = DbfIO.getSyncDir(liga.getLigaName());
        File sourcedir = new File(syncdir, "HTARCHIV");

        // webdir = target dir
        String webdir = getWebdir(liga);
        File targetdir = new File(webdir, "spielplan");
        copyFiles(sourcedir, targetdir, planSuffix.toLowerCase());
    }

    private void copyFiles(File sourcedir, File targetdir, final String suffix) throws IOException {
        File[] sourcefiles = sourcedir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(suffix);
            }
        });
        for (File sourcefile : sourcefiles) {
            // Dateiname parsen, Name der Zieldatei erstellen
            String plainName = sourcefile.getName().substring(0, sourcefile.getName().length() - suffix.length());
            StringTokenizer tokens = new StringTokenizer(plainName, " ");

            String klasse = tokens.nextToken();
            klasse = klasse.toLowerCase();
            if (!Character.isDigit(klasse.charAt(klasse.length() - 1))) {
                if (klasse.equals("bezirkoberliga") || klasse.equals("bezirksoberliga")) {
                    klasse = "bzo";
                    // keine nr ergänzen (siehe westerwald)
                } else if (klasse.equals("bezirksliga")) {
                    klasse = "bz";
                    // ggf. fehlende nr ergänzen (siehe westerwald)
                    klasse = klasse + tokens.nextToken();  // blank nach dem Namen, nur hier, sonst nirgends
                } else {
                    // ggf. fehlende nr ergänzen (siehe benden A*)
                    klasse = klasse + "1";
                }
            }


            if (!targetdir.exists()) targetdir.mkdirs();
            File targetfile = new File(targetdir, klasse + ".htm");

            BufferedReader br = new BufferedReader(new FileReader(sourcefile));
            FileWriter fw = new FileWriter(targetfile);
            String line;
            // Kopieren
            try {
                while ((line = br.readLine()) != null) {
                    if (!line.startsWith("<link rel=File-List")) {
                        // in der Datei die Zeile löschen, die mit "<link rel=File-List" anfängt
                        fw.write(line);
                        fw.write("\n");
                    }
                }
            } finally {
                fw.close();
                br.close();
            }
        }
    }

    public void uploadRangWebdir(Liga liga) {
        // todo nyi

    }

    public void uploadPlanWebdir(Liga liga) {
        // todo nyi
    }
}
