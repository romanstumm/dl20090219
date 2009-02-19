package org.en.tealEye.guiMain;

import javax.swing.*;
import java.awt.*;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 23.02.2008
 * Time: 01:33:44
 */
public class ActivityIndicator extends JFrame {

    private JLabel prgsLabel;
    private JProgressBar prgsBar;

    public ActivityIndicator(){
        this.setSize(200, 100);
        initComponents();
    }

    private void initComponents(){
        prgsLabel = new JLabel("Lade...");
        prgsBar = new JProgressBar(0,100);
        prgsBar.setSize(200,10);
        this.setLayout(new BorderLayout());
        this.getContentPane().add(prgsLabel,BorderLayout.NORTH);
        this.getContentPane().add(prgsBar, BorderLayout.CENTER);
        this.setVisible(true);
        this.validate();
    }

    public JLabel getPrgsLabel() {
        return prgsLabel;
    }

    public void setPrgsLabel(JLabel prgsLabel) {
        this.prgsLabel = prgsLabel;
    }

    public JProgressBar getPrgsBar() {
        return prgsBar;
    }

    public void setPrgsBar(JProgressBar prgsBar) {
        this.prgsBar = prgsBar;
    }

    public void removeActivityIndicator(){
        this.dispose();
    }
}
