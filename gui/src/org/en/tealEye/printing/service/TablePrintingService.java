package org.en.tealEye.printing.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Vector;
import java.util.Date;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 17.11.2007
 * Time: 12:39:38
 */
public class TablePrintingService extends JPanel implements TablePrinting {
    private static final Log log = LogFactory.getLog(TablePrintingService.class);

    private final JTable jTable;
    private final PrintingUtils printingUtils;
    private double mediaWidth;
    private double mediaHeight;

    private final int padX = 20;
    private final int padY = 20;

//    private int pageIndex = 0;
    private double resizeFactor;
    private String pageOrientation = "Hochformat";
    private final Vector<BufferedImage> im = new Vector<BufferedImage>();
    private final Vector<Object[]> tableStrings = new Vector<Object[]>();
//    private int leftBorder;
//    private int upperBorder;
//    private boolean printPageNum;

    public TablePrintingService(JTable objects) {
        this.jTable = objects;
        this.printingUtils = new PrintingUtils();
        aquireValues();
        this.setBackground(Color.WHITE);
        generatePrintableData(objects);
        paintTable();
    }

    private void aquireValues() {
        mediaWidth =  printingUtils.getMediaWidth()-40;
        mediaHeight = printingUtils.getMediaHeight()-40;
        resizeFactor = printingUtils.getColumnWidthFactors(jTable, mediaWidth);
    }

    private void paintTable(){
        for(int i = 0; i<=getPageCount();i++){
            BufferedImage paintCanvas = new BufferedImage((int)mediaWidth, (int)mediaHeight, BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D g2 = paintCanvas.createGraphics();
            drawTable(g2, i);
            im.addElement(paintCanvas);
        }
    }

    public BufferedImage getPaintingCanvas(int pageNum) {
//        pageIndex = pageNum;
        return im.get(pageNum);
    }

    private void drawTable(Graphics2D g2, int pageNum) {
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, (int)mediaWidth, (int)mediaHeight);
        int index = tableStrings.size() - ((printingUtils.getRowsPerPage(jTable.getRowHeight(), mediaHeight)) * pageNum);
        boolean trigger = false;
        boolean touched = false;
//        double columnFactor = printingUtils.getColumnWidthFactors(jTable, mediaWidth);
        int[] columnWidth = printingUtils.getColumnWidths(jTable);


        drawTableGrid(g2);
        g2.setColor(Color.BLACK);
        for (int i = 0; i < printingUtils.getRowsPerPage(jTable.getRowHeight(), mediaHeight); i++) {
            if (trigger) break;
            int iteratedWidths = 0;
            Object[] rowStr = null;
            int fieldIndex = ((i) + tableStrings.size() - (index));
            if (!(fieldIndex>=tableStrings.size())) {
                rowStr = tableStrings.get(fieldIndex);
            }
            for (int j = 0; j < jTable.getColumnCount(); j++) {
                if (i <= index) {
                    if (i == 0) {
                        Font tFont = jTable.getFont();
                        g2.setFont(tFont);
                        String headerStr = jTable.getColumnName(j);
                        g2.drawString(headerStr, iteratedWidths + padX + 3, jTable.getRowHeight() * (i + 1) + padY - 3);
                    } else {
                        String str = rowStr != null ? (String) rowStr[j] : null;
                        if (!(str == null)) {
                            while (g2.getFontMetrics().stringWidth(str) > jTable.getColumnModel().getColumn(j).getWidth() * resizeFactor - 2) {
                                CharSequence strTemp = str.subSequence(0, str.length() - 1);
                                str = strTemp.toString();
                                log.debug(str);
                                touched = true;
                            }
                            String tStr;
                            if (touched) {
                                tStr = str.substring(0, str.length() - 3);
                                str = tStr + "...";
                            }
                            g2.drawString(str, iteratedWidths + padX + 3, jTable.getRowHeight() * (i + 1) + padY - 3);

                        }
                        if (fieldIndex == tableStrings.size() - 1) trigger = true;
                    }
                    iteratedWidths = (iteratedWidths + round(columnWidth[j] * resizeFactor));
                }
                touched = false;
            }
        }
    }

