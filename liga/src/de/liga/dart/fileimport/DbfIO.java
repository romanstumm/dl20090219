package de.liga.dart.fileimport;

import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.exception.DartException;
import de.liga.dart.gruppen.check.ProgressIndicator;
import de.liga.dart.liga.service.LigaService;
import de.liga.dart.model.Liga;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:   <br/>
 * User: roman
 * Date: 17.05.2008, 14:47:57
 */
public abstract class DbfIO implements DataExchanger {
    private static final Log log = LogFactory.getLog(DbfIO.class);
    protected static final Map<String, String> SYNC_DIRS = new HashMap();

    protected Connection connection;
    private boolean success;
    private ProgressIndicator progressIndicator;

    protected Date myCurrentSaison;

    public Connection getConnection() {
        return connection;
    }

    protected void readCurrentSaison(Statement stmt) throws SQLException {
        // ermittele aktuelle Saison
        ResultSet resultSet = stmt.executeQuery("SELECT ANW_SAISON FROM LITANW");
        try {
            if (resultSet.next()) {
                myCurrentSaison = resultSet.getDate(1);
            }
        } finally {
            resultSet.close();
        }
        if (myCurrentSaison == null)
            throw new DartException("Kann aktuelle Saison nicht auslesen.");
    }



    public static void putLigaSync(String liga, String dir) {
        SYNC_DIRS.put(liga, dir);
    }

    public static void clear() {
        SYNC_DIRS.clear();
    }

    public static Map<String, String> getSyncDirs() {
        return SYNC_DIRS;
    }


    public void setProgressIndicator(ProgressIndicator indicator) {
        synchronized (SYNC_DIRS) {
            progressIndicator = indicator;
        }
    }

    public DbfIO() {
    }

    public void connect(String liganame, String path) throws SQLException {
        connectOdbc(liganame);
//        connectStels(path);
    }
/*
    private void connectStels(String path) throws SQLException {
        try {
            Class.forName("jstels.jdbc.dbf.DBFDriver");
        } catch (ClassNotFoundException e) {
            log.fatal("jstels.jdbc.dbf.DBFDriver missing in classpath!!", e);
        }
        Properties props = new Properties();
        props.setProperty("caching", "false");
        props.put("charset", "IBM850");
        connection = DriverManager.getConnection("jdbc:jstels:dbf:" + path, props);
        connection.setAutoCommit(true);
        hack();
    }*/

/*
    public static void hack() {
        try {
            Field h = CommonStatement.class.getDeclaredField("h");
            h.setAccessible(true);
            h.set(null, Integer.MAX_VALUE);
        } catch (Exception ex) {
            log.error("cannot hack!!", ex);
        }
    }*/

    private void connectOdbc(String liganame) throws SQLException {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        } catch (ClassNotFoundException e) {
            log.fatal("sun.jdbc.odbc.JdbcOdbcDriver missing in classpath!!", e);
        }
        connection = DriverManager.getConnection("jdbc:odbc:VFS-" + liganame.toUpperCase());
        connection.setAutoCommit(true);
    }

    public void disconnect() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    protected String actionVerb() {
        return "Verarbeite";
    }

    protected String actionName() {
        return "Verarbeitung";
    }

    public boolean start() {
        synchronized (SYNC_DIRS) {
            success = true;
            ServiceFactory.runAsTransaction(new Runnable() {
                public void run() {
                    LigaService ligaService = ServiceFactory.get(LigaService.class);
                    int step = 100 / (SYNC_DIRS.size() + 1);
                    int percent = 0;
                    for (Map.Entry<String, String> entry : SYNC_DIRS.entrySet()) {
                        percent += step;
                        if (progressIndicator != null) {
                            progressIndicator
                                    .showProgress(percent,
                                            actionVerb() + " " + entry.getKey() + " ...");
                        }
                        Liga liga = ligaService.findLigaByName(entry.getKey());
                        if (liga == null) {
                            log.error("Liga " + entry.getKey() + " fehlt in der Datenbank!");
                        } else {
                            final String path = entry.getValue();
                            try {
                                connect(entry.getKey(), path);
                                exchangeData(liga, path);
                            } catch (SQLException ex) {
                                log.error("Fehler beim " + actionName() + " mit " +
                                        liga.getLigaName(),
                                        ex);
                                success = false;
                            } finally {
                                if (progressIndicator != null) {
                                    progressIndicator.showProgress(0, "");
                                }
                                try {
                                    disconnect();
                                } catch (SQLException e) {
                                    log.error("Fehler beim disconnect", e);
                                }
                            }
                        }
                    }
                }
            });
            return success;
        }
    }

    protected abstract void exchangeData(Liga liga, String path) throws SQLException;

    public boolean start(final String liganame) {
        synchronized (SYNC_DIRS) {
            success = true;
            final String path = getSyncDir(liganame);
            ServiceFactory.runAsTransaction(new Runnable() {
                public void run() {
                    try {
                        if (progressIndicator != null) {
                            progressIndicator
                                    .showProgress(50,
                                            actionVerb() + " " + liganame + " ...");
                        }
                        LigaService ligaService = ServiceFactory.get(LigaService.class);
                        Liga liga = ligaService.findLigaByName(liganame);
                        if (liga == null) {
                            throw new DartException(
                                    "Liga " + liganame + " fehlt in der Datenbank!");
                        } else {
                            connect(liganame, path);
                            exchangeData(liga, path);
                        }
                    } catch (SQLException ex) {
                        log.error("Fehler beim " + actionName() + " mit " + liganame, ex);
                        success = false;
                    } finally {
                        try {
                            disconnect();
                        } catch (SQLException e) {
                            log.error("Fehler beim disconnect", e);
                        }
                        if (progressIndicator != null) {
                            progressIndicator.showProgress(0, "");
                        }
                    }
                }
            });
            return success;
        }
    }

    public static String getSyncDir(String liganame) {
        final String path = SYNC_DIRS.get(liganame);
        if (path == null) throw new DartException("dbfdir." + liganame +
                " nicht bekannt (bitte settings.properties pruefen)");
        return path;
    }

    public ProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }
}
