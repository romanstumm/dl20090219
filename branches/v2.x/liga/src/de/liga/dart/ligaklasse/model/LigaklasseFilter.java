package de.liga.dart.ligaklasse.model;

import de.liga.dart.model.Ligaklasse;

/**
 * Description: Modell, das eine Filteroption für Ligaklassen enthält.  <br/>
 * User: roman
 * Date: 08.11.2009, 13:49:47
 */
public class LigaklasseFilter {
    private final String label;
    private final Ligaklasse[] klassen;

    public LigaklasseFilter(String label) {
        this.label = label;
        this.klassen = null;
    }

    public LigaklasseFilter(Ligaklasse klasse) {
        this.klassen =  new Ligaklasse[] { klasse };
        this.label = klasse.toString();
    }

    public LigaklasseFilter(String label, Ligaklasse... klassen) {
        this.label = label;
        this.klassen = klassen;
    }

    public String toString() {
        return getLabel();
    }

    public String getLabel() {
        return label;
    }

    public Ligaklasse[] getKlassen() {
        return klassen;
    }
}
