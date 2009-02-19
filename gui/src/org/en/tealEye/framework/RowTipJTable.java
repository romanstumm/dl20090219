package org.en.tealEye.framework;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:   <br/>
 * User: roman
 * Date: 30.03.2008, 22:35:08
 */
public class RowTipJTable extends JTable {
    private final Map<Integer,String> rowToolTips = new HashMap();

    @Override
    public String getToolTipText(MouseEvent event) {
        Point p = event.getPoint();

        int hitRowIndex = rowAtPoint(p);
        String text = rowToolTips.get(hitRowIndex);
        if(text != null) {
            return text;
        } else {
            return super.getToolTipText(event);
        }
    }

    public void setToolTipText(int row, String text) {
        rowToolTips.put(row, text);
    }
}
