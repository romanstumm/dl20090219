package de.liga.dart.gruppen.service;

import de.liga.dart.common.service.AbstractService;
import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.exception.DartException;
import de.liga.dart.exception.DartValidationException;
import de.liga.dart.exception.ValidationMessage;
import static de.liga.dart.exception.ValidationReason.MISSING;
import static de.liga.dart.exception.ValidationReason.WRONG;
import de.liga.dart.gruppen.CheckResult;
import de.liga.dart.gruppen.TeamStatusInfo;
import de.liga.dart.gruppen.check.model.*;
import de.liga.dart.ligateam.model.TeamWunsch;
import de.liga.dart.ligateam.service.LigateamService;
import de.liga.dart.model.*;
import de.liga.util.CalendarUtils;
import org.hibernate.Query;

import java.util.*;

/**
 * Description:   <br/>
 * User: roman
 * Date: 02.11.2007, 13:21:45
 */
public class GruppenServiceImpl extends AbstractService implements GruppenService {
    public Ligagruppe findLigagruppeById(long id) {
        return findById(Ligagruppe.class, id);
    }

    public void saveLigagruppe(Ligagruppe gruppe) {
        validate(gruppe);
        autonumber(gruppe);
        gruppe.setModifiedTimestamp(CalendarUtils.currentTimestamp());
        save(gruppe);
    }

    public void autonumber(Ligagruppe gruppe) {
        if (gruppe.getLiga() == null || gruppe.getLigaklasse() == null) return;
//        if (gruppe.getGruppenNr() == 0) {
        List<Ligagruppe> gruppen = findGruppen(gruppe.getLiga(), gruppe.getLigaklasse());
        if (gruppe.getGruppenId() > 0) {
            for (Ligagruppe each : gruppen) { // bei Update in gleicher Liga/Klasse: beibehalten!
                if (each.getGruppenId() == gruppe.getGruppenId()) return;
            }
        }
        // 1. luecke finden
        for (int i = 0; i < gruppen.size(); i++) {
            if (null == findGruppeMitNr(gruppen, i + 1)) {
                gruppe.setGruppenNr(i + 1);
                return;
            }
        }
        // 2. naechst-hoechste nummer vergeben
        gruppe.setGruppenNr(gruppen.size() + 1);
//        }
    }

    private Ligagruppe findGruppeMitNr(List<Ligagruppe> gruppen, int gruppenNr) {
        for (Ligagruppe each : gruppen) {
            if (each.getGruppenNr() == gruppenNr) return each;
        }
        return null;
    }

    private void validate(Ligagruppe gruppe) throws DartValidationException {
        List<ValidationMessage> findings = new ArrayList<ValidationMessage>();

        if (gruppe.getGruppenNr() < 0) {
            findings.add(new ValidationMessage(WRONG, gruppe, "GruppenNr"));
        }
        if (null == gruppe.getLigaklasse()) {
            findings.add(new ValidationMessage(MISSING, gruppe, "Ligaklasse"));
        }
        if (null == gruppe.getLiga()) {
            findings.add(new ValidationMessage(MISSING, gruppe, "Liga"));
        }
        if (!findings.isEmpty()) {
            throw new DartValidationException(findings);
        }
    }

    public List<Ligagruppe> findGruppen(Liga liga, Ligaklasse klasse) {
        Query query;
        if (klasse != null) {
            query = getSession().createQuery(
                    "select g from Ligagruppe g where g.liga=:liga and g.ligaklasse=:klasse order by g.ligaklasse.rang asc, g.gruppenNr asc");
            query.setCacheable(true);
            query.setParameter("liga", liga);
            query.setParameter("klasse", klasse);
        } else { // klasse==null d.h. keine Filterung nach Ligaklasse
            query = getSession().createQuery(
                    "select g from Ligagruppe g where g.liga=:liga order by g.ligaklasse.rang asc, g.gruppenNr asc");
            query.setCacheable(true);
            query.setParameter("liga", liga);
        }
        return query.list();
    }

