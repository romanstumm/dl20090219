package org.en.tealEye.printing.service;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.util.Vector;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 10.04.2008
 * Time: 16:04:49
 */
public class TablePrintSvc extends JPanel implements TablePrinting{

    final PrintingUtils utils = new PrintingUtils();

    final double paperWidth;
    final double paperHeight;

    int vPaperWidth;
    int vPaperHeight;

    final double leftBorder = 20;
    final int upperBorder = 20;

    int rowsPerPage;
    int rowCount;
    int columnCount;
    private final int rowHeight;
    private double columnFactor;

    int pageIndex = 0;
    boolean drawPageNum = false;
    final boolean selection = false;

    final JTable sourceTable;
    private int[] selectedTableRows;
    private final Vector<BufferedImage> imageVector = new Vector<BufferedImage>();
    private int pagesNeeded;
    private int[] columnWidths;


    public TablePrintSvc(JTable sourceTable){
        this.sourceTable = sourceTable;
        this.paperWidth = utils.getMediaWidth();
        this.paperHeight= utils.getMediaHeight();
        this.rowHeight = sourceTable.getRowHeight();

    }

    public void setTableValues() {
        this.rowsPerPage = utils.getRowsPerPage(rowHeight, (paperHeight-(upperBorder*2)));
        this.columnFactor = utils.getColumnWidthFactors(sourceTable, (paperWidth-(leftBorder*2)));
        this.columnCount = sourceTable.getColumnCount();
        
        if(selection){
            selectedTableRows = sourceTable.getSelectedRows();
        }else{
            selectedTableRows = new int[sourceTable.getRowCount()];
                for(int i = 0; i<sourceTable.getRowCount(); i++)
                    selectedTableRows[i] = i;
        }
        this.pagesNeeded = utils.getPagesPerTable(selectedTableRows.length, rowsPerPage);
        this.columnWidths= utils.getColumnWidths(sourceTable);
    }

    public void drawGrid(Graphics2D g2, int pageIndex){
//        String[] cNames = new String[columnCount];
        /*for(int i = 0;i<columnCount;i++){
            cNames[i] = sourceTable.getColumnName(i);
        }*/
        g2.setColor(Color.WHITE);
        g2.fillRect(0,0,(int)paperWidth,(int)paperHeight);
        g2.setColor(Color.black);
            for(int i = 0; i<columnCount;i++){
                g2.drawLine((int) ((columnWidths[i]*columnFactor)*i+leftBorder),upperBorder, (int) ((columnWidths[i]*columnFactor)*i+leftBorder),rowHeight*utils.getRowsThisPage(
                        rowsPerPage, selectedTableRows.length,pageIndex)+rowHeight);
            }

            for(int i = 0; i<utils.getRowsThisPage(rowsPerPage,selectedTableRows.length,pageIndex);i++){
                g2.drawLine((int)leftBorder,rowHeight*i+upperBorder, (int) ((int)paperWidth-leftBorder),rowHeight*i+upperBorder);
//                System.out.println(utils.getRowsThisPage(rowsPerPage,selectedTableRows.length,pageIndex));
            }
    }

    public BufferedImage getPaintingCanvas(int pageNum) {
        if(imageVector.size()<1)
            paintCanvas();
        return imageVector.get(pageNum);
    }

    public void paintCanvas(){
        BufferedImage im;
        for(int i = 0; i<pagesNeeded;i++){
            im = new BufferedImage((int)paperWidth,(int)paperHeight, BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D g2 = im.createGraphics();
            drawGrid(g2,i);
            imageVector.addElement(im);
        }
    }

    public void kill() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void rotate() {
        // do nothing
    }

    public void generateDoc() {
        // do nothing
    }

    public int getPageCount() {
        return 0;  // do nothing
    }

    public void repaintCanvas() {
        // do nothing
    }

    public void setLeftBorder(int leftBorder) {
        // do nothing
    }

    public void setUpperBorder(int upperBorder) {
        // do nothing
    }

    public void setPrintPageNum(boolean b) {
        // do nothing
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        return 0;  // do nothing
    }
}
