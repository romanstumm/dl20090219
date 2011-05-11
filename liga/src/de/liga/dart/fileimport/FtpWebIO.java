package de.liga.dart.fileimport;

import de.liga.dart.exception.DartException;
import de.liga.dart.model.Liga;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

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
    private static String server, user, password, ftpDirPlan, ftpDirRang;
    private static final Log log = LogFactory.getLog(FtpWebIO.class);

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
        server = null;
        user = null;
        password = null;
    }

    public static void setFtpServerUserPassword(String s, String u, String p) {
        server = s;
        user = u;
        password = p;
    }

    public static void setFtpPlanRangDirs(String p, String r) {
        ftpDirPlan = p;
        ftpDirRang = r;
    }

    public void copyRangHtmlToWebdir(Liga liga) throws DartException, IOException {
        // dbfdir/HTARCHIV = source dir
        String syncdir = DbfIO.getSyncDir(liga.getLigaName());
        File sourcedir = new File(syncdir, "HTARCHIV");

        // webdir = target dir
        File targetdir = getWebdirRang(liga);
        copyFiles(sourcedir, targetdir, rangSuffix.toLowerCase());
    }

    private File getWebdirRang(Liga liga) {
        String webdir = getWebdir(liga);
        return new File(webdir, "rangliste");
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
        File targetdir = getWebdirPlan(liga);
        copyFiles(sourcedir, targetdir, planSuffix.toLowerCase());
    }

    private File getWebdirPlan(Liga liga) {
        String webdir = getWebdir(liga);
        return new File(webdir, "spielplan");
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

    public boolean uploadRangWebdir(Liga liga) throws IOException {
        FTPClient ftp = prepareFTP();
        if(ftp == null) return false;
        try {
            File webdir = getWebdirRang(liga);
            if (ftpDirRang != null && ftpDirRang.length() > 0)
                ftp.changeWorkingDirectory(ftpDirRang);
            for (File each : webdir.listFiles()) {
                if (each.isFile())
                    ftpStoreFile(each.getPath(), each.getName(), ftp);
            }
        } finally {
            ftpDisconnect(ftp);
        }
        return true;
    }

    public boolean uploadPlanWebdir(Liga liga) throws IOException {
        FTPClient ftp = prepareFTP();
        if(ftp == null) return false;
        try {
            File webdir = getWebdirPlan(liga);
            if (ftpDirPlan != null && ftpDirPlan.length() > 0)
                ftp.changeWorkingDirectory(ftpDirPlan);
            for (File each : webdir.listFiles()) {
                if (each.isFile())
                    ftpStoreFile(each.getPath(), each.getName(), ftp);
            }
        } finally {
            ftpDisconnect(ftp);
        }
        return true;
    }

    private FTPClient prepareFTP() throws IOException {
        if(server == null || server.length()== 0) return null;
        FTPClient ftp;
        ftp = new FTPClient();
        ftpConnect(server, ftp);
        ftpLogin(user, password, true, ftp);
        return ftp;
    }

    private void ftpDisconnect(FTPClient ftp) throws IOException {
        if (ftp.isConnected()) {
            ftp.disconnect();
        }
    }

    private void ftpStoreFile(String local, String remote, FTPClient ftp) throws IOException {
        InputStream input;
        log.info("ftp uploading file " + local + " to " + remote + "...");
        input = new FileInputStream(local);
        try {
            boolean success = ftp.storeFile(remote, input);
            if (success)
                log.info("ftp upload of file " + remote + " done: successful");
            else
                log.error("ftp upload of file " + remote + " done: failed");
        } finally {
            input.close();
        }
    }

    private void ftpLogin(String username, String password, boolean binaryTransfer, FTPClient ftp) {
        try {
            if (!ftp.login(username, password)) {
                ftp.logout();
            }

            log.info("Remote system is " + ftp.getSystemName());

            if (binaryTransfer)
                ftp.setFileType(FTP.BINARY_FILE_TYPE);

            // Use passive mode as default because most of us are
            // behind firewalls these days.
            ftp.enterLocalPassiveMode();
        } catch (Exception e) {
            throw new DartException("exception during FTP upload", e);
        }
    }

    private void ftpConnect(String server, FTPClient ftp) {
        try {
            ftp.connect(server);
            int reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                throw new DartException("cannot connect to FTP");
            }
        } catch (IOException e) {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                }
                catch (IOException f) {
                    // do nothing
                }
            }
            throw new DartException("exception during FTP connect", e);
        }
    }
}

