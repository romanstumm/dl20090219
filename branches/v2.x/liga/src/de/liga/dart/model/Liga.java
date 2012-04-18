package de.liga.dart.model;

/**
 * Liga
 */
public class Liga implements java.io.Serializable, LigaPersistence {
    private long ligaId;
    private String ligaName;

    public Liga() {
    }

    public long getLigaId() {
        return this.ligaId;
    }

    public void setLigaId(long ligaId) {
        this.ligaId = ligaId;
    }

    public String getLigaName() {
        return this.ligaName;
    }

    public void setLigaName(String ligaName) {
        this.ligaName = ligaName;
    }

    public String toString() {
        return ligaName;
    }


    public long getKeyValue() {
        return getLigaId();
    }

    public Class getModelClass() {
        return Liga.class;
    }

    public String toInfoString() {
        return "Liga \"" + ligaName + "\"";
    }
}


