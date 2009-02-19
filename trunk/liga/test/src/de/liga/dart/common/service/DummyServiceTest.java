package de.liga.dart.common.service;

import de.liga.dart.model.Automatenaufsteller;
import junit.framework.TestCase;

import java.util.List;

/**
 * Description:   <br/>
 * User: roman
 * Date: 02.11.2007, 13:14:43
 */
public class DummyServiceTest extends TestCase {

    public DummyServiceTest(String string) {
        super(string);
    }

    public void testAutomatenaufstellerService() {
        TestDummyService srv =
                ServiceFactory.get(TestDummyService.class);

        Automatenaufsteller found = srv.findAutomatenaufstellereById(4);
        assertNotNull(found);

        List<Automatenaufsteller> all = srv.findAllAutomatenaufsteller();
        assertTrue(!all.isEmpty());

    }
}
