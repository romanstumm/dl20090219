package de.liga.dart.gruppen.check.model;

import de.liga.dart.ligateam.model.WunschArt;

import java.io.Serializable;
import java.util.*;

/**
 * Description:   <br/>
 * User: roman
 * Date: 18.11.2007, 13:16:16
 */
public class ORating implements Serializable {
    private int conflictCount;      // Prio 1
    private int teamOptionalCount;  // Prio 2
    private int freeOptionalCount;  // Prio 3
    private Collection<OConflict> conflicts = new HashSet();

    public int getConflictCount() {
        return conflictCount;
    }

    public int getTeamOptionalCount() {
        return teamOptionalCount;
    }

    public int getFreeOptionalCount() {
        return freeOptionalCount;
    }

    public int getOptionalCount() {
        return teamOptionalCount + freeOptionalCount;
    }

    public void addConflict(OTeam team1, OTeam team2) {
        conflictCount++;
        OConflict conflict = new OConflict(team1, team2);
//        if(!conflicts.contains(conflict)) {  // keinen Conflict doppelt eintragen!!
            conflicts.add(conflict);
//        }
    }

    public void addTeamOption(OTeam team1, OTeam team2) {
        teamOptionalCount++;
        OConflict conflict = new OConflict(team1, team2, OConflict.Prio.P2);
        conflicts.add(conflict);
    }

    public void addFreeOption(OFree otherFree, OFree oFree) {
        freeOptionalCount++;
        OConflict conflict = new OConflict(otherFree, oFree, OConflict.Prio.P3);
        conflicts.add(conflict);
    }

    public void addConflict(OWunsch wunsch, OTeam team2) {
        switch (wunsch.getWunschArt()) {
            case WunschArt.BLACKLIST_SHALL:
            case WunschArt.WHITELIST_SHALL:
                teamOptionalCount++;
                break;
            case WunschArt.BLACKLIST_MUST:
            case WunschArt.WHITELIST_MUST:
            default:
                conflictCount++;
        }
        OConflict conflict = new OConflict(wunsch, team2);
        conflicts.add(conflict);
    }

    /**
     * sortierte Rückgabe!
     * @return
     */
    public List<OConflict> getConflictList() {
        List sorted = new ArrayList(conflicts);
        Collections.sort(sorted);
        return sorted;
    }

    /**
     * sortierte Kopie!
     * @return
     */
    public List<OConflict> getConflictListCopy() {
        return getConflictList(); // is already a copy
    }

    /**
     * unsortierte Rückgabe!
     * @return
     */
    public Collection<OConflict> getConflicts() {
        return conflicts;
    }

    public String toString() {
        return "B " + getConflictCount() + "/" + getOptionalCount() + " " + getConflictList();
    }

    public boolean hasConflict(OConflict parentConflict) {
        // ACHTUNG: nicht TreeSet.contains() rufen, dass funktioniert nicht wie erwartet mit JDK1.6_02!!
        return conflicts.contains(parentConflict); // statt TreeSet wird HashSet genutzt!
    }
}
