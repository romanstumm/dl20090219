package de.liga.dart.database;

import java.util.Map;

/**
 * Description:   <br/>
 * User: roman
 * Date: 07.02.2008, 22:49:55
 */
public class AutoMigratorFactory {
    public static AutoMigrator create(String appVersion, Map settings)
    {
        return new AutoMigratorImpl(appVersion, settings);
    }
}
