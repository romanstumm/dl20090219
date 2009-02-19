package de.liga.dart.common.service;

import de.liga.dart.common.service.util.HibernateUtil;
import de.liga.dart.exception.DartException;
import org.hibernate.Session;

/**
 * Description: superclass for service implementations  <br/>
 * User: roman
 * Date: 02.11.2007, 11:35:10
 */
public abstract class AbstractService implements Service {
    protected Session getSession() {
        return HibernateUtil.getCurrentSession();
    }

    protected void save(Object obj) {
        getSession().saveOrUpdate(obj);
    }

    protected <T> T findById(Class<T> cls, long id) {
        return (T) getSession().get(cls, id);
    }

    protected void delete(Object obj) throws DartException {
        try {
            getSession().delete(obj);
            getSession().flush();
        } catch (Exception ex) { // NON-NLS
            throw new DartException("Kann " + obj.getClass().getSimpleName() + " (" + obj+
                    ") nicht löschen, weil es noch verwendet wird.", ex);
        }
    }
}
