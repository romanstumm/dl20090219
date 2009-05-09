package org.en.tealEye.controller;

import de.liga.dart.ligateam.model.TeamWunsch;
import de.liga.dart.ligateam.service.LigateamService;
import de.liga.dart.model.Ligateam;
import de.liga.dart.model.Spielort;
import de.liga.dart.common.service.ServiceFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.en.tealEye.framework.BeanTableModel;
import org.en.tealEye.framework.SwingUtils;
import org.en.tealEye.framework.TransactionWorker;
import org.en.tealEye.framework.PopupBase;
import org.en.tealEye.guiExt.ExtPanel.ExtJEditPanel;
import org.en.tealEye.guiExt.ExtPanel.ExtJTablePanel;
import org.en.tealEye.guiExt.ExtPanel.ExtendedJPanelImpl;
import org.en.tealEye.guiExt.ExtPanel.JListExt;
import org.en.tealEye.guiMain.*;
import org.en.tealEye.guiPanels.applicationLogicPanels.CreateTeam;
import org.en.tealEye.guiPanels.applicationLogicPanels.ShowTeams;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.util.Collection;
import java.util.Map;
import java.util.List;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 14.11.2007
 * Time: 00:31:55
 */
public class TeamController extends PanelController {
    private static final Log log = LogFactory.getLog(TeamController.class);

    private CreateTeam createTeam;
    private ShowTeams showTeams;

    private final Hypervisor h;

    public TeamController(MainAppFrame mainApp, Hypervisor h) {
        super(mainApp);
//        this.mainApp.addPropertyChangeListener(this); // // RSt: entfernt
        this.mainApp.addKeyListener(this);
        this.h = h;

    }

