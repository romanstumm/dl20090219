package org.en.tealEye.guiMain.FloatPanel;

import org.en.tealEye.controller.gui.MenuController;
import org.en.tealEye.guiExt.TitleBarPanel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 03.11.2007
 * Time: 07:26:40
 */
public class ActiveFrameMenu extends TitleBarPanel {

    private final Map<String, JLabel> buttonMap = new HashMap<String, JLabel>();
    private final Vector<JLabel> buttonVector = new Vector<JLabel>();
    private final MenuController menuController;
    private JPanel buttonPanel;
    private String activeButtonName;

    public ActiveFrameMenu(MenuController menuController) {
        super();
        this.menuController = menuController;
        initComponents();
    }

    private void initComponents() {
        this.setSize(new Dimension(200, 250));
        this.setLayout(new GridBagLayout());
        this.setTitle("Offene Fenster");
        this.setName("ActiveFrameMenu");
        buttonPanel = new JPanel(new GridBagLayout());


        GridBagConstraints gbc;

        gbc = new GridBagConstraints();
        gbc.weighty = 0.1;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 2, 2, 2);
        gbc.anchor = GridBagConstraints.NORTH;

        this.add(buttonPanel, gbc);


    }

    public void addFrameButton(String frameName) {
        Vector<String> v = new Vector<String>();
        if(buttonVector != null){

        for(JLabel label : buttonVector){
            v.addElement(label.getText());
        }
        }
        if(!v.contains(frameName)){
        String title = menuController.getFrameTitle(frameName);
        JLabel button = new JLabel();
        buttonMap.put(frameName, button);
        buttonVector.add(button);
        button.addMouseListener(menuController);
        button.setName(frameName);
        String subTitle;
        if (title.contains("/")) {
            subTitle = title.substring(0, title.lastIndexOf("/"));
            button.setText(subTitle);
        } else button.setText(title);
        button.setPreferredSize(new Dimension(200, 20));
        button.setMinimumSize(new Dimension(200, 20));
        button.setMaximumSize(new Dimension(200, 20));
        makeButton();
        }

    }

    private void makeButton() {
        GridBagConstraints gbc;
        int i = 0;
        for (JLabel button : buttonVector) {
            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.anchor = GridBagConstraints.NORTH;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 1.0;
            gbc.insets = new Insets(0, 0, 0, 0);
            //gbc.weighty = 1.0;
            buttonPanel.add(button, gbc);
            buttonPanel.revalidate();

            i++;
        }
        menuController.setActiveFrameMenu(this);
    }

    public void removeFrameButton(String name) {
        buttonPanel.remove(buttonMap.get(name));
        buttonVector.remove(buttonMap.get(name));
        buttonMap.remove(name);
        buttonPanel.revalidate();
    }

    public void removeAllButtons() {
        buttonVector.clear();
        buttonMap.clear();
        buttonPanel.removeAll();
        buttonPanel.revalidate();
    }

    public void activateFrameButton(String activeFrameName) {
//        String s = buttonMap.get(activeFrameName).getText();
        //String sub = s.replaceFirst("<<<<","");
        //buttonMap.get(activeFrameName).setText(s + " <<<<");
        buttonMap.get(activeFrameName).setForeground(Color.BLUE);
        activeButtonName = activeFrameName;
        //buttonVector.elementAt(buttonMap.get(activeFrameName)).setText(s + " <<<<");        
    }

    public String getActiveButtonName() {
        return activeButtonName;
    }

    public void setActiveButtonName(String name) {
        this.activeButtonName = name;
        setInactiveColor();
    }

    public void setInactiveColor() {
        for (JLabel label : buttonVector) {
            if (!getActiveButtonName().equals(label.getName())) {
                label.setForeground(Color.BLACK);
                String s = label.getText().replaceAll("<html><u>", "");
                String s1 = s.replaceAll("</u></html>", "");
                label.setText(s1);
            } else {
                label.setForeground(Color.BLUE);
                String s = label.getText();
                label.setText("<html><u>" + s + "</u></html>");
            }
        }
    }
}
