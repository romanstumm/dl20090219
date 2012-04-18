package de.liga.dart.common.service;

import de.liga.dart.model.LigaPersistence;

/**
 * Common Dart Services
 */
public interface DartService extends Service { 
    /**
     * shutdown dart, release resources:
     * - wait for managed thread to complete.
     * - disconnect from database.
     */
    void shutdown();

    LigaPersistence rejoin(LigaPersistence obj);
    
}
