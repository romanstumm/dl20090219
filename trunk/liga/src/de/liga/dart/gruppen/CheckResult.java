package de.liga.dart.gruppen;

import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.gruppen.check.GroupOptimizer;
import de.liga.dart.gruppen.check.model.*;
import de.liga.dart.gruppen.service.GruppenService;
import de.liga.dart.ligateam.model.TeamWunsch;
import de.liga.dart.ligateam.service.LigateamService;
import de.liga.dart.model.Ligagruppe;
import de.liga.dart.model.Ligateam;
import de.liga.dart.model.Spielort;
import de.liga.dart.spielort.service.SpielortService;
import de.liga.util.CalendarUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: Klasse zur benutzerlesbaren Aufbereitung eines Verify-Sort-Ergebnisses
 * <br/>
 * User: roman
 * Date: 02.11.2007, 16:29:47
 */
public final class CheckResult {
    // fuer diese ganze Klasse gilt: NON-NLS !

    public enum Outcome {
        KEINE_KONFLIKTE("Gut, keine Konflikte!"),    // OK
        KONFLIKTE_GEFUNDEN("Konflikte gefunden!"), // Nicht OK, aber Korrektur (noch) nicht versucht
        KONFLIKTE_KORRIGIERT(
                "Alle Konflikte korrigiert!"), // War nicht OK, ist jetzt aber OK, weil korrigiert
        KONFLIKTE_NICHT_LOESBAR(
                "Nicht korrigierbare Konflikte!"); // Nicht OK, Korrektur nicht moeglich

        private final String info;

        Outcome(String info) {
            this.info = info;
        }

        public String getInfo() {
            return info;
        }
    }

    private Outcome outcome;
    private List<String> corrections = new ArrayList();
    private List<String> conflicts = new ArrayList();
    private long start, stop;
    private boolean stoppedByUserInteraction;

    public CheckResult() {
        start();
        outcome = Outcome.KEINE_KONFLIKTE;
    }

    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public boolean isStoppedByUserInteraction() {
        return stoppedByUserInteraction;
    }

    public void setStoppedByUserInteraction(boolean stoppedByUserInteraction) {
        this.stoppedByUserInteraction = stoppedByUserInteraction;
    }

    public List<String> getCorrections() {
        return corrections;
    }

    public void addCorrection(String correction) {
        corrections.add(correction);
    }

    public void addConflict(String conflict) {
        conflicts.add(conflict);
    }

    public List<String> getConflicts() {
        return conflicts;
    }

    public final void start() {
        start = System.currentTimeMillis();
    }

    public final void stop() {
        stop = System.currentTimeMillis();
    }

    public void print(PrintStream printer, int limit) {
        if (stop == 0) stop();
        int count = 0;

        printer.println("Ergebnis: " + outcome.getInfo());
        count++;

        if (!conflicts.isEmpty()) {
            printer.println();
            printer.println("Konflikte: ");
            for (String each : conflicts) {
                if (limit != 0 && count > limit) {
                    printer.println("... (weitere vorhanden)");
                    break;
                } else {
                    printer.println(each);
                    count++;
                }
            }
        }
        if (!corrections.isEmpty()) {
            printer.println();
            printer.println("Korrekturen: ");
            for (String each : corrections) {
                if (limit != 0 && count > limit) {
                    printer.println("... (weitere vorhanden)");
                    break;
                } else {
                    printer.println(each);
                    count++;
                }
            }
        }
        printer.println();
        long secs = ((stop - start) / 1000);
        if (secs > 1) {
            printer.println("Verarbeitungsdauer: " + secs +
                    " Sekunden.");
        }
        if (stoppedByUserInteraction) {
            printer.println("** Abbruch durch Benutzer **");
        }
    }

