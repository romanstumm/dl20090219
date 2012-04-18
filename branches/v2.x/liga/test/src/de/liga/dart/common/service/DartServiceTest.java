package de.liga.dart.common.service;

import de.liga.dart.common.service.util.HibernateUtil;
import de.liga.util.thread.ManagedThread;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

/**
 * Description:   <br/>
 * User: roman
 * Date: 04.11.2007, 07:57:50
 */
public class DartServiceTest extends TestCase {
    private static final Log log = LogFactory.getLog(DartServiceTest.class);
    public DartServiceTest(String string) {
        super(string);
    }

    public void testShutdown() {
        new ManagedThread(new Runnable() {

            public void run() {
                try {
                    log.error("waiting");
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    log.error("interrupted");
                }
                log.error("finished");
            }
        }).start();
        ServiceFactory.get(DartService.class).shutdown();

        Session s = HibernateUtil.getCurrentSession();
        assertNull(s);
    }
}
