package org.en.tealEye.guiMain;

import org.en.tealEye.controller.gui.MenuController;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class MainMenu extends JMenuBar {

    private final MenuController menuController;

    public MainMenu(MenuController menuController) {
        super();
        this.menuController = menuController;
        initComponents();

    }

    private void initComponents() {

        JMenu dateiMenu = new JMenu("Datei");
        dateiMenu.setMnemonic('D');
        JMenu configMenu = new JMenu("Einstellungen");
        configMenu.setMnemonic('E');
        JMenu tasksMenu = new JMenu("Bearbeiten");
        tasksMenu.setMnemonic('B');
        JMenu windowsMenu = new JMenu("Fenster");
        windowsMenu.setMnemonic('F');
        JMenu aboutMenu = new JMenu("Über");
        aboutMenu.setMnemonic('Ü');

        JMenuItem item;

        dateiMenu.add(item = new JMenuItem("Drucken"));
        item.setMnemonic('D');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
        dateiMenu.addSeparator();
        // - - - - - - - - - - - -

        JMenu menu = new JMenu("Import von...");
        dateiMenu.add(menu);

        menu.add(item = new JMenuItem("Excel (alles)"));
        item.setActionCommand("MENU_Excelimport");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_MASK));

        menu.add(item = new JMenuItem("vfs/dBase (Spielorte, Aufsteller)"));
        item.setActionCommand("MENU_Datenabgleich");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_MASK));

        menu.add(item = new JMenuItem("CSV-Dateien (Standorte, Aufsteller)"));
        item.setActionCommand("MENU_Datenimport");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.CTRL_MASK));

        addMenuController(menu);
        // - - - - - - - - - - - -
        menu = new JMenu("Export nach...");
        dateiMenu.add(menu);

        menu.add(item = new JMenuItem("Excel (alles)"));
        item.setActionCommand("MENU_Excelexport");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.CTRL_MASK));

        menu.add(item = new JMenuItem("vfs/dBase (Teams, Gruppen)"));
        item.setActionCommand("MENU_Datenexport");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, InputEvent.CTRL_MASK));
        addMenuController(menu);

        menu.add(item = new JMenuItem("Excel (Rangliste aus vfs)"));
        item.setActionCommand("MENU_Rangliste");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));

        menu = new JMenu("HTML-Upload (vfs Dateien zur Website)...");
        dateiMenu.add(menu);

        menu.add(item = new JMenuItem("Ranglisten"));
        item.setActionCommand("MENU_HTML_Ranglisten");

        menu.add(item = new JMenuItem("Spielpläne"));
        item.setActionCommand("MENU_HTML_Spielplaene");
        addMenuController(menu);


        // - - - - - - - - - - - -
        dateiMenu.addSeparator();
        dateiMenu.add(new JMenuItem("Beenden"));


        configMenu.add(item = new JMenuItem("Grundeinstellungen"));
        item.setActionCommand("MENU_Grundeinstellungen");
        configMenu.add(item = new JMenuItem("Sonderwünsche Löschen"));
        item.setActionCommand("MENU_DeleteWunsch");
        windowsMenu.add(item = new JMenuItem("Fenster Maximieren"));
        item.setMnemonic('x');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        windowsMenu.add(item = new JMenuItem("Fenster Minimieren"));
        item.setMnemonic('i');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK));
        windowsMenu.add(item = new JMenuItem("Fenster Normalisieren"));
        item.setMnemonic('N');
        item.setDisplayedMnemonicIndex(8);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        windowsMenu.add(new JMenuItem("Fenster Schliessen"));
        windowsMenu.add(item = new JCheckBoxMenuItem("Hauptmenu ausblenden"));
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
        item.setActionCommand("CB_Hide_FloatPanel");
//        windowsMenu.add(new JSeparator());
//        windowsMenu.add(new JMenu("Offene Fenster"));

        tasksMenu.add(item = new JMenuItem("Gruppe anlegen"));
        item.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.ALT_MASK | InputEvent.CTRL_MASK));
        item.setMnemonic('G');
        tasksMenu.add(item = new JMenuItem("Team anlegen"));
        item.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.ALT_MASK | InputEvent.CTRL_MASK));
        item.setMnemonic('T');
        tasksMenu.add(item = new JMenuItem("Spielort anlegen"));
        item.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK | InputEvent.CTRL_MASK));
        item.setMnemonic('S');
        tasksMenu.add(item = new JMenuItem("Aufsteller anlegen"));
        item.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_MASK | InputEvent.CTRL_MASK));
        item.setMnemonic('A');
        tasksMenu.add(new JSeparator());
        tasksMenu.add(item = new JMenuItem("Gruppen anzeigen"));
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK));
        item.setMnemonic('G');
        tasksMenu.add(item = new JMenuItem("Teams anzeigen"));
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK));
        item.setMnemonic('T');
        tasksMenu.add(item = new JMenuItem("Spielorte anzeigen"));
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        item.setMnemonic('S');
        tasksMenu.add(item = new JMenuItem("Aufsteller anzeigen"));
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        item.setMnemonic('A');

        aboutMenu.add(item = new JMenuItem("Credits..."));
        item.setActionCommand("MENU_About");
        aboutMenu.add(new JSeparator());
        aboutMenu.add(item = new JMenuItem("Info über Ligaverwaltung..."));
        item.setActionCommand("MENU_Info");
        addMenuController(dateiMenu);
        addMenuController(configMenu);
        addMenuController(tasksMenu);
        addMenuController(windowsMenu);
        addMenuController(aboutMenu);

        dateiMenu.getItem(0).setEnabled(false);

        this.add(dateiMenu);
        this.add(configMenu);
        this.add(tasksMenu);
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
