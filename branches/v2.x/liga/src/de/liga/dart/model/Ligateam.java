package de.liga.dart.model;


import de.liga.util.CalendarUtils;
import org.hibernate.Session;

import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

/**
 * Ligateam
 */
public class Ligateam implements java.io.Serializable, LigaPersistence {


    private long ligateamId;
    private Spielort spielort;
    private Liga liga;
    private Ligaklasse ligaklasse;

    private String teamName;
    private int wochentag;
    private Time spielzeit;
    private String externeId;
    private Set<LigateamWunsch> wuensche1 = new HashSet<LigateamWunsch>(0);
    private Set<LigateamWunsch> wuensche2 = new HashSet<LigateamWunsch>(0);
    private Set<Ligateamspiel> ligateamspiele = new HashSet<Ligateamspiel>(0);

    public Ligateam() {
        spielzeit =
                CalendarUtils.createTime(19, 30); // initiale, bevorzugte Zeit
    }

    public long getLigateamId() {
        return this.ligateamId;
    }

    public void setLigateamId(long ligateamId) {
        this.ligateamId = ligateamId;
    }

    public Spielort getSpielort() {
        return this.spielort;
    }

    public void setSpielort(Spielort spielort) {
        this.spielort = spielort;
    }

    public Liga getLiga() {
        return this.liga;
    }

    public void setLiga(Liga liga) {
        this.liga = liga;
    }

    public Ligaklasse getLigaklasse() {
        return this.ligaklasse;
    }

    public void setLigaklasse(Ligaklasse ligaklasse) {
        this.ligaklasse = ligaklasse;
    }

