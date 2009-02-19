package de.liga.dart.exception;

/**
 * Description:   <br/>
 * User: roman
 * Date: 03.11.2007, 14:32:08
 */
public enum ValidationReason {
    MISSING("fehlt"),  // NON-NLS
    WRONG("ungültig"); // NON-NLS

    private String text;

    ValidationReason(String s) {
        text = s;
    }

    public String getText() {
        return text;
    }
}
