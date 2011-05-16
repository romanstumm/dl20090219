package org.en.tealEye.printing.service;

import java.awt.image.BufferedImage;
import java.awt.print.Printable;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 22.11.2007
 * Time: 17:38:07
 */
public interface TablePrinting extends Printable {

    BufferedImage getPaintingCanvas(int pageNum);

    void rotate();

    void setTableValues();

    void generateDoc();

    int getPageCount();

    void repaintCanvas();

    void setLeftBorder(int leftBorder);

    void setUpperBorder(int upperBorder);

    void setPrintPageNum(boolean b);

    void paintCanvas();

    void kill();
}
