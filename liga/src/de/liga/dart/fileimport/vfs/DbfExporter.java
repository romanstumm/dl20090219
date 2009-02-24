package de.liga.dart.fileimport.vfs;

import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.fileimport.DbfIO;
import de.liga.dart.gruppen.service.GruppenService;
import de.liga.dart.model.*;
import de.liga.dart.exception.DartException;
import de.liga.util.CalendarUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FilenameFilter;
import java.io.FileFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Description:  Datenexport von Gruppen und Teams in die Altanwendung (.dbf files) <br/>
 * User: roman
 * Date: 17.05.2008, 14:44:13
 */
public class DbfExporter extends DbfIO {
    private static final Log log = LogFactory.getLog(DbfExporter.class);

    private List<LITLIG> ligList;
    private long maxLigId;
    private int maxTeamID;
    private long spielfreiLOK_NR = -1;
    private Date myCurrentSaison;

    protected String actionVerb() {
        return "Exportiere";
    }

    protected String actionName() {
        return "Export";
    }

    protected void exchangeData(Liga liga, String path) throws SQLException {
        // delete from LITSAD
        Statement stmt = connection.createStatement();
        stmt.execute("DELETE FROM \"LITSAD.DBF\"");
        GruppenService service = ServiceFactory.get(GruppenService.class);
        List<Ligagruppe> gruppen;
        try {

            // ermittele aktuelle Saison
            ResultSet resultSet = stmt.executeQuery("SELECT ANW_SAISON FROM \"LITANW.DBF\"");
            try {
                if (resultSet.next()) {
                    myCurrentSaison = resultSet.getDate(1);
                }
            } finally {
                resultSet.close();
            }
            if (myCurrentSaison == null)
                throw new DartException("Kann aktuelle Saison nicht auslesen.");
            // ermittele die primary keys
            maxLigId = readMaxLigID(stmt);
            maxTeamID = 0; // readMaxTeamID(stmt);
            ligList = readLigList(stmt);
            gruppen = service.findGruppen(liga, null);
            // alle Gruppen / Teams durchgehen (auch Spielfrei einfügen)
            stmt.execute("DELETE FROM \"LITTEA.DBF\"");
        } finally {
            stmt.close();
        }

        // INSERT INTO LITTEA (UPDATE Ligateam.externeId)
        String teamInsert = "INSERT INTO \"LITTEA.DBF\" " +
                "(SAI_NR, LIG_NR, TEA_NR, TEA_NAME, LOK_NR, TEA_KAPIT, TEA_SPIELT, TEA_UHRZEI, " +
                "TEA_STATUS) " +
                "VALUES(?, ?, ?, ?, ?, '', ?, ?, ?)";
        PreparedStatement teamInsertStmt = connection.prepareStatement(teamInsert);

        // oder UPDATE LITTEA (externeID)
        /*String teamUpdate =
"UPDATE \"LITTEA.DBF\" SET LIG_NR=?, TEA_NAME=?, LOK_NR=?, TEA_SPIELT=?, " +
      "TEA_UHRZEI=?, TEA_STATUS=?" +
      " WHERE TEA_NR=?";*/

        // INSERT INTO LITSAD
        String sadInsert =
                "INSERT INTO \"LITSAD.DBF\" (SAI_NR, LIG_NR, SAI_POSNR, TEA_NR, SPI_NR) " +
                        "VALUES (?,?,?,?,'P0')";
        PreparedStatement sadInsertStmt = connection.prepareStatement(sadInsert);

        // wenn LITLIG missing: INSERT INTO LITLIG
        String ligInsert = "INSERT INTO \"LITLIG.DBF\" (LIG_NR, LIG_NAME, LIG_ZUSATZ, SPO_SPORTA," +
                "LIG_STAERK, LIG_DISZIP, LIG_SPIELT, LIG_UHRZEI) " +
                "VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement ligInsertStmt = connection.prepareStatement(ligInsert);

        try {
            for (Ligagruppe gruppe : gruppen) {
                LITLIG lig = findOrCreateLITLIG(gruppe, liga, ligInsertStmt);
                List<Ligateamspiel> spiele = service.findSpieleInGruppe(gruppe);
                int platzNr = 1;
                for (Ligateamspiel spiel : spiele) {
                    LITTEA tea;
                    while (spiel.getPlatzNr() >
                            platzNr) { // fehlende Spielfrei Positionen ergaenzen
                        tea = writeLITTEA(lig, null, teamInsertStmt);
                        writeLITSAD(tea, platzNr, sadInsertStmt);
                        platzNr++;
                    }
                    tea = writeLITTEA(lig, spiel, teamInsertStmt);
                    writeLITSAD(tea, spiel.getPlatzNr(), sadInsertStmt);
                    platzNr++;
                }
            }
        } finally {
            ligInsertStmt.close();
            sadInsertStmt.close();
            teamInsertStmt.close();
            deleteIndexFiles(path);
        }
    }

