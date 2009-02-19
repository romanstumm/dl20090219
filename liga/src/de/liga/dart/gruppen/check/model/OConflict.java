package de.liga.dart.gruppen.check.model;

import de.liga.dart.ligateam.model.WunschArt;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Description:   <br/>
 * User: roman
 * Date: 19.12.2007, 21:01:39
 */
public class OConflict implements Serializable {
    private final OPosition[] positions;
    private final OWunsch wunsch;
    private final boolean optional;

    public OConflict(OWunsch derUnerfuellteWunsch, OTeam team2) {
        positions = new OTeam[]{derUnerfuellteWunsch.getOtherTeam(), team2};
        wunsch = derUnerfuellteWunsch;
        optional = (wunsch.getWunschArt() == WunschArt.BLACKLIST_SHALL ||
                wunsch.getWunschArt() == WunschArt.WHITELIST_SHALL);
    }

    public OConflict(OTeam team1, OTeam team2) {
        this(team1, team2, false);
    }

    /**
     * entweder *beide* vom Typ Team oder *beide* vom Typ Free!!
     * @param team1
     * @param team2
     * @param optional
     */
    public OConflict(OPosition team1, OPosition team2, boolean optional) {
        positions = new OPosition[]{team1, team2};
        this.optional = optional;
        wunsch = null;
    }

    public boolean isFree() {
        return positions[0].isFree(); //dann auch immer true: && positions[1].isFree();
    }

    public OPosition getPosition1() {
        return positions[0];
    }

    public OPosition getPosition2() {
        return positions[1];
    }

    public OTeam getTeam1() {
        return (OTeam) positions[0];
    }

    public OTeam getTeam2() {
        return (OTeam) positions[1];
    }

    public OPosition[] getPositions() {
        return positions;
    }

    public boolean isOptional() {
        return optional;
    }

    public boolean isWunsch() {
        return wunsch != null;
    }

    public int getWunschArt() {
        return wunsch == null ? 0 : wunsch.getWunschArt();
    }

    public String toString() {
        return (isOptional() ? "~" : "!") + ": " + positions[0] + "+" + positions[1];
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OConflict oConflict = (OConflict) o;

        if (optional != oConflict.optional) return false;
        if (!Arrays.equals(positions, oConflict.positions)) return false;
        return !(wunsch != null ? !wunsch.equals(oConflict.wunsch) : oConflict.wunsch != null);
    }

    public int hashCode() {
        int result;
        result = Arrays.hashCode(positions);
        result = 31 * result + (wunsch != null ? wunsch.hashCode() : 0);
        result = 31 * result + (optional ? 1 : 0);
        return result;
    }
}
