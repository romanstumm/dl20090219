package org.en.tealEye.printing.service;

import java.awt.print.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 14.09.2009
 * Time: 09:08:07
 * To change this template use File | Settings | File Templates.
 */
public class EnvelopeBook implements Printable {
    private String[] g;

    public EnvelopeBook(String[] g) {
        this.g = g;
    }
  @Override
  public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
    System.out.println("Page index = " + pageIndex);
    // pageIndex 1 corresponds to page number 2.
    if (pageIndex > 150)
      return Printable.NO_SUCH_PAGE;

    Graphics2D g2 = (Graphics2D) g;

    double w = pf.getImageableWidth();
    double h = pf.getImageableHeight();

    int xo = (int) pf.getImageableX();
    int yo = (int) pf.getImageableY();

    g2.setColor(Color.black);  

    g2.drawString(this.g[0],300,200);
    g2.drawString(this.g[1],300,220);
    g2.drawString(this.g[2],300,240);
    g2.drawString(this.g[3],300,260);

    return Printable.PAGE_EXISTS;
  }
}
