package de.liga.dart.gruppen.check;

import de.liga.dart.gruppen.check.model.*;
import de.liga.dart.ligateam.model.WunschArt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description:  Prüfen und Neusortieren <br/>
 * User: roman
 * Date: 18.11.2007, 11:07:31
 */
public final class GroupOptimizer extends GroupCalculator {
    private OSetting initial; // Aufstellung wie aktuell gespeichert
    private OSetting optimal; // Aufstellung wie bisher am besten errechnet

    private ProgressIndicator progressIndicator;
    /**
     * aktuelle Anzahl durchgef�hrter Berechnungen
     */
    private long calculationCount;
    /**
     * aktuelle Tiefe der Rekursion
     */
    private int calculationDepth;
    /**
     * max. zulaessige Rekursionstiefe (nur wenn Options.increaseRecursion == true)
     */
    private int maxCalculationDepth = 0;

    /**
     * signal to stop recursions and optimizations
     */
    private volatile boolean stopSignaled = false;  // volatile, da auch von anderem Thread gesetzt!

    public GroupOptimizer(OSetting initial) {
        super(initial);
        this.initial = initial;
        optimal = initial;
        if (log.isInfoEnabled()) log.info("Initial setting: " + initial);
    }

    public boolean isStopSignaled() {
        return stopSignaled;
    }

    public void setStopSignaled(boolean stopSignaled) {
        this.stopSignaled = stopSignaled;
    }

