package de.liga.dart.fileimport.vfs;

import de.liga.dart.automatenaufsteller.service.AutomatenaufstellerService;
import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.fileimport.DbfIO;
import de.liga.dart.model.Automatenaufsteller;
import de.liga.dart.model.Liga;
import de.liga.dart.model.Spielort;
import de.liga.dart.spielort.service.SpielortService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by roman.stumm@viaboxx.de
 * User: roman
 * Date: 15.01.12
 * Time: 18:38
 * (c) Viaboxx GmbH, Koenigswinter, 2011
 */
public class DbfExporterSpielorteAufsteller extends DbfIO {
    private static final Log log = LogFactory.getLog(DbfExporterSpielorteAufsteller.class);
    private int maxLokId = 0;
    private int maxAufId = 0;
    private AutomatenaufstellerService aufstellerService;
    private SpielortService spielortService;

    @Override
    protected void exchangeData(Liga liga) throws SQLException {
        exportAufsteller(liga);
        exportSpielorte(liga);
    }

    private void exportAufsteller(Liga liga) throws SQLException {
        String sql;
        sql = "INSERT INTO LITAUF (AUF_NAME, AUF_ZUSATZ, AUF_STRASS, AUF_PLZ, AUF_ORT, AUF_TEL, AUF_FAX, AUF_NR) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement litaufInsert = connection.prepareStatement(sql);
        sql = "UPDATE LITAUF SET AUF_NAME=?, AUF_ZUSATZ=?, AUF_STRASS=?, AUF_PLZ=?, AUF_ORT=?, AUF_TEL=?, AUF_FAX=? " +
                "WHERE AUF_NR = ?";
        PreparedStatement litaufUpdate = connection.prepareStatement(sql);
        PreparedStatement litaufSelect = connection.prepareStatement("SELECT AUF_NR FROM LITAUF ");
        PreparedStatement litaufDelete = connection.prepareStatement("DELETE FROM LITAUF WHERE AUF_NR=? ");

        try {
            aufstellerService = ServiceFactory.get(AutomatenaufstellerService.class);
            List<Automatenaufsteller> alle = aufstellerService.findAllAutomatenaufstellerByLiga(liga);

            Set<String> existingExternalIDs = fetchAllExternalIDs(litaufSelect);
            maxAufId = findMaxId(existingExternalIDs);
            log.info("Read max LITAUF.AUF_NR = " + maxAufId);
            Set<String> newExternalIDs = new HashSet<String>();
            for (Automatenaufsteller each : alle) {
                if (existingExternalIDs.contains(each.getExterneId())) { // update
                    log.info("update dbase LITAUF " + each + ", externalId: " + each.getExterneId());
                    setAufstellerParameters(litaufUpdate, each);
                    log.info("rows affected: " + litaufUpdate.executeUpdate());
                    log.info("done as externalId: " + each.getExterneId());
                } else { // insert
                    log.info("insert dbase LITAUF " + each + ", externalId: " + each.getExterneId());
                    setAufstellerParameters(litaufInsert, each);
                    log.info("rows affected: " + litaufInsert.executeUpdate());
                    log.info("done as with externalId: " + each.getExterneId());
                }
                newExternalIDs.add(each.getExterneId());
            }
            // delete all others
            for (String id : existingExternalIDs) {
                if (!newExternalIDs.contains(id)) { // delete
                    log.info("delete dbase LITAUF.AUF_NR=" + id);
                    litaufDelete.setString(1, id);
                    log.info("rows affected: " + litaufDelete.executeUpdate());
                }
            }
        } finally {
            litaufSelect.close();
            litaufInsert.close();
            litaufUpdate.close();
            litaufDelete.close();
        }
    }

    private int findMaxId(Set<String> existingExternalIDs) {
        int max = 0;
        for (String id : existingExternalIDs) {
            try {
                int theInt = Integer.parseInt(id);
                if (theInt > max) {
                    max = theInt;
                }
            } catch (NumberFormatException ignore) {
            }
        }
        return max;
    }

    private Set<String> fetchAllExternalIDs(PreparedStatement litaufSelect) throws SQLException {
        ResultSet rs = litaufSelect.executeQuery();
        Set<String> ids = new HashSet<String>();
        try {
            while (rs.next()) {
                ids.add(String.valueOf(rs.getInt(1)));
            }
        } finally {
            rs.close();
        }
        return ids;
    }

