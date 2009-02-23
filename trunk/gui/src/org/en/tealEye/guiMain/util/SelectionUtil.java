package org.en.tealEye.guiMain.util;

import de.liga.dart.model.Automatenaufsteller;
import de.liga.dart.model.Liga;
import de.liga.dart.model.Ligaklasse;
import de.liga.dart.model.Spielort;

import javax.swing.*;

/**
 * Description:   <br/>
 * User: roman
 * Date: 09.11.2007, 22:00:36
 */
public class SelectionUtil {
    public enum OPTION {
        ALLE_SELECTED, OBJECT_SELECTED
    }

    public static Liga getLiga(JPanel selectable) {
        if (selectable instanceof LigaSelectable) {
            Object selected = ((LigaSelectable) selectable).getLiga()
                    .getSelectedItem();
            return (selected instanceof Liga) ? (Liga) selected : null;
        } else {
            return null;
        }
    }

    public static Ligaklasse getLigaklasse(JPanel selectable) {
        if (selectable instanceof LigaklasseSelectable) {
            Object selected =
                    ((LigaklasseSelectable) selectable).getLigaklasse()
                            .getSelectedItem();
            return (selected instanceof Ligaklasse) ? (Ligaklasse) selected :
                    null;
        } else {
            return null;
        }
    }

    public static OPTION getLigaklasseOption(JPanel selectable) {
        if (selectable instanceof LigaklasseSelectable) {
            Object selected =
                    ((LigaklasseSelectable) selectable).getLigaklasse()
                            .getSelectedItem();
            if("-alle-".equals(selected)) {
                return OPTION.ALLE_SELECTED;
            } else {
                return OPTION.OBJECT_SELECTED;
            }
        } else {
            return null;
        }
    }

    public static Spielort getSpielort(JPanel selectable) {
        if (selectable instanceof SpielortSelectable) {
            Object selected = ((SpielortSelectable) selectable).getSpielort()
                    .getSelectedItem();
            return (selected instanceof Spielort) ? (Spielort) selected : null;
        } else {
            return null;
        }
    }

    public static Automatenaufsteller getAufsteller(JPanel selectable) {
        if (selectable instanceof AufstellerSelectable) {
            Object selected =
                    ((AufstellerSelectable) selectable).getAutomatenaufsteller()
                            .getSelectedItem();
            return (selected instanceof Automatenaufsteller) ?
                    (Automatenaufsteller) selected : null;
        } else {
            return null;
        }
    }
}
