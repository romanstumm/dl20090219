package de.liga.dart.gruppen.check.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Description:   <br/>
 * User: roman
 * Date: 18.11.2007, 10:37:13
 */
public class OGroup implements Serializable {
    private final int groupIndex;
    private final long groupId;
    private Collection<OPosition> positions;

    public OGroup(long gruppenId, int groupIndex) {
        this.groupId = gruppenId;
        this.groupIndex = groupIndex;
        this.positions = new ArrayList();
    }

    public Collection<OPosition> getPositions() {
        return positions;
    }

    public OPosition[] sortedPositions() {
        List list = new ArrayList(positions);
        Collections.sort(list);
        return (OPosition[]) list.toArray(new OPosition[list.size()]);
    }

    public long getGroupId() {
        return groupId;
    }

    public OPosition getPosition(int positionNumber) {
        for (OPosition position : positions) {
            if (position.getPosition() == positionNumber) return position;
        }
        return null;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OGroup oGroup = (OGroup) o;
        return groupId == oGroup.groupId;
    }

    public int hashCode() {
        return (int) (groupId ^ (groupId >>> 32));
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("Gruppe ").append(groupId).append(": \n");
        for (OPosition position : sortedPositions()) {
            /*if (position.isConflictPossible()) buf.append("*");
            else*/ buf.append(" ");
            buf.append(position.getStatus().getInfo());
            buf.append(position.toString());
            buf.append("\n");
        }
        return buf.toString();
    }

    public int getGroupIndex() {
        return groupIndex;
    }
}
