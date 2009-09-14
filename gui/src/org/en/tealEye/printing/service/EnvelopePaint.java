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
    private ImageIcon imageIcon;

    public EnvelopePaint(Graphics2D g2, String[] g, int width, int height){
        g2.setColor(Color.white);
        g2.fillRect(0,0, width, height);
        g2.setColor(Color.black);        
        g2.drawString(g[0],160,160);
        g2.drawString(g[1],160,120);
        g2.drawString(g[2],160,126);
        g2.drawString(g[3],160,126);
        g2.drawString(g[4],160,126);
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }
}
