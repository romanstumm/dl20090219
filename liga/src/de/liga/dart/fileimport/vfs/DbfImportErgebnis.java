package de.liga.dart.fileimport.vfs;

import de.liga.dart.fileimport.DbfIO;
import de.liga.dart.fileimport.vfs.rangliste.VfsErgebnis;
import de.liga.dart.fileimport.vfs.rangliste.VfsTeam;
import de.liga.dart.fileimport.vfs.rangliste.VfsLiga;
import de.liga.dart.model.Liga;
import de.liga.util.CalendarUtils;

import java.sql.*;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Description:   Liest Rangliste aus vfs Dateien aus holt die Rohdaten zur
 * Berechnung der Rangliste für den Seriendruck der Urkunden.
 * Führt die Rangberechnung durch!<br/>
 * User: roman
 * Date: 03.05.2009, 15:55:57
 */
public class DbfImportErgebnis extends DbfIO {
    private static final Log log = LogFactory.getLog(DbfImportErgebnis.class);
    private List<LITTEA> litteams;
    private Set<Integer> disqualifiedTeaNr;
    private List<VfsTeam> teams;
    private Map<Integer, VfsLiga> ligen;
    private Map<Integer, Integer> abzug; // team-nr -> abzug_punkte
    private List<VfsErgebnis> ergebnisse;

    private final boolean complete;

    public DbfImportErgebnis() {
        this(false);
    }

    public DbfImportErgebnis(boolean complete) {
        this.complete = complete;
    }

    public List<LITTEA> getLitteams() {
        return litteams;
    }

    public List<VfsErgebnis> getErgebnisse() {
        return ergebnisse;
    }

    public List<VfsTeam> getTeams() {
        return teams;
    }


    protected String actionVerb() {
        return "Berechne";
    }

    protected void exchangeData(Liga liga, String path) throws SQLException {
        Statement stmt = connection.createStatement();
        try {
            readCurrentSaison(stmt);
        } finally {
            stmt.close();
        }
        readLitteams();
        readAbzug();
        readErgebnisse();
        computeRangListe();

    }


    /**
     * Nur die Vorberechnung + Sammeln und Zusammenführen der Daten, Addition der Punkte.
     * Sortierung und Druckaufbereitung finden hier NICHT statt!
     */
    private void computeRangListe() {
        teams = new ArrayList(litteams.size());
        for (final LITTEA eachTEAM : getLitteams()) {
            if(disqualifiedTeaNr.contains(eachTEAM.TEA_NR)) continue; // keine Berechnung, Team disqualifiziert

            final VfsTeam team = new VfsTeam();
            teams.add(team);
            team.setTeamNr(eachTEAM.TEA_NR);
            team.setTeamname(eachTEAM.TEA_NAME);
            VfsLiga liga = ligen.get(eachTEAM.LIG_NR);
            if (liga != null) {
                team.setVfsLiga(liga);
            }
            RangCalculator calcHeim = new RangCalculator() {
                public void visit(VfsErgebnis eachERG) {
                    team.setBegegnungen(team.getBegegnungen() + 1);
                    team.setPunkte(team.getPunkte() + eachERG.heim().getPUNKT());
                    team.setSaetze1(team.getSaetze1() + eachERG.heim().getSATZ());
                    team.setSaetze2(team.getSaetze2() + eachERG.gast().getSATZ());
                    team.setSpiele1(team.getSpiele1() + eachERG.heim().getSPIEL());
                    team.setSpiele2(team.getSpiele2() + eachERG.gast().getSPIEL());
                }
            };
            RangCalculator calcGast = new RangCalculator() {
                public void visit(VfsErgebnis eachERG) {
                    team.setBegegnungen(team.getBegegnungen() + 1);
                    team.setPunkte(team.getPunkte() + eachERG.gast().getPUNKT());
                    team.setSaetze1(team.getSaetze1() + eachERG.gast().getSATZ());
                    team.setSaetze2(team.getSaetze2() + eachERG.heim().getSATZ());
                    team.setSpiele1(team.getSpiele1() + eachERG.gast().getSPIEL());
                    team.setSpiele2(team.getSpiele2() + eachERG.heim().getSPIEL());
                }
            };
            team.setBegegnungen(0);
            Integer abzugPunkte = abzug.get(eachTEAM.TEA_NR);
            if (abzugPunkte != null) {
                team.setPunkte(-abzugPunkte.intValue());
            } else {
                team.setPunkte(0);
            }
            team.setSaetze1(0);
            team.setSaetze2(0);
            team.setSpiele1(0);
            team.setSpiele2(0);
            // compute gast
            iterateGast(eachTEAM, calcGast);
            // compute heim
            iterateHeim(eachTEAM, calcHeim);
        }
    }

