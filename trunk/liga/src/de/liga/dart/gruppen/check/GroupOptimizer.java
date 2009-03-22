package de.liga.dart.gruppen.check;

import de.liga.dart.gruppen.check.model.*;
import de.liga.dart.ligateam.model.WunschArt;

import java.util.*;

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
     * aktuelle Anzahl durchgef?hrter Berechnungen
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
        return optimal == null ? initial : optimal;
    }

    public OSetting getInitial() {
        return initial;
    }

    public long getCalculationCount() {
        return calculationCount;
    }

    public boolean optimize(OConflict parentConflict) throws OptimizerNotification {
        if (isStopSignaled()) return true;
        try {
            calculationDepth++;
            calculationCount++;
            showProgress(null);

            if (log.isInfoEnabled() && (calculationCount < 20 || (calculationCount % 1000) == 0)) {
                log.info(calculationDepth + "> Calculating (" + calculationCount + ") ...");
            }
            // neue bewertung durchfuehren
            decideBetterSetting(parentConflict);
            if (current.isPerfect()) {
                setStopSignaled(true);
                return true; // alles klar, nichts zu optimieren
            }
            // wenn conflict nicht geloest, zurueck nach oben
            if (parentConflict != null && current.getRating().hasConflict(parentConflict)) {
                if (log.isDebugEnabled()) log.debug("At " + calculationDepth + "> (" +
                        calculationCount + ") not resolved: " + parentConflict + " within " +
                        current.getRating().getConflictList());
                return false; // hat fuer dieses Problem nichts gebracht, nächstes Problem versuchen
            }

            if (!mayRecurseDeeper()) {
                return true;
            }
            // nur den ersten konflikt lösen...
            // zuerst die wichtigeren Konflikte, dann die nächst wichtigen (nach Prio)
            List<OConflict> candidates = current.getRating().getConflictListCopy();
            boolean triedToOptimize;
            do {
                OConflict next = null;
                for (OConflict each : candidates) {
                    if (next == null) {
                        next = each;
                    } else if (each.getPrio().value() < next.getPrio().value()) {
                        next = each;
                    }
                    if (next.getPrio() == OConflict.Prio.P1) { // Abbruch bei 1. harten Konflikt
                        break;
                    }
                }
                if (next != null) {
                    triedToOptimize = changeAndOptimize(next);
                    if (!triedToOptimize) {
                        // der geht nicht, nicht mehr probieren, anderen auswaehlen
                        candidates.remove(next);
                    }
                } else {
                    triedToOptimize = true;
                }
            } while (!triedToOptimize);
            return true;
        } finally {
            calculationDepth--;
        }
    }

    /**
     * neue bewertung durchfuehren, besseres Setting behalten
     *
     * @param parentConflict
     * @throws OptimizerNotification
     */
    private void decideBetterSetting(OConflict parentConflict) throws OptimizerNotification {
        if (parentConflict != null) {
            calculate();
            boolean showProgressOnly = optimal == null;
            if (showProgressOnly || current.isBetterThan(optimal)) {
                if (showProgressOnly) {
                    optimal = initial;
                } else {
                    optimal = current.deepCopy();
                }
                showProgress("Noch " + optimal.getRating().getConflictCount() + " Konflikte" + (
                        (optimal.getRating().getOptionalCount() == 0) ? "..." :
                                " und " + optimal.getRating().getOptionalCount() +
                                        " Wünsche..."));
                if (!showProgressOnly) {
                    if (log.isInfoEnabled()) log.info("At " + calculationDepth + "> (" +
                            calculationCount + ") found setting: " + optimal);
                    if (Options.optimizedRecusionExit) {
                        throw new OptimizerNotification(); // use exception to exit from recursion-stack
                    }
                }
            }
        }
    }

    private boolean mayRecurseDeeper() {
        if (Options.increaseRecursion && calculationDepth >= maxCalculationDepth) {
            return false;
        }
        return !(Options.maxRecursionDepth > 0 && calculationDepth >= Options.maxRecursionDepth);
    }

    private boolean changeAndOptimize(OConflict conflict) throws OptimizerNotification {
        if (conflict.getPosition1().isChangedOrFixiert() &&
                conflict.getPosition2().isChangedOrFixiert()) {
            return false; // beide bereits getauscht - nicht nochmal versuchen
        }
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
        return executeSuggestions(suggestions, conflict);
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

    private boolean executeSuggestions(List<OChangeSuggestion> suggestions, OConflict conflict)
            throws OptimizerNotification {
        Collections.sort(suggestions);

        boolean triedToOptimize = false;
        for (OChangeSuggestion suggestion : suggestions) {
            if (suggestion.canExecute(conflict)) {
                OChangeOption c1 = suggestion.getOption1();
                OChangeOption c2 = suggestion.getOption2();
                boolean t1 = c1.execute(conflict.getPosition1());
                boolean t2 = c2.execute(conflict.getPosition2());
                if (t1 || t2) {
                    triedToOptimize = optimize(conflict);
                }
                if (isStopSignaled()) return true;
                if (t1) c1.undo(conflict.getPosition1());
                if (t2) c2.undo(conflict.getPosition2());
            }
        }
        return triedToOptimize;
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
