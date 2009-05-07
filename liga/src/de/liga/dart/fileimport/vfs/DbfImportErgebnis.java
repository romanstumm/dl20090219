package de.liga.dart.fileimport.vfs;

import de.liga.dart.fileimport.DbfIO;
import de.liga.dart.fileimport.vfs.rangliste.VfsErgebnis;
import de.liga.dart.fileimport.vfs.rangliste.VfsTeam;
import de.liga.dart.fileimport.vfs.rangliste.VfsLiga;
import de.liga.dart.model.Liga;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Description:   Liest Rangliste aus vfs Dateien aus holt die Rohdaten zur
 * Berechnung der Rangliste f�r den Seriendruck der Urkunden.
 * F�hrt die Rangberechnung durch!<br/>
 * User: roman
 * Date: 03.05.2009, 15:55:57
 */
public class DbfImportErgebnis extends DbfIO {
    private List<LITTEA> litteams;
    private List<VfsErgebnis> ergebnisse;
    private List<VfsTeam> teams;
    private Map<Long, VfsLiga> ligen;
    private Map<Long, Integer> abzug; // team-nr -> abzug_punkte

    public List<LITTEA> getLitteams() {
        return litteams;
    }

    public List<VfsErgebnis> getErgebnisse() {
        return ergebnisse;
    }

    public List<VfsTeam> getTeams() {
        return teams;
    }

    protected void exchangeData(Liga liga, String path) throws SQLException {
        Statement stmt = connection.createStatement();
        try {
            readCurrentSaison(stmt);
            readLitteams();
            readAbzug();
            readErgebnis();
            computeRangListe();
        } finally {
            stmt.close();
        }
    }


