package de.liga.dart.spielort.service;

import de.liga.dart.common.service.Service;
import de.liga.dart.exception.DartException;
import de.liga.dart.model.Automatenaufsteller;
import de.liga.dart.model.Liga;
import de.liga.dart.model.Spielort;

import java.util.List;

/**
 * Description:   <br/>
 * User: roman
 * Date: 02.11.2007, 13:34:35
 */
public interface SpielortService extends Service {
    void saveSpielort(Spielort obj);

    Spielort findSpielortById(long id);

    /**
     * all order by name
     *
     * @return
     */
    List<Spielort> findAllSpielort();

    /**
     * filterung nach liga
     *
     * @param liga - wenn NULL, dann liefert die Query nur die Spielorte OHNE Liga!
     * @return
     */
    List<Spielort> findAllSpielortByLiga(Liga liga);

    /**
     * @param liga - kann null sein
     * @param aufsteller - kann null sein
     * @return
     */
    List<Spielort> findAllSpielortByLigaAufsteller(Liga liga,
                                                   Automatenaufsteller aufsteller);

    void deleteSpielort(Spielort obj) throws DartException;
}
