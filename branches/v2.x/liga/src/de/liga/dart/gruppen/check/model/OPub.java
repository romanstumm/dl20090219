package de.liga.dart.gruppen.check.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:   <br/>
 * User: roman
 * Date: 18.11.2007, 10:55:50
 */
public class OPub implements Serializable {
    private final long pubId;
    private List<OTeam> teams;

    public OPub(long spielortId) {
        this.pubId = spielortId;
        this.teams = new ArrayList();
    }

    public long getPubId() {
        return pubId;
    }

    public List<OTeam> getTeams() {
        return teams;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OPub oPub = (OPub) o;
        return pubId == oPub.pubId;
    }

    public int hashCode() {
        return (int) (pubId  ^ (pubId >>> 32));
    }

    public String toString() {
        return "K"+ pubId;
    }
}
