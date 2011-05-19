package org.en.tealEye.printing.service;

import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.gruppen.service.GruppenService;
import de.liga.dart.model.Ligagruppe;
import de.liga.dart.model.Ligateam;
import de.liga.dart.model.Spielort;
import de.liga.dart.spielort.service.SpielortService;
import de.liga.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.en.tealEye.framework.BeanTableModel;
import org.en.tealEye.guiServices.GlobalGuiService;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.Vector;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 06.04.2008
 * Time: 19:09:48
 */
public class  LabelPrintingService extends JPanel implements LabelPrinting {
    private static final Log log = LogFactory.getLog(LabelPrintingService.class);
    final PrintingUtils utils = new PrintingUtils();

    int labelHeight;
    int labelWidth;
    final int labelLeftBorder;
    final int labelUpperBorder;


    double paperWidth = utils.getMediaWidth();
    double paperHeight = utils.getMediaHeight();
    int pagesNeeded;
    int pageReached = 0;
    int pageIndex = 0;
    int labelsNeeded;
    int labelsWanted;
    public int stringIndex = 0;
    final int fontHeight = 10;
    int padX = 10;
    int padY = 10;

    final JTable sourceTable;
    Vector<String[]> labelStrings = new Vector<String[]>();
    private Vector<BufferedImage> imageVector = new Vector<BufferedImage>();
    private BufferedImage lastImage;
    private String[] entries;
    private double ScaleX;
    private double ScaleY;
    private int hLabelCount;
    private int vLabelCount;
    private boolean guides;
    private int etiZeilenAbstand;
    //private List<int, boolean> dupeMap = new HashMap<int,boolean>();

    private  Font font = new GlobalGuiService().getFontMap().get("EtiFont");
    private final Font smallFont = new Font(font.getName(),font.getStyle(),font.getSize()-6);
    private final Font smallFont1 = new Font(font.getName(),font.getStyle(),font.getSize()-4);
    private boolean teamCount = true;



    // private  Font font = new GlobalGuiService().getFontMap().get("EtiFont"); 

    public LabelPrintingService(JTable sourceTable,int labelWidth, int labelHeight,
                                int labelLeftBorder, int labelRightBorder, int labelsWanted, int etiZeilenAbstand)
    {
       this.labelHeight = labelHeight;
       this.labelWidth = labelWidth;
       this.labelLeftBorder = labelLeftBorder;
       this.labelUpperBorder = labelRightBorder;
       this.sourceTable = sourceTable;
       this.labelsWanted = labelsWanted;
       this.labelsNeeded = labelsWanted;
       this.pagesNeeded = utils.getPagesPerLabel(utils.getLabelsPerPage(this.labelWidth, this.labelHeight),labelsWanted);
       this.etiZeilenAbstand = etiZeilenAbstand;
       sievePrintableData();
       setLabelValues();
       this.setSize((int)paperWidth,(int)paperHeight);
    }

    private void drawPage(Graphics2D g2, int pageIndex){
        g2.setColor(Color.WHITE);
        g2.fillRect(0,0,(int)paperWidth,(int)paperHeight);
        g2.setColor(Color.RED);
        g2.setFont(smallFont);
        g2.drawString("Seite "+(pageIndex+1),(int)paperWidth/2-30,(int)paperHeight-20);
        drawGrid(g2,pageIndex);
    }

    private void drawGrid(Graphics2D g2, int pageIndex){
        ScaleX = utils.getEtiScaleFactorX();
        ScaleY = utils.getEtiScaleFactorY();
        hLabelCount = utils.getHLabelCount(labelWidth);
        vLabelCount = utils.getVLabelCount(labelHeight);
        g2.setColor(Color.BLACK);
            g2.drawLine((int)paperWidth-1,0,(int)paperWidth-1,(int)paperHeight-1);
            g2.drawLine(0,(int)paperHeight-1,(int)paperWidth-1,(int)paperHeight-1);
            for(int y = 0; y<vLabelCount;y++){
                g2.drawLine(0,(int)(labelHeight*ScaleY*y),(int)paperWidth,(int)(labelHeight*ScaleY*y));
                for(int x = 0; x<hLabelCount;x++){
                g2.drawLine((int)(labelWidth*ScaleX*x),0,(int)(labelWidth*ScaleX*x),(int)paperHeight);
                }
            }
        drawLabelPackage(g2,pageIndex);
    }

