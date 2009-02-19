package org.en.tealEye.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.en.tealEye.framework.BeanTableModel;
import org.en.tealEye.framework.TransactionWorker;
import org.en.tealEye.guiExt.ExtPanel.ExtJEditPanel;
import org.en.tealEye.guiMain.*;
import org.en.tealEye.guiPanels.applicationLogicPanels.ShowGroups;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 24.03.2008
 * Time: 21:18:37
 */
public class ShowGroupsController extends PanelController {
    private static final Log log = LogFactory.getLog(ShowGroupsController.class);

    private ShowGroups showGroups;

    private final Hypervisor h;


    public ShowGroupsController(MainAppFrame mainApp, Hypervisor h) {
        super(mainApp);
//        this.mainApp.addPropertyChangeListener(this); // // RSt: entfernt
        this.h = h;
    }

    public void actionPerformed(ActionEvent e) {
        if (isUpdateInProgress()) return; // z.zt. moeglicherweise optional

        JComponent obj = (JComponent) e.getSource();
        if (obj instanceof JComboBox) {
            if (obj.getName().equals("Combo_LigaMitLeer")) {
                DartComponentRegistry.getInstance()
                        .setComboBoxModel(showGroups.getSpielort(), showGroups);
            }
        } else if (obj instanceof JButton) {
            String ac = obj.getName();

            if (ac.equals("sortieren")) {
                DartComponentRegistry.getInstance()
                        .setTableModel(showGroups.getPanelTable(), showGroups);
                mainApp.setTaskbarTask("Gruppen sortiert");
            } else if (ac.equals("löschen")) {
                new DeleteThreadWorker(this, showGroups, mainApp).execute();
            }
        }
    }

    public void setShowPanel(ShowGroups showGroups) {
        this.showGroups = showGroups;
    }

    public void keyTyped(KeyEvent e) {
        // do nothing
    }

    public void keyPressed(KeyEvent e) {
        // do nothing
    }

    public void keyReleased(KeyEvent e) {
        // do nothing
    }

    public void mouseClicked(MouseEvent e) {
        Object obj = e.getSource();

        if (obj instanceof JTable) {
            if (e.getClickCount() == 2) {
                //mainApp.insertInternalFrame("CreateGroup", false);
                Object obje = ((BeanTableModel) ((JTable) obj).getModel())
                        .getObject(((JTable) obj).getSelectedRow());
                try {
                    JPanel p = h.showPanel("CreateGroup");
                    TransactionWorker instance = new EditThreadWorker(
                            ((ExtJEditPanel) p).getPanelController(), (ExtJEditPanel) p, obje,
                            mainApp);
                    instance.addPropertyChangeListener(this);
                    instance.execute();
//                    instance.get();
                } catch (Exception e1) {
                    mainApp.setTaskbarTask("WindowFehler: " + e1.getMessage());
                    log.error(e1.getMessage(), e1);
                }
            }
        }
    }

    protected void initiateRefreshEvent() {
        // do nothing
    }
}