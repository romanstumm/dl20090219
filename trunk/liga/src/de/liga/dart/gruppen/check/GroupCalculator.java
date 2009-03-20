package de.liga.dart.gruppen.check;

import de.liga.dart.gruppen.TeamStatus;
import de.liga.dart.gruppen.check.model.*;
import de.liga.dart.ligateam.model.WunschArt;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * Description:   Sortierung nur pr¸fen<br/>
 * User: roman
 * Date: 14.06.2008, 10:35:16
 */
public class GroupCalculator {
    protected static final Log log = LogFactory.getLog(GroupCalculator.class);
    protected OSetting current; // Aufstellung, die gerade unter Berechnung steht

    public GroupCalculator(OSetting current) {
        this.current = current;
        calculate(); // erstelle initialbewertung
    }

    public OSetting getCurrent() {
        return current;
    }

    /**
     * Neuberechnung durchfuehren, Pruefergebnis ermitteln
     */
    protected void calculate() {
        ORating rating = new ORating();
        current.setRating(rating);
        for (OGroup group : current.getGroups()) {
            for (OPosition pos : group.sortedPositions()) {
                if (pos.isTeam()) {
                    ((OTeam) pos).getUnerfuellteWuensche().clear();
                    ((OTeam) pos).setOther(null);
                } else {
                    ((OFree) pos).setOther(null);
                    pos.setStatus(TeamStatus.FREI);
                }
            }
        }
        for (OGroup group : current.getGroups()) {
            OFree otherFree = null; // nur innerhalb jeder Gruppe
            for (OPosition pos : group.sortedPositions()) {
                if (pos.isTeam()) {
                    computeTeamStatus((OTeam) pos);
                    computeTeamWunschStatus((OTeam) pos);
                } else {
                    otherFree = computeFreeStatus((OFree) pos, otherFree);
                }
            }
        }
    }

    private OFree computeFreeStatus(OFree oFree, OFree otherFree) {
        if (otherFree == null) return oFree;
        else {
            oFree.setOther(otherFree);
            otherFree.setOther(oFree);
            if (oFree.getOtherPosition() != otherFree.getPosition()) {
                oFree.setStatus(TeamStatus.SPIELFREI_WECHSEL_SOLL);
                otherFree.setStatus(TeamStatus.SPIELFREI_WECHSEL_SOLL);
                current.getRating().addFreeOption(otherFree, oFree);
            }
            return null;
        }
    }

    protected void computeTeamWunschStatus(OTeam team) {
        if (team.getStatus() == TeamStatus.FIXIERT_OVERRULED) return;
        if (team.getWuensche().isEmpty()) return;
        for (OWunsch wunsch : team.getWuensche()) {
            switch (wunsch.getWunschArt()) {
                case WunschArt.WHITELIST_SHALL:
                    checkWunschWhiteShall(wunsch, team);
                    break;
                case WunschArt.BLACKLIST_MUST:
                    checkWunschBlackMust(wunsch, team);
                    break;
                case WunschArt.BLACKLIST_SHALL:
                    checkWunschBlackShall(wunsch, team);
                    break;
                case WunschArt.WHITELIST_MUST:
                    checkWunschWhiteMust(wunsch, team);
                    break;
                default:
                    GroupOptimizer.log
                            .warn("Unknown wunschArt: " + wunsch.getWunschArt() + " in " + wunsch);
            }
        }
    }

    private void checkWunschWhiteMust(OWunsch wunsch, OTeam team) {
        if (Options.sortWhitelist) {
            if (wunsch.getOtherTeam().getPosition() != team.getPosition()) {
                if (team.isFixiert()) {
                    if (team.getStatus() != TeamStatus.WECHSEL_MUSS) {
                        team.setStatus(TeamStatus.FIXIERT_OVERRULED);
                    }
                } else {
                    team.setStatus(TeamStatus.WUNSCH_MUSS);
                    team.getUnerfuellteWuensche().add(wunsch);
                    current.getRating()
                            .addConflict(wunsch, team);
                }
            }
        }
    }

    private void checkWunschWhiteShall(OWunsch wunsch, OTeam team) {
        if (Options.sortWhitelist) {
            if (wunsch.getOtherTeam().getPosition() != team.getPosition()) {
                if (team.isFixiert()) {
                    if (team.getStatus() != TeamStatus.WECHSEL_MUSS) {
                        team.setStatus(TeamStatus.FIXIERT_OVERRULED);
                    }
                } else {
                    if (team.getStatus() != TeamStatus.WECHSEL_MUSS) {
                        if (team.getStatus() != TeamStatus.WUNSCH_MUSS) {
                            team.setStatus(TeamStatus.WUNSCH_SOLL);
                        }
                        team.getUnerfuellteWuensche().add(wunsch);
                    }
                    current.getRating()
                            .addConflict(wunsch, team);
                }
            }
        }
    }

    private void checkWunschBlackShall(OWunsch wunsch, OTeam team) {
        if (Options.sortBlacklist) {
            if (wunsch.getOtherTeam().getPosition() != team.getOtherPosition()) {
                if (team.isFixiert()) {
                    team.setStatus(TeamStatus.FIXIERT_OVERRULED);
                } else {
                    if (team.getStatus() != TeamStatus.WECHSEL_MUSS) {
                        if (team.getStatus() != TeamStatus.WUNSCH_MUSS) {
                            team.setStatus(TeamStatus.WUNSCH_SOLL);
                        }
                        team.getUnerfuellteWuensche().add(wunsch);
                    }
                    current.getRating()
                            .addConflict(wunsch, team);
                }
            }
        }
    }

