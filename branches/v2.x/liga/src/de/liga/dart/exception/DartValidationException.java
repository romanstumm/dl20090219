package de.liga.dart.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: Validierungsfehler mit Begründung und anzeigbarer Meldung
 * (getLocalizedMessage())<br/>
 * User: roman
 * Date: 03.11.2007, 14:04:36
 */
public class DartValidationException extends DartException
{
    private List<ValidationMessage> messages = new ArrayList();

    public DartValidationException(List<ValidationMessage> aList) {
        super(aList.toString());
        messages = aList;
    }

    public DartValidationException(String message) {
        super(message);
    }

    public List<ValidationMessage> getMessages() {
        return messages;
    }
}
