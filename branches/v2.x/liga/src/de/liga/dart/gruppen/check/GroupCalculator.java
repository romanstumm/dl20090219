package de.liga.dart.gruppen.check;

import de.liga.dart.gruppen.PositionStatus;
import de.liga.dart.gruppen.check.model.*;
import de.liga.dart.ligateam.model.WunschArt;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * Description:   Sortierung nur prüfen<br/>
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
                    pos.setStatus(PositionStatus.FREI);
                }
            }
        }
        for (OGroup group : current.getGroups()) {
            OFree otherFree = null; // nur innerhalb jeder Gruppe
            for (OPosition pos : group.sortedPositions()) {
                if (pos.isTeam()) {
                    computePositionStatus((OTeam) pos);
                    computeWunschStatus((OTeam) pos);
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
                oFree.setStatus(PositionStatus.C_SPIELFREI_WECHSEL_SOLL);
                otherFree.setStatus(PositionStatus.C_SPIELFREI_WECHSEL_SOLL);
                current.getRating().addFreeOption(otherFree, oFree);
            }
            return null;
        }
    }

    protected void computeWunschStatus(OTeam team) {
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
                current.getRating().addConflict(wunsch, team);
                team.getUnerfuellteWuensche().add(wunsch);
                if (team.isFixiert()) {
                    if (team.getStatus() != PositionStatus.C_WECHSEL_MUSS) {
                        team.setStatus(PositionStatus.C_FIXIERT_CONFLICT);
                    }
                } else {
                    team.setStatus(PositionStatus.C_WUNSCH_MUSS);
                }
            }
        }
    }

    private void checkWunschWhiteShall(OWunsch wunsch, OTeam team) {
        if (Options.sortWhitelist) {
            if (wunsch.getOtherTeam().getPosition() != team.getPosition()) {
                current.getRating().addConflict(wunsch, team);
                team.getUnerfuellteWuensche().add(wunsch);
                if (!team.getStatus().isConflict()) {
                    if (team.isFixiert()) {
                        team.setStatus(PositionStatus.C_FIXIERT_CONFLICT);
                    } else {
                        team.setStatus(PositionStatus.C_WUNSCH_SOLL);
                    }
                }
            }
        }
    }

    private void checkWunschBlackShall(OWunsch wunsch, OTeam team) {
        if (Options.sortBlacklist) {
            if (wunsch.getOtherTeam().getPosition() != team.getOtherPosition()) {
                current.getRating().addConflict(wunsch, team);
                team.getUnerfuellteWuensche().add(wunsch);
                if (!team.getStatus().isConflict()) {
                    if (team.isFixiert()) {
                        team.setStatus(PositionStatus.C_FIXIERT_CONFLICT);
                    } else {
                        team.setStatus(PositionStatus.C_WUNSCH_SOLL);
                    }
                }
            }
        }
    }

    private void checkWunschBlackMust(OWunsch wunsch, OTeam team) {
        if (Options.sortBlacklist) {
            if (wunsch.getOtherTeam().getPosition() != team.getOtherPosition()) {
                current.getRating().addConflict(wunsch, team);
                team.getUnerfuellteWuensche().add(wunsch);
                if (team.isFixiert()) {
                    if (team.getStatus() != PositionStatus.C_WECHSEL_MUSS) {
                        team.setStatus(PositionStatus.C_FIXIERT_CONFLICT);
                    }
                } else {
                    team.setStatus(PositionStatus.C_WUNSCH_MUSS);
                }
            }
        }
    }

    protected void computePositionStatus(OTeam team) {
        if (team.getOther() != null) return;

        // zuerst nach Paarung in der gleichen Gruppe suchen
        int expectPos = team.getOtherPosition();
        OPosition otherPos = team.getGroup().getPosition(expectPos);
        if (otherPos.isTeam()) {
            OTeam other = (OTeam) otherPos;
            if (other.getOther() == null && other.getPub().equals(team.getPub()) &&
                    other.getDay() == team.getDay()) {
                team.setOther(other);
                team.setStatus(PositionStatus.PAARUNG);
                other.setStatus(PositionStatus.PAARUNG);
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
        // Optionale Partner an unterschiedlichen Tagen zuweisen: Vorsicht Prioitäten beachten!
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
        team.setStatus(PositionStatus.FREI);
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
                team.setOther(other); // ?? diese Zeile entfernen ??
                if (team.isFixiert()) {
                    team.setStatus(PositionStatus.C_FIXIERT_CONFLICT);
                } else {
                    team.setStatus(PositionStatus.C_WECHSEL_SOLL);
                }
                rating.addTeamOption(other, team);
            } else {
                team.setStatus(PositionStatus.FREI);
            }
        } else {
            team.setOther(other);
            if (other.getGroup().equals(team.getGroup())) {
                other.setStatus(PositionStatus.PAARUNG_OPTIONAL);
                team.setStatus(PositionStatus.PAARUNG_OPTIONAL);
            } else {
                other.setStatus(PositionStatus.GESETZT_OPTIONAL);
                team.setStatus(PositionStatus.GESETZT_OPTIONAL);
            }
        }
        return true;
    }

    protected boolean setWechsel(OTeam team, OTeam other, ORating rating, int expectPos) {
        if (expectPos != other.getPosition()) {
            if (Options.sortWhitelist) {
                OWunsch wunsch = team.getWunsch(other);
                if (wunsch != null) {
                    if ((wunsch.getWunschArt() == WunschArt.WHITELIST_MUST)  // overrule!
                            && wunsch.getOtherTeam().getPosition() == team.getPosition()) {
                        team.setOther(other);
                        team.setStatus(PositionStatus.WUNSCH_OVERRULED);
                        other.setStatus(PositionStatus.WUNSCH_OVERRULED);
                        return true;
                    }
                }
            }
            team.setOther(other);
            rating.addConflict(other, team);
            if (team.isFixiert()) {
                team.setStatus(PositionStatus.C_FIXIERT_CONFLICT);
            } else {
                team.setStatus(PositionStatus.C_WECHSEL_MUSS);
            }
            if (!other.isFixiert()) {
                other.setStatus(PositionStatus.C_WECHSEL_MUSS);
            }
        } else {
            team.setOther(other);
            if (team.getGroup().equals(other.getGroup())) {
                team.setStatus(PositionStatus.PAARUNG);
                other.setStatus(PositionStatus.PAARUNG);
            } else {
                team.setStatus(PositionStatus.GESETZT);
                other.setStatus(PositionStatus.GESETZT);
            }
        }
        return true;
    }
}
