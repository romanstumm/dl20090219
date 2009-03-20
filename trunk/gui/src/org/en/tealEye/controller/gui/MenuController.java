package org.en.tealEye.controller.gui;

import de.liga.dart.Application;
import de.liga.dart.common.service.DartService;
import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.ligateam.service.LigateamService;
import org.en.tealEye.framework.LigaChooser;
import org.en.tealEye.framework.SwingUtils;
import org.en.tealEye.guiMain.FloatPanel.ActiveFrameMenu;
import org.en.tealEye.guiMain.Hypervisor;
import org.en.tealEye.guiMain.IO_ACTION;
import org.en.tealEye.guiMain.ImportThreadWorker;
import org.en.tealEye.guiMain.MainAppFrame;
import org.en.tealEye.guiPanels.ConfigPanels.MainConfigFrame;
import org.en.tealEye.guiPanels.applicationLogicPanels.AboutFrame;
import org.en.tealEye.printing.controller.PrintingController;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 14.11.2007
 * Time: 03:15:34
 */
public class MenuController implements ActionListener, MouseListener, GuiController {

    private final MainAppFrame mainAppFrame;
    private final Hypervisor h;
    private final Map<String, String> actionPanelMap = new HashMap(8);
    private ActiveFrameMenu afm;

    public MenuController(MainAppFrame mainAppFrame, Hypervisor h) {
        this.mainAppFrame = mainAppFrame;
        this.h = h;
        initActionPanelMap();
    }

    private void initActionPanelMap() {
        actionPanelMap.put("Gruppe anlegen", "CreateGroup");
        actionPanelMap.put("Team anlegen", "CreateTeam");
        actionPanelMap.put("Spielort anlegen", "CreateLocation");
        actionPanelMap.put("Aufsteller anlegen", "CreateVendor");
        actionPanelMap.put("Gruppen anzeigen", "ShowGroups");
        actionPanelMap.put("Teams anzeigen", "ShowTeams");
        actionPanelMap.put("Spielorte anzeigen", "ShowLocations");
        actionPanelMap.put("Aufsteller anzeigen", "ShowVendors");
    }