    /**
     * Nur die Vorberechnung + Sammeln und Zusammenf�hren der Daten, Addition der Punkte.
     * Sortierung und Druckaufbereitung finden hier NICHT statt!
     */
    private void computeRangListe() {
        teams = new ArrayList(litteams.size());
        for (final LITTEA eachTEAM : getLitteams()) {
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
            if(abzugPunkte != null) {
                team.setPunkte(- abzugPunkte.intValue());
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
            if (eachTEAM.TEA_NR == eachERG.gast().getTEA_NR()) {
                calc.visit(eachERG);
            }
        }
    }

    private void iterateHeim(LITTEA eachTEAM, RangCalculator calc) {
        for (VfsErgebnis eachERG : getErgebnisse()) {
            if (eachTEAM.TEA_NR == eachERG.heim().getTEA_NR()) {
                calc.visit(eachERG);
            }
        }
    }

    static interface RangCalculator {
        void visit(VfsErgebnis eachERG);
    }

    private void readErgebnis() throws SQLException {
        String selectErgebnis =
                "SELECT heim.TEA_NR, heim.SAI_POSNR, gast.TEA_NR, gast.SAI_POSNR, " +
                        "par.PAR_RUNDE, par.PAR_SPIEL, par.PAR_CODE, " +
                        "erg.ERG_HPUNKT, erg.ERG_HSPIEL, erg.ERG_HSATZ, erg.ERG_HANZAH, " +
                        "erg.ERG_GPUNKT, erg.ERG_GSPIEL, erg.ERG_GSATZ, erg.ERG_GANZAH " +
                        "FROM \"LITERG.DBF\" erg JOIN \"LITPAR.DBF\" par ON " +
                        "erg.SAI_NR=par.SAI_NR AND erg.LIG_NR=par.LIG_NR AND " +
                        "erg.PAR_RUNDE=par.PAR_RUNDE AND erg.PAR_SPIEL=par.PAR_SPIEL " +
                        "JOIN \"LITSAD.DBF\" heim ON " +
                        "par.PAR_HEIM=heim.SAI_POSNR AND par.SAI_NR=heim.SAI_NR AND par.LIG_NR=heim.LIG_NR " +
                        "JOIN \"LITSAD.DBF\" gast ON " +
                        "par.PAR_GAST=gast.SAI_POSNR AND par.SAI_NR=gast.SAI_NR AND par.LIG_NR=gast.LIG_NR " +
                        "WHERE par.SAI_NR=?"; // ? AND par.PAR_STATUS<>'M'";

        PreparedStatement pstmt = connection.prepareStatement(selectErgebnis);
        pstmt.setDate(1, myCurrentSaison);
        ResultSet resultSet = pstmt.executeQuery();
        ergebnisse = new ArrayList(300);
        while (resultSet.next()) {
            int c = 1;
            VfsErgebnis erg = new VfsErgebnis();
            erg.heim().setTEA_NR(resultSet.getLong(c++));
            erg.heim().setPOSNR(resultSet.getInt(c++));

            erg.gast().setTEA_NR(resultSet.getLong(c++));
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

            ergebnisse.add(erg);
        }
    }

    private void readLitteams() throws SQLException {
        String selectTeams =
                "SELECT l.LIG_NAME, l.LIG_KLASSE, t.SAI_NR, t.LIG_NR, t.TEA_NR, " +
                        "t.TEA_NAME, t.LOK_NR, t.TEA_SPIELT, " +
                        "t.TEA_UHRZEI, t.TEA_STATUS FROM \"LITTEA.DBF\" t " +
                        "JOIN \"LITLIG.DBF\" l ON t.LIG_NR=l.LIG_NR " +
                        "WHERE (t.TEA_STATUS IS NULL OR t.TEA_STATUS<>'D') AND t.SAI_NR=?";
        PreparedStatement pstmt = connection.prepareStatement(selectTeams);
        pstmt.setDate(1, myCurrentSaison);
        ResultSet resultSet = pstmt.executeQuery();
        litteams = new ArrayList(100);
        ligen = new HashMap();
        try {
            while (resultSet.next()) {
                LITTEA litteam = createLittea(resultSet);
                if (!"D".equals(litteam.TEA_STATUS)) { // "D" ist "Spielfrei"
                    litteams.add(litteam);
                    if (ligen.get(litteam.LIG_NR) == null) {
                        VfsLiga liga = createLiga(resultSet);
                        ligen.put(liga.getNr(), liga);
                    }
                }
            }
        } finally {
            resultSet.close();
            pstmt.close();
        }
    }

    private void readAbzug() throws SQLException {
        String selectAbzug =
                "SELECT TEA_NR,ABZ_PUNKTE FROM \"LITABZUG.DBF\" WHERE SAI_NR=?";
        PreparedStatement pstmt = connection.prepareStatement(selectAbzug);
        pstmt.setDate(1, myCurrentSaison);
        ResultSet resultSet = pstmt.executeQuery();
        abzug = new HashMap();
        try {
            while (resultSet.next()) {
                abzug.put(resultSet.getLong(1), resultSet.getInt(2));
            }
        } finally {
            resultSet.close();
            pstmt.close();
        }
    }

    private VfsLiga createLiga(ResultSet resultSet) throws SQLException {
        VfsLiga litlig = new VfsLiga();
        litlig.setName(resultSet.getString(1));
        litlig.setKlasse(resultSet.getString(2));
        litlig.setNr(resultSet.getLong(4));
        return litlig;
    }

    private LITTEA createLittea(ResultSet resultSet) throws SQLException {
        LITTEA littea = new LITTEA();
        int c = 3;
        littea.SAI_NR = resultSet.getDate(c++);
        littea.LIG_NR = resultSet.getLong(c++);
        littea.TEA_NR = resultSet.getLong(c++);
        littea.TEA_NAME = resultSet.getString(c++);
        littea.LOK_NR = resultSet.getLong(c++);
        littea.TEA_SPIELT = resultSet.getString(c++);
        littea.TEA_UHRZEI = resultSet.getLong(c++);
        littea.TEA_STATUS = resultSet.getString(c);
        return littea;
    }

}
