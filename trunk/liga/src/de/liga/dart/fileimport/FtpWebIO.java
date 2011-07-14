package de.liga.dart.fileimport;

import de.liga.dart.exception.DartException;
import de.liga.dart.gruppen.check.ProgressIndicator;
import de.liga.dart.model.Liga;
import org.apache.commons.lang.StringUtils;
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
public class FtpWebIO implements ProgressIndicator {
    // target files
    protected static final Map<String, String> LIGA_DIRS = new HashMap();
    protected static String rangSuffix = "_rang.html", planSuffix = "_plan.html";
    private static final String DEFAULT_WEB_DIR = "C:/web/liga/";
    private static final String DEFAULT_FTP_DIR = "liga/";

    private static boolean fakeFtp;
    private static String server, user, password, ftpDir = DEFAULT_FTP_DIR, webDir = DEFAULT_WEB_DIR;
    private static final Log log = LogFactory.getLog(FtpWebIO.class);
    private final ProgressIndicator mainAppFrame;

    public static boolean isFakeFtp() {
        return fakeFtp;
    }

    public static void setFakeFtp(boolean value) {
        fakeFtp = value;
    }

    public FtpWebIO(ProgressIndicator mainAppFrame) {
        this.mainAppFrame = mainAppFrame;
    }

    public static void setRangSuffix(String rangSuffix) {
        FtpWebIO.rangSuffix = rangSuffix;
    }

    public static void setPlanSuffix(String planSuffix) {
        FtpWebIO.planSuffix = planSuffix;
    }

    public static void putLigaDir(String liga, String dir) {
        LIGA_DIRS.put(liga, dir);
    }

    public static Map<String, String> getLigaDirs() {
        return LIGA_DIRS;
    }

    public static void clear() {
        LIGA_DIRS.clear();
        ftpDir = DEFAULT_FTP_DIR;
        webDir = DEFAULT_WEB_DIR;
        server = null;
        user = null;
        password = null;
    }

    public static void setFtpServerUserPassword(String s, String u, String p) {
        server = s;
        user = u;
        password = p;
    }

    public static void setWebAndFtpDir(String p, String r) {
        webDir = p;
        ftpDir = r;
        if (StringUtils.isEmpty(webDir)) {
            webDir = DEFAULT_WEB_DIR;
        }
        if (StringUtils.isEmpty(ftpDir)) {
            ftpDir = DEFAULT_FTP_DIR;
        }
    }

    public void copyRangHtmlToWebdir(Liga liga) throws DartException, IOException {
        // dbfdir/HTARCHIV = source dir
        String syncdir = DbfIO.getSyncDir(liga.getLigaName());
        File sourcedir = new File(syncdir, "HTARCHIV");

        // webdir = target dir
        File targetdir = getWebDirRang(liga);
        copyFiles(sourcedir, targetdir, rangSuffix.toLowerCase());
    }

    private File getWebDirRang(Liga liga) {
        String webdir = getWebDir(liga);
        return new File(webdir, "rangliste");
    }

    private String getFtpDirRang(Liga liga) {
        String dir = getFtpDir(liga);
        return new File(dir, "rangliste").getPath();
    }

    private String getWebDir(Liga liga) {
        final String path = getLigaDirs().get(liga.getLigaName());
        if (path == null) throw new DartException("webdir." + liga.getLigaName() +
                " nicht bekannt (bitte settings.properties pruefen)");
        File fpath = new File(path);
        if (fpath.isAbsolute()) {
            return fpath.getPath();
        } else {
            return new File(webDir, path).getPath();
        }
    }

    private String getFtpDir(Liga liga) {
        final String path = getLigaDirs().get(liga.getLigaName());
        if (path == null) throw new DartException("webdir." + liga.getLigaName() +
                " nicht bekannt (bitte settings.properties pruefen)");
        File fpath = new File(path);
        if (fpath.isAbsolute()) {
            return fpath.getPath();
        } else {
            return new File(ftpDir, path).getPath();
        }
    }

    public void copyPlanHtmlToWebdir(Liga liga) throws IOException {
        String syncdir = DbfIO.getSyncDir(liga.getLigaName());
        File sourcedir = new File(syncdir, "HTARCHIV");

        // webdir = target dir
        File targetdir = getWebDirPlan(liga);
        copyFiles(sourcedir, targetdir, planSuffix.toLowerCase());
    }

    private File getWebDirPlan(Liga liga) {
        String dir = getWebDir(liga);
        return new File(dir, "spielplan");
    }

    private String getFtpDirPlan(Liga liga) {
        String dir = getFtpDir(liga);
        return new File(dir, "spielplan").getPath();
    }

