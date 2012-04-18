package de.liga.dart.fileimport;

import org.apache.poi.hssf.usermodel.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.Map;
import java.util.HashMap;

import de.liga.dart.common.service.util.HibernateUtil;

/**
 * Description: write all data of TABLES into excel file<br/>
 * User: roman
 * Date: 22.02.2009, 11:57:52
 */
public class ExcelExporter extends ExcelIO {
    private final File outFile;
    private Connection jdbcConnection;
    private Map<String, HSSFCellStyle> styles = new HashMap();
    private static final String STYLE_BOLD = "bold";
    private static final String STYLE_ITALIC = "italic";

    public ExcelExporter(File file) throws FileNotFoundException {
        outFile = file;
    }

    protected void exchangeData() throws Exception {
        jdbcConnection = HibernateUtil.getCurrentSession().connection();
        if (progressIndicator != null) {
            progressIndicator.showProgress(10, outFile.getName() + " berechnen...");
        }
        FileOutputStream fileOut = new FileOutputStream(outFile);
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            initStyles(wb);

            int step = 80 / TABLES.length;
            int percent = 10;
            for (String table : TABLES) {
                if (progressIndicator != null) {
                    percent += step;
                    progressIndicator
                            .showProgress(percent, outFile.getName() + ", " + table + "...");
                }
                HSSFSheet sheet = wb.createSheet(table);
                writeSheet(sheet, table);
            }
            if (progressIndicator != null) {
                progressIndicator.showProgress(90, SHEET_SEQUENCES + " speichern...");
            }
            writeSequences(wb.createSheet(SHEET_SEQUENCES));
            if (progressIndicator != null) {
                progressIndicator.showProgress(95, outFile.getName() + " speichern...");
            }
            wb.write(fileOut);
        } finally {
            fileOut.close();
            if (progressIndicator != null) {
                progressIndicator.showProgress(0, "");
            }
        }
    }

    private void writeSequences(HSSFSheet sheet) throws SQLException {
        Statement stmt = jdbcConnection.createStatement();

        // header
        HSSFRow headerRow = sheet.createRow(0);
        HSSFCell cell;
        (cell = headerRow.createCell(0)).setCellValue(new HSSFRichTextString("SEQUENCE"));
        cell.setCellStyle(styles.get(STYLE_BOLD));
        (cell = headerRow.createCell(1)).setCellValue(new HSSFRichTextString("NEXTVAL"));
        cell.setCellStyle(styles.get(STYLE_BOLD));
        headerRow = sheet.createRow(1);
        (cell = headerRow.createCell(0)).setCellValue(new HSSFRichTextString("varchar"));
        cell.setCellStyle(styles.get(STYLE_ITALIC));
        (cell = headerRow.createCell(1)).setCellValue(new HSSFRichTextString("bigint"));
        cell.setCellStyle(styles.get(STYLE_ITALIC));

        // data
        int rowIdx = 2;
        try {
            for (String seq : SEQUENCES) {
                String value = "";
                ResultSet resultSet = stmt.executeQuery("select nextval('" + seq + "')");
                try {
                    if (resultSet.next()) {
                        value = resultSet.getString(1);
                    }
                } finally {
                    resultSet.close();
                }
                HSSFRow row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(new HSSFRichTextString(seq));
                row.createCell(1).setCellValue(new HSSFRichTextString(value));
            }
        } finally {
            stmt.close();
        }
    }

    private void initStyles(HSSFWorkbook wb) {
        // cache styles used to write text into cells
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        styles.put(STYLE_BOLD, style);

        style = wb.createCellStyle();
        font = wb.createFont();
        font.setItalic(true);
        style.setFont(font);
        styles.put(STYLE_ITALIC, style);
    }

    private void writeSheet(HSSFSheet sheet, String table) throws SQLException {
        Statement stmt = jdbcConnection.createStatement();
        try {
            ResultSet resultSet = stmt.executeQuery("select * from " + table);
            int columnCount = writeColumnHeaders(sheet, resultSet);
            try {
                short rowNum = 2;
                while (resultSet.next()) {
                    HSSFRow row = sheet.createRow(rowNum++);
                    for (int i = 0; i < columnCount; i++) {
                        HSSFCell cell = row.createCell(i);
                        cell.setCellValue(new HSSFRichTextString(resultSet.getString(i + 1)));
                    }
                }
            } finally {
                resultSet.close();
            }
        } finally {
            stmt.close();
        }
    }

    private int writeColumnHeaders(HSSFSheet sheet, ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        HSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            HSSFCell cell = headerRow.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(metaData.getColumnName(i + 1));
            cell.setCellValue(text);
            cell.setCellStyle(styles.get(STYLE_BOLD));
        }
        headerRow = sheet.createRow(1);
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            HSSFCell cell = headerRow.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(metaData.getColumnTypeName(i + 1));
            cell.setCellValue(text);
            cell.setCellStyle(styles.get(STYLE_ITALIC));
        }
        return metaData.getColumnCount();
    }
}
