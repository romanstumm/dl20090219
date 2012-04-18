package de.liga.dart.common.service.util;

import de.liga.dart.exception.ExceptionHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Description: Jeder Aufruf wird in eine Transaction
 * gehuellt, falls noch keine zuvor lief.<br/>
 * User: roman
 * Date: 02.11.2007, 11:21:40
 */
public class TransactServiceHandler implements InvocationHandler {
    private static final Log log =
            LogFactory.getLog(TransactServiceHandler.class);
    final Object target;

    public TransactServiceHandler(Object target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        final boolean startTA;
        try {
            startTA =
                    !HibernateUtil.getCurrentTransaction().isActive();
            if (startTA) HibernateUtil.getCurrentSession().beginTransaction();
            Object result = method.invoke(target, args);
            if (startTA && HibernateUtil.getCurrentTransaction().isActive())
                HibernateUtil.getCurrentTransaction().commit();
            return result;
        } catch (Exception ex) {
            if (HibernateUtil.getCurrentTransaction().isActive()) {
                try {
                    HibernateUtil.getCurrentTransaction().rollback();
                } catch (Exception ex2) {
                    log.error("error during rollback", ex2);
                }
            }
            throw ExceptionHandler.wrap(ex);
        }
    }
}
