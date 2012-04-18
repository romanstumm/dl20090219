package de.liga.dart.fileimport.vfs;

import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.fileimport.DbfIO;
import de.liga.dart.gruppen.service.GruppenService;
import de.liga.dart.model.*;
import de.liga.util.CalendarUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:  Datenexport von Gruppen und Teams in die Altanwendung (.dbf files) <br/>
 * User: roman
 * Date: 17.05.2008, 14:44:13
 */
public class DbfExporterTeamsGruppen extends DbfIO {
    private static final Log log = LogFactory.getLog(DbfExporterTeamsGruppen.class);

    private List<LITLIG> ligList;
    private int maxLigId;
    private int maxTeamID;
    private int spielfreiLOK_NR = -1;

    protected String actionVerb() {
        return "Exportiere";
    }

    protected String actionName() {
        return "Export";
    }

    protected void exchangeData(Liga liga) throws SQLException {
        // delete from LITSAD
        Statement stmt = connection.createStatement();
        log.debug("Alle LITSAD l�schen");
        stmt.execute("DELETE FROM LITSAD");
        GruppenService service = ServiceFactory.get(GruppenService.class);
        List<Ligagruppe> gruppen;
        try {
            readCurrentSaison(stmt);

            log.debug("ermittele die primary keys...");
            maxLigId = readMaxLigID(stmt);
            maxTeamID = 0; // readMaxTeamID(stmt);
            ligList = readLigList(stmt);
            gruppen = service.findGruppen(liga, null);
            // alle Gruppen / Teams durchgehen (auch Spielfrei einf�gen)
            log.debug("alle LITTEA l�schen");
            stmt.execute("DELETE FROM LITTEA");
        } finally {
            stmt.close();
        }

        // INSERT INTO LITTEA (UPDATE Ligateam.externeId)
        String teamInsert = "INSERT INTO LITTEA " +
                "(SAI_NR, LIG_NR, TEA_NR, TEA_NAME, LOK_NR, TEA_KAPIT, TEA_SPIELT, TEA_UHRZEI, " +
                "TEA_STATUS) " +
                "VALUES(?, ?, ?, ?, ?, '', ?, ?, ?)";
        PreparedStatement teamInsertStmt = connection.prepareStatement(teamInsert);

        // oder UPDATE LITTEA (externeID)
        /*String teamUpdate =
"UPDATE LITTEA SET LIG_NR=?, TEA_NAME=?, LOK_NR=?, TEA_SPIELT=?, " +
      "TEA_UHRZEI=?, TEA_STATUS=?" +
      " WHERE TEA_NR=?";*/

        // INSERT INTO LITSAD
        String sadInsert =
                "INSERT INTO LITSAD (SAI_NR, LIG_NR, SAI_POSNR, TEA_NR, SPI_NR) " +
                        "VALUES (?,?,?,?,'P0')";
        PreparedStatement sadInsertStmt = connection.prepareStatement(sadInsert);

        // wenn LITLIG missing: INSERT INTO LITLIG
        String ligInsert = "INSERT INTO LITLIG (LIG_NR, LIG_NAME, LIG_ZUSATZ, SPO_SPORTA," +
                "LIG_STAERK, LIG_DISZIP, LIG_SPIELT, LIG_UHRZEI) " +
                "VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement ligInsertStmt = connection.prepareStatement(ligInsert);

        // wenn LITSAI missing: INSERT INTO LITSAI
        String saiInsert = "INSERT INTO LITSAI (SAI_NR, LIG_NR, SAI_EINZEL, SAI_STATUS) " +
                "VALUES (?,?,?,?)";
        PreparedStatement saiInsertStmt = connection.prepareStatement(saiInsert);

        try {
            for (Ligagruppe gruppe : gruppen) {
                LITLIG lig = findOrCreateLITLIG(gruppe, liga, ligInsertStmt);
                LITSAI sai = findOrCreateLITSAI(lig, saiInsertStmt); // ensure sai exists (required by vfs)
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
            saiInsertStmt.close();
//            deleteIndexFiles(path);
        }
    }

    private List<LITSAI> saiList;

    private LITSAI findOrCreateLITSAI(LITLIG lig, PreparedStatement pstmt) throws SQLException {
        if (saiList == null) {
            Statement stmt = connection.createStatement();
            try {
                saiList = readSaiList(stmt);
            } finally {
                stmt.close();
            }
        }
        for(LITSAI sai : saiList) {
            if(sai.LIG_NR == lig.LIG_NR && sai.SAI_NR.equals(myCurrentSaison)) {
                return sai;
            }
        }
        LITSAI sai = new LITSAI();
        sai.LIG_NR = lig.LIG_NR;
        sai.SAI_NR = myCurrentSaison;
        sai.SAI_EINZEL = "kein";
        sai.SAI_STATUS = null;
        insertLITSAI(pstmt, sai);
        return sai;
    }


    private void insertLITSAI(PreparedStatement stmt, LITSAI sai) throws SQLException {
        stmt.setDate(1, sai.SAI_NR);
        stmt.setInt(2, sai.LIG_NR);
        stmt.setString(3, sai.SAI_EINZEL);
        stmt.setString(4, sai.SAI_STATUS);
        stmt.executeUpdate();
    }

/*
    private void deleteIndexFiles(String path) {
        File dir = new File(path);
        File[] indexFiles = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toUpperCase().endsWith(".MDX");
            }
        });
        for (File each : indexFiles) {
            if (each.delete()) {
                log.info(each.getPath() + " wurde gel�scht.");
            } else {
                log.warn(each.getPath() + " konnte nicht gel�scht werden!");
            }
        }
    }*/

    private LITSAD writeLITSAD(LITTEA tea, int platzNr,
                               PreparedStatement sadInsertStmt) throws SQLException {
        LITSAD sad = new LITSAD();
        sad.SAI_NR = myCurrentSaison;
        sad.LIG_NR = tea.LIG_NR;
        sad.SAI_POSNR = platzNr;
        sad.TEA_NR = tea.TEA_NR;
        if (log.isDebugEnabled())
            log.debug("LITSAD einf�gen: " + sad.TEA_NR + ", " + sad.SAI_POSNR);
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
            tea.TEA_UHRZEI = CalendarUtils.toVfsValue(team.getSpielzeit());
            tea.TEA_STATUS = "";
        }
        if (log.isDebugEnabled())
            log.debug("LITTEA einf�gen: " + tea.TEA_NAME + " (" + tea.TEA_NR + ")");
        insertLITTEA(teamInsertStmt, tea);
        return tea;
    }

