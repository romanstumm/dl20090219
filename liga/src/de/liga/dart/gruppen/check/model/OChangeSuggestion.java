package de.liga.dart.gruppen.check.model;

import java.io.Serializable;

/**
 * Description:   <br/>
 * User: roman
 * Date: 21.12.2007, 10:07:58
 */
public class OChangeSuggestion implements Serializable, Comparable {
    private final int ranking;
    final OChangeOption option1;
    final OChangeOption option2;
    private final boolean ignore1;
    private final boolean ignore2;

    public OChangeSuggestion(OChangeOption option1, OChangeOption option2, OConflict conflict) {
        this.option1 = option1;
        this.option2 = option2;
        if (conflict == null || conflict.isWunsch()) {
            ignore1 = option1.getFromPosition() == option1.getToPosition();
            ignore2 = option2.getFromPosition() == option2.getToPosition();
        } else {
            ignore1 = canIgnore(option1, conflict.getPosition1());
            ignore2 = canIgnore(option2, conflict.getPosition2());
        }
        ranking = computeRanking();
    }

    private boolean canIgnore(final OChangeOption option, final OPosition pos) {
        /**
         * Hinweis: isChanged darf hier nicht beruecksichtigt werden, da dies
         * durch den Optimizer abgefragt wird und sich auch noch ändern kann,
         * nachdem die ChangeSuggestion berechnet wurde. Daher zunaechst alle
         * Suggestions berechnen, auch wenn die hier vorgeschlagenen Positionen
         * ggf. bereits einmal geändert wurden. 
         */
        if (pos.isFixiert() || option.getFromPosition() == option.getToPosition()) return true;

        // ignorieren, wenn Team an Zielposition der Wechselpartner in der Besetzung ist...
        // ... und hier kein Wunschkonflikt zur Lösung anliegt.
        OPosition other = pos.getGroup().getPosition(option.getToPosition());
        if (other.isFixiert()) return true;
        if (pos.isTeam() && other.isFree()) return false;
        if (pos.isFree() && other.isTeam()) return false;
        if (pos.isTeam()) {
            OTeam otherteam = (OTeam) other;
            OTeam team = (OTeam) pos;
            return (otherteam.getPub().equals(team.getPub()) &&
                    otherteam.getDay() == team.getDay());
        } else {
            return true;  // tausch von Frei mit Frei ist sinnlos
        }
    }

    public OChangeOption getOption1() {
        return option1;
    }

    public OChangeOption getOption2() {
        return option2;
    }

    public boolean isIgnore() {
        return ignore1 && ignore2;
    }

    /**
     * je kleiner das ranking, desto eher wird dieser Vorschlag geprüft, d.h.
     * kleines Ranking = hohe Priorität
     */
    private int computeRanking() {
        // 1. nur 2. team tauschen
        // 2. nur 1. team tauschen
        // 3. beide teams tauschen

        int prio = 0;
        if (!ignore1) {
            prio += 20;
        }
        if (!ignore2) {
            prio += 10;
        }
        return prio == 0 ? 1000 : prio;
    }

    public int compareTo(Object o) {
        OChangeSuggestion other = (OChangeSuggestion) o;
        return ranking - other.ranking;
    }

    public String toString() {
        return option1 + "+" + option2;
    }
}