    public ProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }

    public void setProgressIndicator(ProgressIndicator progressIndicator) {
        this.progressIndicator = progressIndicator;
    }

    public OSetting getOptimal() {
        return optimal;
    }

    public OSetting getInitial() {
        return initial;
    }

    public long getCalculationCount() {
        return calculationCount;
    }

    public void optimize(OConflict parentConflict) throws OptimizerNotification {
        if (isStopSignaled()) return;
        try {
            calculationDepth++;
            calculationCount++;
            showProgress(null);

            if (log.isInfoEnabled() && (calculationCount < 20 || (calculationCount % 1000) == 0)) {
                log.info(calculationDepth + "> Calculating (" + calculationCount + ") ...");
            }
            // neue bewertung durchfuehren
            if (parentConflict != null) {
                calculate();
                if (current.isBetterThan(optimal)) {
                    optimal = current.deepCopy();
                    showProgress("Noch " + optimal.getRating().getConflictCount() + " Konflikte" + (
                            (optimal.getRating().getOptionalCount() == 0) ? "..." :
                                    " und " + optimal.getRating().getOptionalCount() +
                                            " Wünsche..."));
                    if (log.isInfoEnabled()) log.info("At " + calculationDepth + ", " +
                            calculationCount + " found setting: " + optimal);
                    if (Options.optimizedRecusionExit) {
                        throw new OptimizerNotification(); // use exception to exit from recursion-stack
                    }
                }
            }

            if (current.isPerfect()) {
                setStopSignaled(true);
                return; // alles klar, nichts zu optimieren
            }
            // wenn conflict nicht geloest, zurueck nach oben
            if (parentConflict != null && current.getRating().getConflicts()
                    .contains(parentConflict)) {
                if (log.isInfoEnabled()) log.info("At " + calculationDepth + ", " +
                        calculationCount + " not resolved: " + parentConflict + " within " +
                        current.getRating().getConflicts());
                return; // hat fuer dieses Problem nichts gebracht
            }

            // nur den ersten konflikt lösen...
            // zuerst "harte" Konflikte, zweitrangig Optionale Wünsche zu erfüllen versuchen
            OConflict conflict = null, optional = null;
            for (OConflict each : current.getRating().getConflicts()) {
                if (each.isOptional() && optional == null) {
                    optional = each; // merke 1. optionalen Wunsch
                } else if (!each.isOptional()) {
                    conflict = each; // Abbruch bei 1. harten Konflikt
                    break;
                }
            }
            if (!mayRecurseDeeper()) {
                return;
            }
            if (conflict != null) {
                changeAndOptimize(conflict);
            } else if (optional != null) {
                changeAndOptimize(optional);
            }
        } finally {
            calculationDepth--;
        }
    }

    private boolean mayRecurseDeeper() {
        if (Options.increaseRecursion && calculationDepth >= maxCalculationDepth) {
            return false;
        }
        return !(Options.maxRecursionDepth > 0 && calculationDepth >= Options.maxRecursionDepth);
    }

    protected void changeAndOptimize(OConflict conflict) throws OptimizerNotification {
        if (conflict.getPosition1().isChanged()) return; // bereits getauscht
        if (conflict.getPosition2().isChanged()) return; // bereits getauscht

        final List<OChangeSuggestion> suggestions;
        if (conflict.isWunsch()) { // tausche um Wunsch zu erfuellen
            suggestions = computeWunschSuggestions(conflict);
        } else {
            if (conflict.isFree()) {
                suggestions = computeFreeWechselSuggestions(conflict);
            } else {
                suggestions = computeTeamWechselSuggestions(conflict);
            }
        }
        executeSuggestions(suggestions, conflict);
    }

    private List<OChangeSuggestion> computeTeamWechselSuggestions(OConflict conflict) {
        List<OChangeSuggestion> suggestions;
        final int max;
        if (conflict.getPosition1().getGroup()
                .equals(conflict.getPosition2().getGroup())) {
            max = 1;
        } else {
            max = 2;
        }
        suggestions = new ArrayList(OPosition.WECHSEL.length * max);
        for (int[] wechsel : OPosition.WECHSEL) {
            for (int t = 0; t < max; t++) {
                OChangeOption c1 =
                        new OChangeOption(conflict.getPosition1().getPosition(),
                                wechsel[t]);
                OChangeOption c2 =
                        new OChangeOption(conflict.getPosition2().getPosition(),
                                wechsel[1 - t]);
                suggestions.add(new OChangeSuggestion(c1, c2, conflict));
            }
        }
        return suggestions;
    }

    private List<OChangeSuggestion> computeFreeWechselSuggestions(OConflict conflict) {
        List<OChangeSuggestion> suggestions;
        suggestions = new ArrayList(OPosition.SPIELFREI_WECHSEL.length);
        for (int[] wechsel : OPosition.SPIELFREI_WECHSEL) {
            OChangeOption c1 =
                    new OChangeOption(conflict.getPosition1().getPosition(),
                            wechsel[0]);
            OChangeOption c2 =
                    new OChangeOption(conflict.getPosition2().getPosition(),
                            wechsel[1]);
            suggestions.add(new OChangeSuggestion(c1, c2, conflict));
        }
        return suggestions;
    }

    private List<OChangeSuggestion> computeWunschSuggestions(OConflict conflict) {
        List<OChangeSuggestion> suggestions;
        switch (conflict.getWunschArt()) {
            case WunschArt.BLACKLIST_MUST:
            case WunschArt.BLACKLIST_SHALL:
                // Blacklist = Wechselbesetzungspflicht (1/5, 2/6, 3/7, 4/8, 5/1, 6/2, 7/3, 8/4)
                return computeTeamWechselSuggestions(conflict);
                /* // so hatte ich es einst verstanden, ist aber fachlich falsch:
               // Moeglichkeiten 2 Teams auf 8 Positionen anzuordnen (die immer unterschiedlich sind)
                CombinationGenerator combGen = new CombinationGenerator(8, 2);
                suggestions = new ArrayList(combGen.getTotal().intValue() * 2);
                while (combGen.hasMore()) {
                    int[] next = combGen.getNext();
                    OChangeOption c1 =
                            new OChangeOption(conflict.getPosition1().getPosition(), next[0]);
                    OChangeOption c2 =
                            new OChangeOption(conflict.getPosition2().getPosition(), next[1]);
                    suggestions.add(new OChangeSuggestion(c1, c2, conflict));
                    c1 = new OChangeOption(conflict.getPosition1().getPosition(), next[1]);
                    c2 = new OChangeOption(conflict.getPosition2().getPosition(), next[0]);
                    suggestions.add(new OChangeSuggestion(c1, c2, conflict));
                }
                break;*/
            case WunschArt.WHITELIST_SHALL:
            case WunschArt.WHITELIST_MUST:
            default:
                // Moeglichkeiten 2 Teams auf 8 Positionen anzuordenen (die immer gleich sind)
                suggestions = new ArrayList(8);
                for (int i = 1; i <= 8; i++) {
                    OChangeOption c1, c2;
                    c1 = new OChangeOption(conflict.getPosition1().getPosition(), i);
                    c2 = new OChangeOption(conflict.getPosition2().getPosition(), i);
                    suggestions.add(new OChangeSuggestion(c1, c2, conflict));
                }
        }
        return suggestions;
    }

    private void executeSuggestions(List<OChangeSuggestion> suggestions, OConflict conflict)
            throws OptimizerNotification {
        Collections.sort(suggestions);

        for (OChangeSuggestion suggestion : suggestions) {
            if (!suggestion.isIgnore()) {
                OChangeOption c1 = suggestion.getOption1();
                OChangeOption c2 = suggestion.getOption2();
                c1.execute(conflict.getPosition1());
                c2.execute(conflict.getPosition2());
                optimize(conflict);
                if (isStopSignaled()) return;
                c1.undo(conflict.getPosition1());
                c2.undo(conflict.getPosition2());
            }
        }
    }

    private void showProgress(String message) {
        if (progressIndicator == null) return;
        if (message != null || (calculationCount % 50) == 0) {
            long relevance = calculationCount / 50;
            progressIndicator.showProgress((int) (relevance % 101), message);
        }
    }

    public void optimize() {
        current = initial.deepCopy();
        calculationCount = 0;
        calculationDepth = 0;
        if (Options.increaseRecursion) {
            maxCalculationDepth = 0;
            while (!isStopSignaled()) {
                maxCalculationDepth++;
                int maxDepth;
                try {
                    optimize(null);
                    maxDepth = maxCalculationDepth;
                } catch (OptimizerNotification ex) {
                    current.resetChangedState();
                    maxDepth = maxCalculationDepth;
                    maxCalculationDepth = 0;
                }
                if (log.isInfoEnabled()) {
                    log.info(
                            "Optimal setting with maxDepth=" + maxDepth + ":" + optimal);
                    log.info("After " + calculationCount + " calculations");
                }
            }
        } else {
            while (!isStopSignaled()) {
                try {
                    optimize(null);
                    setStopSignaled(true);
                } catch (OptimizerNotification ex) {
                    current.resetChangedState();
                }
            }
        }
        if (log.isInfoEnabled()) {
            log.info("Optimal setting: " + optimal);
            log.info("After " + calculationCount + " calculations");
        }

    }
}