    private int getOrCreateLokNr(Spielort spielort) throws SQLException {
        // wenn Spielort.externeId nicht vorhanden: insert Spielort!
        if (spielort.getExterneId() != null && spielort.getExterneId().length() > 0
                && StringUtils.isNumeric(spielort.getExterneId())) {
            return Integer.parseInt(spielort.getExterneId());
        } else { // erzeuge spielort (fehlte bisher in Altdaten)
            int lokId = findNextLogID();
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
                connection.prepareStatement("insert into LITLOK (" +
                        "LOK_NR, LOK_ID, LOK_DFUE, LOK_NAME, LOK_ZUSATZ, LOK_STRASS, LOK_PLZ, " +
                        "LOK_ORT, LOK_TEL, LOK_FAX, LOK_RUHETA, AUF_NR) VALUES " +
                        "(?, 0, 0, ?, 'Gastst�tte', ?, ?, ?, ?, ?, ?, 1)");
        pstmt.setInt(1, Integer.parseInt(obj.LOK_NR));
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

    private int findNextLogID() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT MAX(LOK_NR) FROM LITLOK");
        resultSet.next();
        int id = resultSet.getInt(1);
        resultSet.close();
        stmt.close();
        return id + 1;
    }

    private int findOrCreateSpielfreiOrt() throws SQLException {
        if (spielfreiLOK_NR == -1) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet =
                    stmt.executeQuery(
                            "SELECT LOK_NR FROM LITLOK WHERE LOK_NAME = 'Spielfrei'");
            if (resultSet.next()) {
                spielfreiLOK_NR = resultSet.getInt(1);
            }
            resultSet.close();
            stmt.close();
            if (spielfreiLOK_NR < 0) {
                spielfreiLOK_NR = findNextLogID();
                stmt = connection.createStatement();
                stmt.execute(
                        "INSERT INTO LITLOK (LOK_NR, LOK_ID, LOK_DFUE, LOK_NAME, LOK_RUHETA, AUF_NR) " +
                                "VALUES(" + (spielfreiLOK_NR) + ",0,0,'Spielfrei','kein',2)");
                stmt.close();
            }
        }
        return spielfreiLOK_NR;
    }

