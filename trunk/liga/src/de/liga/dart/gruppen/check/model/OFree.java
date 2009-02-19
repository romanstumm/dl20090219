package de.liga.dart.gruppen.check.model;

import de.liga.dart.gruppen.TeamStatus;

import java.io.Serializable;

/**
 * Description:   <br/>
 * User: roman
 * Date: 18.11.2007, 10:38:00
 */
public class OFree extends OPosition implements Serializable {

    private OFree other;

    /**
     * als TeamStatus sind
     * hier nur moeglich: {@link TeamStatus#SPIELFREI_WECHSEL_SOLL} oder {@link TeamStatus#FREI}
     */

    public OFree(OGroup group) {
        super(group);
    }

    public boolean isFree() {
        return true;
    }

    public String toString() {
        return super.toString() + "=[/]";
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
}
