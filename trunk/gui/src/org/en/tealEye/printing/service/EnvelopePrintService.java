package org.en.tealEye.printing.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 14.09.2009
 * Time: 01:24:41
 * To change this template use File | Settings | File Templates.
 */
public class EnvelopePrintService {
     protected static final Log log = LogFactory.getLog(EnvelopePrintService.class);

    private Object parentObject;
    private Vector<Vector<String[]>> labelStrings = new Vector<Vector<String[]>>();
    private Vector<String[]> groupString = new Vector<String[]>();

    private int pages;
    private boolean withGraphic;
    private boolean withSender;
    private boolean senderPosition;
    private boolean orderByTable;
    private int format;
    private int orientation;
    private Font addressFont;
    private Font senderFont;
    private Font additionalFont;



    private int xAxisAddress = 0;
    private int yAxisAddress = 0;

    private int xAxisSender = 0;
    private int yAxisSender = 0;

    private int xAxisGraphic = 0;
    private int yAxisGraphic = 0;

    private int sliderSender;
    private int sliderAddress;

    private int pagesToPrint;
    private Attribute mediaFormat;
    private int mediaSize;

    private Book book;
    private PrinterJob pj;
    private PageFormat pf;
    private String[] sender;
    private String graphicPath;


    public EnvelopePrintService(Object parentObject, boolean withGraphic, boolean withSender, int format, int orientation, Object pages, boolean orderByTable, boolean senderPosition, Font addressFont, Font senderFont, String graphicPath, String[] sender, String path) {
        this.parentObject = parentObject;
        this.pages = Integer.parseInt(pages.toString());
        this.withGraphic = withGraphic;
        this.withSender = withSender;
        this.format = format;
        this.orientation = orientation;
        this.orderByTable = orderByTable;
        this.senderPosition = senderPosition;
        this.addressFont = addressFont;
        this.senderFont = senderFont;
        this.additionalFont = new Font("Arial",0,10);
        this.sender = sender;
        this.graphicPath = graphicPath;
        startPrinterJob();
        try {
            Object obj = new ServiceWorker(parentObject).executeTransaction();
            labelStrings = (Vector<Vector<String[]>>) obj;
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        getPaperWidth();
        getStringArray();
    }

    private void initGraphics() {
        getPaperWidth();
        getStringArray();
    }

    public void startPrinting() {
        pj.setPageable(book);
        Date date = new Date();
        pj.setJobName(date.toString());
    if (pj.printDialog()) {
      try {
        pj.print();
      } catch (PrinterException e) {
        log.error(e.getMessage(), e);
      }
    }
    }

    private void startPrinterJob(){
        pj = PrinterJob.getPrinterJob();
    }

    

    private PageFormat getPaperWidth(){
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        MediaSizeName ms = setMediaSize();
        aset.add(ms);
        pf = pj.getPageFormat(aset);
        pf.setOrientation(PageFormat.LANDSCAPE);
        return pf;
    }

    private MediaSizeName setMediaSize(){
        switch(format){
            case 0: return MediaSizeName.ISO_C3;
            case 1: return MediaSizeName.ISO_B4;
            case 2: return MediaSizeName.ISO_C4;
            case 3: return MediaSizeName.ISO_B5;
            case 4: return MediaSizeName.ISO_C5;
            case 5: return MediaSizeName.ISO_C6;
            case 6: return MediaSizeName.ISO_DESIGNATED_LONG;
            case 7: return MediaSizeName.ISO_B6;
            case 8: return MediaSizeName.ISO_C6;
        }
         return null;
    }

    private void getStringArray(){
            book = new Book();
            groupString.clear();
            for (Vector<String[]> group : labelStrings) {
                for (int y = 0; y <pages; y++) {
                    for (String[] g : group) {
                        groupString.add(g);
                        book.append(new EnvelopeBook(g,pf,xAxisAddress, yAxisAddress, xAxisSender, yAxisSender, xAxisGraphic, yAxisGraphic, sender, graphicPath, addressFont, senderFont,withGraphic, senderPosition, withSender,pages*groupString.size(), sliderSender, sliderAddress),pf,1);
                    }
                }
            }
    }




    public ImageIcon getGraphic(int page) {
        BufferedImage img = new BufferedImage((int)pf.getWidth(),(int)pf.getHeight(),BufferedImage.TYPE_BYTE_INDEXED);
        Graphics2D g2 = (Graphics2D) img.getGraphics();
        String[] g = groupString.get(page);
        new EnvelopePaint(g2,g,pf,xAxisAddress, yAxisAddress, xAxisSender, yAxisSender, xAxisGraphic, yAxisGraphic, sender, graphicPath, addressFont, senderFont,withGraphic, senderPosition, withSender, sliderSender, sliderAddress);
        return new ImageIcon(img);
    }
    
    public void setPages(int pages) {
        this.pages = pages;
        initGraphics();
    }

    public void setWithGraphic(boolean withGraphic) {
        this.withGraphic = withGraphic;
        initGraphics();
    }

    public void setWithSender(boolean withSender) {
        this.withSender = withSender;
        initGraphics();
    }

    public void setSenderPosition(boolean senderPosition) {
        this.senderPosition = senderPosition;
        initGraphics();
    }

    public void setOrderByTable(boolean orderByTable) {
        this.orderByTable = orderByTable;
        initGraphics();
    }

    public void setFormat(int format) {
        this.format = format;
        initGraphics();

    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
        initGraphics();
    }

    public void setXAxisAddress(int xAxisAddress) {
        this.xAxisAddress = this.xAxisAddress + xAxisAddress;
        initGraphics();
    }

    public void setYAxisAddress(int yAxisAddress) {
        this.yAxisAddress = this.yAxisAddress+yAxisAddress;
        initGraphics();
    }

    public void setXAxisSender(int xAxisSender) {
        this.xAxisSender = this.xAxisSender+xAxisSender;
        initGraphics();
    }

    public void setYAxisSender(int yAxisSender) {
        this.yAxisSender = this.yAxisSender+yAxisSender;
        initGraphics();
    }

    public void setXAxisGraphic(int xAxisGraphic) {
        this.xAxisGraphic = this.xAxisGraphic+xAxisGraphic;
        initGraphics();
    }

    public void setYAxisGraphic(int yAxisGraphic) {
        this.yAxisGraphic = this.yAxisGraphic+yAxisGraphic;
        initGraphics();
    }

    public void setParentObject(Object parentObject) {
        this.parentObject = parentObject;
    }


    public void setSenderFont(Font font) {
        this.senderFont = font;
        initGraphics();
    }

    public void setAddressFont(Font font) {
        this.addressFont = font;
        initGraphics();
    }

    public int getXAxisAddress() {
        return xAxisAddress;
    }

    public int getYAxisAddress() {
        return yAxisAddress;
    }

    public int getXAxisSender() {
        return xAxisSender;
    }

    public int getYAxisSender() {
        return yAxisSender;
    }

    public int getXAxisGraphic() {
        return xAxisGraphic;
    }

    public int getYAxisGraphic() {
        return yAxisGraphic;
    }

    public void setSliderSender(int i){
        sliderSender = i;
        initGraphics();
    }

    public void setSliderAddress(int i){
        sliderAddress = i;
        initGraphics();
    }

    public int getSliderSender(){
        return sliderSender;
    }

    public int getSliderAddress(){
        return sliderAddress;
    }

    public void setGraphicPath(String text) {
        this.graphicPath = text;
    }

    public int getMaxPages(){
        return groupString.size();
    }

    public void setSender(String[] sender){
        this.sender = sender;
    }
}