    private void drawLabelPackage(Graphics2D g2, int pageIndex){
        stringIndex = pageIndex/pagesNeeded;
        labelsNeeded = labelsWanted-((pageIndex-pagesNeeded*stringIndex)*utils.getLabelsPerPage(labelWidth,labelHeight));
        Stroke s = new BasicStroke(0.5f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,1f,new float[]{2f,2f},0.5f);
        for(int y = 0; y<(vLabelCount); y++){
             for(int x = 0; x<hLabelCount; x++){
                if(labelsNeeded < 1 ){
                    break;
                }
                 if(stringIndex<labelStrings.size())
                    entries = labelStrings.get(stringIndex);
                    if(guides){
                        g2.setStroke(s);
                        g2.setColor(Color.RED);
                        g2.drawLine((int)(labelWidth*ScaleX*x+(padX*ScaleX)),0,(int)(labelWidth*ScaleX*x+(padX*ScaleX)), (int) paperHeight);
                        g2.drawLine(0,(int)(labelHeight*ScaleY*y+(padY*ScaleY))+2, (int) paperWidth, (int)(labelHeight*ScaleY*y+(padY*ScaleY))+2);
                    }
                    g2.setColor(Color.BLACK);

                            if(teamCount){
                                g2.setFont(smallFont1);
                                g2.drawString(entries[5], (int) (labelWidth * ScaleX * x + (padX * ScaleX)), (int) (labelHeight * ScaleY * y + (fontHeight) + (padY * ScaleY))-2);
                                g2.setStroke(new BasicStroke());
                                g2.drawLine((int)(labelWidth*ScaleX*x+(padX*ScaleX)),(int)(labelHeight*ScaleY*y+(fontHeight)+(padY*ScaleY)),(int)((labelWidth*ScaleX*x+(padX*ScaleX))+(labelWidth*ScaleX-padX*ScaleX-25)),(int)(labelHeight*ScaleY*y+(fontHeight)+(padY*ScaleY)));
                            }
                            g2.setFont(font);
                            g2.drawString(entries[0],(int)(labelWidth*ScaleX*x+(padX*ScaleX)),(int)(labelHeight*ScaleY*y+(fontHeight*2)+(padY*ScaleY)));


                        if(entries[1].matches("")){
                            g2.drawString(entries[2],(int)(labelWidth*ScaleX*x+(padX*ScaleX)),(int)(labelHeight*ScaleY*y+(fontHeight*3)+(padY*ScaleY))+etiZeilenAbstand);
                            g2.drawString(entries[3],(int)(labelWidth*ScaleX*x+(padX*ScaleX)),(int)(labelHeight*ScaleY*y+(fontHeight*4)+(padY*ScaleY))+etiZeilenAbstand*2);
                        }else{
                            g2.drawString(entries[1],(int)(labelWidth*ScaleX*x+(padX*ScaleX)),(int)(labelHeight*ScaleY*y+(fontHeight*3)+(padY*ScaleY))+etiZeilenAbstand);
                            g2.drawString(entries[2],(int)(labelWidth*ScaleX*x+(padX*ScaleX)),(int)(labelHeight*ScaleY*y+(fontHeight*4)+(padY*ScaleY))+etiZeilenAbstand*2);
                          //g2.drawString(entries[3],(int)(labelWidth*ScaleX*x+(padX*ScaleX)),(int)(labelHeight*ScaleY*y+(fontHeight*4)+(padY*ScaleY))+etiZeilenAbstand*4);
                            g2.drawString(entries[3],(int)(labelWidth*ScaleX*x+(padX*ScaleX)),(int)(labelHeight*ScaleY*y+(fontHeight*5)+(padY*ScaleY))+etiZeilenAbstand*3);




                    }
                    if(!entries[4].equals("")){
                        addLocationCount(g2,x,y);
                    }
                    labelsNeeded--;
            }
    }
           this.pageIndex++;
    }


    private void addLocationCount(Graphics2D g2, int x, int y){
        g2.setFont(smallFont);
//        System.out.println(g2.getFont());
        g2.drawString(entries[4], (float) ((int)(labelWidth*ScaleX*x+((labelWidth*ScaleX)/1))-50+(padX*ScaleX)), (float) ((int)(labelHeight*ScaleY*y+(fontHeight))+(padY)));
    }

