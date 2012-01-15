package org.en.tealEye.controller.gui;

import de.liga.dart.Application;
import de.liga.dart.common.service.DartService;
import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.ligateam.model.TeamWunsch;
import de.liga.dart.ligateam.service.LigateamService;
import de.liga.dart.model.Automatenaufsteller;
import de.liga.dart.model.Ligateam;
import de.liga.dart.model.Spielort;
import org.en.tealEye.controller.PanelController;
import org.en.tealEye.framework.BeanTableModel;
import org.en.tealEye.framework.LigaChooser;
import org.en.tealEye.framework.SwingUtils;
import org.en.tealEye.framework.TransactionWorker;
import org.en.tealEye.guiExt.ExtPanel.ExtJEditPanel;
import org.en.tealEye.guiMain.*;
import org.en.tealEye.guiMain.FloatPanel.ActiveFrameMenu;
import org.en.tealEye.guiPanels.ConfigPanels.MainConfigFrame;
import org.en.tealEye.guiPanels.applicationLogicPanels.AboutFrame;
import org.en.tealEye.guiPanels.applicationLogicPanels.CreateGroup;
import org.en.tealEye.printing.controller.PrintingController;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 14.11.2007
 * Time: 03:15:34
 */
public class MenuController extends PanelController {

    private final MainAppFrame mainAppFrame;
    private final Hypervisor h;
    private final Map<String, String> actionPanelMap = new HashMap(8);
    private ActiveFrameMenu afm;
    private PanelController pC;

