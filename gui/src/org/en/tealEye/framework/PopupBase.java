package org.en.tealEye.framework;

import org.en.tealEye.controller.gui.MenuController;
import org.en.tealEye.controller.TeamController;
import org.en.tealEye.controller.PanelController;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: stephan
 * Date: 06.05.2009
 * Time: 04:19:42
 * To change this template use File | Settings | File Templates.
 */
public class PopupBase extends JPopupMenu {

    private MenuController menuController;
    private PanelController teamController;

    public PopupBase(MenuController menuController, PanelController teamController, Component invoker, int x, int y, int mode) {
        this.menuController = menuController;
        this.teamController = teamController;
        this.addMouseListener(teamController);
        menuController.setPanelController(teamController);
        this.setSelectionMode(mode);
        this.show(invoker, x,y);
    }

    private void initComponents(){



    }

    public void setSelectionMode(int mode){
        JMenuItem menuItem;
        switch(mode){
            case 0: menuItem = new JMenuItem("Team bearbeiten"); //Team
                    menuItem.setActionCommand("pum_EditTeam");
                    menuItem.addActionListener(menuController);
                    this.add(menuItem);
                    break;
            case 1: menuItem = new JMenuItem("Aufsteller bearbeiten");//Aufsteller
                    menuItem.setActionCommand("pum_EditVendor");
                    menuItem.addActionListener(menuController);
                    this.add(menuItem);
                    break;
            case 2: menuItem = new JMenuItem("Location bearbeiten");//Location
                    menuItem.setActionCommand("pum_EditLocation");
                    menuItem.addActionListener(menuController);
                    this.add(menuItem);
                    break;
            case 3: menuItem = new JMenuItem("Aufsteller bearbeiten");//Aufsteller & Location
                    menuItem.setActionCommand("pum_EditVendor");
                    menuItem.addActionListener(menuController);
                    this.add(menuItem);
                    menuItem = new JMenuItem("Location bearbeiten");
                    menuItem.setActionCommand("pum_EditLocation");
                    menuItem.addActionListener(menuController);
                    this.add(menuItem);
                    break;
            case 4: menuItem = new JMenuItem("Team bearbeiten");//Team & Location
                    menuItem.setActionCommand("pum_EditTeam");
                    menuItem.addActionListener(menuController);
                    this.add(menuItem);
                    menuItem = new JMenuItem("Location bearbeiten");
                    menuItem.setActionCommand("pum_EditLocation");
                    menuItem.addActionListener(menuController);
                    this.add(menuItem);
                    break;


        }
    }


}
