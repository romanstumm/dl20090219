package org.en.tealEye.framework;

import org.en.tealEye.controller.PanelController;
import org.en.tealEye.controller.gui.MenuController;

import javax.swing.*;
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

    public static enum MODE {
        Team, Aufsteller_and_Location, Team_and_Location, Location, Aufsteller
    }

    public PopupBase(MenuController menuController, PanelController teamController,
                     Component invoker, int x, int y, MODE mode) {
        this.menuController = menuController;
        this.teamController = teamController;
        this.addMouseListener(teamController);
        menuController.setPanelController(teamController);
        this.setSelectionMode(mode);
        this.show(invoker, x, y);
    }

    protected void setSelectionMode(MODE mode) {
        JMenuItem menuItem;
        switch (mode) {
            case Team:
                menuItem = new JMenuItem("Team bearbeiten"); //Team
                menuItem.setActionCommand("pum_EditTeam");
                menuItem.addActionListener(menuController);
                this.add(menuItem);
                break;
            case Aufsteller:
                menuItem = new JMenuItem("Aufsteller bearbeiten");//Aufsteller
                menuItem.setActionCommand("pum_EditVendor");
                menuItem.addActionListener(menuController);
                this.add(menuItem);
                break;
            case Location:
                menuItem = new JMenuItem("Spielort bearbeiten");//Location
                menuItem.setActionCommand("pum_EditLocation");
                menuItem.addActionListener(menuController);
                this.add(menuItem);
                break;
            case Aufsteller_and_Location:
                menuItem = new JMenuItem("Aufsteller bearbeiten");//Aufsteller & Location
                menuItem.setActionCommand("pum_EditVendor");
                menuItem.addActionListener(menuController);
                this.add(menuItem);
                menuItem = new JMenuItem("Spielort bearbeiten");
                menuItem.setActionCommand("pum_EditLocation");
                menuItem.addActionListener(menuController);
                this.add(menuItem);
                break;
            case Team_and_Location:
                menuItem = new JMenuItem("Team bearbeiten");//Team & Location
                menuItem.setActionCommand("pum_EditTeam");
                menuItem.addActionListener(menuController);
                this.add(menuItem);
                menuItem = new JMenuItem("Spielort bearbeiten");
                menuItem.setActionCommand("pum_EditLocation");
                menuItem.addActionListener(menuController);
                this.add(menuItem);
                break;


        }
    }


}
