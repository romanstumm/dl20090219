package de.liga.dart.gruppen.check.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Description:   <br/>
 * User: roman
 * Date: 18.11.2007, 10:37:51
 */
public class OTeam extends OPosition implements Serializable {
    private long teamId;
    private int day;
    private Time time;
    private OPub pub;
//    private OHotSpot hotSpot;

    private OTeam other;

    private List<OWunsch> wuensche = Collections.EMPTY_LIST;
    private Set<OWunsch> unerfuellteWuensche = new HashSet(1);

    public OTeam(OGroup group, OPub pub, long ligateamId,
                 int wochentag, boolean fixiert) {
        super(group, fixiert);
        this.teamId = ligateamId;
        this.day = wochentag;
        this.pub = pub;
    }

    public List<OWunsch> getWuensche() {
        return wuensche;
    }

    public Set<OWunsch> getUnerfuellteWuensche() {
        return unerfuellteWuensche;
    }

    public OWunsch getWunsch(OTeam other) {
        for(OWunsch wunsch : wuensche) {
            if(other.equals(wunsch.getOtherTeam())) return wunsch;
        }
        return null;
    }

    public void setWuensche(List<OWunsch> wuensche) {
        this.wuensche = wuensche;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public long getTeamId() {
        return teamId;
    }

    public int getDay() {
        return day;
    }

    public Time getTime() {
        return time;
    }

    public OPub getPub() {
        return pub;
    }

    public int getOtherPosition() {
        for (int[] wechsel : WECHSEL) {
            if (getPosition() == wechsel[0]) return wechsel[1];
            if (getPosition() == wechsel[1]) return wechsel[0];
        }
        throw new IllegalStateException(); // keine gueltige Position, sollte nie bis hierhin kommen!
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OTeam oTeam = (OTeam) o;
        return teamId == oTeam.teamId;
    }

    public int hashCode() {
        return (int) (teamId  ^ (teamId >>> 32));
    }

    public boolean isFree() {
        return false;
    }

    public void setPub(OPub pub) {
        this.pub = pub;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(toSingleString());
        if (getOther() != null) {
            buf.append(" + ");
            if (!other.getGroup().equals(getGroup())) {
                buf.append("G").append(other.getGroup().getGroupId())
                        .append(": ");
            }
            buf.append(other.toSingleString());
        }
        return buf.toString();
    }

    protected String toSingleString() {
        StringBuilder buf = new StringBuilder();
        buf.append(super.toString());
        buf.append("=[");
        buf.append(teamId);
        buf.append(" in ");
        buf.append(getPub());
        buf.append(" at T");
        buf.append(day);
        buf.append("]");
        return buf.toString();
    }
 
    public OTeam getOther() {
        return other;
    }

    public void setOther(final OTeam other) {
        if (other != null) {
            if (other.other != null) {
                other.other.other = null;
            }
            other.other = this;
        }
        this.other = other;
    }
}
