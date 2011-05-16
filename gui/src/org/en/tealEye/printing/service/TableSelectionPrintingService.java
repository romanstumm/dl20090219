package org.en.tealEye.printing.service;

import org.apache.log4j.Logger;

import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.util.Locale;
import java.util.Vector;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 17.11.2007
 * Time: 13:14:26
 */
public class TableSelectionPrintingService extends JPanel implements TablePrinting {
    private static final Logger log = Logger.getLogger(TablePrintingService.class);
    private final JTable jTable;
    private final PrintingUtils printingUtils;
    private int mediaWidth;
    private int mediaHeight;

    private final int padX = 20;
    private final int padY = 20;

//    private int pageIndex = 0;
    private double resizeFactor;
    private String pageOrientation = "Hochformat";
    private final int firstIndex = 0;
    private final Vector<BufferedImage> im = new Vector<BufferedImage>();

    public TableSelectionPrintingService(JTable objects) {
        this.jTable = objects;
        this.printingUtils = new PrintingUtils();
        aquireValues();
        paintTable();

    }

    private void aquireValues() {
        mediaWidth = (int) printingUtils.getMediaWidth();
        mediaHeight = (int) printingUtils.getMediaHeight();
        resizeFactor = printingUtils.getColumnWidthFactors(jTable, mediaWidth);
    }

