package de.liga.dart.fileimport.vfs.rangliste;

/**
 * Description:   <br/>
 * User: roman
 * Date: 03.05.2009, 16:00:14
 */
public class VfsTeam {
    private long teamNr;
    private String klasse;
    private String liganame;
    private String platz;
    private String ligameister;
    private String teamname;
    private VfsLiga vfsLiga;

    private int rang, // 1 - 8
            begegnungen, // anzahl begegnungen
            punkte,  // gesamtpunkte
            spiele1, spiele2, // spiele x:y
            saetze1, saetze2; // saetze x:y


    public VfsTeam() {
    }

    public long getTeamNr() {
        return teamNr;
    }

    public void setTeamNr(long teamNr) {
        this.teamNr = teamNr;
    }

    /**
     * Fische die Zahl aus dem Liganamen
     */
    public String getPlainGruppenNr() {
        int start = 0;
        String txt = vfsLiga.getName();
        while (start < txt.length() && !Character.isDigit(txt.charAt(start))) {
            start++;
        }
        int end = start;
        while (end < txt.length() && Character.isDigit(txt.charAt(end))) {
            end++;
        }
        return txt.substring(start, end);
    }

    /**
     * Der letzte Teil des Liganamens nach dem Leerzeichen ist
     * der PlainName der Liga 
     */
    public String getPlainLigaName() {
        int idx = vfsLiga.getName().lastIndexOf(' ');
        return vfsLiga.getName().substring(idx + 1);
    }

    public VfsLiga getVfsLiga() {
        return vfsLiga;
    }

    public void setVfsLiga(VfsLiga vfsLiga) {
        this.vfsLiga = vfsLiga;
    }

    public String getKlasse() {
        return klasse;
    }

    public String getLiganame() {
        return liganame;
    }

    public String getPlatz() {
        return platz;
    }

    public String getLigameister() {
        return ligameister;
    }

    public void setKlasse(String klasse) {
        this.klasse = klasse;
    }

    public void setLiganame(String liganame) {
        this.liganame = liganame;
    }

    public void setPlatz(String platz) {
        this.platz = platz;
    }

    public void setLigameister(String ligameister) {
        this.ligameister = ligameister;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public String getTeamname() {
        return teamname;
    }

    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }

    public int getBegegnungen() {
        return begegnungen;
    }

    public void setBegegnungen(int begegnungen) {
        this.begegnungen = begegnungen;
    }

    public int getPunkte() {
        return punkte;
    }

    public void setPunkte(int punkte) {
        this.punkte = punkte;
    }

    public int getSpiele1() {
        return spiele1;
    }

    public void setSpiele1(int spiele1) {
        this.spiele1 = spiele1;
    }

    public int getSpiele2() {
        return spiele2;
    }

    public void setSpiele2(int spiele2) {
        this.spiele2 = spiele2;
    }

    public int getSaetze1() {
        return saetze1;
    }

    public void setSaetze1(int saetze1) {
        this.saetze1 = saetze1;
    }

    public int getSaetze2() {
        return saetze2;
    }

    public void setSaetze2(int saetze2) {
        this.saetze2 = saetze2;
    }
}
