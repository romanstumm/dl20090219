package de.liga.dart.automatenaufsteller.service;

import de.liga.dart.common.service.Service;
import de.liga.dart.exception.DartException;
import de.liga.dart.model.Automatenaufsteller;
import de.liga.dart.model.Liga;

import java.util.List;

/**
 * Description:   <br/>
 * User: roman
 * Date: 02.11.2007, 13:12:41
 */
public interface AutomatenaufstellerService extends Service {
    void saveAutomatenaufsteller(Automatenaufsteller obj);

    Automatenaufsteller findAutomatenaufstellerById(long id);

    /**
     * alle ungefiltert anzeigen
     * @return
     */
    List<Automatenaufsteller> findAllAutomatenaufsteller();

    /**
     * filterung nach liga
     * @param liga - wenn NULL, dann liefert die Query nur die Aufsteller OHNE Liga!
     * @return
     */
    List<Automatenaufsteller> findAllAutomatenaufstellerByLiga(Liga liga);

    void deleteAutomatenaufsteller(Automatenaufsteller obj)
            throws DartException;
}
