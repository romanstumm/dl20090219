package de.liga.dart.model;


import java.sql.Timestamp;
import java.util.List;

/**
 * Ligagruppe
 */
public class Ligagruppe implements java.io.Serializable, LigaPersistence {
    private long gruppenId;
    private Liga liga;
    private Ligaklasse ligaklasse;
    private int gruppenNr;
    private Timestamp modifiedTimestamp;

    public Ligagruppe() {
    }

    public long getGruppenId() {
        return this.gruppenId;
    }

    public void setGruppenId(long gruppenId) {
        this.gruppenId = gruppenId;
    }

    public Liga getLiga() {
        return this.liga;
    }

    public void setLiga(Liga liga) {
        this.liga = liga;
    }

    public Ligaklasse getLigaklasse() {
        return this.ligaklasse;
    }

    public void setLigaklasse(Ligaklasse ligaklasse) {
        this.ligaklasse = ligaklasse;
    }

    public int getGruppenNr() {
        return this.gruppenNr;
    }

    public void setGruppenNr(int gruppenNr) {
        this.gruppenNr = gruppenNr;
    }

    public Timestamp getModifiedTimestamp() {
        return modifiedTimestamp;
    }

    public void setModifiedTimestamp(Timestamp modifiedTimestamp) {
        this.modifiedTimestamp = modifiedTimestamp;
    }

    /**
     * @return zusammengesetzter Gruppenname aus KlassenName + GruppenNr
     */
    public String getGruppenName() {
        if (ligaklasse == null) {
            return "";
        } else if (getGruppenNr() > 0) {
            return ligaklasse.getKlassenName() + " " + getGruppenNr();
        } else {
            return ligaklasse.getKlassenName() + " ?";
        }
    }

    public String getGruppenNameCompact() {
        if (ligaklasse == null) {
            return "";
        } else if (getGruppenNr() > 0) {
            return ligaklasse.getKlassenName() + getGruppenNr();
        } else {
            return ligaklasse.getKlassenName();
        }
    }

    public String toString() {
        return getGruppenName();
    }

    public Ligateamspiel findSpiel(List<Ligateamspiel> spiele, int platzNr) {
        if (platzNr < 1 || platzNr > 8)
            throw new IllegalArgumentException("1<=platznr<=8");
        for (Ligateamspiel spiel : spiele) {
            if (spiel.getPlatzNr() == platzNr) return spiel;
        }
        return null;
    }

    public long getKeyValue() {
        return getGruppenId();
    }

    public Class getModelClass() {
        return Ligagruppe.class;
    }

    public String toInfoString() {
        return "Gruppe \"" + getGruppenName() + "\"";
    }
}


