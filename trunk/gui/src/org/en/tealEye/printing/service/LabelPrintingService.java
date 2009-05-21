package org.en.tealEye.printing.service;

import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.gruppen.service.GruppenService;
import de.liga.dart.model.Ligagruppe;
import de.liga.dart.model.Ligateam;
import de.liga.dart.model.Spielort;
import de.liga.dart.spielort.service.SpielortService;
import de.liga.util.StringUtils;
import org.en.tealEye.framework.BeanTableModel;
import org.en.tealEye.guiServices.GuiService;
import org.en.tealEye.guiServices.GlobalGuiService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Set;
import java.util.Vector;
import java.util.Date;

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
    final Vector<String[]> labelStrings = new Vector<String[]>();
    private final Vector<BufferedImage> imageVector = new Vector<BufferedImage>();
    private BufferedImage lastImage;
    private String[] entries;
    private double ScaleX;
    private double ScaleY;
    private int hLabelCount;
    private int vLabelCount;
    private boolean guides;
    private int etiZeilenAbstand;

    private  Font font = new GlobalGuiService().getFontMap().get("EtiFont");
    private final Font smallFont = new Font(font.getName(),font.getStyle(),font.getSize()-6);


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
                    g2.setFont(font);
                    g2.drawString(entries[0],(int)(labelWidth*ScaleX*x+(padX*ScaleX)),(int)(labelHeight*ScaleY*y+(fontHeight)+(padY*ScaleY)));
                        if(entries[1].matches("")){
                            g2.drawString(entries[2],(int)(labelWidth*ScaleX*x+(padX*ScaleX)),(int)(labelHeight*ScaleY*y+(fontHeight*2)+(padY*ScaleY))+etiZeilenAbstand);
                            g2.drawString(entries[4],(int)(labelWidth*ScaleX*x+(padX*ScaleX)),(int)(labelHeight*ScaleY*y+(fontHeight*3)+(padY*ScaleY))+etiZeilenAbstand*2);
                        }else{
                            g2.drawString(entries[1],(int)(labelWidth*ScaleX*x+(padX*ScaleX)),(int)(labelHeight*ScaleY*y+(fontHeight*2)+(padY*ScaleY))+etiZeilenAbstand);
                            g2.drawString(entries[2],(int)(labelWidth*ScaleX*x+(padX*ScaleX)),(int)(labelHeight*ScaleY*y+(fontHeight*3)+(padY*ScaleY))+etiZeilenAbstand*2);
                          //g2.drawString(entries[3],(int)(labelWidth*ScaleX*x+(padX*ScaleX)),(int)(labelHeight*ScaleY*y+(fontHeight*4)+(padY*ScaleY))+etiZeilenAbstand*4);
                            g2.drawString(entries[4],(int)(labelWidth*ScaleX*x+(padX*ScaleX)),(int)(labelHeight*ScaleY*y+(fontHeight*4)+(padY*ScaleY))+etiZeilenAbstand*3);
                    }
                    if(!entries[5].equals("count1")){
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
        g2.drawString(entries[5], (float) ((int)(labelWidth*ScaleX*x+((labelWidth*ScaleX)/1))-50+(padX*ScaleX)), (float) ((int)(labelHeight*ScaleY*y+(fontHeight))+(padY)));
    }

    private void sievePrintableData(){
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
        ArrayList<Long> ortIds = new ArrayList<Long>();
        if(sourceTable.getName().equals("Table_Ligagruppe")){
            for(int rowIndex:rowSelectionCount){
                Object obj = ((BeanTableModel) sourceTable.getModel()).getObject(rowIndex);
                Ligagruppe gruppe = ((Ligagruppe) obj);
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
                            rowString = new String[6];
                            String lines[] = StringUtils.wordWrap(spielort.getSpielortName(), 25);
                            rowString[0] = lines[0];
                            if(lines.length > 1) {
                                rowString[1] = lines[1];
                            } else {
                                rowString[1] = "";
                            }
                            rowString[2] = spielort.getStrasse();
                            rowString[3] = "";
                            rowString[4] = spielort.getPlzUndOrt();
                            rowString[5] = "count1";
                            labelStrings.addElement(rowString);
                            ortIds.add(spielort.getSpielortId());
                        }else{
                            Set<Ligateam> teams = spielort.getLigateamsInGruppe(gruppe);
                            int idx = ortIds.indexOf(spielort.getSpielortId());
                            String[] entries = labelStrings.get(idx);
                            entries[4] = String.valueOf(teams.size());
                        }
                    }
            }
            }
        }
    }
    public void paintCanvas(){
        BufferedImage im;
        for(int i = 0; i< getAllPagesNeeded();i++){
        im = new BufferedImage((int) (paperWidth),(int) (paperHeight),BufferedImage.TYPE_BYTE_INDEXED);
        Graphics2D g2 = im.createGraphics();
                drawPage(g2,i);
            imageVector.addElement(im);
        }

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
        if(!(pageNum>=getAllPagesNeeded())){
            lastImage = imageVector.get(pageNum);
            return imageVector.get(pageNum);
        }
        else
            return lastImage;// do nothing
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
}
