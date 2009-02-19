package de.liga.dart.model;

/**
 * Ligateamspiel
 */
public class Ligateamspiel implements java.io.Serializable, LigaPersistence {


    private long spielId;
    private Ligagruppe ligagruppe;
    private Ligateam ligateam;
    private int platzNr;
    private boolean fixiert;

    public Ligateamspiel() {
    }

    public long getSpielId() {
        return this.spielId;
    }

    public void setSpielId(long spielId) {
        this.spielId = spielId;
    }

    public Ligagruppe getLigagruppe() {
        return this.ligagruppe;
    }

    public void setLigagruppe(Ligagruppe ligagruppe) {
        this.ligagruppe = ligagruppe;
    }

    public Ligateam getLigateam() {
        return this.ligateam;
    }

    public void setLigateam(Ligateam ligateam) {
        this.ligateam = ligateam;
    }

    public int getPlatzNr() {
        return this.platzNr;
    }

    public void setPlatzNr(int platzNr) {
        this.platzNr = platzNr;
    }

    /**
     * kein team am spiel ==> spielfrei
     *
     * @return true wenn spielfrei, ansonsten false
     */
    public boolean isSpielfrei() {
        return getLigateam() == null;
    }

    public long getKeyValue() {
        return getSpielId();
    }

    public Class getModelClass() {
        return Ligateamspiel.class;
    }

    public String toInfoString() {
        return getClass().getSimpleName();
    }

    /**
     * @return true wenn team in der gruppe an dieser position festgesetzt ist,
     * und durch die Sortierung weder verschoben noch als Konflikt behandelt
     * werden soll.
     */
    public boolean isFixiert() {
        return fixiert;
    }

    public void setFixiert(boolean fixiert) {
        this.fixiert = fixiert;
    }
}