    public List<Ligagruppe> findGruppenAndOrt(Liga liga, Spielort ort) {
        if (ort != null && liga != null) {
            Query query;
            query = getSession().createQuery(
                    "select g from Ligateamspiel s inner join s.ligagruppe g where g.liga=:liga and s.ligateam.spielort=:ort" +
                            " order by g.ligaklasse.rang asc, g.gruppenNr asc");
            query.setParameter("liga", liga);
            query.setParameter("ort", ort);
            return query.list();
        } else if (ort != null) {
            Query query;
            query = getSession().createQuery(
                    "select g from Ligateamspiel s inner join s.ligagruppe g where s.ligateam.spielort=:ort" +
                            " order by g.ligaklasse.rang asc, g.gruppenNr asc");
            query.setParameter("ort", ort);
            return query.list();
        } else if (liga != null) {
            Query query;
            query = getSession().createQuery(
                    "select g from Ligagruppe g where g.liga=:liga " +
                            " order by g.ligaklasse.rang asc, g.gruppenNr asc");
            query.setParameter("liga", liga);
            return query.list();
        } else {
            Query query;
            query = getSession().createQuery(
                    "select g from Ligagruppe g order by g.ligaklasse.rang asc, g.gruppenNr asc");
            return query.list();
        }
    }

    /**
     * Nur die teams, die in der angeg. liga sind
     * UND noch in keiner ligagruppe sind
     * UND der angegebenen ligaklasse angeh?ren
     * zurueckgeben.
     */
    public List<Ligateam> findUnassignedTeams(Liga liga, Ligaklasse klasse,
                                              Set<Long> teamsInGroup, Long gruppeId) {
        Query query = getSession().createQuery(
                "select t from Ligateam t where t.liga=:liga and t.ligaklasse=:klasse and " +
                        "not exists (from Ligateamspiel s where s.ligateam=t) order by t.teamName");
        query.setParameter("liga", liga);
        query.setParameter("klasse", klasse);
        List<Ligateam> teams = query.list();
        if (teamsInGroup != null) {
            for (Long teamIdToRemove : teamsInGroup) {
                if (teamIdToRemove != null)
                    removeTeamWithId(teams, teamIdToRemove.longValue());
            }
        }
        if (gruppeId != null && teamsInGroup != null && gruppeId.longValue() > 0) {
            // teams hinzu, die nicht in teamsInGroup enthalten sind, aber in 'spiele'
            Ligagruppe gruppe = this.findLigagruppeById(gruppeId);
            if (gruppe != null) {
                List<Ligateamspiel> spiele = findSpieleInGruppe(gruppe);
                for (Ligateamspiel spiel : spiele) {
                    if (spiel.getLigateam() != null) {
                        addTeamIfAbsent(teams, spiel.getLigateam(), teamsInGroup);
                    }
                }
            }
        }
        return teams;
    }

    private void addTeamIfAbsent(List<Ligateam> teams, Ligateam ligateam,
                                 Set<Long> teamsInGroup) {
        if (!containsTeam(teamsInGroup, ligateam)) {
            if (!teams.contains(ligateam)) teams.add(ligateam);
        }
    }

    private boolean containsTeam(Set<Long> teamsInGroup, Ligateam ligateam) {
        for (Long teamId : teamsInGroup) {
            if (teamId != null && ligateam.getLigateamId() == teamId.longValue()) return true;
        }
        return false;
    }

    private void removeTeamWithId(List<Ligateam> teams, long teamIdToRemove) {
        Iterator<Ligateam> iter = teams.iterator();
        while (iter.hasNext()) {
            Ligateam team = iter.next();
            if (team.getLigateamId() == teamIdToRemove) {
                iter.remove();
                break;
            }
        }
    }

    public List<Ligateamspiel> findSpieleInGruppe(Ligagruppe gruppe) {
        if (gruppe == null || gruppe.getGruppenId() == 0) return Collections.EMPTY_LIST;
        Query query = getSession().createQuery(
                "select s from Ligateamspiel s where s.ligagruppe=? order by s.platzNr");
        query.setCacheable(true);
        query.setParameter(0, gruppe);
        return query.list();
    }

