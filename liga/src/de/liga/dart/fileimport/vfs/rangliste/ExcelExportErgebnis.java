package de.liga.dart.fileimport.vfs.rangliste;

import de.liga.dart.fileimport.ExcelIO;
import de.liga.util.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.sql.SQLException;

import org.apache.poi.hssf.usermodel.*;

/**
 * Description:  Die berechneten Daten der Rangliste werden nach Excel exportiert <br/>
 * User: roman
 * Date: 03.05.2009, 15:59:24
 */
public class ExcelExportErgebnis extends ExcelIO {
    private final File outFile;
    private final List<VfsTeam> rangListe;
    private Map<String, HSSFCellStyle> styles = new HashMap();
    private static final String[] HEADERS = {
            "Klasse", "Liganame", "Platz", "Liga-Meister", "Teamname",
            "Rang", "Begegnungen", "Punkte", "Heim-Spiele", "Gast-Spiele", "Heim-Sätze", "Gast-Sätze"
    };
    private static final String[] COLUMNS = {  // property names to get the value from a VfsTeam
            "klasse", "liganame", "platz", "ligameister", "teamname",
            "rang", "begegnungen", "punkte", "spiele1", "spiele2", "saetze1", "saetze2"
    };

    private static final String STYLE_BOLD = "bold";


    public ExcelExportErgebnis(File outFile, List<VfsTeam> rangListe) {
        this.outFile = outFile;
        this.rangListe = rangListe;
    }

    protected void exchangeData() throws Exception {
        FileOutputStream fileOut = new FileOutputStream(outFile);
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            initStyles(wb);
            HSSFSheet sheet = wb.createSheet("Sheet1");
            writeHeaders(sheet);
            writeTeams(sheet);
            wb.write(fileOut);
        } finally {
            fileOut.close();
        }
    }

    private void writeTeams(HSSFSheet sheet) {
        int rowNum = 1;
        for (VfsTeam team : rangListe) {
            HSSFRow row = sheet.createRow(rowNum++);
            int cellNum = 0;
            for (String col : COLUMNS) {
                HSSFCell cell = row.createCell(cellNum++);
                Object val = Value.getDefault().getAttribute(team, col);
                if (val instanceof String) {
                    cell.setCellValue(new HSSFRichTextString((String) val));
                } else if (val instanceof Number) {
                    cell.setCellValue(((Number) val).doubleValue());
                } else if (val != null) {
                    cell.setCellValue(new HSSFRichTextString(String.valueOf(val)));
                }
            }
        }
    }

    private void initStyles(HSSFWorkbook wb) {
        // cache styles used to write text into cells
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        styles.put(STYLE_BOLD, style);
    }

    private void writeHeaders(HSSFSheet sheet) throws SQLException {
        HSSFRow headerRow = sheet.createRow(0);
        short i = 0;
        for (String each : HEADERS) {
            HSSFCell cell = headerRow.createCell(i++);
            HSSFRichTextString text = new HSSFRichTextString(each);
            cell.setCellValue(text);
            cell.setCellStyle(styles.get(STYLE_BOLD));
        }
    }
}