    public String getFrameTitle(String frameName) {
        return mainAppFrame.getFrameMap().get(frameName).getTitle();
    }

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        Object obj = e.getSource();
        ImportThreadWorker instance;
        if (action.equals("Drucken")) {
            new PrintingController(mainAppFrame);
        } else if (action.startsWith("MENU_Datenabgleich")) {
            instance = new ImportThreadWorker(mainAppFrame, IO_ACTION.DBaseImport);
            instance.execute();
        } else if (action.startsWith("MENU_Datenexport")) {
            instance = new ImportThreadWorker(mainAppFrame, IO_ACTION.DBaseExport);
            instance.execute();
        } else if (action.startsWith("MENU_Datenimport")) {
            instance = new ImportThreadWorker(mainAppFrame, IO_ACTION.CsvImport);
            instance.execute();
        } else if (action.startsWith("MENU_Excelimport")) {
            instance = new ImportThreadWorker(mainAppFrame, IO_ACTION.ExcelImport);
            instance.execute();
        } else if (action.startsWith("MENU_Excelexport")) {
            instance = new ImportThreadWorker(mainAppFrame, IO_ACTION.ExcelExport);
            instance.execute();
        } else if (action.startsWith("MENU_Info")) {
            SwingUtils.createOkDialog(mainAppFrame,
                    Application.APPLICATION_NAME + " " + Application.APPLICATION_VERSION +
                            "\nAutoren: Stephan Pudras (pudras@gmx.net), Roman Stumm (roman.stumm@gmx.de)" +
                            "\nGruppensortierung und Verwaltung von Dartligen (2007-2009)" +
                            "\nLizenz: " + mainAppFrame.getLicense().getInfoMessage() +
                            "\nWebsite: --",
                    "Info über...");
        } else if (action.equals("Beenden")) {
            // und tschuess...
            ServiceFactory.get(DartService.class).shutdown();
            System.exit(0);
        } else if (action.equals("MENU_Grundeinstellungen")) {
            new MainConfigFrame(mainAppFrame);
        } else if (action.equals("MENU_About")) {
            new AboutFrame();
        } else if (action.equals("MENU_DeleteWunsch")) {
            LigaChooser chooser = new LigaChooser("Team Sonderwünsche löschen", mainAppFrame);
            if (chooser.choose()) {
                LigateamService service = ServiceFactory.get(LigateamService.class);
                service.deleteAllLigateamWunsch(chooser.getSelectedLiga());
                if (chooser.getSelectedLiga() != null) {
                    mainAppFrame.setMessage("Sonderwünsche von Liga " +
                            chooser.getSelectedLiga().getLigaName() + " gelöscht");
                } else {
                    mainAppFrame.setMessage("Sonderwünsche aller Ligen gelöscht");
                }
            } else {
                mainAppFrame.setMessage("Sonderwünsche löschen - Abbruch!");
            }
        } else if (action.equals("Fenster Minimieren")) {
            mainAppFrame.minimizeAllFrames();
        } else if (action.equals("Fenster Maximieren")) {
            mainAppFrame.maximizeAllFrames();
        } else if (action.equals("Fenster Schliessen")) {
            mainAppFrame.closeAllFrames();
        } else if (action.equals("Fenster Normalisieren")) {
            mainAppFrame.normalizeAllFrames();
        } else if (action.equals("CB_Hide_FloatPanel")) {
            if (obj instanceof JCheckBoxMenuItem) {
                JCheckBoxMenuItem cb = (JCheckBoxMenuItem) obj;
                if (cb.isSelected()) {
                    mainAppFrame.hideFloatingToolbar();
                } else if (!cb.isSelected()) {
                    mainAppFrame.showFloatingToolbar();
                }
            }

        } else if (actionPanelMap.containsKey(action)) {
            h.showPanel(actionPanelMap.get(action));
            if (afm != null) {
                afm.activateFrameButton(mainAppFrame.getActiveFrame());
                afm.setInactiveColor();
            }
        }
        /* else if (obj instanceof JButton &&
               mainAppFrame.getFrameMap().containsKey(action)) {
           mainAppFrame.insertInternalFrame(action, true);
       } */
    }

    public void mouseClicked(MouseEvent e) {
        Object obj = e.getSource();
        if (obj instanceof JTree) {
            JTree tree = (JTree) e.getSource();
            if (tree.getLastSelectedPathComponent() == null) return; // fixed NPE
            String action = (String)
                    ((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).getUserObject();
            String name = actionPanelMap.get(action);
            if (name != null) h.showPanel(name);
        }
        if (obj instanceof JLabel) {
            //mainAppFrame.insertInternalFrame(((JLabel)obj).getName(), true);
            h.showPanel(((JLabel) obj).getName());
            mainAppFrame.getActiveFrame();
            if (afm != null) {
                afm.activateFrameButton(mainAppFrame.getActiveFrame());
                afm.setInactiveColor();
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        Object obj = e.getSource();
        if (obj instanceof JLabel) {
            JLabel label = (JLabel) obj;
            label.setForeground(Color.GREEN);
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        }   
    }

    public void mouseReleased(MouseEvent e) {
        Object obj = e.getSource();
        if (obj instanceof JLabel) {
            JLabel label = (JLabel) obj;
            label.setForeground(Color.BLUE);
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    public void mouseEntered(MouseEvent e) {
        Object obj = e.getSource();
        if (obj instanceof JLabel) {
            JLabel label = (JLabel) obj;
            label.setForeground(Color.BLUE);
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    public void mouseExited(MouseEvent e) {
        Object obj = e.getSource();
        if (obj instanceof JLabel) {
            JLabel label = (JLabel) obj;
            label.setForeground(Color.BLACK);
            label.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            if (afm != null) afm.setInactiveColor();
        }
    }

    public void setActiveFrameMenu(ActiveFrameMenu afm) {
        this.afm = afm;
    }

    public void setActiveFrameName(String name) {
        afm.setActiveButtonName(name);
    }
}
