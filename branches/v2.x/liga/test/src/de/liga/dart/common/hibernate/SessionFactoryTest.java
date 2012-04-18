package de.liga.dart.common.hibernate;

import de.liga.dart.common.service.util.HibernateUtil;
import junit.framework.TestCase;
import org.hibernate.SessionFactory;

public class SessionFactoryTest extends TestCase {
    public SessionFactoryTest(String string) {
        super(string);
    }

    public void testHibernateUtil() {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        assertNotNull(sf);
    }
}
