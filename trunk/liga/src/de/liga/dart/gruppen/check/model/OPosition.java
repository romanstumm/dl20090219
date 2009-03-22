package de.liga.dart.gruppen.check.model;

import de.liga.dart.gruppen.PositionStatus;

import java.io.Serializable;

/**
 * Description:   <br/>
 * User: roman
 * Date: 18.11.2007, 10:37:44
 */
public abstract class OPosition implements Serializable, Comparable {
    protected final OGroup group;
    protected final boolean fixiert;
    protected PositionStatus status = PositionStatus.FREI; // wird nur 1x vom GroupCalculator gesetzt
    protected int position; // computed, variable

    /**
     * true wenn die Position bereits in der Optimierung
     * getauscht wurde.
     * Wird genutzt, um Endlosrekursionen zu vermeiden.
     */
    private boolean changed = false;

    public boolean isChangedOrFixiert() {
        return changed || fixiert;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public static final int[][] WECHSEL = {{1, 5}, {2, 6}, {3, 7}, {4, 8}};

    /**
     * Bei mehr als 1 Spielfrei pro Gruppe soll die Sortierung,
     * falls möglich, die Spielfrei-Positionen setzen auf:
     */
    public static final int[][] SPIELFREI_WECHSEL = {{1, 8}, {2, 7}, {3, 6}, {4, 5}};

    public OPosition(OGroup group, boolean fixiert) {
        this.group = group;
        this.fixiert = fixiert;
    }

    public OGroup getGroup() {
        return group;
    }

    /**
     * @param newPosition
     * @return true when changed, false when unchanged
     */
    public boolean moveTo(int newPosition) {
        if (isChanged() || isFixiert() || getPosition() == newPosition) return false;

        if (newPosition < 1 || newPosition > 8)
            throw new IllegalArgumentException(
                    "unerlaubte PlatzNr " + newPosition);
        OPosition position = group.getPosition(newPosition);
        if (position.isFixiert()) return false;
        position.setPosition(this.position);
        setPosition(newPosition);
        return true;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public abstract boolean isFree();

    public final boolean isTeam() {
        return !isFree();
    }

    public int getPosition() {
        return position;
    }

    public int compareTo(Object o) {
        OPosition position = (OPosition) o;
        return this.position - position.getPosition();
    }

    public String toString() {
        return (changed ? "^P" : "P") + position;
    }

    public PositionStatus getStatus() {
        return status;
    }

    public void setStatus(PositionStatus status) {
        this.status = status;
    }

    public boolean isFixiert() {
        return fixiert;
    }

    public abstract boolean equals(Object o);    
    public abstract int hashCode();
}
