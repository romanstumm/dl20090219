package org.en.tealEye.guiMain.FloatPanel;

import org.en.tealEye.guiExt.ExtPanel.ExtendedJPanelImpl;

import javax.swing.*;
import java.awt.*;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 31.10.2007
 * Time: 23:02:08
 */
public class FloatingToolbarPanel extends JToolBar {

//    private Map<String, JComponent> menuPanelMap = new HashMap<String, JComponent>();
    private ExtendedJPanelImpl jPanel;

    public FloatingToolbarPanel() {
        super("Hauptmenu");
        initComponents();
    }

    private void initComponents() {
        jPanel = new ExtendedJPanelImpl();
        jPanel.setLayout(new GridBagLayout());
        jPanel.setPreferredSize(new Dimension(200, 500));
        //this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEtchedBorder(1)));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        this.add(jPanel, gbc);
        this.setFloatable(true);
        this.setBorderPainted(true);
        this.setAutoscrolls(true);
        this.setOrientation(1);
        this.setSize(new Dimension(250, 500));

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTH;
//        JPanel dummyPanel = new JPanel();
        //this.add(dummyPanel,gbc);
        //dummyPanel.setPreferredSize(new Dimension(200,500));
    }

    public void addPanelComponent(JComponent component) {
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        gbc.gridx = 0;
        try {
            gbc.gridy = jPanel.getComponentCount();
        } catch (NullPointerException e) {
            gbc.gridy = 0;
        }
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTH;
        jPanel.add(component, gbc);
//        menuPanelMap.put(component.getName(), component);
    }

    protected void collapsePanelComponent(String name) {
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.gridx = 0;
        try {
            gbc.gridy = jPanel.getComponentCount();
        } catch (NullPointerException e) {
            gbc.gridy = 0;
        }
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTH;
    }


}
