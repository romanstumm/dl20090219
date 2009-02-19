package de.liga.dart.liga.service;

import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.model.Liga;
import junit.framework.TestCase;

import java.util.List;

/**
 * Description:   <br/>
 * User: roman
 * Date: 02.11.2007, 11:39:06
 */
public class LigaServiceTest extends TestCase {
    public LigaServiceTest(String string) {
        super(string);
    }

    public void testBasicLigaAccess() {
        LigaService service = ServiceFactory.get(LigaService.class);

        Liga liga = service.findLigaByName("Münster");
        if(liga == null) {
            liga = new Liga();
            liga.setLigaName("Münster");
            service.saveLiga(liga);
        }
        List all = service.findAllLiga();
        assertTrue(!all.isEmpty());
        Liga one = service.findLigaById(liga.getLigaId());
        assertTrue(one != liga);
    }
}
