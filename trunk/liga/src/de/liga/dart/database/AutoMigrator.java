package de.liga.dart.database;

import java.sql.SQLException;

/**
 * Description:
 * Pr�ft ob Datenbankmigration erforderlich und f�hrt sie dann durch.
 * <br/>
 * User: roman
 * Date: 07.02.2008, 22:49:21
 */
public interface AutoMigrator {
    void connect() throws SQLException;

    boolean isVersionUpToDate() throws SQLException;

    void migrate() throws Exception;

    void disconnect() throws SQLException;
}
