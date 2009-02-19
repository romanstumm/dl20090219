package de.liga.dart.liga.service;

import de.liga.dart.common.service.Service;
import de.liga.dart.exception.DartException;
import de.liga.dart.model.Liga;

import java.util.List;

/**
 * Serviceinterface der Ligaverwaltung (Entität: Liga)
 */
public interface LigaService extends Service {
    /**
     * insert / update
     * @param aLiga
     */
    void saveLiga(Liga aLiga);
    Liga findLigaById(long ligaId);
    Liga findLigaByName(String ligaName);

    /**
     * all order by name
     * @return
     */
    List<Liga> findAllLiga();
    
    void deleteLiga(Liga aLiga) throws DartException;
}