    private void setAufstellerParameters(PreparedStatement stmt, Automatenaufsteller each) throws SQLException {
        stmt.setString(1, each.getAufstellerName());   // NAME
        stmt.setString(2, each.getKontaktName()); // ZUSATZ
        stmt.setString(3, each.getStrasse());     // STRASS
        stmt.setString(4, each.getPlz());          // PLZ
        stmt.setString(5, each.getOrt());          // ORT
        stmt.setString(6, each.getTelefon());      // TEL
        stmt.setString(7, each.getFax());          // FAX
        if (each.getExterneId() == null || each.getExterneId().length() == 0) {
            each.setExterneId(String.valueOf(++maxAufId));
            aufstellerService.saveAutomatenaufsteller(each);
        }
        stmt.setString(8, each.getExterneId());    // AUF_NR
    }

    private void exportSpielorte(Liga liga) throws SQLException {
        String sql;
        sql = "INSERT INTO LITLOK (LOK_NAME, LOK_ZUSATZ, LOK_STRASS, LOK_PLZ, LOK_ORT, LOK_TEL, LOK_FAX, LOK_RUHETA, AUF_NR, LOK_NR, LOK_ID, LOK_DFUE) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement litaufInsert = connection.prepareStatement(sql);
        sql = "UPDATE LITLOK SET LOK_NAME=?, LOK_ZUSATZ=?, LOK_STRASS=?, LOK_PLZ=?, LOK_ORT=?, LOK_TEL=?, LOK_FAX=?, LOK_RUHETA=?, AUF_NR=? " +
                "WHERE LOK_NR = ?";
        PreparedStatement litlokUpdate = connection.prepareStatement(sql);
        PreparedStatement litlokSelect = connection.prepareStatement("SELECT LOK_NR FROM LITLOK");
        PreparedStatement litlokDelete = connection.prepareStatement("DELETE FROM LITLOK WHERE LOK_NR=? ");

        try {
            spielortService = ServiceFactory.get(SpielortService.class);
            List<Spielort> alle = spielortService.findAllSpielortByLiga(liga);

            Set<String> existingExternalIDs = fetchAllExternalIDs(litlokSelect);
            maxLokId = findMaxId(existingExternalIDs);
            log.info("Read max LITLOK.LOK_NR = " + maxLokId);
            Set<String> newExternalIDs = new HashSet<String>();
            for (Spielort each : alle) {
                if (existingExternalIDs.contains(each.getExterneId())) { // update
                    log.info("update dbase LITLOK " + each + ", externalId: " + each.getExterneId());
                    setSpielortParameters(litlokUpdate, each);
                    log.info("rows affected: " + litlokUpdate.executeUpdate());
                    log.info("done as externalId: " + each.getExterneId());
                } else { // insert
                    log.info("insert dbase LITLOK " + each + ", externalId: " + each.getExterneId());
                    setSpielortParameters(litaufInsert, each);
                    litaufInsert.setInt(11, 0);
                    litaufInsert.setInt(12, 0);
                    log.info("rows affected: " + litaufInsert.executeUpdate());
                    log.info("done as externalId: " + each.getExterneId());
                }
                newExternalIDs.add(each.getExterneId());
            }
            // delete all others
            for (String id : existingExternalIDs) {
                if (!newExternalIDs.contains(id)) { // delete
                    log.info("delete dbase LITLOK.LOK_NR=" + id);
                    litlokDelete.setString(1, id);
                    log.info("rows affected: " + litlokDelete.executeUpdate());
                }
            }
        } finally {
            litlokSelect.close();
            litaufInsert.close();
            litlokUpdate.close();
            litlokDelete.close();
        }
    }

    private void setSpielortParameters(PreparedStatement stmt, Spielort spielort) throws SQLException {
        stmt.setString(1, spielort.getSpielortName()); // LOK_NAME,
        stmt.setString(2, spielort.getKontaktName()); // LOK_ZUSATZ,
        stmt.setString(3, spielort.getStrasse()); // LOK_STRASS,
        stmt.setString(4, spielort.getPlz()); // LOK_PLZ,
        stmt.setString(5, spielort.getOrt()); // LOK_ORT,
        stmt.setString(6, spielort.getTelefon()); // LOK_TEL,
        stmt.setString(7, spielort.getFax()); // LOK_FAX,
        stmt.setString(8, spielort.getFreierTagName()); // LOK_RUHETA,
        stmt.setString(9, spielort.getAutomatenaufsteller().getExterneId()); // AUF_NR,
        if (spielort.getExterneId() == null || spielort.getExterneId().length() == 0) {
            spielort.setExterneId(String.valueOf(++maxLokId));
            spielortService.saveSpielort(spielort);
        }
        stmt.setString(10, spielort.getExterneId()); // LOK_NR
    }
}