    private void sievePrintableData(){
        ServiceFactory.runAsTransaction(new Runnable() {
            public void run() {
        int[] rowSelectionCount;
              rowSelectionCount = sourceTable.getSelectedRows();
        if(rowSelectionCount.length<1){
            rowSelectionCount = new int[sourceTable.getRowCount()];
            for(int i = 0; i<sourceTable.getRowCount();i++){
                rowSelectionCount[i] = i;
            }
        }
        GruppenService gs = ServiceFactory.get(GruppenService.class);
        Spielort spielort = null;
        String[] rowString;

        ArrayList<Long> ortIdx = new ArrayList<Long>();
        if(sourceTable.getName().equals("Table_Ligagruppe")){
            for(int rowIndex:rowSelectionCount){
                Object obj = ((BeanTableModel) sourceTable.getModel()).getObject(sourceTable.convertRowIndexToModel(rowIndex));
                Ligagruppe gruppe = ((Ligagruppe) obj);
                ArrayList<Long> ortIds = new ArrayList<Long>();
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
                            rowString = new String[8];
                            String lines[] = StringUtils.wordWrap(spielort.getSpielortName(), 25);
                            rowString[0] = lines[0];
                            rowString[5] = spielort.getLigagruppenLabel(lt.getLiga());
                            rowString[6] = lt.getLiga().getLigaName();
                            rowString[7] = String.valueOf(spielort.getSpielortId());
                            if(lines.length > 1) {
                                rowString[1] = lines[1];
                            } else {
                                rowString[1] = "";
                            }
                            rowString[2] = spielort.getStrasse();

                            rowString[3] = spielort.getPlzUndOrt();
                            rowString[4] = "";


                            labelStrings.addElement(rowString);
                            ortIds.add(spielort.getSpielortId());
                            ortIdx.add(spielort.getSpielortId());
                        }else{
                            Set<Ligateam> teams = spielort.getLigateamsInGruppe(gruppe);
                            int idx = ortIdx.lastIndexOf(spielort.getSpielortId());
                            
                            String[] entries = labelStrings.get(idx);
                            ortIds.add(spielort.getSpielortId());
                            int tmpAnzahl = 0;
                            for(Long anzahl : ortIds){
                                if(anzahl == spielort.getSpielortId())
                                    tmpAnzahl++;
                            }
                            entries[4] = String.valueOf(tmpAnzahl);
                           // entries[5] = spielort.getLigagruppenLabel(lt.getLiga());
//                            System.out.println(entries[5]);


                        }
                    }
                }
            }
        }}});
        if(teamCount)
        clean();
    }

    private void clean(){

        String spielortName;
        String spielortPLZOrt;
        String spielortStrasse;
        String spielortLiga;
        String spielortID;
        Vector<String[]> tmpVec = new Vector<String[]>();

        int size = labelStrings.size();

        for(int idx = 0; idx<size;idx++){
            if(idx==labelStrings.size())
                break;
            String[]s = labelStrings.get(idx);
            spielortName = s[0];
            spielortStrasse = s[2];
            spielortPLZOrt = s[3];
            spielortLiga = s[6];
            spielortID = s[7];

                for(int i = 0;i<size;i++){
                    if(i>=labelStrings.size())
                        break;
                String[] str = labelStrings.get(i);
 //                           System.out.println(str[0]);

//                            System.out.println(str[2]);
//                            System.out.println(str[3]);
                    if(str[7].equals(spielortID) && str[6].equals(spielortLiga) && i!=idx) {
                            labelStrings.remove(i);
                    }

                }

        }
        //labelStrings.clear();
        //labelStrings = tmpVec;
    }
    public void paintCanvas(){
        BufferedImage im;
        for(int i = 0; i< getAllPagesNeeded();i++){
        im = new BufferedImage((int) (paperWidth),(int) (paperHeight),BufferedImage.TYPE_BYTE_INDEXED);
        Graphics2D g2 = im.createGraphics();
                drawPage(g2,i);
            //imageVector.addElement(im);
        }

    }

    public void kill() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex >= getAllPagesNeeded()) {
            return Printable.NO_SUCH_PAGE;
        } else{
            Graphics2D g2 = (Graphics2D) graphics;
            guides = false;
               drawLabelPackage(g2,pageIndex);
            return Printable.PAGE_EXISTS;
        }
    }

    public void setLabelWidth(int labelWidth) {
        this.labelWidth = labelWidth;
    }

    public void setLabelHeight(int labelHeight) {
        this.labelHeight = labelHeight;
    }

    public void setLabelSideBorder(int labelSideBorder) {
        this.padX = labelSideBorder;
    }

    public void setLabelUpperBorder(int labelUpperBorder) {
        this.padY = labelUpperBorder;
    }

    public void setLabelCount(int labelCount) {
        this.labelsWanted = labelCount;
    }

    public void setLabelFont(Font font){
        this.font = font;
    }

    public void setEtiZeilenAbstand(int i){
        etiZeilenAbstand = i;
    }

    public void repaintCanvas(int pageNum) {
        setLabelValues();
        stringIndex = 0;
        imageVector.removeAllElements();
        pageIndex = getAllPagesNeeded();
        paintCanvas();
    }

    public BufferedImage getPaintingCanvas(int pageNum) {

        BufferedImage im;
        im = new BufferedImage((int) (paperWidth),(int) (paperHeight),BufferedImage.TYPE_BYTE_INDEXED);
        boolean trigger = false;
        int lpp = utils.getLabelsPerPage(labelWidth,labelHeight);
        int seitenProLabel = utils.getPagesPerLabel(lpp,this.labelsWanted);

        if(seitenProLabel<pageNum){
                double t;
                     t = Math.ceil(pageNum/seitenProLabel);

                int th = (int) ((int)t*seitenProLabel);
                int pageOfStringBlock = pageNum-th;
                int stringBlock = (int) t;
            String[] strings = labelStrings.get(stringBlock);
                if(!strings[4].equals("")){
                    if(seitenProLabel-1 == pageOfStringBlock){
                        log.debug("beep");
                    }
                }
            log.debug("Seite von Stringblock:"+pageOfStringBlock);
            log.debug("Stringblock:"+stringBlock);
            log.debug("Seite:"+ pageNum);

//                System.out.println(strings[0]);
//                System.out.println(strings[1]);
//                System.out.println(strings[2]);
//                System.out.println(strings[3]);

        }else{
                double t;
                    t = Math.ceil(pageNum/seitenProLabel);
                int th = (int) ((int)t*seitenProLabel);
                int pageOfStringBlock = pageNum-th;
                int stringBlock = (int) t;
             String[] strings = labelStrings.get(stringBlock);
                 if(!strings[4].equals("")){
                    if(seitenProLabel-1 == pageOfStringBlock){
//                        System.out.println("beep");
                    }
                }
                log.debug("Seite von Stringblock:"+pageOfStringBlock);
                log.debug("Stringblock:"+stringBlock);
                log.debug("Seite:"+ pageNum);

//                System.out.println(strings[0]);
//                System.out.println(strings[1]);
//                System.out.println(strings[2]);
//                System.out.println(strings[3]);

        }
        Graphics2D g2 = im.createGraphics();
                drawPage(g2,pageNum);
         return im;
    }

    @SuppressWarnings({"SuspiciousNameCombination"})
    public void rotate() {
        double tmpX = paperWidth;
        paperWidth = paperHeight;
        paperHeight = tmpX;
        repaintCanvas(pageIndex);
    }

    public void setTableValues() {
        // do nothing
    }

    public void generateDoc() {
        RepaintManager currentManager = RepaintManager.currentManager(this);
        currentManager.setDoubleBufferingEnabled(false);
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        job.setJobName("print-liga-label-" + new Date());
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
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

    public void setLabelValues(){
        this.pagesNeeded = utils.getPagesPerLabel(utils.getLabelsPerPage(labelWidth, labelHeight),labelsWanted);
        this.labelsNeeded = labelsWanted;
        pageReached = 1;
    }

    public int getAllPagesNeeded(){
        return pagesNeeded*(labelStrings.size());
    }

    public void flushStringIndex() {
        pageIndex = 0;
        stringIndex = 0;
    }


    public void setGuides(boolean b) {
        guides = b;
    }
    
    public void setTeamsInSpielort(boolean b){
        teamCount = b;
        sievePrintableData();
    }

    public void cleanUp(){
        imageVector = null;
        System.gc();
        this.removeAll();
    }
}
