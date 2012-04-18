package org.en.tealEye.printing.service;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 27.12.2007
 * Time: 04:01:51
 */
public interface LabelPrinting extends TablePrinting{

    void setLabelWidth(int labelWidth);

    void setLabelHeight(int labelHeight);

    void setLabelSideBorder(int labelSideBorder);

    void setLabelUpperBorder(int labelUpperBorder);

    void setLabelCount(int labelCount);

    void repaintCanvas(int pageNum);

    int getAllPagesNeeded();
}
