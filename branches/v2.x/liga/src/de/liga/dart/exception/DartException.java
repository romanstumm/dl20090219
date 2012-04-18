package de.liga.dart.exception;

/**
 * Fachlicher Fehler, Validierungsfehler, Exception als m�glicher
 * R�ckgabewert einer Methode der Serviceschicht.
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