    public MenuController(MainAppFrame mainAppFrame, Hypervisor h) {
        super(mainAppFrame);
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
        } else if (action.equalsIgnoreCase("MENU_Datenimport_Spielorte_Aufsteller")) {
            instance = new ImportThreadWorker(mainAppFrame, IO_ACTION.DBaseImportSpielorteAufsteller);
            instance.execute();
        } else if (action.equalsIgnoreCase("MENU_Spielort_Aufsteller_Datenexport")) {
            instance = new ImportThreadWorker(mainAppFrame, IO_ACTION.DBaseExportSpielorteAufsteller);
            instance.execute();
        } else if (action.equalsIgnoreCase("MENU_Teams_Gruppen_Datenexport")) {
            instance = new ImportThreadWorker(mainAppFrame, IO_ACTION.DBaseExportTeamsGruppen);
            instance.execute();
        } else if (action.equalsIgnoreCase("MENU_Datenimport")) {
            instance = new ImportThreadWorker(mainAppFrame, IO_ACTION.CsvImport);
            instance.execute();
        } else if (action.equalsIgnoreCase("MENU_Excelimport")) {
            instance = new ImportThreadWorker(mainAppFrame, IO_ACTION.ExcelImport);
            instance.execute();
        } else if (action.equalsIgnoreCase("MENU_Excelexport")) {
            instance = new ImportThreadWorker(mainAppFrame, IO_ACTION.ExcelExport);
            instance.execute();
        } else if (action.equalsIgnoreCase("MENU_Rangliste")) {
            instance = new ImportThreadWorker(mainAppFrame, IO_ACTION.ExcelRangliste);
            instance.execute();
        } else if (action.equalsIgnoreCase("MENU_HTML_Ranglisten")) {
            instance = new ImportThreadWorker(mainAppFrame, IO_ACTION.HTMLRanglisten);
            instance.execute();
        } else if (action.equalsIgnoreCase("MENU_HTML_Spielplaene")) {
            instance = new ImportThreadWorker(mainAppFrame, IO_ACTION.HTMLSpielplaene);
            instance.execute();
        } else if (action.startsWith("MENU_Info")) {
            SwingUtils.createOkDialog(mainAppFrame,
                    Application.APPLICATION_NAME + " " + Application.APPLICATION_VERSION +
                            "\nAutoren: Stephan Pudras (pudras@gmx.net), Roman Stumm (roman.stumm@gmx.de)" +
                            "\nGruppensortierung und Verwaltung von Dartligen (2007-2011)" +
                            "\nLizenz: " + mainAppFrame.getLicense().getInfoMessage(),
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

        } else if (action.startsWith("MENU_help")) {
//                new HelpMain();
        } else if (action.startsWith("pum_EditTeam")) {
            Component comp = pC.getPopupSource();
            Ligateam ligateam = null;
            if ("List_possibleTeamsForWunschList".equals(comp.getName())) {
                ligateam = (Ligateam) ((JList) comp).getSelectedValue();
            } else if ("List_TeamWunschList".equals(comp.getName())) {
                TeamWunsch wunsch = (TeamWunsch) ((JList) comp).getSelectedValue();
                if (wunsch != null) {
                    ligateam = wunsch.getOtherTeam();
                }
            } else if ("List_LigateamsNochFrei".equals(comp.getName())) {
                ligateam = (Ligateam) ((JList) comp).getSelectedValue();
            } else if ("Table_Gruppe".equals(comp.getName())) {
                JTable table = (JTable) comp;
                ligateam =
                        (Ligateam) table.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), CreateGroup.C_TEAMNAME);
            }
            if (ligateam != null) {
                try {
                    JPanel p = h.showPanel("CreateTeam");
                    TransactionWorker work = new EditThreadWorker(
                            this, (ExtJEditPanel) p, ligateam, mainAppFrame);
                    work.addPropertyChangeListener(this);
                    work.execute();
                } catch (Exception e1) {
                    mainApp.setTaskbarTask("WindowFehler: " + e1.getMessage());
                    log.error(e1.getMessage(), e1);
                }
            }
        } else if (action.startsWith("pum_EditLocation")) {
            Component comp = pC.getPopupSource();
            Spielort location = null;
            if ("Table_Gruppe".equals(comp.getName())) {
                JTable table = (JTable) comp;
                Ligateam ligateam =
                        (Ligateam) table.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), CreateGroup.C_TEAMNAME);
                if (ligateam != null) location = ligateam.getSpielort();
            } else if (comp instanceof JTable) {
                JTable table = (JTable) comp;
                location = (Spielort)
                        ((BeanTableModel) table.getModel()).getObject(table.convertRowIndexToModel(table.getSelectedRow()));
            }
            if (location != null) {
                try {
                    JPanel p = h.showPanel("CreateLocation");
                    TransactionWorker work = new EditThreadWorker(
                            this, (ExtJEditPanel) p, location, mainAppFrame);
                    work.addPropertyChangeListener(this);
                    work.execute();
                } catch (Exception e1) {
                    mainApp.setTaskbarTask("WindowFehler: " + e1.getMessage());
                    log.error(e1.getMessage(), e1);
                }
            }
        } else if (action.startsWith("pum_EditVendor")) {
            JTable comp = (JTable) pC.getPopupSource();
            Automatenaufsteller aufsteller = null;
            if ("Table_SpielortProLiga".equals(comp.getName())) {
                Spielort spielort =
                        (Spielort) ((BeanTableModel) comp.getModel())
                                .getObject(comp.convertRowIndexToModel(comp.getSelectedRow()));
                if (spielort != null) aufsteller = spielort.getAutomatenaufsteller();
            } else {
                aufsteller = (Automatenaufsteller) ((BeanTableModel) comp.getModel())
                        .getObject(comp.convertRowIndexToModel(comp.getSelectedRow()));
            }
            if (aufsteller != null) {
                try {
                    JPanel p = h.showPanel("CreateVendor");
                    TransactionWorker work = new EditThreadWorker(
                            this, (ExtJEditPanel) p, aufsteller, mainAppFrame);
                    work.addPropertyChangeListener(this);
                    work.execute();
                } catch (Exception e1) {
                    mainApp.setTaskbarTask("WindowFehler: " + e1.getMessage());
                    log.error(e1.getMessage(), e1);
                }
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

    protected void initiateRefreshEvent() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setActiveFrameMenu(ActiveFrameMenu afm) {
        this.afm = afm;
    }

    public void setActiveFrameName(String name) {
        afm.setActiveButtonName(name);
    }

    public void keyTyped(KeyEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void keyPressed(KeyEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void keyReleased(KeyEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setPanelController(PanelController pC) {
        this.pC = pC;
    }
}
