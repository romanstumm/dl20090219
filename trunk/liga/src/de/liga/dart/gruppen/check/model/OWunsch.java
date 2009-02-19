package de.liga.dart.gruppen.check.model;

import java.io.Serializable;

/**
 * Description:   <br/>
 * User: roman
 * Date: 09.02.2008, 13:12:50
 */
public class OWunsch implements Serializable {
    private final OTeam otherTeam;
    private final int wunschArt;
    private final long wunschId;

    public OWunsch(long wunschId, OTeam otherTeam, int wunschArt) {
        this.wunschId = wunschId;
        this.otherTeam = otherTeam;
        this.wunschArt = wunschArt;
    }

    public OTeam getOtherTeam() {
        return otherTeam;
    }

    public int getWunschArt() {
        return wunschArt;
    }

    /**
     * das identifiziert den Wunsch eindeutig
     * @return
     */
    public long getWunschId() {
        return wunschId;
    }

    public String toString() {
        return "W(" + wunschId + ";" + otherTeam + ";" + wunschArt + ")";
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OWunsch oWunsch = (OWunsch) o;
        return wunschId == oWunsch.wunschId;
    }

    public int hashCode() {
        return (int) (wunschId ^ (wunschId >>> 32));
    }
}
