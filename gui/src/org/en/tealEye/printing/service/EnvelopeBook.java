package org.en.tealEye.printing.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import java.awt.print.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 14.09.2009
 * Time: 09:08:07
 * To change this template use File | Settings | File Templates.
 */
public class EnvelopeBook implements Printable {
    protected static final Log log = LogFactory.getLog(EnvelopeBook.class);
    private String[] grp;

    private int xAxisAddress = 0;
    private int yAxisAddress = 0;

    private int xAxisSender = 0;
    private int yAxisSender = 0;

    private int xAxisGraphic = 0;
    private int yAxisGraphic = 0;

    private Font senderFont;
    private Font addressFont;
    private String[] sender;
    private String graphicPath;
    private boolean senderInCorner;
    private boolean showGraphic;
    private boolean showSender;
    private PageFormat pf;
    private int maximumPages;

    private int sliderSender;
    private int sliderAddress;

    public EnvelopeBook(String[] g, PageFormat pf, int xAxisAddress, int yAxisAddress, int xAxisSender, int yAxisSender, int xAxisGraphic, int yAxisGraphic, String[] sender, String graphicPath, Font addressFont, Font senderFont, boolean showGraphic, boolean leftCornerSender, boolean showSender, int maximumPages, int sliderSender, int sliderAddress) {
        this.grp = g;
        this.pf = pf;
        this.xAxisAddress = xAxisAddress;
        this.yAxisAddress = yAxisAddress;
        this.xAxisSender = xAxisSender;
        this.yAxisSender = yAxisSender;
        this.xAxisGraphic = xAxisGraphic;
        this.yAxisGraphic = yAxisGraphic;
        this.senderFont  = senderFont;
        this.addressFont = addressFont;
        this.sender = sender;
        this.graphicPath = graphicPath;
        this.senderInCorner = leftCornerSender;
        this.showGraphic = showGraphic;
        this.showSender = showSender;
        this.maximumPages = maximumPages;
        this.sliderAddress = sliderAddress;
        this.sliderSender = sliderSender;
    }
  public int print(Graphics gfx, PageFormat pf, int pageIndex) throws PrinterException {
    // pageIndex 1 corresponds to page number 2.
    if (pageIndex > maximumPages)
      return Printable.NO_SUCH_PAGE;

        Graphics2D g2 = (Graphics2D) gfx;
        //g2.setColor(Color.white);
        //g2.fillRect(0,0,(int)pf.getImageableWidth(),(int)pf.getImageableHeight());
        //g2.setColor(Color.lightGray);
        FontMetrics sfm = g2.getFontMetrics(senderFont);
        FontMetrics afm = g2.getFontMetrics(addressFont);
        int a = sfm.stringWidth(sender[0]);
        int b = sfm.stringWidth(sender[1]);
        int c = sfm.stringWidth(sender[2]);
        int d = sfm.stringWidth(sender[3]);
        int e = sfm.stringWidth(sender[4]);
        //g2.fillRect((int)(pf.getImageableWidth()*0.85),(int)(pf.getImageableHeight()*0.1),80,60);
        g2.setColor(Color.black);
        if(showGraphic){
            BufferedImage img = null;
            File f = new File(graphicPath);
            if(f.canRead()){
            try {
                img = ImageIO.read(f);
            } catch (IOException x) {
                log.error(x.getMessage(), x);
            }
            }
            AffineTransform bio = new AffineTransform();
            bio.scale(0.1,0.1);
            g2.drawImage(img,(int)(pf.getImageableWidth()*0.6)+xAxisGraphic,(int)(pf.getImageableHeight()*0.5)+yAxisGraphic,(int)(pf.getImageableWidth()*0.4),afm.getHeight()*8,Color.white,null);

        }
        if(showSender){
            g2.setFont(senderFont);
                if(senderInCorner){
                    int i = 0;
                    for(String s : sender){
                        if(i==0){
                        g2.drawString(s,(int)(pf.getImageableWidth()*0.1)+xAxisSender, (int)(pf.getImageableHeight()*0.1)+sfm.getHeight()*i+sfm.getHeight()/2+yAxisSender);
                        }else {
                            if(i>3)
                                g2.drawString(s,(int)(pf.getImageableWidth()*0.1)+5+d+xAxisSender, (int)(pf.getImageableHeight()*0.1)+sfm.getHeight()*(i-1)+sfm.getHeight()/2+sliderSender*(i-1)+yAxisSender);
                            else
                                g2.drawString(s,(int)(pf.getImageableWidth()*0.1)+xAxisSender, (int)(pf.getImageableHeight()*0.1)+sfm.getHeight()*i+sfm.getHeight()/2+sliderSender*(i)+yAxisSender);
                        }
                        i++;
                    }
               }else{
                    g2.drawString(sender[0],(int)(pf.getImageableWidth()*0.6)+xAxisSender, (int)(pf.getImageableHeight()*0.5)-afm.getHeight()/2+yAxisSender);
                    g2.drawString(sender[1],(int)(pf.getImageableWidth()*0.6)+5+a+xAxisSender, (int)(pf.getImageableHeight()*0.5)-afm.getHeight()/2+yAxisSender);
                    g2.drawString(sender[2],(int)(pf.getImageableWidth()*0.6)+10+a+b+xAxisSender, (int)(pf.getImageableHeight()*0.5)-afm.getHeight()/2+yAxisSender);
                    g2.drawString(sender[3],(int)(pf.getImageableWidth()*0.6)+15+a+b+c+xAxisSender, (int)(pf.getImageableHeight()*0.5)-afm.getHeight()/2+yAxisSender);
                    g2.drawString(sender[4],(int)(pf.getImageableWidth()*0.6)+20+a+b+c+d+xAxisSender, (int)(pf.getImageableHeight()*0.5)-afm.getHeight()/2+yAxisSender);
                    g2.drawLine((int)(pf.getImageableWidth()*0.6)+xAxisSender,(int)((pf.getImageableHeight()*0.5)-afm.getHeight()/2)+1+yAxisSender,(int)(pf.getImageableWidth()*0.6)+20+a+b+c+d+e+xAxisSender,(int)((pf.getImageableHeight()*0.5)-afm.getHeight()/2)+1+yAxisSender);
                }
        }
        g2.setFont(addressFont);
        int i = 0;
        for(String s:grp){
            if(i==0){
                if(s.equals("")){

                }else
                g2.drawString(s,(int)(pf.getImageableWidth()*0.6)+xAxisAddress,(int)(pf.getImageableHeight()*0.5)+(afm.getHeight()/2)+sliderAddress*i+ yAxisAddress);
                i++;
            }
            else{
                if(s.equals("")){

                }
                else{
                    if(Character.isDigit(s.toCharArray()[0]) && s.length()<2){
                    g2.setFont(new Font("Arial",Font.PLAIN,6));
                    g2.drawString(s,(int)(pf.getImageableWidth()*0.6)+afm.stringWidth(grp[0])+ afm.stringWidth(grp[0])/3 +xAxisAddress,(int)(pf.getImageableHeight()*0.5)+(afm.getHeight()/2)+ yAxisAddress);
                    i++;
                    }else{
                    g2.drawString(s,(int)(pf.getImageableWidth()*0.6)+xAxisAddress,(int)(pf.getImageableHeight()*0.5)+afm.getHeight()*i+(afm.getHeight()/2)+sliderAddress*i+ yAxisAddress);
                    i++;
                    }
                }
            }
        }

    return Printable.PAGE_EXISTS;
  }
}