    public String getTeamName() {
        return this.teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getWochentag() {
        return this.wochentag;
    }

    public void setWochentag(int wochentag) {
        this.wochentag = wochentag;
    }

    public String getTeamNameTagName() {
        return teamName + " (" + getWochentagName() + ")";
    }

    /**
     * Hilfsmethoden, damit Zuweisung von/nach String für
     * Wochentage ueber GUI einfach möglich ist.
     *
     * @return
     */
    public String getWochentagName() {
        return CalendarUtils.getWeekdayName(wochentag);
    }

    public void setWochentagName(String aWochentag) {
        Integer temp = CalendarUtils.getWeekday(aWochentag);
        wochentag = temp == null ? 0 : temp;
    }

    public Time getSpielzeit() {
        return this.spielzeit;
    }

    public void setSpielzeit(Time spielzeit) {
        this.spielzeit = spielzeit;
    }

    public String getExterneId() {
        return externeId;
    }

    public void setExterneId(String externeId) {
        this.externeId = externeId;
    }

    public Set<LigateamWunsch> getWuensche() {
        Set<LigateamWunsch> wuensche =
                new HashSet(getWuensche1().size() + getWuensche2().size());
        wuensche.addAll(getWuensche1());
        wuensche.addAll(getWuensche2());
        return wuensche;
    }

    /**
     * INTERNAL -
     * bidirectional setter for wunschlist:
     * add the team to the wunschlist of this team.
     *
     * @param team - the other team
     */
    public LigateamWunsch addToWuensche(Ligateam team, int wunschArt,
                                        Session session) {
        if (team.equals(this)) return null;
        /** immer beide seiten anlegen, damit sich die wunschlist symmetrisch verhaelt **/
        LigateamWunsch wunsch = findWunsch(team);
        if (wunsch == null) {
            wunsch = new LigateamWunsch();
            wunsch.setTeam1(this);
            getWuensche1().add(wunsch);
            wunsch.setTeam2(team);
            team.getWuensche2().add(wunsch);
            wunsch.setWunschArt(wunschArt);
            if (session != null) session.save(wunsch);
        } else {
            wunsch.setWunschArt(wunschArt);
            if (session != null) session.update(wunsch);
        }
        return wunsch;
    }

    /**
     * INTERNAL -
     * finde den wunsch, falls vorhanden, mit dem anderen Team
     *
     * @param team
     * @return den wunsch oder null, falls keiner zwischen den teams gewählt
     */
    public LigateamWunsch findWunsch(Ligateam team) {
        for (LigateamWunsch wunsch : getWuensche1()) {
            if (wunsch.getTeam1() != this &&
                    wunsch.getTeam1().getLigateamId() == team.getLigateamId()) return wunsch;
            if (wunsch.getTeam2() != this &&
                    wunsch.getTeam2().getLigateamId() == team.getLigateamId()) return wunsch;
        }
        for (LigateamWunsch wunsch : getWuensche2()) {
            if (wunsch.getTeam1() != this &&
                    wunsch.getTeam1().getLigateamId() == team.getLigateamId()) return wunsch;
            if (wunsch.getTeam2() != this &&
                    wunsch.getTeam2().getLigateamId() == team.getLigateamId()) return wunsch;
        }
        return null;
    }

    private void _removeFromWuensche(LigateamWunsch wunsch) {
        getWuensche1().remove(wunsch);
        getWuensche2().remove(wunsch);
    }

    /**
     * INTERNAL - bidirectional removal of a wunsch
     *
     * @param wunsch
     * @param session
     */
    public void removeFromWuensche(LigateamWunsch wunsch, Session session) {
        if (wunsch.getTeam1() != null)
            wunsch.getTeam1()._removeFromWuensche(wunsch);
        if (wunsch.getTeam2() != null)
            wunsch.getTeam2()._removeFromWuensche(wunsch);
        wunsch.setTeam1(null);
        wunsch.setTeam2(null);
        if (session != null) session.delete(wunsch);
    }

    /**
     * INTERNAL - remove all wuensche associated with this team
     * in any way
     *
     * @param session
     */
    public void clearWuensche(Session session) {
        for (LigateamWunsch wunsch : getWuensche()) {
            removeFromWuensche(wunsch, session);
        }
    }

    protected Set<LigateamWunsch> getWuensche1() {
        return wuensche1;
    }

    protected void setWuensche1(Set<LigateamWunsch> wuensche1) {
        this.wuensche1 = wuensche1;
    }

    protected Set<LigateamWunsch> getWuensche2() {
        return wuensche2;
    }

    protected void setWuensche2(Set<LigateamWunsch> wuensche2) {
        this.wuensche2 = wuensche2;
    }

    /**
     * es gibt nur max. 1 Spiel, use getLigateamspiel() instead
     */
    protected Set<Ligateamspiel> getLigateamspiele() {
        return this.ligateamspiele;
    }

    protected void setLigateamspiele(Set<Ligateamspiel> ligateamspiele) {
        this.ligateamspiele = ligateamspiele;
    }

    public Ligateamspiel getLigateamspiel() {
        if (getLigateamspiele().isEmpty()) return null;
        else return getLigateamspiele().iterator().next();
    }

    /**
     * @return gib gruppenname zurück, in dem Team spielt.
     *         falls Team noch nicht eingeteilt ist (d.h. nirgends spielt), gibt Ligaklasse.klassename zurück
     */
    public String getGruppeKlasse() {
        Ligateamspiel spiel = getLigateamspiel();
        if (spiel == null) {
            return getLigaklasse() == null ? "" : getLigaklasse().getKlassenName();
        } else {
            return spiel.getLigagruppe().getGruppenName();
        }
    }

    /**
     * bidirectional setter!
     * cascade="all"
     *
     * @param ligateamspiel
     */
    public void setLigateamspiel(Ligateamspiel ligateamspiel) {
        if (ligateamspiel == null ||
                !getLigateamspiele().contains(ligateamspiel)) {
            getLigateamspiele().clear();
            if (ligateamspiel != null) {
                getLigateamspiele().add(ligateamspiel);
                ligateamspiel.setLigateam(this);
            }
        }
    }

    public String toString() {
        return getTeamName();
    }

    public long getKeyValue() {
        return getLigateamId();
    }

    public Class getModelClass() {
        return Ligateam.class;
    }

    public String toInfoString() {
        return "Team \"" + getTeamName() + "\"";
    }
}


