package de.liga.dart.common.service.util;

/**
 * Description:   <br/>
 * User: roman
 * Date: 02.11.2007, 11:08:11
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public final class HibernateUtil {
    private static final Log log = LogFactory.getLog(HibernateUtil.class);

    private static final SessionFactory sessionFactory;

    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            log.fatal("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getCurrentSession() {
        if(sessionFactory.isClosed()) return null;
        return getSessionFactory().getCurrentSession();
    }

    public static Transaction getCurrentTransaction() {
        if(sessionFactory.isClosed()) return null;
        return getCurrentSession().getTransaction();
    }
}