package org.en.tealEye.guiExt;

import org.en.tealEye.guiExt.ExtPanel.ExtendedJPanelImpl;

import java.awt.*;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 03.11.2007
 * Time: 08:30:57
 */
public class TitleBarPanel extends ExtendedJPanelImpl {


    public TitleBarPanel() {
        super();
        this.setSize(new Dimension(getPreferredSize()));
        //this.setLayout( new BorderLayout());
        repaint();
    }

    public void paint(Graphics g) {

        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.YELLOW);
        //g2.setXORMode(Color.BLUE);

        g2.setColor(Color.decode("#5f73f1"));
        g2.setStroke(new BasicStroke(1.0f));
        g2.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);

        g2.setColor(Color.decode("#aab9fb"));
        g2.drawLine(1, 1, this.getWidth() - 2, 1);
        g2.setColor(Color.decode("#abbafc"));
        g2.drawLine(1, 2, this.getWidth() - 2, 2);
        g2.setColor(Color.decode("#a7b6fb"));
        g2.drawLine(1, 3, this.getWidth() - 2, 3);
        g2.setColor(Color.decode("#a3b3fa"));
        g2.drawLine(1, 4, this.getWidth() - 2, 4);
        g2.setColor(Color.decode("#9faefa"));
        g2.drawLine(1, 5, this.getWidth() - 2, 5);
        g2.setColor(Color.decode("#99a9f9"));
        g2.drawLine(1, 6, this.getWidth() - 2, 6);
        g2.setColor(Color.decode("#94a5f8"));
        g2.drawLine(1, 7, this.getWidth() - 2, 7);
        g2.setColor(Color.decode("#8e9ff8"));
        g2.drawLine(1, 8, this.getWidth() - 2, 8);
        g2.setColor(Color.decode("#889af6"));
        g2.drawLine(1, 9, this.getWidth() - 2, 9);
        g2.setColor(Color.decode("#8294f6"));
        g2.drawLine(1, 10, this.getWidth() - 2, 10);
        g2.setColor(Color.decode("#7c8ff5"));
        g2.drawLine(1, 11, this.getWidth() - 2, 11);
        g2.setColor(Color.decode("#7689f4"));
        g2.drawLine(1, 12, this.getWidth() - 2, 12);
        g2.setColor(Color.decode("#7284f3"));
        g2.drawLine(1, 13, this.getWidth() - 2, 13);
        g2.setColor(Color.decode("#6c80f3"));
        g2.drawLine(1, 14, this.getWidth() - 2, 14);
        g2.setColor(Color.decode("#687cf2"));
        g2.drawLine(1, 15, this.getWidth() - 2, 15);
        g2.setColor(Color.decode("#6478f2"));
        g2.drawLine(1, 16, this.getWidth() - 2, 16);
        g2.setColor(Color.decode("#6175f1"));
        g2.drawLine(1, 17, this.getWidth() - 2, 17);
        g2.drawRect(0, 0, this.getWidth(), 17);
        //g2.fillRect(0,0,this.getWidth(), 16);
        g2.setColor(Color.ORANGE);
        g2.fillRect(this.getWidth() - 60, 3, 57, 4);
        g2.setColor(Color.YELLOW);
        g2.drawString(this.getTitle(), 5, 14);
    }
}
