package de.liga.dart.fileimport;

import de.liga.dart.Application;
import de.liga.dart.common.service.util.HibernateUtil;
import de.liga.dart.exception.DartException;
import de.liga.util.CalendarUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.*;

/**
 * Description:   <br/>
 * User: roman
 * Date: 22.02.2009, 13:43:02
 */
public class ExcelImporter extends ExcelIO {
    private final File inFile;
    private Connection jdbcConnection;
    private final Map<String, List<Map<String, String>>> excelData;
    private final Map<String, String[]> sheetHeaders;
    private final Map<String, String[]> sheetTypes;

    public ExcelImporter(File file) {
        inFile = file;
        excelData = new HashMap();
        sheetHeaders = new HashMap();
        sheetTypes = new HashMap();
    }

    protected void exchangeData() throws Exception {
        jdbcConnection = HibernateUtil.getCurrentSession().connection();
        if (progressIndicator != null) {
            progressIndicator.showProgress(5, inFile.getName() + " laden...");
        }
        FileInputStream fileIn = new FileInputStream(inFile);
        try {
            POIFSFileSystem fileSystem = new POIFSFileSystem(fileIn);
            HSSFWorkbook wb = new HSSFWorkbook(fileSystem);
            checkDbVersion(wb);
            int step = 40 / TABLES.length;
            int percent = 10;
            for (String table : TABLES) {
                if (progressIndicator != null) {
                    percent += step;
                    progressIndicator
                            .showProgress(percent, inFile.getName() + ", " + table + "...");
                }
                HSSFSheet sheet = wb.getSheet(table);
                if (sheet != null) {
                    readSheet(sheet, table);
                } else {
                    throw new DartException("Die Tabelle " + table + " fehlt in der Excel Datei!");
                }
            }
            HSSFSheet seqSheet = wb.getSheet(SHEET_SEQUENCES);
            if (seqSheet != null) {
                readSheet(seqSheet, SHEET_SEQUENCES);
            } else {
                throw new DartException(
                        "Die Tabelle " + SHEET_SEQUENCES + " fehlt in der Excel Datei!");
            }
            deleteAllData();
            writeAllData();
            writeSequences();
        } finally {
            fileIn.close();
            if (progressIndicator != null) {
                progressIndicator.showProgress(0, "");
            }
        }
    }

    private void writeSequences() throws SQLException {
        List<Map<String, String>> sheetData = excelData.get(SHEET_SEQUENCES);
//        String[] headers = sheetHeaders.get(SHEET_SEQUENCES);
//        String[] types = sheetTypes.get(SHEET_SEQUENCES);

        PreparedStatement stmt = jdbcConnection.prepareStatement("select setval(?, ?, false)");
        try {
            for (Map<String, String> dataMap : sheetData) {
                String seqName = dataMap.get("SEQUENCE");
                String seqVal = dataMap.get("NEXTVAL");
                stmt.setString(1, seqName);
                stmt.setLong(2, Long.parseLong(seqVal));
                stmt.execute();
            }
        } finally {
            stmt.close();
        }
    }

    private void writeAllData() throws SQLException {
        int step = 40 / TABLES.length;
        int percent = 50;
        for (String table : TABLES) {
            if (progressIndicator != null) {
                percent += step;
                progressIndicator
                        .showProgress(percent, table + " speichern...");
            }
            List<Map<String, String>> sheetData = excelData.get(table);
            String[] headers = sheetHeaders.get(table);
            String[] types = sheetTypes.get(table);
            String sql = generateInsert(table, headers);
            PreparedStatement stmt = jdbcConnection.prepareStatement(sql);
            try {
                for (Map<String, String> dataMap : sheetData) {
                    int i = 1;
                    for (String column : headers) {
                        String colValue = dataMap.get(column);
                        setParameter(stmt, i, colValue, types[i - 1]);
                        i++;
                    }
                    stmt.execute();
                }
            } finally {
                stmt.close();
            }
        }
    }

    private void setParameter(PreparedStatement stmt, int i, String colValue, String type)
            throws SQLException {
        if ("varchar".equals(type)) {
            stmt.setString(i, colValue);
        } else if ("timestamp".equals(type)) { // e.g. 2009-02-22 14:12:04.468
            if (colValue == null || colValue.length() == 0) {
                stmt.setNull(i, Types.TIMESTAMP);
            } else {
                Timestamp ts = CalendarUtils.dbstringToTimestamp(colValue);
                stmt.setTimestamp(i, ts);
            }
        } else if ("int4".equals(type) || "int2".equals(type)) {
            if (colValue == null || colValue.length() == 0) {
                stmt.setNull(i, Types.INTEGER);
            } else {
                stmt.setInt(i, Integer.parseInt(colValue));
            }
        } else if ("bigint".equals(type)) {
            if (colValue == null || colValue.length() == 0) {
                stmt.setNull(i, Types.BIGINT);
            } else {
                stmt.setLong(i, Long.parseLong(colValue));
            }
        } else if ("time".equals(type)) {   // 19:00:00
            if (colValue == null || colValue.length() == 0) {
                stmt.setNull(i, Types.TIME);
            } else {
                Time t = CalendarUtils.dbstringToTime(colValue);
                stmt.setTime(i, t);
            }
        } else if ("bool".equals(type)) {
            stmt.setBoolean(i, !colValue.equals("f"));
        } else { // unknown type, use string (sollte nie vorkommen!)
            stmt.setString(i, colValue);
        }
    }

