package de.liga.dart.gruppen.check.model;

import java.io.Serializable;

/**
 * Description:   <br/>
 * User: roman
 * Date: 24.11.2007, 15:36:20
 */
public class OChangeOption implements Serializable {
    private final int fromPosition;
    private final int toPosition;

    public OChangeOption(int vonPlatz, int aufPlatz) {
        this.fromPosition = vonPlatz;
        this.toPosition = aufPlatz;
    }

    public int getToPosition() {
        return toPosition;
    }

    public int getFromPosition() {
        return fromPosition;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OChangeOption that = (OChangeOption) o;

        return toPosition == that.toPosition && fromPosition == that.fromPosition;
    }

    public int hashCode() {
        int result;
        result = fromPosition;
        result = 31 * result + toPosition;
        return result;
    }

    public String toString() {
        return "TO [" + fromPosition + "->" + toPosition + "]";
    }

    /** den tausch durchfuehren */
    public void execute(OPosition team) {
        if (team.moveTo(toPosition)) {
            team.setChanged(true);
        }
    }

    /**
     * den tausch rueckgaengig machen, d.h.
     * auf die vorige Position setzen
     */
    public void undo(OPosition team) {
        team.moveTo(fromPosition);
        team.setChanged(false);
    }
}
