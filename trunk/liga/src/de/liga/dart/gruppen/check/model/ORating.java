package de.liga.dart.gruppen.check.model;

import de.liga.dart.ligateam.model.WunschArt;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Description:   <br/>
 * User: roman
 * Date: 18.11.2007, 13:16:16
 */
public class ORating implements Serializable {
    private int conflictCount;
    private int optionalCount;
    private Set<OConflict> conflicts = new HashSet();

    public int getConflictCount() {
        return conflictCount;
    }

    public int getOptionalCount() {
        return optionalCount;
    }

    public void addConflict(OTeam team1, OTeam team2) {
        conflictCount++;
        OConflict conflict = new OConflict(team1, team2);
        conflicts.add(conflict);
    }

    public void addOption(OTeam team1, OTeam team2) {
        optionalCount++;
        OConflict conflict = new OConflict(team1, team2, true);
        conflicts.add(conflict);
    }

    public void addOption(OFree otherFree, OFree oFree) {
        optionalCount++;
        OConflict conflict = new OConflict(otherFree, oFree, true);
        conflicts.add(conflict);
    }

    public void addConflict(OWunsch wunsch, OTeam team2) {
        switch (wunsch.getWunschArt()) {
            case WunschArt.BLACKLIST_SHALL:
            case WunschArt.WHITELIST_SHALL:
                optionalCount++;
                break;
            case WunschArt.BLACKLIST_MUST:
            case WunschArt.WHITELIST_MUST:
            default:
                conflictCount++;
        }
        OConflict conflict = new OConflict(wunsch, team2);
        conflicts.add(conflict);
    }

    public Set<OConflict> getConflicts() {
        return conflicts;
    }

    public String toString() {
        return "B " + conflictCount + "/" + optionalCount + " " + conflicts;
    }
}
