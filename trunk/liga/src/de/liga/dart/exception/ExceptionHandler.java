package de.liga.dart.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * Description:   <br/>
 * User: roman
 * Date: 02.11.2007, 11:28:39
 */
public final class ExceptionHandler {
    protected static final Log log = LogFactory.getLog(ExceptionHandler.class);

    public static Throwable wrap(Throwable ex) throws Throwable {
        while(ex instanceof InvocationTargetException) {
            ex = ex.getCause();
        }
        log.error(ex.getMessage(), ex);
        if (ex instanceof DartException) {
            return ex;
        } else {
            return new DartTechnicalException(ex);
        }
    }
}