    private void copyFiles(File sourcedir, File targetdir, final String suffix) throws IOException {
        File[] sourcefiles = sourcedir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(suffix);
            }
        });
        if (sourcefiles == null) throw new IOException("Quell-Verzeichnis " + sourcedir.getPath() + " nicht vorhanden");
        for (File sourcefile : sourcefiles) {
            // Dateiname parsen, Name der Zieldatei erstellen
            String plainName = sourcefile.getName().substring(0, sourcefile.getName().length() - suffix.length());
            StringTokenizer tokens = new StringTokenizer(plainName, " ");

            String klasse = tokens.nextToken();
            klasse = klasse.toLowerCase();
            if (!Character.isDigit(klasse.charAt(klasse.length() - 1))) {
                if (klasse.equals("bezirkoberliga") || klasse.equals("bezirksoberliga")) {
                    klasse = "bzo";
                    // keine nr erg�nzen (siehe westerwald)
                } else if (klasse.equals("bezirksliga")) {
                    klasse = "bz";
                    // ggf. fehlende nr erg�nzen (siehe westerwald)
                    klasse = klasse + tokens.nextToken();  // blank nach dem Namen, nur hier, sonst nirgends
                } else {
                    // ggf. fehlende nr erg�nzen (siehe benden A*)
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
                    if (line.startsWith("<link rel=File-List")) {
                        while(line != null && !line.contains(">")) {
                            line = br.readLine();
                        }
                    } else {
                        // in der Datei die Zeile loeschen, die mit "<link rel=File-List" anf�ngt
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
        if (ftp == null) return false;
        try {
            File webdir = getWebDirRang(liga);
            String theFtpDir = getFtpDirRang(liga);
            if (!changeDir(ftp, theFtpDir)) return false;
            uploadFtpFiles(ftp, webdir);
            log.info("done upload rang from " + webdir.getPath() + " to " + theFtpDir + " for " + liga.getLigaName());
        } finally {
            ftpDisconnect(ftp);
        }
        return true;
    }

    private boolean changeDir(FTPClient ftp, String theFtpDir) throws IOException {
        if (theFtpDir != null && theFtpDir.length() > 0) {
            theFtpDir = theFtpDir.replace("\\", "/");
            log.info("FTP change working dir to: " + theFtpDir);
            if(!fakeFtp) {
                if(!ftp.changeWorkingDirectory(theFtpDir)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void uploadFtpFiles(FTPClient ftp, File webdir) throws IOException {
        File[] files = webdir.listFiles();
        if (files.length == 0) return;
        int idx = 1;
        int filesLength = 0;
        for (File each : files) {
            if (each.isFile()) {
                filesLength++;
            }
        }
        int step = 100 / filesLength;
        for (File each : files) {
            if (each.isFile()) {
                showProgress(step * idx, each.getName() + "...");
                ftpStoreFile(each.getPath(), each.getName(), ftp);
                idx++;
            }
        }
    }

    public boolean uploadPlanWebdir(Liga liga) throws IOException {
        FTPClient ftp = prepareFTP();
        if (ftp == null) return false;
        try {
            File webdir = getWebDirPlan(liga);
            String theFtpDir = getFtpDirPlan(liga);
            if (!changeDir(ftp, theFtpDir)) return false;
            uploadFtpFiles(ftp, webdir);
            log.info("done upload spielplan from " + webdir.getPath() + " to " + theFtpDir + " for " + liga.getLigaName());
        } finally {
            ftpDisconnect(ftp);
        }
        return true;
    }

    private FTPClient prepareFTP() throws IOException {
        log.info("FTP connect to " + server);
        if (server == null || server.length() == 0) {
            log.warn("no 'ftp.server' configured. no upload possible.");
            return null;
        }
        FTPClient ftp;
        ftp = new FTPClient();
        if (!fakeFtp) {
            ftpConnect(server, ftp);
            ftpLogin(user, password, true, ftp);
            log.info("FTP login successful");
        } else {
            log.info("Faking FTP connect");
        }
        return ftp;
    }

    private void ftpDisconnect(FTPClient ftp) throws IOException {
        if (ftp.isConnected() && !fakeFtp) {
            ftp.disconnect();
            log.info("FTP disconnected");
        }
    }

    private void ftpStoreFile(String local, String remote, FTPClient ftp) throws IOException {
        InputStream input;
        log.info("FTP uploading file " + local + " to " + remote + "...");
        input = new FileInputStream(local);
        try {
            boolean success;

            if (!fakeFtp) {
                success = ftp.storeFile(remote, input);
            } else {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                }
                success = true;
            }

            if (success)
                log.info("FTP upload of file " + remote + " done: successful");
            else
                log.error("FTP upload of file " + remote + " done: failed");
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

    public void showProgress(int percent, String message) {
        if (mainAppFrame != null) {
            mainAppFrame.showProgress(percent, message);
        }
    }
}

