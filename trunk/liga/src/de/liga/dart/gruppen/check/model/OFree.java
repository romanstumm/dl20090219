package de.liga.dart.gruppen.check.model;

import de.liga.dart.gruppen.PositionStatus;

import java.io.Serializable;

/**
 * Description:   <br/>
 * User: roman
 * Date: 18.11.2007, 10:38:00
 */
public class OFree extends OPosition implements Serializable {

    private OFree other;

    /**
     * als PositionStatus sind
     * hier nur moeglich: {@link PositionStatus#C_SPIELFREI_WECHSEL_SOLL} oder {@link PositionStatus#FREI}
     */

    public OFree(OGroup group, boolean fixiert) {
        super(group, fixiert);
    }

    public boolean isFree() {
        return true;
    }

    public String toString() {
        return super.toString() + "G" + getGroup().getGroupId() + "=[/]";
    }

    public OFree getOther() {
        return other;
    }

    public void setOther(OFree other) {
        this.other = other;
    }

    public int getOtherPosition() {
        for (int[] wechsel : SPIELFREI_WECHSEL) {
            if (getPosition() == wechsel[0]) return wechsel[1];
            if (getPosition() == wechsel[1]) return wechsel[0];
        }
        throw new IllegalStateException(); // keine gueltige Position, sollte nie bis hierhin kommen!
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OPosition oPosition = (OPosition) o;
        if (position != oPosition.position) return false;
        return group.equals(oPosition.group);
    }

    public int hashCode() {
        return (group != null ? group.hashCode() : 0);
    }
}
