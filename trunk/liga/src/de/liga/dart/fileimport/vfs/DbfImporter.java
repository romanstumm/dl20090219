package de.liga.dart.fileimport.vfs;

import de.liga.dart.automatenaufsteller.service.AutomatenaufstellerService;
import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.fileimport.DbfIO;
import de.liga.dart.model.Automatenaufsteller;
import de.liga.dart.model.Liga;
import de.liga.dart.model.Spielort;
import de.liga.dart.spielort.service.SpielortService;
import org.apache.commons.lang.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Description: Datenabgleich mit Altdaten in DBF Datenbank
 * (Aufsteller, Spielorte)<br/>
 * User: roman
 * Date: 21.03.2008, 11:31:57
 */
public class DbfImporter extends DbfIO {
    protected String actionVerb() { return "Importiere"; }
    protected String actionName() { return "Import"; }

    protected void exchangeData(Liga liga, String path) throws SQLException {
        importAufsteller(liga);
        importSpielorte(liga);
    }

    private void importAufsteller(Liga liga) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT AUF_NR, AUF_NAME, AUF_ZUSATZ, AUF_STRASS, AUF_PLZ, AUF_ORT, AUF_TEL, AUF_FAX FROM \"LITAUF.DBF\"");
        try {
            AutomatenaufstellerService service =
                    ServiceFactory.get(AutomatenaufstellerService.class);
            List<Automatenaufsteller> alle = service.findAllAutomatenaufstellerByLiga(liga);
            while (rs.next()) {
                LITAUF litauf = readLITAUF(rs);
                if (isValid(litauf)) {
                    Automatenaufsteller aufsteller = findAufsteller(alle, litauf);
                    if (aufsteller == null) {
                        aufsteller = createAufsteller(litauf);
                        aufsteller.setLiga(liga);
                    } else {
                        updateAufsteller(aufsteller, litauf);
                    }
                    service.saveAutomatenaufsteller(aufsteller);
                }
            }
        } finally {
            rs.close();
            stmt.close();
        }
    }

    private void updateAufsteller(Automatenaufsteller aufsteller, LITAUF litauf) {
        aufsteller.setExterneId(litauf.AUF_NR);
        aufsteller.setAufstellerName(litauf.AUF_NAME);
        if (StringUtils.isNotEmpty(litauf.AUF_ZUSATZ)) {
            aufsteller.setKontaktName(litauf.AUF_ZUSATZ);
        }
        if (StringUtils.isNotEmpty(litauf.AUF_STRASS)) {
            aufsteller.setTelefon(litauf.AUF_STRASS);
        }
        if (StringUtils.isNotEmpty(litauf.AUF_PLZ)) {
            aufsteller.setPlz(litauf.AUF_PLZ);
        }
        if (StringUtils.isNotEmpty(litauf.AUF_ORT)) {
            aufsteller.setOrt(litauf.AUF_ORT);
        }
        if (StringUtils.isNotEmpty(litauf.AUF_TEL)) {
            aufsteller.setTelefon(litauf.AUF_TEL);
        }
        if (StringUtils.isNotEmpty(litauf.AUF_FAX)) {
            aufsteller.setFax(litauf.AUF_FAX);
        }
    }

    private boolean isValid(LITAUF litauf) {
        return !"Spielfrei".equalsIgnoreCase(litauf.AUF_NAME) &&
                StringUtils.isNotEmpty(litauf.AUF_NAME);
    }

    private Automatenaufsteller createAufsteller(LITAUF litauf) {
        Automatenaufsteller aufsteller = new Automatenaufsteller();
        updateAufsteller(aufsteller, litauf);
        return aufsteller;
    }

    private Automatenaufsteller findAufsteller(List<Automatenaufsteller> alleAufsteller,
                                               String auf_nr) {
        for (Automatenaufsteller each : alleAufsteller) {
            if (each.getExterneId() != null && each.getExterneId().equals(auf_nr)) {
                return each;
            }
        }
        return null;
    }

    private Automatenaufsteller findAufsteller(List<Automatenaufsteller> alle, LITAUF litauf) {
        Automatenaufsteller found = findAufsteller(alle, litauf.AUF_NR);
        if (found != null) return found;
        for (Automatenaufsteller each : alle) {
            if (each.getAufstellerName() != null && each.getPlz() != null
                    && each.getAufstellerName().equals(litauf.AUF_NAME)
                    && each.getPlz().equals(litauf.AUF_PLZ)) {
                return each;
            }
        }
        return null;  // not found
    }

    private void importSpielorte(Liga liga) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT LOK_NR, LOK_NAME, LOK_ZUSATZ, LOK_STRASS, LOK_PLZ, LOK_ORT, LOK_TEL, LOK_FAX, LOK_RUHETA, AUF_NR FROM \"LITLOK.DBF\"");
        try {
            SpielortService service = ServiceFactory.get(SpielortService.class);
            List<Spielort> alle = service.findAllSpielortByLiga(liga);
            AutomatenaufstellerService aufstellerService =
                    ServiceFactory.get(AutomatenaufstellerService.class);
            List<Automatenaufsteller> alleAufsteller =
                    aufstellerService.findAllAutomatenaufstellerByLiga(liga);
            while (rs.next()) {
                LITLOK litlok = readLITLOK(rs);
                if (isValid(litlok)) {
                    Automatenaufsteller aufsteller = findAufsteller(alleAufsteller, litlok.AUF_NR);
                    Spielort spielort = findSpielort(alle, litlok);
                    if (spielort == null) {
                        spielort = createSpielort(litlok);
                        spielort.setLiga(liga);
                    } else {
                        updateSpielort(spielort, litlok);
                    }
                    if (aufsteller != null) {
                        spielort.setAutomatenaufsteller(aufsteller);
                    }
                    service.saveSpielort(spielort);
                }
            }
        } finally {
            rs.close();
            stmt.close();
        }
    }

    private void updateSpielort(Spielort spielort, LITLOK litlok) {
        spielort.setExterneId(litlok.LOK_NR);
        spielort.setSpielortName(litlok.LOK_NAME);
        if (StringUtils.isNotEmpty(litlok.LOK_STRASS)) {
            spielort.setStrasse(litlok.LOK_STRASS);
        }
        if (StringUtils.isNotEmpty(litlok.LOK_PLZ)) {
            spielort.setPlz(litlok.LOK_PLZ);
        }
        if (StringUtils.isNotEmpty(litlok.LOK_ORT)) {
            spielort.setOrt(litlok.LOK_ORT);
        }
        if (StringUtils.isNotEmpty(litlok.LOK_TEL)) {
            spielort.setTelefon(litlok.LOK_TEL);
        }
        if (StringUtils.isNotEmpty(litlok.LOK_FAX)) {
            spielort.setFax(litlok.LOK_FAX);
        }
        spielort.setFreierTagName(litlok.LOK_RUHETA);
    }

    private Spielort createSpielort(LITLOK litlok) {
        Spielort spielort = new Spielort();
        updateSpielort(spielort, litlok);
        return spielort;
    }

    private Spielort findSpielort(List<Spielort> alle, LITLOK litlok) {
        for (Spielort each : alle) {
            if (each.getExterneId() != null &&
                    each.getExterneId().equals(litlok.LOK_NR)) {
                return each;
            }
        }
        for (Spielort each : alle) {
            if (each.getSpielortName() != null && each.getPlz() != null
                    && each.getSpielortName().equals(litlok.LOK_NAME)
                    && each.getPlz().equals(litlok.LOK_PLZ)) {
                return each;
            }
        }
        return null;  // not found
    }

    private boolean isValid(LITLOK litlok) {
        return !"Spielfrei".equalsIgnoreCase(litlok.LOK_NAME) &&
                StringUtils.isNotEmpty(litlok.LOK_NAME);
    }

    private LITAUF readLITAUF(ResultSet rs) throws SQLException {
        LITAUF obj = new LITAUF();
        obj.AUF_NR = rs.getString(1);
        obj.AUF_NAME = rs.getString(2);
        obj.AUF_ZUSATZ = rs.getString(3);
        obj.AUF_STRASS = rs.getString(4);
        obj.AUF_PLZ = rs.getString(5);
        obj.AUF_ORT = rs.getString(6);
        obj.AUF_TEL = rs.getString(7);
        obj.AUF_FAX = rs.getString(8);
        return obj;
    }

    private LITLOK readLITLOK(ResultSet rs) throws SQLException {
        LITLOK obj = new LITLOK();
        obj.LOK_NR = rs.getString(1);
        obj.LOK_NAME = rs.getString(2);
        obj.LOK_ZUSATZ = rs.getString(3);
        obj.LOK_STRASS = rs.getString(4);
        obj.LOK_PLZ = rs.getString(5);
        obj.LOK_ORT = rs.getString(6);
        obj.LOK_TEL = rs.getString(7);
        obj.LOK_FAX = rs.getString(8);
        obj.LOK_RUHETA = rs.getString(9);
        obj.AUF_NR = rs.getString(10);
        return obj;
    }
}