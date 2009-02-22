package de.liga.dart.database;

import de.liga.util.CalendarUtils;
import de.liga.util.ResourceUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Description:   <br/>
 * User: roman
 * Date: 07.02.2008, 22:50:47
 */
public class AutoMigratorImpl implements AutoMigrator {
    private static final Log log = LogFactory.getLog(AutoMigratorImpl.class);

    private final String appVersion;
    private Map<String, String> settings;

    private static final String JDBC_DRIVER = "jdbc.driver";
    private static final String JDBC_URL = "jdbc.url";
    private static final String JDBC_USER = "jdbc.user";
    private static final String JDBC_PASSWORD = "jdbc.password";
    private static final String MIGRATE_DIR = "migrate.dir";
    private static final String CP_PREFIX = "cp://";

    private Connection connection;
    private String currentVersion = "1.0.0"; // fange bei 1.0.0 an.

    public AutoMigratorImpl(String appVersion, Map<String, String> settings) {
        this.appVersion = appVersion;
        this.settings = settings;
    }

    public boolean isVersionUpToDate() throws SQLException {
        connect();
        Statement stmt = connection.createStatement();
        try {
            ResultSet result =
                    stmt.executeQuery("SELECT VERSION FROM DB_VERSION");
            if (result.next()) {
                currentVersion = result.getString(1);
            }
            result.close();
        } catch (SQLException ex) {
            createDBVersion(stmt);
        } finally {
            stmt.close();
        }

        return appVersion.equals(currentVersion);
    }

    private void createDBVersion(Statement stmt) throws SQLException {
        execSql(stmt,
                "CREATE TABLE DB_VERSION (version VARCHAR(100), since TIMESTAMP)");

        PreparedStatement pstmt =
                prepSql("INSERT INTO DB_VERSION (version, since) VALUES (?, ?)");
        try {
            pstmt.setString(1, currentVersion);
            pstmt.setTimestamp(2, CalendarUtils.currentTimestamp());
            pstmt.execute();
        } finally {
            pstmt.close();
        }
    }

    private boolean execSql(Statement stmt, String sql) throws SQLException {
        log.info(sql);
        return stmt.execute(sql);
    }

    private PreparedStatement prepSql(String sql) throws SQLException {
        log.info(sql);
        return connection.prepareStatement(sql);
    }

    public void connect() throws SQLException {
        if (connection == null) {
            try {
                Class.forName(settings.get(JDBC_DRIVER));
                connection = DriverManager.getConnection(settings.get(JDBC_URL),
                        settings.get(JDBC_USER), settings.get(JDBC_PASSWORD));
                connection.setAutoCommit(true);
            } catch (ClassNotFoundException e) {
                throw new SQLException(
                        "Driver " + settings.get(JDBC_DRIVER) + " not found", e);
            }
        }
    }

    public void disconnect() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    public void migrate() throws Exception {
        if (!isVersionUpToDate()) {
            List<DBVersion> versions = filterVersionFiles();
            if (!versions.isEmpty()) {
                Statement stmt = connection.createStatement();
                try {
                    for (DBVersion each : versions) {
                        log.info("About to execute file " + each.getUrl());
                        execSqlFile(each.getUrl(), stmt);
                        updateDBVersion(each.getVersion());
                    }
                    updateDBVersion(appVersion);
                    log.info("** ALL files DONE successfully! **");
                } finally {
                    stmt.close();
                }
            } else {
                updateDBVersion(appVersion);
            }
        }
    }

    private void updateDBVersion(String version) throws SQLException {
        PreparedStatement pstmt =
                prepSql("UPDATE DB_VERSION SET version=?, since=?");
        try {
            pstmt.setString(1, version);
            pstmt.setTimestamp(2, CalendarUtils.currentTimestamp());
            pstmt.execute();
        } finally {
            pstmt.close();
        }
    }

    private List<DBVersion> filterVersionFiles() throws IOException {
        URL[] files;
        if (settings.get(MIGRATE_DIR).startsWith(CP_PREFIX)) {
            files = ResourceUtils.listFileResources(getClass().getClassLoader().getResource(
                    settings.get(MIGRATE_DIR).substring(CP_PREFIX.length())
            ));
        } else {
            File[] ffiles = new File(settings.get(MIGRATE_DIR)).listFiles();
            files = new URL[ffiles.length];
            for (int i = 0; i < ffiles.length; i++) {
                files[i] = ffiles[i].toURI().toURL();
            }
        }

        if (files == null)
            throw new FileNotFoundException(settings.get(MIGRATE_DIR));
        List<DBVersion> versions = new ArrayList<DBVersion>(files.length);
        DBVersion current = new DBVersion(currentVersion);
        DBVersion maxVersion = new DBVersion(appVersion);
        for (URL each : files) {
            String fileName = each.getPath().substring(each.getPath().lastIndexOf("/") + 1);
            if (fileName.startsWith("up-") &&
                    fileName.endsWith(".sql")) {
                DBVersion version =
                        new DBVersion(fileName.substring(3,
                                fileName.length() - 4));
                if (version.isHigherThan(current) &&
                        !version.isHigherThan(maxVersion)) {
                    version.setUrl(each);
                    versions.add(version);
                }
            }
        }
        Collections.sort(versions);
        return versions;
    }

    private void execSqlFile(URL file, Statement stmt)
            throws IOException, SQLException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(file.openStream()));
        try {
            StringBuilder buf = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                if (line.trim().startsWith("--")) {
                    log.info("Comment: " + line);
                } else {
                    int pos = line.indexOf(";");
                    if (pos >= 0) {
                        if (pos > 0) {
                            appendFragment(buf, line.substring(0, pos));
                        }
                        if (buf.length() > 0) {
                            execSql(stmt, buf.toString());
                        }
                        buf = new StringBuilder();
                        if (pos + 2 < line.length()) {
                            line = line.substring(pos + 1);
                            continue;
                        }
                    } else {
                        appendFragment(buf, line);
                    }
                }
                line = reader.readLine();
            }
            if (buf.length() > 0) {
                execSql(stmt, buf.toString());
            }
        } finally {
            reader.close();
        }
    }

    private void appendFragment(StringBuilder buf, String line) {
        if (line != null && line.length() > 0) {
            if (buf.length() > 0 &&
                    !Character.isWhitespace(buf.charAt(buf.length() - 1))) {
                buf.append(' ');
            }
            buf.append(line);
        }
    }
}
