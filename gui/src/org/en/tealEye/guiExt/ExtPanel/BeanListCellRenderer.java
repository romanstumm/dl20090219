package org.en.tealEye.guiExt.ExtPanel;

import de.liga.util.Value;

import javax.swing.*;
import java.awt.*;

/**
 * Description: Code based on DefaultListCellRenderer.
 * Neuheit: Man kann die Methode, die zur Darstellung des Objekts
 * in der Liste verwendet werden soll, durch setProperty() setzen.
 * Sie wird per Reflection aufgerufen.
 *
 * Der DefaultListCellRenderer verwendet immer hard-coded die Methode toString().<br/>
 * User: roman
 * Date: 07.02.2008, 21:19:47
 */
public class BeanListCellRenderer extends DefaultListCellRenderer {
    private String property;

    public String getProperty() {
        return property;
    }

    // enhancement: kann performance-getuned werden, wenn man hier
    // schon prueft, ob property einen . enthält um
    // die Pfadauswertung bei getPath() zu ersparen
    // und stattdessen eine andere Methode von Value zu rufen...
    public void setProperty(String property) {
        this.property = property;
    }

    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
        setComponentOrientation(list.getComponentOrientation());
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        if (value instanceof Icon) {
            setIcon((Icon) value);
            setText("");
        } else {
            setIcon(null);
            if (value == null) {
                setText("");
            } else if (property != null) {
                setText(String.valueOf(
                        Value.getDefault().getPath(value, property, "")));
            } else {
                setText(value.toString());
            }
        }

        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setBorder((cellHasFocus) ?
                UIManager.getBorder("List.focusCellHighlightBorder") :
                noFocusBorder);

        return this;
    }

}
