package de.liga.dart.exception;

/**
 * Fachlicher Fehler, Validierungsfehler, Exception als möglicher
 * Rückgabewert einer Methode der Serviceschicht.
 */
public class DartException extends RuntimeException {

    public DartException(String message) {
        super(message);
    }

    public DartException(String message, Throwable cause) {
        super(message, cause);
    }

    public DartException(Throwable cause) {
        super(cause);
    }
}
