package org.en.tealEye.printing.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.*;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 19.11.2007
 * Time: 14:46:02
 */
public class PrintingUtils {
    private static final Log log = LogFactory.getLog(PrintingUtils.class);

    private double mediaWidth;
    private double mediaHeight;
    private PrinterJob job;
    public PrintingUtils() {
        getMediaSize();
    }

    public void getMediaSize() {
        double[] mediaSize = new double[2];
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        aset.add(MediaSizeName.ISO_A4);
        aset.add(OrientationRequested.PORTRAIT);
        job = PrinterJob.getPrinterJob();
        PageFormat pf = job.getPageFormat(aset);
        mediaSize[0] = Math.max(1000, pf.getImageableWidth());
        mediaSize[1] = Math.max(1000, pf.getImageableHeight());
        log.debug(pf.getImageableHeight() + pf.getImageableWidth());
        mediaWidth = mediaSize[0];
        mediaHeight = mediaSize[1];
    }



    public PrinterJob getPrinterJob(){
        return job;
    }

    public int getRowsPerPage(int rowHeight, double mediaHeight) {
        return (int) ((mediaHeight - 40) / rowHeight);
    }

    public double getMediaWidth() {
        return mediaWidth;
    }

    public double getMediaHeight() {
        return mediaHeight;
    }

    public int[] getColumnWidths(JTable jTable) {
        int cCount = jTable.getColumnModel().getColumnCount();
        jTable.getColumnModel().getTotalColumnWidth();
        int[] cWidths = new int[cCount];
        for (int i = 0; i < cCount; i++) {
            cWidths[i] = jTable.getColumnModel().getColumn(i).getWidth();
        }

        return cWidths;
    }

    public double getColumnWidthFactors(JTable jTable, double mediaWidth) {
//        double totalWidth = jTable.getColumnModel().getTotalColumnWidth();
        log.debug((mediaWidth) / jTable.getWidth());
        log.debug(mediaWidth);
        log.debug(jTable.getWidth());
        return (mediaWidth-40) / jTable.getWidth();
    }

    public int[] getGroupColumnWidths() {
        int[] columns = new int[7];
        columns[0] = 40;
        columns[1] = 20;
        columns[2] = 155;
        columns[3] = 155;
        columns[4] = 25;
        columns[5] = 70;
        columns[6] = 35;
        return columns;
    }

    public int getGroupColumnWidths(int index) {
        int[] columns = new int[7];
        columns[0] = 40;
        columns[1] = 20;
        columns[2] = 155;
        columns[3] = 155;
        columns[4] = 25;
        columns[5] = 70;
        columns[6] = 35;
        return columns[index];
    }

    public double getGroupColumnWidthFactors(int mediaWidth) {
        return (double) (mediaWidth - 40) / 500;
    }
/*
    public String chopTheString(String sourceString, Graphics2D g2, int slotWidth) {
        char[] strArr = sourceString.toCharArray();
        return null;
    }*/

  /*  public double getLabelFactor(int width, int height){
        double widthFactor = 210 / width;
        double heightFactor = 297 / height;
        return widthFactor;
    }*/

    public int getHLabelCount(int width ){
        return 210 / width;
    }

    public int getVLabelCount(int height){
        return 297 / height;
    }

    public double getEtiScaleFactorX(){
        return getMediaWidth()/210;
    }

    public double getEtiScaleFactorY(){
        return getMediaHeight() /((double)(297));
    }

    public int getLabelsPerPage(int width, int height){
        log.debug(getVLabelCount(height)*getHLabelCount(width));
        return getVLabelCount(height)*getHLabelCount(width);
    }

    public int getNeededPages(int width, int height, int labelCount){
        log.debug(labelCount/getLabelsPerPage(width,height));
        return round((double)labelCount/getLabelsPerPage(width,height));
    }

    public int getEntityCount(int labelCount, int height, int width){
        log.debug(labelCount);
        log.debug(getNeededPages(width,height,labelCount));
        log.debug(labelCount);

        return getNeededPages(width,height,labelCount)*getLabelsPerPage(width,height);
    }

    public int round(double d) {
        int i = (int) d;
        double rest = d -((double)i);
        log.debug(d);
        if(rest != 0){
            i = i+1;
        }
        return i;
    }

    public int getLabelPageCount(int vectorLength, int labelsPerPage) {
        return round((double) vectorLength / labelsPerPage);
    }

    public int getPagesPerLabel(int labelsPerPage, int labelsWanted){
        double pPL = (double)labelsWanted/(double)labelsPerPage;
        return round(pPL);
    }

    public int getRowsThisPage(int rowsPerPage, int tableRows, int pageIndex){
        return tableRows-(rowsPerPage*pageIndex+1);
    }

    public int getPagesPerTable(int tableRows, int rowsPerPage){
        return round((double)tableRows/(double)rowsPerPage);
    }

}
