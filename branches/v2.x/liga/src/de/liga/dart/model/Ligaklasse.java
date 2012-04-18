package de.liga.dart.model;

/**
 * Ligaklasse
 */
public class Ligaklasse implements java.io.Serializable, LigaPersistence {


     private long klassenId;
     private String klassenName;
     private int rang;

    public Ligaklasse() {
    }

    public long getKlassenId() {
        return this.klassenId;
    }
    
    public void setKlassenId(long klassenId) {
        this.klassenId = klassenId;
    }
    public String getKlassenName() {
        return this.klassenName;
    }
    
    public void setKlassenName(String klassenName) {
        this.klassenName = klassenName;
    }
    public int getRang() {
        return this.rang;
    }
    
    public void setRang(int rang) {
        this.rang = rang;
    }

    public String toString() {
        return getKlassenName();
    }

    public long getKeyValue() {
        return getKlassenId();
    }

    public Class getModelClass() {
        return Ligaklasse.class;
    }

    public String toInfoString() {
        return "Klasse \"" + getKlassenName() + "\"";                 
    }
}