    public Ligateamspiel setTeamIntoGruppe(Ligagruppe gruppe, Ligateam team,
                                           int platzNr, boolean fixiert) {
        Ligateamspiel spielInTeam = team.getLigateamspiel();
        Ligateamspiel spielInGruppe = findSpiel(gruppe, platzNr);

        if (spielInTeam != null) { // team spielte auf anderer position
            if (spielInGruppe != null &&
                    spielInGruppe.getSpielId() != spielInTeam.getSpielId()) {
                deleteSpiel(spielInGruppe);
            }
            spielInTeam.setPlatzNr(platzNr); // wechsele spielplatz des teams
            spielInTeam.setLigagruppe(gruppe); // lege spiel in die group
            spielInTeam.setFixiert(fixiert);
            save(spielInTeam);
            save(team);
            return spielInTeam;
        } else {
            if (spielInGruppe == null) {
                // dieser gruppenplatz war noch nicht besetzt
                spielInGruppe = new Ligateamspiel();
                spielInGruppe.setLigagruppe(gruppe);
                spielInGruppe.setPlatzNr(platzNr);
            }
            team.setLigateamspiel(spielInGruppe); // setze team auf gruppenSpiel platz
            spielInGruppe.setFixiert(fixiert);
            save(spielInGruppe);
            save(team);
            return spielInGruppe;
        }
    }

    public void deleteSpiel(Ligateamspiel spiel) {
        if (spiel.getLigateam() != null) {
            spiel.getLigateam().setLigateamspiel(null);
            spiel.setLigateam(null);
        }
        getSession().delete(spiel); // l?sche anderes spiel
        // mach es JETZT, damit kein Konflikt mit Unique Constraints
        // bei anderen Inserts/updates entsteht!
        getSession().flush();
    }

    public Ligateamspiel setSpielfreiIntoGruppe(Ligagruppe gruppe, int platzNr, boolean fixiert) {
        Ligateamspiel spielInGruppe = findSpiel(gruppe, platzNr);
        if (spielInGruppe == null) {
            // dieser gruppenplatz war noch nicht besetzt
            spielInGruppe = new Ligateamspiel();
            spielInGruppe.setLigagruppe(gruppe);
            spielInGruppe.setPlatzNr(platzNr);
            spielInGruppe.setFixiert(fixiert);
            save(spielInGruppe);
        } else {
            if (spielInGruppe.getLigateam() != null) {
                spielInGruppe.getLigateam().setLigateamspiel(null);
            }
            spielInGruppe.setFixiert(fixiert);
            spielInGruppe.setLigateam(null); // null = spielfrei
        }
        return spielInGruppe;
    }

    public GruppenSortierer createSortierer(Liga liga) {
        return ServiceFactory.decorateAsTransaction(new GruppenSortiererImpl(this, liga),
                GruppenSortierer.class);
    }

    public CheckResult checkGruppen(Liga liga) {
        return new GruppenChecker(this).check(liga);
    }

    public void deleteLigagruppe(Ligagruppe gruppe) throws DartException {
        List<Ligateamspiel> spiele = findSpieleInGruppe(gruppe);
        for (Ligateamspiel spiel : spiele) {
            if (spiel.getLigateam() != null) spiel.getLigateam().setLigateamspiel(null);
            spiel.setLigagruppe(null);
            delete(spiel);
        }
        delete(gruppe);
    }

    public TeamStatusInfo[] getTeamStatus(Ligagruppe gruppe) {
        GruppenChecker checker = new GruppenChecker(this);
        OGroup group = checker.getGroup(gruppe);
        if (group != null) {
            return createStatusInfo(group);
        } else {
            return null;
        }
    }