    private void drawTableGrid(Graphics2D g2) {
        int rowsPP = printingUtils.getRowsPerPage(jTable.getRowHeight(), mediaHeight);
        int[] columnWidth = printingUtils.getColumnWidths(jTable);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(padX, padY,(int) mediaWidth - (padX * 2), jTable.getRowHeight());
        g2.setColor(Color.BLACK);
        double iteratedWidths = 0;
        if (tableStrings.size() > rowsPP) {
            g2.drawLine(padX, padY,(int) mediaWidth - padX, padY);
            g2.drawLine(padX, padY, padX, rowsPP * jTable.getRowHeight() + padY);
            //g2.drawLine(mediaWidth-padX,0+padY,mediaWidth-padX,rowsToPaint*jTable.getRowHeight());
            for (int y = 0; y < rowsPP + 1; y++) {
                g2.drawLine(padX, jTable.getRowHeight() * y + padY, (int)mediaWidth - padX, jTable.getRowHeight() * y + padY);
            }
            for (int cWidth : columnWidth) {
//                System.out.println(resizeFactor);
//                System.out.println(cWidth);
                iteratedWidths = iteratedWidths + (cWidth * resizeFactor);
                g2.drawLine((int)( iteratedWidths + padX), padY, (int)( iteratedWidths + padX), rowsPP * jTable.getRowHeight() + padY);
            }
            g2.drawLine(padX, (rowsPP * jTable.getRowHeight() + padY) * columnWidth.length, (int)mediaWidth - padX, (rowsPP * jTable.getRowHeight() + padY) * columnWidth.length);
        } else {
            g2.drawLine(padX, padY,(int) mediaWidth - padX, padY);
            g2.drawLine(padX, padY, padX, tableStrings.size() * jTable.getRowHeight() + padY);
            for (int y = 0; y < tableStrings.size() + 1; y++) {
                g2.drawLine(padX, jTable.getRowHeight() * y + padY, (int)mediaWidth - padX, jTable.getRowHeight() * y + padY);
            }
            for (int cWidth : columnWidth) {
                iteratedWidths = iteratedWidths + (double) cWidth * resizeFactor;
                g2.drawLine(((int) iteratedWidths + padX), padY, ((int) iteratedWidths + padX), tableStrings.size() * jTable.getRowHeight() + padY);
            }
            g2.drawLine(padX, (tableStrings.size() * jTable.getRowHeight() + padY) * columnWidth.length,(int) mediaWidth - padX, (tableStrings.size() * jTable.getRowHeight() + padY) * columnWidth.length);
        }
    }

   /* private void drawTableHeader(Graphics2D g2, int page) {

    }

    private void drawTableStrings(Graphics2D g2, int j, int widthIteration, String str) {
        g2.setColor(Color.BLACK);
        if (str == null) {
        } else
            g2.drawString(str, widthIteration, j);
    }

    private void drawTablePageNum(Graphics2D g2, boolean pNum, int mode) {

    }*/

    private int round(double d) {
        int i = (int) d;
        double rest = d - i;
        if (rest != 0) {
            return i + 1;
        } else return i;
    }

    private void generatePrintableData(JTable jTable) {
        int rowC = jTable.getRowCount();
        int cCount = jTable.getColumnModel().getColumnCount();
//        Object[][] v = new String[rowC + 1][];
        Object[] row;
        for (int i = 1; i < rowC + 1; i++) {
            row = new String[cCount];
            for (int j = 0; j < cCount; j++) {
                row[j] = jTable.getValueAt(i - 1, j);
            }
            tableStrings.addElement(row);
        }

    }

    @SuppressWarnings({"SuspiciousNameCombination"})
    public void rotate() {
        int mediaHeightT = (int)mediaHeight;
        mediaHeight = mediaWidth;
        mediaWidth = mediaHeightT;
        resizeFactor = printingUtils.getColumnWidthFactors(jTable, mediaWidth);
        if (pageOrientation.equals("Hochformat"))
            pageOrientation = "Querformat";
        else
            pageOrientation = "Hochformat";
    }

    public void setTableValues() {
        // do nothing
    }

    public void generateDoc() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        job.setJobName("print-liga-table-" + new Date());
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException ex) {
                log.error("error while printing", ex);
            }
        }
    }

    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {

        int noPages = getPageCount();
        if (pageIndex < noPages) {
//            this.pageIndex = pageIndex;
            Graphics2D g2 = (Graphics2D) g;
            drawTable(g2,pageIndex);
            return Printable.PAGE_EXISTS;
        } else return Printable.NO_SUCH_PAGE;
    }

    public int getPageCount() {
        return round((double) tableStrings.size() / printingUtils.getRowsPerPage(jTable.getRowHeight(), mediaHeight));
    }

    public void repaintCanvas() {
        im.removeAllElements();
        paintTable();
    }

    public void setLeftBorder(int leftBorder) {
        //this.padX = leftBorder;
    }

    public void setUpperBorder(int upperBorder) {
        //this.padY = upperBorder;
    }

    public void setPrintPageNum(boolean b) {
//        this.printPageNum = true;
    }

    public void paintCanvas() {
        // do nothing
    }
}
