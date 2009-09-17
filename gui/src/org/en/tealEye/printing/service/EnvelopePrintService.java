package org.en.tealEye.printing.service;

import de.liga.dart.gruppen.service.GruppenService;
import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.model.Spielort;
import de.liga.dart.model.Ligagruppe;
import de.liga.dart.model.Ligateam;
import de.liga.dart.spielort.service.SpielortService;
import de.liga.util.StringUtils;

import javax.swing.*;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.Attribute;
import javax.print.attribute.standard.MediaSizeName;
import java.awt.*;
import java.awt.print.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Set;
import java.util.Vector;

import org.en.tealEye.framework.BeanTableModel;
import org.en.tealEye.printing.controller.GenericThread;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 14.09.2009
 * Time: 01:24:41
 * To change this template use File | Settings | File Templates.
 */
public class EnvelopePrintService {

    private Object parentObject;
    final Vector<Vector<String[]>> labelStrings = new Vector<Vector<String[]>>();
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
    private String graphicPath;


    private double xAxisAddress;
    private double yAxisAddress;

    private double xAxisSender;
    private double yAxisSender;

    private double xAxisGraphic;
    private double yAxisGraphic;

    private int pagesToPrint;
    private Attribute mediaFormat;
    private int mediaSize;

    private Book book;
    private PrinterJob pj;
    private PageFormat pf;


    public EnvelopePrintService(Object parentObject, boolean withGraphic, boolean withSender, int format, int orientation, Object pages, boolean orderByTable, boolean senderPosition, Font addressFont, Font senderFont, String graphicPath) {
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
        this.graphicPath = graphicPath;
        startPrinterJob();
        aquireData();
        getPaperWidth();
        getStringArray();
    }

    private void initGraphics() {
        getPaperWidth();
        getStringArray();
    }

    public void startPrinting() {
        pj.setPageable(book);
        pj.setJobName("My book");
    if (pj.printDialog()) {
      try {
        pj.print();
      } catch (PrinterException e) {
        System.out.println(e);
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
        System.out.println(format);
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
            for (Vector<String[]> group : labelStrings) {
                for (int y = 0; y <pages; y++) {
                    for (String[] g : group) {
                        groupString.add(g);
                        book.append(new EnvelopeBook(g),pf,1);

                    }
                }
            }
    }


    private void aquireData(){
        ServiceFactory.runAsTransaction(new Runnable() {
            public void run() {
        JTable table = (JTable)parentObject;
        int[] rowSelectionCount = table.getSelectedRows();
        if(rowSelectionCount.length<1){
            rowSelectionCount = new int[table.getRowCount()];
            for(int i = 0; i<table.getRowCount();i++){
                rowSelectionCount[i] = i;
            }
        }
        GruppenService gs = ServiceFactory.get(GruppenService.class);
        Spielort spielort = null;
        String[] rowString;

        ArrayList<Long> ortIdx = new ArrayList<Long>();
        if(table.getName().equals("Table_Ligagruppe")){
            for(int rowIndex:rowSelectionCount){
                Object obj = ((BeanTableModel) table.getModel()).getObject(rowIndex);
                Ligagruppe gruppe = ((Ligagruppe) obj);
                ArrayList<Long> ortIds = new ArrayList<Long>();
                Vector<String[]> groupVec = new Vector<String[]>();
            for(int teamIndex = 1; teamIndex <= 8; teamIndex++){

                    Ligateam lt;
                    try {
                        lt = gruppe.findSpiel(gs.findSpieleInGruppe(gruppe), teamIndex).getLigateam();
                        
                    } catch (NullPointerException e) {
                        lt = null;
                    }
                    if (lt != null) {
                        spielort = ServiceFactory.get(SpielortService.class)
                                .findSpielortById(lt.getSpielort().getSpielortId());
                    }
                    if(!(lt==null)){
                        if(!ortIds.contains(spielort.getSpielortId())){
                            rowString = new String[5];
                            String lines[] = StringUtils.wordWrap(spielort.getSpielortName(), 25);
                            rowString[0] = lines[0];
                            if(lines.length > 1) {
                                rowString[1] = lines[1];
                            } else {
                                rowString[1] = "";
                            }
                            rowString[2] = spielort.getStrasse();

                            rowString[3] = spielort.getPlzUndOrt();
                            rowString[4] = "";
                            groupVec.addElement(rowString);
                            ortIds.add(spielort.getSpielortId());
                            ortIdx.add(spielort.getSpielortId());
                            //System.out.println(rowString[0]);
                        }else{
                            Set<Ligateam> teams = spielort.getLigateamsInGruppe(gruppe);
                            int idx = ortIdx.lastIndexOf(spielort.getSpielortId());

                            //String[] entries = labelStrings.get(idx);
                            ortIds.add(spielort.getSpielortId());
                            int tmpAnzahl = 0;
                            for(Long anzahl : ortIds){
                                if(anzahl == spielort.getSpielortId())
                                    tmpAnzahl++;
                            }
                            //entries[4] = String.valueOf(tmpAnzahl);
                        }
                    }
                }
                    labelStrings.add(groupVec);
            }
        }
    }
        });
    }

    public ImageIcon getGraphic(int page) {
        BufferedImage img = new BufferedImage((int)pf.getWidth(),(int)pf.getHeight(),BufferedImage.TYPE_BYTE_INDEXED);
        Graphics2D g2 = (Graphics2D) img.getGraphics();
        String[] g = groupString.get(page);
        new EnvelopePaint(g2,g,pf);
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

    public void setParentObject(Object parentObject) {
        this.parentObject = parentObject;
    }


}