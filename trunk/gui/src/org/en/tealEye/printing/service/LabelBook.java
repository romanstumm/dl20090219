package org.en.tealEye.printing.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.en.tealEye.guiServices.GlobalGuiService;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 17.01.11
 * Time: 17:01
 * To change this template use File | Settings | File Templates.
 */
public class LabelBook implements Printable {


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
    private Vector<BufferedImage> imageVector = new Vector<BufferedImage>();
    private BufferedImage lastImage;
    private String[] entries;
    private double ScaleX;
    private double ScaleY;
    private int hLabelCount;
    private int vLabelCount;
    private boolean guides;
    private int etiZeilenAbstand;
    private int x;
    private int y;

    private  Font font = new GlobalGuiService().getFontMap().get("EtiFont");
    private final Font smallFont = new Font(font.getName(),font.getStyle(),font.getSize()-6);

    public LabelBook(JTable sourceTable,int labelWidth, int labelHeight,
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
       //this.setSize((int)paperWidth,(int)paperHeight);
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {

        if (pageIndex > pagesNeeded)
        return Printable.NO_SUCH_PAGE;

        Graphics2D g2 = (Graphics2D) graphics;

        g2.setColor(Color.WHITE);
        g2.fillRect(0,0,(int)paperWidth,(int)paperHeight);
        g2.setColor(Color.RED);
        g2.setFont(smallFont);
        g2.drawString("Seite "+(pageIndex+1),(int)paperWidth/2-30,(int)paperHeight-20);

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
                            g2.drawString(entries[3],(int)(labelWidth*ScaleX*x+(padX*ScaleX)),(int)(labelHeight*ScaleY*y+(fontHeight*3)+(padY*ScaleY))+etiZeilenAbstand*2);
                        }else{
                            g2.drawString(entries[1],(int)(labelWidth*ScaleX*x+(padX*ScaleX)),(int)(labelHeight*ScaleY*y+(fontHeight*2)+(padY*ScaleY))+etiZeilenAbstand);
                            g2.drawString(entries[2],(int)(labelWidth*ScaleX*x+(padX*ScaleX)),(int)(labelHeight*ScaleY*y+(fontHeight*3)+(padY*ScaleY))+etiZeilenAbstand*2);
                          //g2.drawString(entries[3],(int)(labelWidth*ScaleX*x+(padX*ScaleX)),(int)(labelHeight*ScaleY*y+(fontHeight*4)+(padY*ScaleY))+etiZeilenAbstand*4);
                            g2.drawString(entries[3],(int)(labelWidth*ScaleX*x+(padX*ScaleX)),(int)(labelHeight*ScaleY*y+(fontHeight*4)+(padY*ScaleY))+etiZeilenAbstand*3);
                    }
                    if(!entries[4].equals("")){
                            g2.setFont(smallFont);
                  //        System.out.println(g2.getFont());
                            g2.drawString(entries[4], (float) ((int)(labelWidth*ScaleX*x+((labelWidth*ScaleX)/1))-50+(padX*ScaleX)), (float) ((int)(labelHeight*ScaleY*y+(fontHeight))+(padY)));

                    }
                    labelsNeeded--;
            }
    }
           this.pageIndex++;








        return Printable.PAGE_EXISTS;

    }

}
