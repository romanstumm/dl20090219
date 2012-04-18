package de.liga.dart.gruppen.check.model;

import de.liga.dart.ligateam.model.WunschArt;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Description:   <br/>
 * User: roman
 * Date: 19.12.2007, 21:01:39
 */
public class OConflict implements Serializable, Comparable {
    public static enum Prio {
        /**
         * ganz wichtig
         */
        P1,
        /**
         * noch ziemlich wichtig
         */
        P2,
        /**
         * nicht ganz unwichtig
         */
        P3;

        public int value() {
            return ordinal();
        }
    }

    private final OPosition[] positions;
    private final OWunsch wunsch;
    private final Prio prio;

    public OConflict(OWunsch derUnerfuellteWunsch, OTeam team2) {
        positions = new OTeam[]{derUnerfuellteWunsch.getOtherTeam(), team2};
        wunsch = derUnerfuellteWunsch;
        if (wunsch.getWunschArt() == WunschArt.BLACKLIST_SHALL ||
                wunsch.getWunschArt() == WunschArt.WHITELIST_SHALL) {
            prio = Prio.P2;
        } else {
            prio = Prio.P1;
        }
    }

    public OConflict(OTeam team1, OTeam team2) {
        this(team1, team2, Prio.P1);
    }

    /**
     * entweder *beide* vom Typ Team oder *beide* vom Typ Free!!
     *
     * @param team1
     * @param team2
     * @param optional
     */
    public OConflict(OPosition team1, OPosition team2, Prio prio) {
        positions = new OPosition[]{team1, team2};
        this.prio = prio;
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

    public Prio getPrio() {
        return prio;
    }

    private boolean isOptional() {
        return prio != Prio.P1;
    }

    public boolean isWunsch() {
        return wunsch != null;
    }

    public int getWunschArt() {
        return wunsch == null ? 0 : wunsch.getWunschArt();
    }

    public String toString() {
        return (isOptional() ? "[~" : "[!") + ":" + positions[0] + "+" + positions[1] + "]";
    }

    /**
     * Für nach Priorität aufsteigende Sortierung.
     * Bei gleicher Prio, wird nach GroupId,
     * bei gleicher Group nach Position sortiert
     * @param o
     * @return
     */
    public int compareTo(Object o) {
        OConflict other = (OConflict) o;
        int result;
        result = getPrio().value() - other.getPrio().value();
        if (result == 0) {
            long diff = (getPosition1().getGroup().getGroupId() -
                    other.getPosition1().getGroup().getGroupId());
            if (diff < 0) result = -1;
            else if (diff > 0) result = 1;
            if (result == 0) {
                result = getPosition1().getPosition() - other.getPosition1().getPosition();
            }
        }
        return result;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OConflict oConflict = (OConflict) o;

        if (prio != oConflict.getPrio()) return false;
        if (!Arrays.equals(positions, oConflict.positions)) return false;
        return !(wunsch != null ? !wunsch.equals(oConflict.wunsch) : oConflict.wunsch != null);
    }

    public int hashCode() {
        int result;
        result = Arrays.hashCode(positions);
        result = 31 * result + (wunsch != null ? wunsch.hashCode() : 0);
        result = 31 * result + prio.hashCode();
        return result;
    }

}
