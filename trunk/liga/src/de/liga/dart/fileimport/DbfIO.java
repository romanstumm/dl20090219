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
    // key: liga, value: C:/DRLIGA/<liga>/LIGA path
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

    public void connect(String liganame) throws SQLException {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        } catch (ClassNotFoundException e) {
            log.fatal("sun.jdbc.odbc.JdbcOdbcDriver missing in classpath!!", e);
        }
        String odbcName = "jdbc:odbc:VFS-" + liganame.toUpperCase();
        try {
            connection = DriverManager.getConnection(odbcName);
            connection.setAutoCommit(true);
        } catch (SQLException ex) {
            throw new SQLException("Konnte nicht mit Datenquelle '" + odbcName + "' verbinden!", ex);
        }
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
                    for (String ligaName : SYNC_DIRS.keySet()) {
                        percent += step;
                        if (progressIndicator != null) {
                            progressIndicator
                                    .showProgress(percent,
                                            actionVerb() + " " + ligaName + " ...");
                        }
                        Liga liga = ligaService.findLigaByName(ligaName);
                        if (liga == null) {
                            log.error("Liga " +ligaName + " fehlt in der Datenbank!");
                        } else {
                            try {
                                connect(ligaName);
                                exchangeData(liga);
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

    protected abstract void exchangeData(Liga liga) throws SQLException;

    public boolean start(final String liganame) {
        synchronized (SYNC_DIRS) {
            success = true;
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
                            connect(liganame);
                            exchangeData(liga);
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
