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
        JMenu aboutMenu = new JMenu("Über");

        JMenuItem item;

        dateiMenu.add(new JMenuItem("Drucken"));
        dateiMenu.addSeparator();
        // - - - - - - - - - - - -

        JMenu menu = new JMenu("Import von...");
        dateiMenu.add(menu);

        menu.add(item = new JMenuItem("Excel (alles)"));
        item.setActionCommand("MENU_Excelimport");

        menu.add(item = new JMenuItem("vfs/dBase (Spielorte, Aufsteller)"));
        item.setActionCommand("MENU_Datenabgleich");

        menu.add(item = new JMenuItem("CSV-Dateien (Standorte, Aufsteller)"));
        item.setActionCommand("MENU_Datenimport");

        addMenuController(menu);
        // - - - - - - - - - - - -
        menu = new JMenu("Export nach...");
        dateiMenu.add(menu);

        menu.add(item = new JMenuItem("Excel (alles)"));
        item.setActionCommand("MENU_Excelexport");

        menu.add(item = new JMenuItem("vfs/dBase (Teams, Gruppen)"));
        item.setActionCommand("MENU_Datenexport");
        dateiMenu.addSeparator();
        addMenuController(menu);
        // - - - - - - - - - - - -
        dateiMenu.add(new JMenuItem("Beenden"));

        aboutMenu.add(item = new JMenuItem("Über dieses Programm"));
        item.setActionCommand("MENU_About");

        configMenu.add(item = new JMenuItem("Grundeinstellungen"));
        item.setActionCommand("MENU_Grundeinstellungen");
        configMenu.add(item = new JMenuItem("Sonderwünsche Löschen"));
        item.setActionCommand("MENU_DeleteWunsch");
        windowsMenu.add(new JMenuItem("Fenster Maximieren"));
        windowsMenu.add(new JMenuItem("Fenster Minimieren"));
        windowsMenu.add(new JMenuItem("Fenster Normalisieren"));
        windowsMenu.add(new JMenuItem("Fenster Schliessen"));
        windowsMenu.add(item = new JCheckBoxMenuItem("Hauptmenu ausblenden"));
        item.setActionCommand("CB_Hide_FloatPanel");
        windowsMenu.add(new JSeparator());
        windowsMenu.add(item = new JMenuItem("Info über Ligaverwaltung..."));
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
        for (int i = 0; i < dateiMenu.getMenuComponentCount(); i++) {
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