    public void mouseClicked(MouseEvent e) {
        Object obj = e.getSource();
        if (obj instanceof JTable) {
            if (((JTable) obj).getName().equals("Table_Ligateam")) {

                if (e.getClickCount() == 2) {
                    //mainApp.insertInternalFrame("CreateTeam", false);
                    JPanel p = h.showPanel("CreateTeam");
                    Object obje = ((BeanTableModel) ((JTable) obj).getModel())
                            .getObject(((JTable) obj).getSelectedRow());
                    try {
                        TransactionWorker instance =
                                new EditThreadWorker(this, (ExtJEditPanel) p, obje, mainApp);
                        instance.addPropertyChangeListener(this);
                        instance.execute();
//                        instance.get();
                    } catch (Exception e1) {
                        mainApp.setTaskbarTask("WindowFehler: " + e1.getMessage());
                        log.error(e1.getMessage(), e1);
                    }
                }
            } else if (((JTable) obj).getName().equals("Table_SpielortProLiga")) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int y = e.getY();
                    int row = Math.round(y / ((JTable) obj).getRowHeight());

                    ((JTable) obj).setRowSelectionInterval(row, row);
                    this.setPopupSource(((JTable) obj));
                    PopupBase pum = new PopupBase(mainApp.getMenuController(),
                            this, e.getComponent(), e.getX(), e.getY(),
                            PopupBase.MODE.Aufsteller_and_Location);
                }
            } else if (((JTable) obj).getName().equals("Table_SpielortProLiga")) {
                if (e.getClickCount() == 2) {
                    CreateTeam panel = (CreateTeam) mainApp.getPanelMap().get("CreateTeam");
                    panel.setSpielortTextField();
                }
            }
        } else if (obj instanceof JList) {
            ((JList) obj).requestFocus();

            if (((JList) obj).getName().equals("List_possibleTeamsForWunschList") &&
                    ((JList) obj).getSelectedValue() != null) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    popupEditTeam(e, obj);
                    // this.setPopupSource(((JList) obj));
                } else if (e.getClickCount() == 2) {
                    moveTeamToWunschList();
                }
            } else if (((JList) obj).getName().equals("List_TeamWunschList") &&
                    ((JList) obj).getSelectedValue() != null) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    popupEditTeam(e, obj);
                } else if (e.getClickCount() == 2) {
                    removeTeamFromWunschList();
                }
            }
        }
    }

    private void popupEditTeam(MouseEvent e, Object obj) {
        int y = e.getY();
        int row = ((JList) obj).locationToIndex(e.getPoint());
        ((JList) obj).setSelectedIndex(row);
        this.setPopupSource(((JList) obj));
        PopupBase pum = new PopupBase(mainApp.getMenuController(), this, e.getComponent(),
                e.getX(), e.getY(), PopupBase.MODE.Team);
    }

    private void removeTeamFromWunschList() {
        JList jlistPool = createTeam.getPossibleTeamsForWunschList();
        JList jlistTarget = createTeam.getWunschListTeams();
        TeamWunsch obj =
                (TeamWunsch) jlistTarget.getModel().getElementAt(jlistTarget.getSelectedIndex());
        DefaultListModel dlm = (DefaultListModel) jlistPool.getModel();
        dlm.addElement(obj.getOtherTeam());

        DefaultListModel dlm2 = (DefaultListModel) jlistTarget.getModel();
        dlm2.remove(jlistTarget.getSelectedIndex());

        jlistPool.setSelectedIndex(dlm.getSize() - 1);
    }

    private void moveTeamToWunschList() {
        JList jlistPool = createTeam.getPossibleTeamsForWunschList();
        JList jlistTarget = createTeam.getWunschListTeams();
        Ligateam obj = (Ligateam) jlistPool.getModel().getElementAt(jlistPool.getSelectedIndex());
        DefaultListModel dlm = (DefaultListModel) jlistTarget.getModel();
        TeamWunsch entry = new TeamWunsch(obj);
        dlm.addElement(entry);

        DefaultListModel dlm2 = (DefaultListModel) jlistPool.getModel();
        dlm2.remove(jlistPool.getSelectedIndex());

        jlistTarget.setSelectedIndex(dlm.getSize() - 1);
    }

    public void actionPerformed(ActionEvent e) {
        if (isUpdateInProgress()) return; // z.zt. moeglicherweise optional

        JComponent obj = (JComponent) e.getSource();
        String ac = obj.getName();
        ExtendedJPanelImpl parent = SwingUtils.getExtendedJPanel(obj);
        if (obj instanceof JComboBox && StringUtils.isNotEmpty(ac)) {
            JComboBox combobox = (JComboBox) obj;
            if (ac.startsWith("Combo_Liga")) {
                if (parent == showTeams)
                    DartComponentRegistry.getInstance()
                            .setComboBoxModel(showTeams.getSpielort(), showTeams);
                if (parent == createTeam) {
                    DartComponentRegistry.getInstance()
                            .setTableModel(createTeam.getJTable1(), createTeam);
                    DartComponentRegistry.getInstance()
                            .setListModel(createTeam.getPossibleTeamsForWunschList(), createTeam);
                }
            } else if (ac.equals("Combo_WunschListLigaklasse")) {
                DartComponentRegistry.getInstance()
                        .setListModel(createTeam.getPossibleTeamsForWunschList(), createTeam);
            } else if (parent == createTeam && ac.equals("Combo_WunschArten")) {
                // die Wunschart beim gew?hlten Item anpassen
                Object[] wuensche = createTeam.getWunschListTeams().getSelectedValues();
                if (wuensche != null) for (Object aWuensche : wuensche) {
                    TeamWunsch wunsch = (TeamWunsch) aWuensche;
                    wunsch.setWunschArt(
                            TeamWunsch.valueOf(String.valueOf(combobox.getSelectedItem())));
                }
                createTeam.getWunschListTeams().repaint();
            }
        } else if (obj instanceof JButton) {
            if ("neu".equals(ac)) {
                createTeam.newModelEntity();
                createTeam.clearTextAreas();
                mainApp.setMessage("Neues Team");
                mainApp.commitRefresh(this);
            } else if ("speichern".equals(ac)) {
                new SaveThreadWorker(createTeam, mainApp).execute();
                mainApp.commitRefresh(this);
                createTeam.setTextfieldFocus();
            } else if ("abbrechen".equals(ac))
                mainApp.removeInternalFrame(parent.getName());
            else if ("sortieren".equals(ac)) {
                DartComponentRegistry.getInstance()
                        .setTableModel(showTeams.getPanelTable(), showTeams);
                mainApp.setTaskbarTask("Teams sortiert");
            } else if ("löschen".equals(ac)) {
                new DeleteThreadWorker(this, showTeams, mainApp).execute();
            } else if ("infos".equals(ac)) {
                ServiceFactory.runAsTransaction(new Runnable() {
                    public void run() {
                        Ligateam team = (Ligateam) createTeam.getModelEntity();
                        String infoText =
                                ServiceFactory.get(LigateamService.class).queryTeamInfos(team);
                        SwingUtils.createOkDialog(mainApp, infoText,
                                "Infos zu " + team.getTeamName());
                    }
                });
            }
        }
    }

    public void setCreatePanel(CreateTeam panel) {
        this.createTeam = panel;
    }

    public void setShowPanel(ShowTeams panel) {
        this.showTeams = panel;
    }


    public void initiateRefreshEvent() {
        Collection<ExtendedJPanelImpl> panels = mainApp.getPanelMap().values();
        for (ExtendedJPanelImpl panel : panels) {
            if (panel instanceof ExtJTablePanel)
                panel.getPanelController().refreshAndWait(panel);
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {


    }

    public void keyReleased(KeyEvent e) {

    }

}