    private TeamStatusInfo[] createStatusInfo(OGroup group) {
        TeamStatusInfo[] status = new TeamStatusInfo[group.getPositions().size()];
        int i = 0;
        LigateamService teamService = ServiceFactory.get(LigateamService.class);
        for (OPosition pos : group.sortedPositions()) {
            final TeamStatusInfo info;
            if (pos.isTeam()) {
                OTeam other = ((OTeam) pos).getOther();
                OTeam team = (OTeam)pos;
                StringBuilder text = new StringBuilder();
                if(!team.getUnerfuellteWuensche().isEmpty()) {
                    int w=0;
                    for(OWunsch each : team.getUnerfuellteWuensche()) {
                        if(w++==0) text.append(": "); else text.append("; ");
                        Ligateam otherTeam = teamService.findLigateamById(each.getOtherTeam().getTeamId());
                        text.append(TeamWunsch.name(each.getWunschArt()));
                        text.append("mit ");
                        text.append(otherTeam.getTeamName());
                    }
                }
                if (other != null) {
                    Ligateam otherTeam = teamService.findLigateamById(other.getTeamId());
                    if (otherTeam != null) {
                        text.append(". Paarung mit ").append(otherTeam.getTeamNameTagName()).
                                append(" in ").append(
                                otherTeam.getGruppeKlasse());
                    }
                }
                info = new TeamStatusInfo(pos.getStatus(), text.toString());
            } else if (pos.isFree()) {
                OFree other = ((OFree) pos).getOther();
                String text = "";
                if (other != null) {
                    text = ". Paarung mit Position " + other.getPosition();
                }
                info = new TeamStatusInfo(pos.getStatus(), text);
            } else {
                info = new TeamStatusInfo(pos.getStatus(), "");
            }
            status[i++] = info;
        }
        return status;
    }

    public OSetting getAufstellungsStatus(Liga liga) {
        return new GruppenChecker(this).getAufstellungsStatus(liga);
    }

    public Ligagruppe findPreviousGroup(Ligagruppe gruppe) {
        if (isInvalidZumBlaettern(gruppe)) return null;
        List<Ligagruppe> gruppen = findGruppen(gruppe.getLiga(), null);
        for (int i = gruppen.size() - 1; i >= 0; i--) {
            Ligagruppe each = gruppen.get(i);
            if (each.getLigaklasse().getRang() < gruppe.getLigaklasse().getRang() ||
                    (each.getLigaklasse().getRang() == gruppe.getLigaklasse().getRang() &&
                            each.getGruppenNr() < gruppe.getGruppenNr())) {
                return each;
            }
        }
        return null;  // not found
    }

    private boolean isInvalidZumBlaettern(Ligagruppe gruppe) {
        return (gruppe == null || gruppe.getLiga() == null || gruppe.getLigaklasse() == null ||
                gruppe.getGruppenNr() == 0);
    }

    public Ligagruppe findNextGroup(Ligagruppe gruppe) {
        if (isInvalidZumBlaettern(gruppe)) return null;
        List<Ligagruppe> gruppen = findGruppen(gruppe.getLiga(), null);
        for (Ligagruppe each : gruppen) {
            if (each.getLigaklasse().getRang() > gruppe.getLigaklasse().getRang() ||
                    (each.getLigaklasse().getRang() == gruppe.getLigaklasse().getRang() &&
                            each.getGruppenNr() > gruppe.getGruppenNr())) {
                return each;
            }
        }
        return null;  // not found
    }

    /**
     * finde das spiel mit der angeg. platzNr
     *
     * @param gruppe  - not null
     * @param platzNr - 1..8
     * @return null or the spiel
     */
    protected Ligateamspiel findSpiel
            (Ligagruppe
                    gruppe, int platzNr) {
        if (platzNr > 8 && platzNr < 1)
            throw new IllegalArgumentException("1<=platzNr<=8");
        Query query = getSession().createQuery(
                "select s from Ligateamspiel s where s.ligagruppe=:group and s.platzNr=:platzNr");
        query.setParameter("group", gruppe);
        query.setParameter("platzNr", platzNr);
        List<Ligateamspiel> spiele = query.list();
        return gruppe.findSpiel(spiele, platzNr);
    }
}
