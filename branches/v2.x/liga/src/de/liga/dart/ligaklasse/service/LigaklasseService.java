package de.liga.dart.ligaklasse.service;

import de.liga.dart.common.service.Service;
import de.liga.dart.exception.DartException;
import de.liga.dart.model.Ligaklasse;

import java.util.List;

/**
 * Description:   <br/>
 * User: roman
 * Date: 02.11.2007, 13:03:42
 */
public interface LigaklasseService extends Service {
    void saveLigaklasse(Ligaklasse aLiga);

    Ligaklasse findLigaklasseById(long ligaId);

    Ligaklasse findLigaklasseByName(String ligaName);

    /**
     * all order by rang
     * @return
     */
    List<Ligaklasse> findAllLigaklasse();

    void deleteLigaklasse(Ligaklasse aLiga) throws DartException;
}
