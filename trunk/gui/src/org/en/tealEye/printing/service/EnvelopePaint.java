package org.en.tealEye.printing.service;

import javax.swing.*;
import java.awt.*;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 14.09.2009
 * Time: 09:33:05
 * To change this template use File | Settings | File Templates.
 */
public class EnvelopePaint{
    private String[] g;
    private PageFormat pf;

    public EnvelopePaint(Graphics2D g2, String[] g, PageFormat pf){
        this.g = g;
        this.pf = pf;
        paint(g2);
    }

    public void paint(Graphics gfx){
        Graphics2D g2 = (Graphics2D) gfx;
        g2.setColor(Color.white);
        g2.fillRect(0,0,(int)pf.getImageableWidth(),(int)pf.getImageableHeight());
        g2.setColor(Color.black);
        g2.drawString(g[0],200,100);
        g2.drawString(g[1],200,120);
        g2.drawString(g[2],200,160);
        g2.drawString(g[3],200,180);
        g2.drawString(g[4],200,200);
    }
}