    private String generateInsert(String table, String[] columns) {
        StringBuilder buf = new StringBuilder();
        buf.append("INSERT INTO ");
        buf.append(table);
        buf.append(" (");
        for (int i = 0; i < columns.length - 1; i++) {
            buf.append(columns[i]);
            buf.append(",");
        }
        buf.append(columns[columns.length - 1]);
        buf.append(" ) VALUES (");
        for (int i = 0; i < columns.length - 1; i++) {
            buf.append("?,");
        }
        buf.append("?)");
        return buf.toString();
    }

    private void deleteAllData() throws SQLException {
        if (progressIndicator != null) {
            progressIndicator
                    .showProgress(50, "Daten in Datenbank löschen...");
        }
        // 2nd level caches löschen (weil wir gleich direkt mit JDBC schreiben!)
        HibernateUtil.evictSecondLevelCaches();
        Statement stmt = jdbcConnection.createStatement();
        try {
            for (int i = TABLES.length - 1; i >= 0; i--) {
                String table = TABLES[i];
                stmt.execute("DELETE FROM " + table);
            }
        } finally {
            stmt.close();
        }
    }

    private void checkDbVersion(HSSFWorkbook wb) {
        HSSFSheet sheet = wb.getSheet("db_version");
        if (sheet == null) throw new DartException("Datei enthält keine Ligadatenbank!");
        Iterator<HSSFRow> rowIter = sheet.rowIterator();
        String[] headers = null;
        if (rowIter.hasNext()) {
            HSSFRow headerRow = rowIter.next();
            headers = readColumnHeaders(headerRow);
            if (rowIter.hasNext()) rowIter.next(); // skip type row
        }
        Map<String, String> dataMap = null;
        if (headers != null && rowIter.hasNext()) {
            HSSFRow dataRow = rowIter.next();
            dataMap = readRowData(headers, dataRow);
        }
        if (dataMap != null) {
            String version = dataMap.get("version");
            if (version != null) {
                // kompatible Versionen hier prüfen
                if (!version.equals(Application.APPLICATION_VERSION) &&
                    !version.startsWith("2.2.") && !version.startsWith("2.3.")) {
                    throw new DartException(
                            "Ungültige Version! In der Excel Datei steht " + version +
                                    " - erwartet wird jedoch " + Application.APPLICATION_VERSION);
                } else {
                    return;
                }
            }
        }
        throw new DartException(
                "Kann erforderliche Versionsinformation in der Excel Datei nicht finden!");
    }

    private Map<String, String> readRowData(String[] headers, HSSFRow dataRow) {
        Map<String, String> dataMap = new HashMap();
        int i = 0;
        for (String header : headers) {
            HSSFCell cell = dataRow.getCell(i++);
            if (cell != null) {
                dataMap.put(header, cell.getRichStringCellValue().getString());
            }
        }
        return dataMap;
    }

    private String[] readColumnHeaders(HSSFRow headerRow) {
        List<String> headers = new LinkedList();
        Iterator<HSSFCell> cellIter = headerRow.cellIterator();
        while (cellIter.hasNext()) {
            HSSFCell cell = cellIter.next();
            headers.add(cell.getRichStringCellValue().getString());
        }
        return headers.toArray(new String[headers.size()]);
    }

    private String[] readColumnTypes(HSSFRow typesRow) {
        List<String> typeNames = new LinkedList();
        Iterator<HSSFCell> cellIter = typesRow.cellIterator();
        while (cellIter.hasNext()) {
            HSSFCell cell = cellIter.next();
            typeNames.add(cell.getRichStringCellValue().getString());
        }
        return typeNames.toArray(new String[typeNames.size()]);
    }

    private void readSheet(HSSFSheet sheet, String table) {
        Iterator<HSSFRow> rowIter = sheet.rowIterator();
        String[] headers = null;
        String[] types = null;
        if (rowIter.hasNext()) {
            HSSFRow headerRow = rowIter.next();
            headers = readColumnHeaders(headerRow);
        }
        sheetHeaders.put(table, headers);
        if (rowIter.hasNext()) {
            HSSFRow typeRow = rowIter.next();
            types = readColumnTypes(typeRow);
        }
        sheetTypes.put(table, types);
        List<Map<String, String>> sheetData = new LinkedList();
        excelData.put(table, sheetData);
        if (headers != null) {
            while (rowIter.hasNext()) {
                HSSFRow dataRow = rowIter.next();
                Map<String, String> dataMap = readRowData(headers, dataRow);
                sheetData.add(dataMap);
            }
        }
    }

}