    private void paintTable(){

        for(int i = 0; i<=getPageCount();i++){
            BufferedImage paintCanvas = new BufferedImage(mediaWidth, mediaHeight, BufferedImage.TYPE_BYTE_GRAY);
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
        g2.fillRect(0, 0, mediaWidth, mediaHeight);
        Object[][] v = generatePrintableData(jTable);
        int index = v.length - ((printingUtils.getRowsPerPage(jTable.getRowHeight(), mediaHeight)) * pageNum);
        boolean trigger = false;
//        double columnFactor = printingUtils.getColumnWidthFactors(jTable, mediaWidth);
        int[] columnWidth = printingUtils.getColumnWidths(jTable);
        boolean touched = false;

        drawTableGrid(g2);
        g2.setColor(Color.BLACK);
        for (int i = firstIndex; i < printingUtils.getRowsPerPage(jTable.getRowHeight(), mediaHeight); i++) {
            if (trigger) break;
            int iteratedWidths = 0;
            int fieldIndex = ((i) + v.length - (index));
            Object[] rowStr = null;
   
            if (!(fieldIndex >= v.length))

                rowStr = v[fieldIndex];

            for (int j = 0; j < jTable.getColumnCount(); j++) {
                if (i <= index) {
                    if (i == 0) {
                        Font tFont = jTable.getFont();
                        g2.setFont(tFont);
                        String headerStr = jTable.getColumnName(j);
                        g2.drawString(headerStr, iteratedWidths + padX + 3, jTable.getRowHeight() * (i + 1) + padY - 3);
                    } else {
                        String str;
                        try {
                            str = (String) rowStr[j];
                        } catch (NullPointerException e) {
                            break;
                        }
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
                        if (fieldIndex == v.length) trigger = true;
                    }
                    iteratedWidths = (iteratedWidths + round(columnWidth[j] * resizeFactor));
                }
                touched = false;
            }
        }
    }

    private void drawTableGrid(Graphics2D g2) {
//        int rowsPP = printingUtils.getRowsPerPage(jTable.getRowHeight(), mediaHeight);
        int[] columnWidth = printingUtils.getColumnWidths(jTable);
//        int totalTableWidth = jTable.getColumnModel().getTotalColumnWidth();
        Object[][] v = generatePrintableData(jTable);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(padX, padY, mediaWidth - (padX * 2), jTable.getRowHeight());
        g2.setColor(Color.BLACK);
        double iteratedWidths;
        iteratedWidths = 0;
        g2.drawLine(padX, padY, mediaWidth - padX, padY);
        g2.drawLine(padX, padY, padX, v.length * jTable.getRowHeight() + padY);
        //g2.drawLine(mediaWidth-padX,0+padY,mediaWidth-padX,rowsToPaint*jTable.getRowHeight());
        for (int y = 0; y < v.length + 1; y++) {
            g2.drawLine(padX, jTable.getRowHeight() * y + padY, mediaWidth - padX, jTable.getRowHeight() * y + padY);
        }
        for (int cWidth : columnWidth) {
            iteratedWidths = (iteratedWidths + ((double) cWidth * resizeFactor));
            g2.drawLine(((int) iteratedWidths + padX), padY, ((int) iteratedWidths + padX), v.length * jTable.getRowHeight() + padY);
        }
        g2.drawLine(padX, (v.length * jTable.getRowHeight() + padY) * columnWidth.length, mediaWidth - padX, (v.length * jTable.getRowHeight() + padY) * columnWidth.length);
    }

    private int round(double d) {
        int i = (int) d;
        double rest = d - i;
        if (rest != 0) {
            return i + 1;
        } else return i;
    }

    private Object[][] generatePrintableData(JTable jTable) {
        int[] rowC = jTable.getSelectedRows();
        int cCount = jTable.getColumnModel().getColumnCount();
        Object[][] v = new Object[rowC.length + 1][];
        Object[] row;
        for (int i = 1; i < rowC.length + 1; i++) {
            row = new String[cCount];

            for (int j = 0; j < cCount; j++) {
                row[j] = jTable.getValueAt(rowC[i - 1], j);
            }
            try {
                v[i] = row;
            } catch (ArrayIndexOutOfBoundsException e) {
                break;
            }
        }
        return v;
    }

    @SuppressWarnings({"SuspiciousNameCombination"})
    public void rotate() {
        int mediaHeightT = mediaHeight;
        int mediaWidthT = mediaWidth;
        mediaHeight = 0;
        mediaWidth = 0;
        mediaHeight = mediaWidthT;
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

        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        if (pageOrientation.equals("Hochformat")) {
            aset.add(OrientationRequested.PORTRAIT);
        } else if (!pageOrientation.equals("Hochformat")) {
            aset.add(OrientationRequested.LANDSCAPE);
        }
        aset.add(new JobName("Tabelle", Locale.GERMANY));
        MediaPrintableArea pa;
        pa = new MediaPrintableArea(0, 0, mediaWidth, mediaHeight, MediaPrintableArea.MM);
        aset.add(pa);
        PrinterJob job = printingUtils.getPrinterJob();
        Paper paper = new Paper();
        paper.setImageableArea(0, 0, mediaWidth, mediaHeight);
        job.setPrintable(this);
        boolean ok = job.printDialog(aset);
        Attribute[] a = aset.toArray();
        for (Attribute anA : a) log.debug(anA.getName());
        for (int i = 0; i < job.getPageFormat(aset).getMatrix().length; i++)
            log.debug(job.getPageFormat(aset).getMatrix()[i]);
         if (ok) {

            try {
                job.print(aset);
            } catch (PrinterException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }

    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
        int noPages = getPageCount();
        if (pageIndex < noPages) {
//            this.pageIndex = pageIndex;
            drawTable((Graphics2D) g, pageIndex);
            return Printable.PAGE_EXISTS;
        } else return Printable.NO_SUCH_PAGE;
    }

    public int getPageCount() {
        return round((double) generatePrintableData(jTable).length / printingUtils.getRowsPerPage(jTable.getRowHeight(), mediaHeight) - 1);
    }

    public void repaintCanvas() {
        // do nothing
    }

    public void setLeftBorder(int leftBorder) {
        
    }

    public void setUpperBorder(int upperBorder) {
        // do nothing
    }

    public void setPrintPageNum(boolean b) {
        // do nothing
    }

    public void paintCanvas() {
        // do nothing
    }

    public void kill() {
            System.gc();
     }
}
