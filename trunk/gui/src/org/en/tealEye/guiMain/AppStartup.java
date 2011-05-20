package org.en.tealEye.guiMain;

import de.liga.dart.Application;
import de.liga.dart.common.service.util.HibernateUtil;
import de.liga.dart.database.AutoMigrator;
import de.liga.dart.database.AutoMigratorFactory;
import de.liga.dart.fileimport.FtpWebIO;
import de.liga.dart.fileimport.vfs.DbfImporter;
import de.liga.dart.gruppen.check.Options;
import de.liga.dart.license.Licensable;
import de.liga.dart.license.License;
import org.en.tealEye.framework.SwingUtils;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Properties;

/**
 * Main startup class
 */
public class AppStartup implements Application {
    private static Properties settings;

    public static void main(String args[]) throws Exception {
        AppStartup startup = new AppStartup();
        final MainAppFrame mainappframe = new MainAppFrame();
        startup.loadSettings(null);
        startup.checkLicense(null, mainappframe);
        startup.migrateDatabaseIfRequired();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainappframe.run();
            }
        });
        HibernateUtil.getSessionFactory(); // start init hibernate while mainframe opens 
    }

    public static String getProperty(String key) {
        return settings.getProperty(key);
    }

    private void migrateDatabaseIfRequired() throws Exception {
        AutoMigrator migrator =
                AutoMigratorFactory.create(Application.APPLICATION_VERSION,
                        settings);
        try {
            migrator.migrate();
        } finally {
            migrator.disconnect();
        }
    }

    private String getLicense() {
        return settings.getProperty("ligaverwaltung-license");
    }

    private String getCustomer() {
        return settings.getProperty("ligaverwaltung-customer");
    }

    private void checkLicense(PrintStream out, Licensable object) {
        String msg = "";
        try {
            License licenser =
                    ((Class<License>) Class.forName(getLicense()))
                            .newInstance();
            if (!licenser
                    .isValid(APPLICATION_NAME, APPLICATION_VERSION,
                            getCustomer())) {
                msg = licenser.getInfoMessage();
                throw new IllegalAccessException(msg);
            } else {
                licenser.license(object);
            }
        } catch (Exception e) {
            String m2 =
                    "\nDie Lizenz ist nicht mehr g�ltig.\nBitte kontaktieren Sie den Hersteller: Stefan Pudras (pudras@gmx.net)";
            if (out != null) {
                out.println(msg);
                out.println(m2);
                try {
                    Thread.sleep(15000L);
                } catch (InterruptedException e1) { // empty by purpose
                }
            } else {
                SwingUtils
                        .createOkDialog(null, msg + m2, "Lizenz erforderlich");
            }
            System.exit(-1);
        }
    }

    private void loadSettings(PrintStream out) {
        Properties prop = new Properties();
        try {
            InputStream url = getClass().getClassLoader().getResourceAsStream(
                    "settings.properties");
            if (url == null) throw new FileNotFoundException(
                    "ligaverwaltung/settings.properties");
            prop.load(url);
        } catch (IOException e) {
            if (out != null)
                out.println("ERROR! Cannot find settings: " + e.getMessage());
            else
                SwingUtils.createOkDialog(null,
                        "Cannot find settings: " + e.getMessage(),
                        "Fehler beim Programmstart");
        }
        settings = prop;
        if (settings.containsKey("options.sortBlacklist"))
            Options.sortBlacklist = Boolean.valueOf((String) settings.get("options.sortBlacklist"));
        if (settings.containsKey("options.sortOptional"))
            Options.sortOptional = Boolean.valueOf((String) settings.get("options.sortOptional"));
        if (settings.containsKey("options.sortWhitelist"))
            Options.sortWhitelist = Boolean.valueOf((String) settings.get("options.sortWhitelist"));
        if (settings.containsKey("options.increaseRecursion"))
            Options.increaseRecursion =
                    Boolean.valueOf((String) settings.get("options.increaseRecursion"));
        if (settings.containsKey("options.maxRecursionDepth"))
            Options.maxRecursionDepth =
                    Integer.parseInt((String) settings.get("options.maxRecursionDepth"));
        if (settings.containsKey("options.optimizedRecusionExit"))
            Options.optimizedRecusionExit =
                    Boolean.valueOf((String) settings.get("options.optimizedRecusionExit"));
        DbfImporter.clear();
        FtpWebIO.clear();
        FtpWebIO.setFtpServerUserPassword(
                settings.getProperty("ftp.server"),
                settings.getProperty("ftp.user"),
                settings.getProperty("ftp.password"));
        FtpWebIO.setWebAndFtpDir(
                settings.getProperty("web.dir"),
                settings.getProperty("ftp.dir"));
        if(Boolean.parseBoolean(settings.getProperty("ftp.fake"))) {
            FtpWebIO.setFakeFtp(true);
        }
        
        for (Map.Entry<Object, Object> entry1 : settings.entrySet()) {
            //noinspection RedundantCast
            Map.Entry<String, String> entry = (Map.Entry) entry1;
            if (entry.getKey().startsWith("dbfdir.")) {
                DbfImporter.putLigaSync(entry.getKey().substring("dbfdir.".length()),
                        entry.getValue());
            } else if (entry.getKey().startsWith("webdir.")) {
                FtpWebIO.putLigaDir(entry.getKey().substring("webdir.".length()),
                        entry.getValue());
            } else if (entry.getKey().equals("webfile.rang.suffix")) {
                FtpWebIO.setRangSuffix(entry.getValue());
            } else if (entry.getKey().equals("webfile.plan.suffix")) {
                FtpWebIO.setPlanSuffix(entry.getValue());
            }
        }
    }
}
