package de.liga.dart.gruppen.check.model;

import de.liga.util.ObjectCloner;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.*;

/**
 * Description:   <br/>
 * User: roman
 * Date: 18.11.2007, 10:36:58
 */
public class OSetting implements Serializable {
    private List<OGroup> groups;
    private Map<Long, OPub> pubs;
    private ORating rating;

    public OSetting() {
        this.groups = new ArrayList();
        this.pubs = new HashMap();
    }

    public List<OGroup> getGroups() {
        return groups;
    }

    public OGroup getGroup(long groupId) {
        for (OGroup group : groups) {
            if (group.getGroupId() == groupId) {
                return group;
            }
        }
        return null;
    }

    public Collection<OPub> getPubs() {
        return pubs.values();
    }

    public OPub getPub(long pubId) {
        return pubs.get(pubId);
    }

    public void addPub(OPub pub) {
        pubs.put(pub.getPubId(), pub);
    }

    public OSetting deepCopy() {
        return (OSetting) new ObjectCloner().clone(this);
    }

    public ORating getRating() {
        return rating;
    }

    public void setRating(ORating rating) {
        this.rating = rating;
    }

    public boolean isBetterThan(OSetting other) {
        boolean better = rating.getConflictCount() <
                other.getRating().getConflictCount();
        if (rating.getConflictCount() == other.getRating().getConflictCount()) {
            better = rating.getOptionalCount() <
                    other.getRating().getOptionalCount();
        }
        return better;
    }

    public boolean isPerfect() {
        return rating.getConflictCount() == 0 && rating.getOptionalCount() == 0;
    }

    public boolean isAcceptable() {
        return rating.getConflictCount() == 0;
    }

    public List<OTeam> getPreviousPositions(OTeam team) {
        List<OTeam> positions = new ArrayList();
        if (team.getPub().getTeams().size() < 2) return positions;
        int groupNum = getGroups().indexOf(team.getGroup());
        for (; groupNum >= 0; groupNum--) {
            OGroup group = getGroups().get(groupNum);
            OPosition[] poss = group.sortedPositions();
            for (int pi = poss.length - 1; pi >= 0; pi--) {
                OPosition pos = poss[pi];
                if (pos.isFree()) continue;
                OTeam other = (OTeam) pos;
                if (group.equals(team.getGroup())) {
                    if (pos.getPosition() < team.getPosition()) {
                        if (other.getPub().equals(team.getPub())) {
                            positions.add(other);
                        }
                    }
                } else {
                    if (other.getPub().equals(team.getPub())) {
                        positions.add(other);
                    }
                }
            }
        }
        return positions;
    }

    public String toString() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        if (rating != null) {
            pw.println(rating);
        }
        if (groups != null) {
            for (OGroup group : groups) {
                pw.println(group.toString());
            }
        }
        return sw.getBuffer().toString();
    }

    public OTeam getTeam(long ligateamId) {
        for (OGroup group : groups) {
            for (OPosition pos : group.getPositions()) {
                if (pos.isTeam()) {
                    OTeam team = (OTeam) pos;
                    if (team.getTeamId() == ligateamId) return team;
                }
            }
        }
        return null;
    }

    public void resetChangedState() {
      for(OGroup group : getGroups()) {
          for(OPosition position : group.getPositions()) {
              position.setChanged(false);
          }
      }
    }
}
