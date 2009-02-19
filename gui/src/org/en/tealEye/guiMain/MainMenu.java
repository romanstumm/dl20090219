package org.en.tealEye.guiMain;

import org.en.tealEye.controller.gui.MenuController;

import javax.swing.*;

public class MainMenu extends JMenuBar {

    private final MenuController menuController;

    public MainMenu(MenuController menuController) {
        super();
        this.menuController = menuController;
        initComponents();

    }

    private void initComponents() {

        JMenu dateiMenu = new JMenu("Datei");
        JMenu configMenu = new JMenu("Einstellungen");
        JMenu windowsMenu = new JMenu("Fenster");
        JMenu aboutMenu = new JMenu("�ber");

        JMenuItem item;

        dateiMenu.add(new JMenuItem("Drucken"));
        dateiMenu.addSeparator();
        dateiMenu.add(item = new JMenuItem("Datenimport (csv Standorte, Aufsteller)"));
        item.setActionCommand("MENU_Datenimport");
        dateiMenu.add(item = new JMenuItem("Datenimport (dBase Spielorte, Aufsteller)"));
        item.setActionCommand("MENU_Datenabgleich");
        dateiMenu.addSeparator();
        dateiMenu.add(item = new JMenuItem("Datenexport (dBase Teams, Gruppen)"));
        item.setActionCommand("MENU_Datenexport");
        dateiMenu.addSeparator();
        dateiMenu.add(new JMenuItem("Beenden"));

        aboutMenu.add(item = new JMenuItem("�ber dieses Programm"));
        item.setActionCommand("MENU_About");

        configMenu.add(item = new JMenuItem("Grundeinstellungen"));
        item.setActionCommand("MENU_Grundeinstellungen");
        configMenu.add(item = new JMenuItem("Sonderw�nsche L�schen"));
        item.setActionCommand("MENU_DeleteWunsch");
        windowsMenu.add(new JMenuItem("Fenster Maximieren"));
        windowsMenu.add(new JMenuItem("Fenster Minimieren"));
        windowsMenu.add(new JMenuItem("Fenster Normalisieren"));
        windowsMenu.add(new JMenuItem("Fenster Schliessen"));
        windowsMenu.add(item = new JCheckBoxMenuItem("Hauptmenu ausblenden"));
        item.setActionCommand("CB_Hide_FloatPanel");
        windowsMenu.add(new JSeparator());
        windowsMenu.add(item = new JMenuItem("Info �ber Ligaverwaltung..."));
        item.setActionCommand("MENU_Info");
        addMenuController(dateiMenu);
        addMenuController(configMenu);
        addMenuController(windowsMenu);
        addMenuController(aboutMenu);
        dateiMenu.getItem(0).setEnabled(false);

        this.add(dateiMenu);
        this.add(configMenu);
        this.add(windowsMenu);
        this.add(aboutMenu);
        setPrintingDisabled();
    }

    private void addMenuController(JMenu dateiMenu) {
        for (int i = 0; i < dateiMenu.getItemCount(); i++) {
            JMenuItem item = dateiMenu.getItem(i);
            if (item != null) {
                item.addActionListener(menuController);
            }
        }
    }

    public void setPrintingEnabled() {
        this.getMenu(0).getMenuComponent(0).setEnabled(true);
    }

    public void setPrintingDisabled() {
        this.getMenu(0).getMenuComponent(0).setEnabled(false);
    }
}
