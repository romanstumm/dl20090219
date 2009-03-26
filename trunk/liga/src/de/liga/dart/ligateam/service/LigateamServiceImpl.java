package de.liga.dart.ligateam.service;

import de.liga.dart.common.service.AbstractService;
import de.liga.dart.common.service.util.HibernateUtil;
import de.liga.dart.exception.DartException;
import de.liga.dart.exception.DartValidationException;
import de.liga.dart.exception.ValidationMessage;
import static de.liga.dart.exception.ValidationReason.MISSING;
import static de.liga.dart.exception.ValidationReason.WRONG;
import de.liga.dart.ligateam.model.TeamWunsch;
import de.liga.dart.model.*;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.*;

/**
 * Description:   <br/>
 * User: roman
 * Date: 02.11.2007, 13:25:51
 */
public class LigateamServiceImpl extends AbstractService implements LigateamService {
    public void saveLigateam(Ligateam team) throws DartValidationException {
        saveLigateam(team, null);
    }

    public void saveLigateam(Ligateam team, List<TeamWunsch> wuensche)
            throws DartValidationException {
        validate(team);
        save(team);
        if (wuensche != null) saveWuensche(team, wuensche);
    }

    private void saveWuensche(Ligateam team, List<TeamWunsch> wuensche) {
        Session session = getSession();
        team = (Ligateam) session.merge(team);
        Set<Ligateam> wunschTeams = new HashSet(wuensche.size());
        for (TeamWunsch wunsch : wuensche) {
            Ligateam otherTeam = wunsch.getOtherTeam();
            otherTeam = (Ligateam) session.get(otherTeam.getModelClass(), otherTeam.getKeyValue());
            wunsch.setOtherTeam(otherTeam);
            wunschTeams.add(otherTeam);
            team.addToWuensche(otherTeam, wunsch.getWunschArt(), session);
        }
        for (LigateamWunsch ltw : team.getWuensche()) {
            TeamWunsch wunsch = new TeamWunsch(team, ltw);
            if (!wunschTeams.contains(wunsch.getOtherTeam())) {
                team.removeFromWuensche(ltw, session);
            }
        }
    }

    private void validate(Ligateam team) throws DartValidationException {
        List<ValidationMessage> findings = new ArrayList<ValidationMessage>();

        if (team.getLiga() != null && team.getSpielort() != null &&
                team.getSpielort().getLiga() != null) {
            if (team.getLiga().getLigaId() != team.getSpielort().getLiga().getLigaId()) {
                findings.add(new ValidationMessage(WRONG, team,
                        "Liga paßt nicht zur Liga des Spielorts"));
            }
        }
        if (StringUtils.isEmpty(team.getTeamName())) {
            findings.add(new ValidationMessage(MISSING, team, "TeamName"));
        }
        if (null == team.getSpielort()) {
            findings.add(new ValidationMessage(MISSING, team, "Spielort"));
        }
        if (null == team.getLigaklasse()) {
            findings.add(new ValidationMessage(MISSING, team, "Ligaklasse"));
        }
        if (null == team.getLiga()) {
            findings.add(new ValidationMessage(MISSING, team, "Liga"));
        }
        if (team.getWochentag() < Calendar.SUNDAY || team.getWochentag() > Calendar.SATURDAY) {
            findings.add(new ValidationMessage(WRONG, team, "Wochentag"));
        }
        if (null == team.getSpielzeit()) {
            findings.add(new ValidationMessage(MISSING, team, "Spielzeit"));
        }
        if (!findings.isEmpty()) {
            throw new DartValidationException(findings);
        }
    }

    public Ligateam findLigateamById(long teamId) {
        return findById(Ligateam.class, teamId);
    }

    public List<Ligateam> findAllTeamsInLiga(Liga liga) {
        Query query;
        if (liga != null) {
            query = getSession()
                    .createQuery("select t from Ligateam t where t.liga = ? order by t.teamName");
            query.setParameter(0, liga);
        } else {
            query = getSession().createQuery("select t from Ligateam t order by t.teamName");
        }
        return query.list();
    }