    private void checkWunschBlackMust(OWunsch wunsch, OTeam team) {
        if (Options.sortBlacklist) {
            if (wunsch.getOtherTeam().getPosition() != team.getOtherPosition()) {
                if (team.isFixiert()) {
                    team.setStatus(TeamStatus.FIXIERT_OVERRULED);
                } else {
                    team.setStatus(TeamStatus.WUNSCH_MUSS);
                    team.getUnerfuellteWuensche().add(wunsch);
                    current.getRating()
                            .addConflict(wunsch, team);
                }
            }
        }
    }

    protected void computeTeamStatus(OTeam team) {
        if (team.getOther() != null) return;

        // zuerst nach Paarung in der gleichen Gruppe suchen
        int expectPos = team.getOtherPosition();
        OPosition otherPos = team.getGroup().getPosition(expectPos);
        if (otherPos.isTeam()) {
            OTeam other = (OTeam) otherPos;
            if (other.getOther() == null && other.getPub().equals(team.getPub()) &&
                    other.getDay() == team.getDay()) {
                team.setOther(other);
                team.setStatus(TeamStatus.PAARUNG);
                other.setStatus(TeamStatus.PAARUNG);
                return;
            }
        }

        List<OTeam> previous = current.getPreviousPositions(team);
        for (OTeam prev : previous) {
            if (prev.getDay() == team.getDay()) {
                if (prev.getOther() == null) { // hat noch keinen Partner: Partner gefunden!
                    if (setWechsel(team, prev, current.getRating(), expectPos)) {
                        return;
                    }
                }
            }
        }
        // Optionale Partner an unterschiedlichen Tagen zuweisen: Vorsicht Prioit‰ten beachten!
        if (getUnassignedMandatoryPartner(team) == null) {
            for (OTeam prev : previous) {
                if (prev.getOther() == null && getUnassignedMandatoryPartner(prev) == null &&
                        prev.getDay() != team.getDay()) {
                    if (setWechselOptional(team, prev, current.getRating(), expectPos)) {
                        return;
                    }
                }
            }
        }
        team.setStatus(TeamStatus.FREI);
    }

    private OTeam getUnassignedMandatoryPartner(OTeam team) {
        for (OTeam each : team.getPub().getTeams()) {
            if (each != team && each.getOther() == null && each.getDay() == team.getDay())
                return each;
        }
        return null;
    }

    protected boolean setWechselOptional(OTeam team, OTeam other, ORating rating, int expectPos) {
        if (expectPos != other.getPosition()) {
            if (Options.sortOptional) {
//                if (team.isFixiert()) { // fixiert heiﬂt nicht, dass Wechsel nicht erkannt werden soll
//                    team.setStatus(TeamStatus.FIXIERT_OVERRULED);
//                } else {
                /* if (other.isFixiert()) { // fixiert heiﬂt nicht, dass Wechsel nicht erkannt werden soll
                  return false;
              } else {*/
                team.setOther(other); // ?? diese Zeile entfernen ??
                if (team.isFixiert()) {
                    team.setStatus(TeamStatus.FIXIERT_OVERRULED);
                } else {
                    team.setStatus(TeamStatus.WECHSEL_SOLL);
                }
                rating.addTeamOption(other, team);
//                    }
//                }
            } else {
                team.setStatus(TeamStatus.FREI);
            }
        } else {
            team.setOther(other);
            if (other.getGroup().equals(team.getGroup())) {
                other.setStatus(TeamStatus.PAARUNG_OPTIONAL);
                team.setStatus(TeamStatus.PAARUNG_OPTIONAL);
            } else {
                other.setStatus(TeamStatus.GESETZT_OPTIONAL);
                team.setStatus(TeamStatus.GESETZT_OPTIONAL);
            }
        }
        return true;
    }

    protected boolean setWechsel(OTeam team, OTeam other, ORating rating, int expectPos) {
        if (expectPos != other.getPosition()) {
//            if (team.isFixiert()) { // fixiert heiﬂt nicht, dass Wechsel nicht erkannt werden soll
//                team.setStatus(TeamStatus.FIXIERT_OVERRULED);
//            } else {
            if (Options.sortWhitelist) {
                OWunsch wunsch = team.getWunsch(other);
                if (wunsch != null) {
                    if ((wunsch.getWunschArt() == WunschArt.WHITELIST_MUST)  // overrule!
                            && wunsch.getOtherTeam().getPosition() == team.getPosition()) {
                        team.setOther(other);
                        team.setStatus(TeamStatus.WUNSCH_OVERRULED);
                        other.setStatus(TeamStatus.WUNSCH_OVERRULED);
                        return true;
                    }
                }
            }
//                if (other.isFixiert()) { // fixiert heiﬂt nicht, dass Wechsel nicht erkannt werden soll
//                    return false;
//                } else {
            team.setOther(other);
            rating.addConflict(other, team);
            if (team.isFixiert()) {
                team.setStatus(TeamStatus.FIXIERT_OVERRULED);
            } else {
                team.setStatus(TeamStatus.WECHSEL_MUSS);
            }
            if(!other.isFixiert()) {
                other.setStatus(TeamStatus.WECHSEL_MUSS);
            }
//                }
//            }
        } else {
            team.setOther(other);
            if (team.getGroup().equals(other.getGroup())) {
                team.setStatus(TeamStatus.PAARUNG);
                other.setStatus(TeamStatus.PAARUNG);
            } else {
                team.setStatus(TeamStatus.GESETZT);
                other.setStatus(TeamStatus.GESETZT);
            }
        }
        return true;
    }
}