    public String toStringWithLimit(int limit) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        print(new PrintStream(out), limit);
        return out.toString();
    }

    public String toString() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        print(new PrintStream(out), 0);
        return out.toString();
    }

    public void initializeFrom(GroupOptimizer optimizer) {
        if (optimizer.getCalculationCount() ==
                0) { // nur initialbewertung vorhanden
            if (optimizer.getInitial().isPerfect()) {
                outcome = Outcome.KEINE_KONFLIKTE;
            } else {
                outcome = Outcome.KONFLIKTE_GEFUNDEN;
            }
        } else { // sortierung wurde durchgeführt
            if (optimizer.getInitial().isPerfect()) {
                outcome = Outcome.KEINE_KONFLIKTE;
            } else if (optimizer.getOptimal().isPerfect()) {
                outcome = Outcome.KONFLIKTE_KORRIGIERT;
            } else {
                outcome = Outcome.KONFLIKTE_NICHT_LOESBAR;
            }
        }
        // Konflikte anzeigen
        LigateamService teamService = ServiceFactory.get(LigateamService.class);
        GruppenService gtservice = ServiceFactory.get(GruppenService.class);
        if (!optimizer.getOptimal().isPerfect()) {
            SpielortService aservice =
                    ServiceFactory.get(SpielortService.class);
            for (OConflict conflict : optimizer.getOptimal().getRating()
                    .getConflicts()) {
                StringBuilder buf = new StringBuilder();
                if (conflict.isFree()) {
                    buf.append("Spielfrei an ").append(conflict.getPosition1().getPosition())
                            .append(" und ").append(conflict.getPosition2().getPosition())
                            .append(" in ");
                    Ligagruppe gruppe =
                            gtservice.findLigagruppeById(
                                    conflict.getPosition1().getGroup().getGroupId());
                    buf.append(gruppe.getGruppenName());
                } else {
                    Spielort spielort =
                            aservice.findSpielortById(
                                    conflict.getTeam2().getPub().getPubId());
                    buf.append(spielort.getSpielortName());
                    buf.append(" am ");
                    buf.append(CalendarUtils.getWeekdayName(
                            conflict.getTeam2().getDay()));
                    buf.append(": ");
                    Ligateam team = teamService
                            .findLigateamById(conflict.getTeam1().getTeamId());
                    buf.append(team.getTeamName());
                    buf.append(" (");
                    buf.append(team.getLigateamspiel()
                            .getLigagruppe().getGruppenName());
                    buf.append(" | ");
                    buf.append(conflict.getPosition1().getPosition());
                    buf.append(") ");
                    if (conflict.isWunsch()) buf.append(TeamWunsch.name(conflict.getWunschArt()));
                    else if (conflict.isOptional()) buf.append("OPTIONAL ");
                    buf.append("mit ");
                    team = teamService
                            .findLigateamById(conflict.getTeam2().getTeamId());
                    buf.append(team.getTeamName());
                    buf.append(" (");
                    buf.append(team.getLigateamspiel()
                            .getLigagruppe().getGruppenName());
                    buf.append(" | ");
                    buf.append(conflict.getPosition2().getPosition());
                    buf.append(")");
                }
                addConflict(buf.toString());
            }
        }
        // Korrekturen anzeigen
        if (optimizer.getOptimal() != optimizer.getInitial()) {
            OSetting initial = optimizer.getInitial();
            OSetting optimal = optimizer.getOptimal();
            int groupIndex = 0;
            for (OGroup group : initial.getGroups()) {
                boolean conflictInGroup = false;
                for (OPosition pos : group.sortedPositions()) {
                    int newPos = findPosition(
                            optimal.getGroups().get(groupIndex), pos);
                    if (newPos != pos.getPosition()) {
                        StringBuilder buf = new StringBuilder();
                        if (pos.isTeam()) {
                            if (!conflictInGroup) {
                                Ligagruppe gruppe = gtservice
                                        .findLigagruppeById(group.getGroupId());
                                addCorrection("Gruppe " +
                                        gruppe.getGruppenName() + ":");
                                conflictInGroup = true;
                            }
                            OTeam oteam = (OTeam) pos;
                            Ligateam lteam =
                                    teamService.findLigateamById(
                                            oteam.getTeamId());
                            buf.append(lteam.getTeamName());
                            buf.append(": ");
                            buf.append(pos.getPosition());
                            buf.append(" -> ");
                            buf.append(newPos);
                            addCorrection(buf.toString());
                        }
                    }
                }
                groupIndex++;
            }
        }
    }

    private int findPosition(OGroup group, OPosition thing) {
        for (OPosition pos : group.getPositions()) {
            if (pos.isFree() && thing.isFree()) {
                return pos.getPosition();
            } else if (pos.isTeam() && thing.isTeam()) {
                OTeam t1 = (OTeam) pos;
                OTeam t2 = (OTeam) thing;
                if (t1.getTeamId() == t2.getTeamId()) return pos.getPosition();
            }
        }
        return -1;
    }

}
