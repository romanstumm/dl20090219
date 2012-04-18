package org.en.tealEye.guiExt.ExtPanel;

import de.liga.util.Value;

import javax.swing.*;

/**
 * Description: Code based on JTextField.
 * Neuheit: Man kann die Methode, die zur Darstellung des Objekts
 * verwendet werden soll, durch setProperty() setzen.
 * Sie wird per Reflection aufgerufen.
 *
 * JTextField verwendet immer hard-coded die Methode toString().<br/>
 * User: roman
 * Date: 07.02.2008, 21:19:47
 */
public class JExtTextField extends JTextField {

    private Object object;
    private String property;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setObject(Object obj) {
        this.object = obj;
        if (obj != null) {
            if (property == null) {
                setText(obj.toString());
            } else {
                setText(String.valueOf(
                        Value.getDefault().getPath(obj, property, "")));
            }
        } else {
            setText("");
        }
    }

    public Object getObject() {
        return object;
    }

    public void removeObject() {
        this.object = null;
    }

    public boolean isEmpty() {
        return "".equals(this.getText());
    }
}
