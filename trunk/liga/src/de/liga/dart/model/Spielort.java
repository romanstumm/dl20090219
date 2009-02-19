package de.liga.dart.model;


import de.liga.util.CalendarUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Spielort
 */
public class Spielort implements java.io.Serializable, LigaPersistence {


    private long spielortId;
    private Automatenaufsteller automatenaufsteller;
    private Liga liga;
    private String spielortName;
    private int automatenAnzahl;
    private Integer freierTag;
    private String kontaktName;
    private String email;
    private String telefon;
    private String mobil;
    private String fax;
    private String plz;
    private String ort;
    private String strasse;
    private String externeId;
    private Set<Ligateam> ligateams = new HashSet<Ligateam>(0);

    public Spielort() {
    }

    public long getSpielortId() {
        return this.spielortId;
    }

    public void setSpielortId(long spielortId) {
        this.spielortId = spielortId;
    }

    public Automatenaufsteller getAutomatenaufsteller() {
        return this.automatenaufsteller;
    }

    public void setAutomatenaufsteller(
            Automatenaufsteller automatenaufsteller) {
        this.automatenaufsteller = automatenaufsteller;
    }

    public Liga getLiga() {
        return this.liga;
    }

    public void setLiga(Liga liga) {
        this.liga = liga;
    }

    public String getSpielortName() {
        return this.spielortName;
    }

    public void setSpielortName(String spielortName) {
        this.spielortName = spielortName;
    }

    public int getAutomatenAnzahl() {
        return this.automatenAnzahl;
    }

    public void setAutomatenAnzahl(int automatenAnzahl) {
        this.automatenAnzahl = automatenAnzahl;
    }

    public Integer getFreierTag() {
        return this.freierTag;
    }

    public void setFreierTag(Integer freierTag) {
        this.freierTag = freierTag;
    }

    /**
     * Hilfsmethoden, damit Zuweisung von/nach String f?r
     * Wochentage ueber GUI einfach m?glich ist.
     *
     * @return
     */
    public String getFreierTagName() {
        if (freierTag == null) return null;
        return CalendarUtils.getWeekdayName(freierTag.intValue());
    }

    public void setFreierTagName(String wochentag) {
        freierTag = CalendarUtils.getWeekday(wochentag);
    }

    public String getKontaktName() {
        return this.kontaktName;
    }

    public void setKontaktName(String kontaktName) {
        this.kontaktName = kontaktName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return this.telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getMobil() {
        return this.mobil;
    }

    public void setMobil(String mobil) {
        this.mobil = mobil;
    }

    public String getFax() {
        return this.fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPlz() {
        return this.plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return this.ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getStrasse() {
        return this.strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getExterneId() {
        return externeId;
    }

    public void setExterneId(String externeId) {
        this.externeId = externeId;
    }

    /**
     * Gib alle Ligateams zur?ck, die diesen Spielort als Stammkneipe angegeben haben
     * (unabh?ngig davon, ob diese Teams in einer Gruppe eingeteilt sind)
     *
     * @return
     */
    public Set<Ligateam> getLigateams() {
        return this.ligateams;
    }

    public void setLigateams(Set<Ligateam> ligateams) {
        this.ligateams = ligateams;
    }

    /**
     * Gib alle Ligateams zur?ck, die diesen Spielort als Stammkneipe angegeben haben
     * und auch tats?chlich spielen, d.h. in einer Gruppe eingeteilt sind.
     *
     * @param gruppe - die gruppe, um die es geht
     */
    public Set<Ligateam> getLigateamsInGruppe(Ligagruppe gruppe) {
        Set<Ligateam> resultTeams = new HashSet<Ligateam>(ligateams.size());
        for (Ligateam each : getLigateams()) {
            if (each.getLigateamspiel() == null) continue; // spielt nicht
            if (each.getLigateamspiel().getLigagruppe().getGruppenId() == gruppe.getGruppenId()) {
                resultTeams.add(each);
            }
        }
        return resultTeams;
    }

    public String toString() {
        return getSpielortName();
    }

    public long getKeyValue() {
        return getSpielortId();
    }

    public Class getModelClass() {
        return Spielort.class;
    }

    public String toInfoString() {
        return "Spielort \"" + getSpielortName() + "\"";
    }

    public String getPlzUndOrt() {
        return (getPlz() != null ? getPlz() : "") + " " +
                (getOrt() != null ? getOrt() : "");
    }
}