    private void iterateGast(LITTEA eachTEAM, RangCalculator calc) {
        for (VfsErgebnis eachERG : getErgebnisse()) {
            if (eachTEAM.TEA_NR == eachERG.gast().getTEA_NR() &&
                    !disqualifiedTeaNr.contains(eachERG.heim().getTEA_NR())) {
                calc.visit(eachERG);
            }
        }
    }

    private void iterateHeim(LITTEA eachTEAM, RangCalculator calc) {
        for (VfsErgebnis eachERG : getErgebnisse()) {
            if (eachTEAM.TEA_NR == eachERG.heim().getTEA_NR() &&
                    !disqualifiedTeaNr.contains(eachERG.gast().getTEA_NR())) {
                calc.visit(eachERG);
            }
        }
    }

    static interface RangCalculator {
        void visit(VfsErgebnis eachERG);
    }

    protected void readErgebnisse() throws SQLException {
/*
        String selectErgebnis =
                "SELECT heim.TEA_NR, heim.SAI_POSNR, gast.TEA_NR, gast.SAI_POSNR, " +
                        "par.PAR_RUNDE, par.PAR_SPIEL, par.PAR_CODE, " +
                        "erg.ERG_HPUNKT, erg.ERG_HSPIEL, erg.ERG_HSATZ, erg.ERG_HANZAH, " +
                        "erg.ERG_GPUNKT, erg.ERG_GSPIEL, erg.ERG_GSATZ, erg.ERG_GANZAH " +
                        "FROM LITERG erg JOIN LITPAR par ON " +
                        "erg.SAI_NR=par.SAI_NR AND erg.LIG_NR=par.LIG_NR AND " +
                        "erg.PAR_RUNDE=par.PAR_RUNDE AND erg.PAR_SPIEL=par.PAR_SPIEL " +
                        "JOIN LITSAD heim ON " +
                        "par.PAR_HEIM=heim.SAI_POSNR AND par.SAI_NR=heim.SAI_NR AND par.LIG_NR=heim.LIG_NR " +
                        "JOIN LITSAD gast ON " +
                        "par.PAR_GAST=gast.SAI_POSNR AND par.SAI_NR=gast.SAI_NR AND par.LIG_NR=gast.LIG_NR " +
                        "WHERE par.SAI_NR=?";*/
        String selectErgebnis =
                "SELECT heim.TEA_NR, heim.SAI_POSNR, gast.TEA_NR, gast.SAI_POSNR, " +
                        "par.PAR_RUNDE, par.PAR_SPIEL, par.PAR_CODE, " +
                        "erg.ERG_HPUNKT, erg.ERG_HSPIEL, erg.ERG_HSATZ, erg.ERG_HANZAH, " +
                        "erg.ERG_GPUNKT, erg.ERG_GSPIEL, erg.ERG_GSATZ, erg.ERG_GANZAH " +
                        "FROM LITERG erg, LITPAR par, LITSAD heim, LITSAD gast " +
                        "WHERE " + saisonSQL("par") + " AND " +
                        "erg.SAI_NR=par.SAI_NR AND erg.LIG_NR=par.LIG_NR AND " +
                        "erg.PAR_RUNDE=par.PAR_RUNDE AND erg.PAR_SPIEL=par.PAR_SPIEL AND " +
                        "par.PAR_HEIM=heim.SAI_POSNR AND par.SAI_NR=heim.SAI_NR AND par.LIG_NR=heim.LIG_NR AND " +
                        "par.PAR_GAST=gast.SAI_POSNR AND par.SAI_NR=gast.SAI_NR AND par.LIG_NR=gast.LIG_NR";
        if (!complete) {
            selectErgebnis =
                    selectErgebnis + " AND par.PAR_DATUM<?"; // AND par.PAR_STATUS<>'M'"; ??
        }
        /*int rowcount = checkRowCountLitErg();
        boolean quick = true;
        if (rowcount == -1 || rowcount > 990) {
            selectErgebnis = selectErgebnis + " AND erg.LIG_NR=?";
            quick = false;
        }*/

        PreparedStatement pstmt = connection.prepareStatement(selectErgebnis);
        final java.sql.Date tomorrow;
        if (complete) {
            tomorrow = null;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, 1);
            tomorrow = new java.sql.Date(cal.getTimeInMillis());
        }
        try {
            ergebnisse = new ArrayList();
            /*if (!quick) {
                if (!ligen.isEmpty()) {
                    double step = 35.0 / ((double) ligen.size());
                    double percent = 50.0;
                    for (int ligaNr : ligen.keySet()) {
                        if (getProgressIndicator() != null) {
                            percent += step;
                            getProgressIndicator().showProgress((int) percent,
                                    "Lese " + ligen.get(ligaNr).getName() + "...");
                        }
                        log.info("Reading Liga " + ligaNr + "/" + ligen.size() + "...");
                        List<VfsErgebnis> teilErg;
                        teilErg = readErgebnis(pstmt, ligaNr, tomorrow);
                        log.info(teilErg.size() + " Ergebnisse.");
                        ergebnisse.addAll(teilErg);
                    }
                }
            } else {*/
                ergebnisse.addAll(readErgebnis(pstmt, /*null, */tomorrow));
//            }
        } finally {
            pstmt.close();
        }
    }