    private LITLIG findOrCreateLITLIG(Ligagruppe gruppe, Liga liga, PreparedStatement ligInsertStmt)
            throws SQLException {
        log.debug("suche LITLIG " + gruppe.getLigaklasse() + ", nr: " + gruppe.getGruppenNr());
        LITLIG lig = findLITLIG(gruppe.getLigaklasse(), gruppe.getGruppenNr());
        if (lig == null) {
            String klassenName = gruppe.getLigaklasse().getKlassenName();
            String liganame = liga.getLigaName();
            lig = new LITLIG();
            lig.LIG_NR = (++maxLigId);
            lig.LIG_ZUSATZ = "";
            lig.SPO_SPORTA = "DART";
            if ("A".equals(klassenName)) {
                lig.LIG_STAERK = 3;
            } else if ("B".equals(klassenName)) {
                lig.LIG_STAERK = 2;
            } else if ("C".equals(klassenName)) {
                lig.LIG_STAERK = 1;
            } else if ("Bez".equals(klassenName)) {
                lig.LIG_STAERK = 4;
            } else {
                lig.LIG_STAERK = 0;
            }
            if (klassenName.equals("Bez")) klassenName = "Bezirksliga "; // vfs nennt es so
            if ("Rhl-WW".equals(liganame)) liganame = "Rheinland-Westerwald"; // vfs nennt es so
            lig.LIG_NAME = (klassenName + gruppe.getGruppenNr() +
                    " " + liganame);
            lig.LIG_DISZIP = "301";
            lig.LIG_SPIELT = "Sonntag";
            lig.LIG_UHRZEI = 20 * 60 * 60;
            if (log.isDebugEnabled()) log.debug("LITLIG einf�gen: " + lig.LIG_NAME);
            insertLITLIG(ligInsertStmt, lig);
        } else {
            log.debug("gefunden!");
        }
        return lig;
    }

    private LITLIG findLITLIG(Ligaklasse ligaklasse, int gruppenNr) {
        LITLIG found = findLITLIG(ligaklasse.getKlassenName(), gruppenNr);
        if (found == null && "Bez".equals(ligaklasse.getKlassenName()))
            found = findLITLIG("Bezirksliga ", gruppenNr);
        if (found == null && "Bzo".equals(ligaklasse.getKlassenName()))
            found = findLITLIG("Bezirksoberliga ", gruppenNr);
        return found;
    }

    private LITLIG findLITLIG(String klassenname, int gruppenNr) {
        String search = klassenname + gruppenNr + " ";
        for (LITLIG lig : ligList) {
            if (lig.LIG_NAME.startsWith(search)) {
                return lig;
            }
        }
        if (gruppenNr == 1) {
            search = klassenname + " ";
            for (LITLIG lig : ligList) {
                if (lig.LIG_NAME.startsWith(search)) {
                    return lig;
                }
            }
        }
        return null;
    }

