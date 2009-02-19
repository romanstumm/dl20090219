package org.en.tealEye.guiPanels.ConfigPanels;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 05.11.2007
 * Time: 18:55:02
 */
public class MainConfigMenu extends JMenuBar {

    public MainConfigMenu(ActionListener al) {
        super();
        initComponents(al);
    }

    private void initComponents(ActionListener al) {

        JMenu dateiMenu = new JMenu("Datei");
        dateiMenu.add(new JMenuItem("Beenden"));
        dateiMenu.getItem(0).addActionListener(al);
        this.add(dateiMenu);
    }


}