    private void deleteIndexFiles(String path) {
        File dir = new File(path);
        File[] indexFiles = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toUpperCase().endsWith(".MDX");
            }
        });
        for (File each : indexFiles) {
            if (each.delete()) {
                log.info(each.getPath() + " wurde gelöscht.");
            } else {
                log.warn(each.getPath() + " konnte nicht gelöscht werden!");
            }
        }
    }

    private LITSAD writeLITSAD(LITTEA tea, int platzNr,
                               PreparedStatement sadInsertStmt) throws SQLException {
        LITSAD sad = new LITSAD();
        sad.SAI_NR = myCurrentSaison;
        sad.LIG_NR = tea.LIG_NR;
        sad.SAI_POSNR = platzNr;
        sad.TEA_NR = tea.TEA_NR;
        insertLITSAD(sadInsertStmt, sad);
        return sad;
    }

    private LITTEA writeLITTEA(LITLIG lig, Ligateamspiel spiel, PreparedStatement teamInsertStmt)
            throws SQLException {
        LITTEA tea = new LITTEA();
        tea.TEA_NR = (++maxTeamID);
        tea.LIG_NR = lig.LIG_NR;
        tea.SAI_NR = myCurrentSaison;
        if (spiel == null || spiel.isSpielfrei()) {
            tea.TEA_NAME = "Spielfrei";
            // Spielort fuer Spielfrei finden, ggf. erzeugen
            tea.LOK_NR = findOrCreateSpielfreiOrt();
            tea.TEA_SPIELT = "Montag"; // ??
            tea.TEA_STATUS = "D";
            tea.TEA_UHRZEI = 20 * 60 * 60;
        } else {
            Ligateam team = spiel.getLigateam();
            team.setExterneId(String.valueOf(tea.TEA_NR)); // derzeit nutzlos, aber dennoch...
            tea.TEA_NAME = team.getTeamName();
            tea.LOK_NR = getOrCreateLokNr(team.getSpielort());
            tea.TEA_SPIELT = team.getWochentagName();
            tea.TEA_UHRZEI = CalendarUtils.toOldLongValue(team.getSpielzeit());
            tea.TEA_STATUS = "";
        }
        insertLITTEA(teamInsertStmt, tea);
        return tea;
    }

    private long getOrCreateLokNr(Spielort spielort) throws SQLException {
        // wenn Spielort.externeId nicht vorhanden: insert Spielort!
        if (spielort.getExterneId() != null) {
            return Long.parseLong(spielort.getExterneId());
        } else { // erzeuge spielort (fehlte bisher in Altdaten)
            long lokId = findNextLogID();
            LITLOK obj = new LITLOK();
            obj.LOK_NR = String.valueOf(lokId);
            obj.LOK_NAME = spielort.getSpielortName();
            obj.LOK_STRASS = spielort.getStrasse();
            obj.LOK_PLZ = spielort.getPlz();
            obj.LOK_ORT = spielort.getOrt();
            obj.LOK_TEL = spielort.getTelefon();
            obj.LOK_FAX = spielort.getFax();
            obj.LOK_RUHETA = spielort.getFreierTagName();
            insertLITLOK(obj);
            spielort.setExterneId(String.valueOf(lokId));
            return lokId;
        }
    }

    private LITLOK insertLITLOK(LITLOK obj) throws SQLException {
        PreparedStatement pstmt =
                connection.prepareStatement("insert into \"LITLOK.DBF\" (" +
                        "LOK_NR, LOK_ID, LOK_DFUE, LOK_NAME, LOK_ZUSATZ, LOK_STRASS, LOK_PLZ, " +
                        "LOK_ORT, LOK_TEL, LOK_FAX, LOK_RUHETA, AUF_NR) VALUES " +
                        "(?, 0, 0, ?, 'Gaststätte', ?, ?, ?, ?, ?, ?, 1)");
        pstmt.setLong(1, Long.parseLong(obj.LOK_NR));
        pstmt.setString(2, obj.LOK_NAME);
        pstmt.setString(3, obj.LOK_STRASS);
        pstmt.setString(4, obj.LOK_PLZ);
        pstmt.setString(5, obj.LOK_ORT);
        pstmt.setString(6, obj.LOK_TEL);
        pstmt.setString(7, obj.LOK_FAX);
        pstmt.setString(8, obj.LOK_RUHETA);
        pstmt.close();
        return obj;
    }

    private long findNextLogID() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT MAX(LOK_NR) FROM \"LITLOK.DBF\"");
        resultSet.next();
        long id = resultSet.getLong(1);
        resultSet.close();
        stmt.close();
        return id + 1;
    }

    private long findOrCreateSpielfreiOrt() throws SQLException {
        if (spielfreiLOK_NR == -1) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet =
                    stmt.executeQuery(
                            "SELECT LOK_NR FROM \"LITLOK.DBF\" WHERE LOK_NAME = 'Spielfrei'");
            if (resultSet.next()) {
                spielfreiLOK_NR = resultSet.getLong(1);
            }
            resultSet.close();
            stmt.close();
            if (spielfreiLOK_NR < 0) {
                spielfreiLOK_NR = findNextLogID();
                stmt = connection.createStatement();
                stmt.execute(
                        "INSERT INTO \"LITLOK.DBF\" (LOK_NR, LOK_ID, LOK_DFUE, LOK_NAME, LOK_RUHETA, AUF_NR) " +
                                "VALUES(" + (spielfreiLOK_NR) + ",0,0,'Spielfrei','kein',2)");
                stmt.close();
            }
        }
        return spielfreiLOK_NR;
    }

    private LITLIG findOrCreateLITLIG(Ligagruppe gruppe, Liga liga, PreparedStatement ligInsertStmt)
            throws SQLException {
        LITLIG lig = findLITLIG(gruppe.getLigaklasse(), gruppe.getGruppenNr());
        if (lig == null) {
            String klassenName = gruppe.getLigaklasse().getKlassenName();
            lig = new LITLIG();
            lig.LIG_NR = (++maxLigId);
            lig.LIG_NAME = (klassenName + gruppe.getGruppenNr() +
                    " " + liga.getLigaName());
            lig.LIG_ZUSATZ = "";
            lig.SPO_SPORTA = "DART";
            if ("A".equals(klassenName)) {
                lig.LIG_STAERK = 3;
            } else if ("B".equals(klassenName)) {
                lig.LIG_STAERK = 2;
            } else if ("C".equals(klassenName)) {
                lig.LIG_STAERK = 1;
            } else {
                lig.LIG_STAERK = 0;
            }
            lig.LIG_DISZIP = "301";
            lig.LIG_SPIELT = "Sonntag";
            lig.LIG_UHRZEI = 20 * 60 * 60;
            insertLITLIG(ligInsertStmt, lig);
        }
        return lig;
    }

    private LITLIG findLITLIG(Ligaklasse ligaklasse, int gruppenNr) {
        String search = ligaklasse.getKlassenName() + gruppenNr + " ";
        for (LITLIG lig : ligList) {
            if (lig.LIG_NAME.startsWith(search)) {
                return lig;
            }
        }
        if (gruppenNr == 1) {
            search = ligaklasse.getKlassenName() + " ";
            for (LITLIG lig : ligList) {
                if (lig.LIG_NAME.startsWith(search)) {
                    return lig;
                }
            }
        }
        return null;
    }

    /*private long readMaxTeamID(Statement stmt) throws SQLException {
        String maxTeamIDSql = "SELECT MAX(TEA_NR) FROM \"LITTEA.DBF\"";
        ResultSet resultSet = stmt.executeQuery(maxTeamIDSql);
        resultSet.next();
        long maxTeamID = resultSet.getLong(1);
        resultSet.close();
        return maxTeamID;
    }     */

    private long readMaxLigID(Statement stmt) throws SQLException {
        String maxLigIDSql = "SELECT MAX(LIG_NR) FROM \"LITLIG.DBF\"";
        ResultSet resultSet = stmt.executeQuery(maxLigIDSql);
        resultSet.next();
        long maxLigId = resultSet.getLong(1);
        resultSet.close();
        return maxLigId;
    }

    private List<LITLIG> readLigList(Statement stmt) throws SQLException {
        ResultSet resultSet;
        String ligSelect = "SELECT LIG_NR, LIG_NAME, LIG_ZUSATZ, SPO_SPORTA," +
                "LIG_STAERK, LIG_DISZIP, LIG_SPIELT, LIG_UHRZEI FROM \"LITLIG.DBF\"";
        resultSet = stmt.executeQuery(ligSelect);
        List<LITLIG> ligList = new ArrayList();
        while (resultSet.next()) {
            ligList.add(readLITLIG(resultSet));
        }
        resultSet.close();
        return ligList;
    }

    private void insertLITSAD(PreparedStatement stmt, LITSAD sad) throws SQLException {
        stmt.setDate(1, sad.SAI_NR);
        stmt.setLong(2, sad.LIG_NR);
        stmt.setInt(3, sad.SAI_POSNR);
        stmt.setLong(4, sad.TEA_NR);
        stmt.executeUpdate();
    }

    private void insertLITTEA(PreparedStatement stmt, LITTEA team) throws SQLException {
        stmt.setDate(1, team.SAI_NR);
        stmt.setLong(2, team.LIG_NR);
        stmt.setLong(3, team.TEA_NR);
        stmt.setString(4, team.TEA_NAME);
        stmt.setLong(5, team.LOK_NR);
        stmt.setString(6, team.TEA_SPIELT);
        stmt.setLong(7, team.TEA_UHRZEI);
        stmt.setString(8, team.TEA_STATUS);
        stmt.executeUpdate();
    }

    /*
    private boolean updateLITTEA(PreparedStatement stmt, LITTEA team) throws SQLException {
        stmt.setLong(1, team.LIG_NR);
        stmt.setString(2, team.TEA_NAME);
        stmt.setLong(3, team.LOK_NR);
        stmt.setString(4, team.TEA_SPIELT);
        stmt.setLong(5, team.TEA_UHRZEI);
        stmt.setString(6, team.TEA_STATUS);
        stmt.setLong(7, team.TEA_NR);
        return stmt.executeUpdate() > 0;
    }  */

    private LITLIG readLITLIG(ResultSet resultSet) throws SQLException {
        LITLIG lig = new LITLIG();
        lig.LIG_NR = resultSet.getLong(1);
        lig.LIG_NAME = resultSet.getString(2);
        lig.LIG_ZUSATZ = resultSet.getString(3);
        lig.SPO_SPORTA = resultSet.getString(4);
        lig.LIG_STAERK = resultSet.getInt(5);
        lig.LIG_DISZIP = resultSet.getString(6);
        lig.LIG_SPIELT = resultSet.getString(7);
        lig.LIG_UHRZEI = resultSet.getLong(8);
        return lig;
    }

    private void insertLITLIG(PreparedStatement stmt, LITLIG lig) throws SQLException {
        stmt.setLong(1, lig.LIG_NR);
        stmt.setString(2, lig.LIG_NAME);
        stmt.setString(3, lig.LIG_ZUSATZ);
        stmt.setString(4, lig.SPO_SPORTA);
        stmt.setInt(5, lig.LIG_STAERK);
        stmt.setString(6, lig.LIG_DISZIP);
        stmt.setString(7, lig.LIG_SPIELT);
        stmt.setLong(8, lig.LIG_UHRZEI);
        stmt.executeUpdate();
    }

}