    private int readMaxLigID(Statement stmt) throws SQLException {
        String maxLigIDSql = "SELECT MAX(LIG_NR) FROM LITLIG";
        ResultSet resultSet = stmt.executeQuery(maxLigIDSql);
        resultSet.next();
        int maxLigId = resultSet.getInt(1);
        resultSet.close();
        return maxLigId;
    }

    private List<LITLIG> readLigList(Statement stmt) throws SQLException {
        ResultSet resultSet;
        String ligSelect = "SELECT LIG_NR, LIG_NAME, LIG_ZUSATZ, SPO_SPORTA," +
                "LIG_STAERK, LIG_DISZIP, LIG_SPIELT, LIG_UHRZEI FROM LITLIG";
        resultSet = stmt.executeQuery(ligSelect);
        List<LITLIG> ligList = new ArrayList();
        while (resultSet.next()) {
            ligList.add(readLITLIG(resultSet));
        }
        resultSet.close();
        return ligList;
    }

    private List<LITSAI> readSaiList(Statement stmt) throws SQLException {
        ResultSet resultSet;
        String saiSelect = "SELECT SAI_NR, LIG_NR, SAI_EINZEL, SAI_STATUS FROM LITSAI";
        resultSet = stmt.executeQuery(saiSelect);
        try {
            saiList = new ArrayList();
            while (resultSet.next()) {
                saiList.add(readLITSAI(resultSet));
            }
        } finally {
            resultSet.close();
        }
        return saiList;
    }

    private LITSAI readLITSAI(ResultSet resultSet) throws SQLException {
        LITSAI sai = new LITSAI();
        sai.SAI_NR = resultSet.getDate(1);
        sai.LIG_NR = resultSet.getInt(2);
        sai.SAI_EINZEL = resultSet.getString(3);
        sai.SAI_STATUS = resultSet.getString(4);
        return sai;
    }

    private void insertLITSAD(PreparedStatement stmt, LITSAD sad) throws SQLException {
        stmt.setDate(1, sad.SAI_NR);
        stmt.setInt(2, sad.LIG_NR);
        stmt.setInt(3, sad.SAI_POSNR);
        stmt.setInt(4, sad.TEA_NR);
        stmt.executeUpdate();
    }

    private void insertLITTEA(PreparedStatement stmt, LITTEA team) throws SQLException {
        stmt.setDate(1, team.SAI_NR);
        stmt.setInt(2, team.LIG_NR);
        stmt.setInt(3, team.TEA_NR);
        stmt.setString(4, team.TEA_NAME);
        stmt.setInt(5, team.LOK_NR);
        stmt.setString(6, team.TEA_SPIELT);
        stmt.setInt(7, team.TEA_UHRZEI);
        stmt.setString(8, team.TEA_STATUS);
        stmt.executeUpdate();
    }

    private LITLIG readLITLIG(ResultSet resultSet) throws SQLException {
        LITLIG lig = new LITLIG();
        lig.LIG_NR = resultSet.getInt(1);
        lig.LIG_NAME = resultSet.getString(2);
        lig.LIG_ZUSATZ = resultSet.getString(3);
        lig.SPO_SPORTA = resultSet.getString(4);
        lig.LIG_STAERK = resultSet.getInt(5);
        lig.LIG_DISZIP = resultSet.getString(6);
        lig.LIG_SPIELT = resultSet.getString(7);
        lig.LIG_UHRZEI = resultSet.getInt(8);
        return lig;
    }

    private void insertLITLIG(PreparedStatement stmt, LITLIG lig) throws SQLException {
        stmt.setInt(1, lig.LIG_NR);
        stmt.setString(2, lig.LIG_NAME);
        stmt.setString(3, lig.LIG_ZUSATZ);
        stmt.setString(4, lig.SPO_SPORTA);
        stmt.setInt(5, lig.LIG_STAERK);
        stmt.setString(6, lig.LIG_DISZIP);
        stmt.setString(7, lig.LIG_SPIELT);
        stmt.setObject(8, lig.LIG_UHRZEI);
        stmt.executeUpdate();
    }

}