    public List<Ligateam> findTeamsByLigaKlasseOrt(Liga liga, Ligaklasse klasse, Spielort ort,
                                                   boolean keineGruppe) {
        Query query;
        StringBuilder buf = new StringBuilder("select t from Ligateam t ");
        Map<String, Object> params = new HashMap();
        boolean where = false;
        if (keineGruppe) {
            where = and(where, buf);
            buf.append("t.ligateamspiele is empty ");
        }
        if (liga != null) {
            params.put("liga", liga);
            where = and(where, buf);
            buf.append("t.liga=:liga ");
        }
        if (klasse != null) {
            params.put("klasse", klasse);
            where = and(where, buf);
            buf.append("t.ligaklasse=:klasse ");
        }
        if (ort != null) {
            params.put("ort", ort);
            //noinspection UnusedAssignment
            where = and(where, buf);
            buf.append("t.spielort=:ort ");
        }
        buf.append("order by t.teamName");
        query = getSession().createQuery(buf.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.list();
    }

    private boolean and(boolean where, StringBuilder buf) {
        if (!where) {
            buf.append("where ");
        } else {
            buf.append("and ");
        }
        return true;
    }

    public List<Ligateam> findAllTeams() {
        Query query = getSession()
                .createQuery("select t from Ligateam t");
        //query.setParameter(0, liga);
        return query.list();
    }

    public void deleteLigateam(Ligateam team, boolean validate)
            throws DartException, DartValidationException {
        if (team.getLigateamId() > 0) {
            // reload: needed for wunschlist deletion!
            team = findLigateamById(team.getLigateamId());
            if(team == null) return;
            if (validate) {
                if (team.getLigateamspiel() != null) {
                    throw new DartValidationException("Löschen verhindert: \"" + team.getTeamName()
                            + "\" spielt in Gruppe " + team
                            .getLigateamspiel().getLigagruppe().getGruppenName() +
                            " auf Platz " + team.getLigateamspiel().getPlatzNr() + ".");
                }
            }
            team.clearWuensche(getSession());
            delete(team);
        }
    }

    /**
     * finde alle Kandidaten fuer die Auswahl in der Wunschlist,
     * d.h. alle teams in der gleichen liga, die noch NICHT
     * in der Wunschlist des angegenen Teams sind (und natuerlich
     * auch nicht das angeg. Team selbst)
     *
     * @param team - nie null
     * @return list mit anderen teams
     */
    public List<Ligateam> findWunschListCandidates(Ligateam team, Liga filterLiga,
                                                   Ligaklasse filterKlasse) {
        if (filterLiga == null) return Collections.EMPTY_LIST;


        Query query = getSession().createQuery(
                "select t from Ligateam t where t.liga = :liga and t.ligateamId <> :id order by t.teamName");
        query.setParameter("id", team.getLigateamId());
        query.setParameter("liga", filterLiga);

        team = findLigateamById(team.getLigateamId());
        // das geht auch geschickter, aber was solls...
        List<Ligateam> teams = (List<Ligateam>) query.list();
        Iterator<Ligateam> iter = teams.iterator();
        while (iter.hasNext()) {
            Ligateam each = iter.next();
            if (team != null && team.findWunsch(each) != null) {
                iter.remove();
                continue;
            }
            if (filterKlasse != null &&
                    each.getLigaklasse().getKlassenId() != filterKlasse.getKlassenId()) {
                iter.remove();
            }
        }
        return teams;
    }

    public void deleteAllLigateamWunsch(Liga liga) {
        // do nothing
        Query query;
        if (liga != null) {
            query = getSession()
                    .createQuery(
                            "select distinct w from LigateamWunsch w where w.team1.liga=:liga or w.team2.liga=:liga");
            query.setParameter("liga", liga);
        } else {
            query = getSession()
                    .createQuery("select w from LigateamWunsch w");

        }
        List<LigateamWunsch> wuensche = query.list();
        for (LigateamWunsch wunsch : wuensche) {
            if (wunsch.getTeam1() != null)
                wunsch.getTeam1().removeFromWuensche(wunsch, getSession());
        }

    }

    public String queryTeamInfos(Ligateam team) {
        // teams in gleicher gaststaette
        // incl gruppenzuordnng
        //      platz
        //      tag
        Spielort ort = team.getSpielort();
        getSession().refresh(ort);
        List<String> infos = new ArrayList();
        for (Ligateam otherteam : ort.getLigateams()) {
            if (otherteam.getLigateamId() != team.getLigateamId()) {
                StringBuilder buf = new StringBuilder();
                buf.append(otherteam.getLiga().getLigaName());
                buf.append(" ");
                buf.append(otherteam.getGruppeKlasse());
                if (otherteam.getLigateamspiel() != null) {
                    buf.append(" Platz ");
                    buf.append(otherteam.getLigateamspiel().getPlatzNr());
                }
                buf.append(": \"");
                buf.append(otherteam.getTeamNameTagName());
                buf.append("\"");
                infos.add(buf.toString());
            }
        }
        Collections.sort(infos);
        final String infoText;
        if (infos.isEmpty()) {
            infoText = "Keine weiteren Teams in \"" + ort.getSpielortName() + "\"";
        } else {
            StringBuilder buf = new StringBuilder();
            buf.append("Neben \"");
            buf.append(team.getTeamName());
            buf.append("\" spielen in \"");
            buf.append(ort.getSpielortName());
            buf.append("\" die Teams: \n");
            for (String line : infos) {
                buf.append(line);
                buf.append("\n");
            }
            infoText = buf.toString();
        }
        return infoText;
    }
}
