package de.liga.dart.exception;

import java.lang.reflect.InvocationTargetException;

/**
 * Description:   <br/>
 * User: roman
 * Date: 02.11.2007, 11:28:39
 */
public final class ExceptionHandler {
    public static Throwable wrap(Throwable ex) throws Throwable {
        while(ex instanceof InvocationTargetException) {
            ex = ex.getCause();
        }
        if (ex instanceof DartException) {
            return ex;
        } else {
            return new DartTechnicalException(ex);
        }
    }
}
