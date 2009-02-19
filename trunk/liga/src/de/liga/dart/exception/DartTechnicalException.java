package de.liga.dart.exception;

/**
 * Technische Exception f�r Bugs, verlorene Datenbank etc. (NullPointer usw.)
 */
public class DartTechnicalException extends DartException {

    public DartTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public DartTechnicalException(Throwable cause) {
        super(cause);
    }
}