/*
    private int checkRowCountLitErg() throws SQLException {
        int rowcount = -1;
        PreparedStatement stmt = connection.prepareStatement(
                "select count(*) from LITERG where " + saisonSQL(null));
        provideSaison(stmt, 1);
        try {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                rowcount = rs.getInt(1);
            }
            rs.close();
        } finally {
            stmt.close();
        }
        return rowcount;
    }*/

    public List<VfsErgebnis> readErgebnis(PreparedStatement pstmt, /*Integer ligaNr, */
                                          java.sql.Date stichtag) throws SQLException {
        int pindex = 1;
        pindex = provideSaison(pstmt, pindex);
        if (stichtag != null) {
            pstmt.setDate(pindex++, stichtag);
        }
       /* if (ligaNr != null) {
            // liganr nur, um die Ergebnisgroesse < 1000 zu halten
            pstmt.setInt(pindex, ligaNr.intValue());
        }*/
        ResultSet resultSet = pstmt.executeQuery();
        List<VfsErgebnis> parts = new ArrayList(300);
        //int rowc = 0;
        try {
            while (resultSet.next()) {
                int c = 1;
                VfsErgebnis erg = new VfsErgebnis();
                erg.heim().setTEA_NR(resultSet.getInt(c++));
                erg.heim().setPOSNR(resultSet.getInt(c++));

                erg.gast().setTEA_NR(resultSet.getInt(c++));
                erg.gast().setPOSNR(resultSet.getInt(c++));

                erg.setPAR_RUNDE(resultSet.getInt(c++));
                erg.setPAR_SPIEL(resultSet.getInt(c++));
                erg.setPAR_CODE(resultSet.getString(c++));

                erg.heim().setPUNKT(resultSet.getInt(c++));
                erg.heim().setSPIEL(resultSet.getInt(c++));
                erg.heim().setSATZ(resultSet.getInt(c++));
                erg.heim().setANZAH(resultSet.getInt(c++));

                erg.gast().setPUNKT(resultSet.getInt(c++));
                erg.gast().setSPIEL(resultSet.getInt(c++));
                erg.gast().setSATZ(resultSet.getInt(c++));
                erg.gast().setANZAH(resultSet.getInt(c));
                //rowc++;
                //System.out.println(rowc + ". " + erg);
                parts.add(erg);
            }
        } finally {
            resultSet.close();
        }
        return parts;
    }

    private void readLitteams() throws SQLException {
        /*String selectTeams =
                "SELECT l.LIG_NAME, l.LIG_KLASSE, t.SAI_NR, t.LIG_NR, t.TEA_NR, " +
                        "t.TEA_NAME, t.LOK_NR, t.TEA_SPIELT, " +
                        "t.TEA_UHRZEI, t.TEA_STATUS FROM LITTEA t " +
                        "JOIN LITLIG l ON t.LIG_NR=l.LIG_NR " +
                        "WHERE (t.TEA_STATUS IS NULL OR t.TEA_STATUS<>'D') AND t.SAI_NR=?";*/
        String selectTeams =
                "SELECT l.LIG_NAME, l.LIG_KLASSE, t.SAI_NR, t.LIG_NR, t.TEA_NR, " +
                        "t.TEA_NAME, t.LOK_NR, t.TEA_SPIELT, " +
                        "t.TEA_UHRZEI, t.TEA_STATUS FROM LITTEA t, LITLIG l " +
                        "WHERE t.LIG_NR=l.LIG_NR AND (t.TEA_STATUS IS NULL OR t.TEA_STATUS<>'D') AND " +
                        saisonSQL("t");
        PreparedStatement pstmt = connection.prepareStatement(selectTeams);
        provideSaison(pstmt, 1);
        ResultSet resultSet = pstmt.executeQuery();
        litteams = new ArrayList(100);
        disqualifiedTeaNr = new HashSet();
        ligen = new HashMap();
        try {
            while (resultSet.next()) {
                LITTEA litteam = createLittea(resultSet);
                if (!"D".equals(litteam.TEA_STATUS)) { // "D" ist "Spielfrei"
                    litteams.add(litteam);
                    if (ligen.get(litteam.LIG_NR) == null) {
                        VfsLiga liga = createLiga(resultSet, litteam.LIG_NR);
                        ligen.put(liga.getNr(), liga);
                    }
                } else {
                    disqualifiedTeaNr.add(litteam.TEA_NR);
                }
            }
        } finally {
            resultSet.close();
            pstmt.close();
        }
    }

    protected String saisonSQL(String alias) {
//        return "t.SAI_NR=?";    par.SAI_NR=?
        if (alias != null) {
            return "YEAR(" + alias + ".SAI_NR)=? AND MONTH(" + alias + ".SAI_NR)=?";
        } else {
            return "YEAR(SAI_NR)=? AND MONTH(SAI_NR)=?";
        }
    }

    protected int provideSaison(PreparedStatement pstmt, int offset) throws SQLException {
//        pstmt.setDate(offset++, myCurrentSaison);
        pstmt.setInt(offset++, CalendarUtils.getYear(myCurrentSaison));
        pstmt.setInt(offset++, CalendarUtils.getMonth(myCurrentSaison));
        return offset;
    }

    private void readAbzug() throws SQLException {
        String selectAbzug =
                "SELECT TEA_NR,ABZ_PUNKTE FROM LITABZUG WHERE " + saisonSQL(null);
        PreparedStatement pstmt = connection.prepareStatement(selectAbzug);
        provideSaison(pstmt, 1);
        ResultSet resultSet = pstmt.executeQuery();
        abzug = new HashMap();
        try {
            while (resultSet.next()) {
                int abzugp = resultSet.getInt(2);
                if (abzugp != 0) {
                    abzug.put(resultSet.getInt(1), abzugp);
                }
            }
        } finally {
            resultSet.close();
            pstmt.close();
        }
    }

    private VfsLiga createLiga(ResultSet resultSet, int ligNr) throws SQLException {
        VfsLiga litlig = new VfsLiga();
        litlig.setName(resultSet.getString(1));
        litlig.setKlasse(resultSet.getString(2));
        litlig.setNr(ligNr);
        return litlig;
    }

    private LITTEA createLittea(ResultSet resultSet) throws SQLException {
        LITTEA littea = new LITTEA();
        int c = 3;
        littea.SAI_NR = resultSet.getDate(c++);
        littea.LIG_NR = resultSet.getInt(c++);
        littea.TEA_NR = resultSet.getInt(c++);
        littea.TEA_NAME = resultSet.getString(c++);
        littea.LOK_NR = resultSet.getInt(c++);
        littea.TEA_SPIELT = resultSet.getString(c++);
        littea.TEA_UHRZEI = resultSet.getInt(c++);
        littea.TEA_STATUS = resultSet.getString(c);
        return littea;
    }

}
