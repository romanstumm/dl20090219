package de.liga.dart.exception;

import java.io.Serializable;

/**
 * Description:   <br/>
 * User: roman
 * Date: 03.11.2007, 14:31:09
 */
public class ValidationMessage implements Serializable {
    private ValidationReason reason;
    private Object entity;
    private String attribute;

    public ValidationMessage(ValidationReason reason, Object entity,
                             String attribute) {
        this.reason = reason;
        this.entity = entity;
        this.attribute = attribute;
    }

    public ValidationReason getReason() {
        return reason;
    }

    public Object getEntity() {
        return entity;
    }

    public String getAttribute() {
        return attribute;
    }

    public String toString() {   // NON-NLS
        return attribute + (entity != null
                ? (" in " + entity.getClass().getSimpleName())
                : "") + ": " + reason.getText();
    }
}
