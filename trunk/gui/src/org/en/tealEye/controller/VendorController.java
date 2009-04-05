package org.en.tealEye.controller;

import org.en.tealEye.framework.BeanTableModel;
import org.en.tealEye.framework.TransactionWorker;
import org.en.tealEye.guiExt.ExtPanel.ExtJEditPanel;
import org.en.tealEye.guiExt.ExtPanel.ExtJTablePanel;
import org.en.tealEye.guiExt.ExtPanel.ExtendedJPanelImpl;
import org.en.tealEye.guiMain.*;
import org.en.tealEye.guiPanels.applicationLogicPanels.CreateVendor;
import org.en.tealEye.guiPanels.applicationLogicPanels.ShowVendors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Collection;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 14.11.2007
 * Time: 00:32:34
 */
public class VendorController extends PanelController {
    private static final Log log = LogFactory.getLog(VendorController.class);

    private CreateVendor createVendor;
    private ShowVendors showVendors;
    private final Hypervisor h;

    public VendorController(MainAppFrame mainApp, Hypervisor h) {
        super(mainApp);
//        this.mainApp.addPropertyChangeListener(this); // RSt: entfernt
        this.h = h;
    }

    public void mouseClicked(MouseEvent e) {
        Object obj = e.getSource();
        if (obj instanceof JTable) {
            if (e.getClickCount() == 2) {
                //mainApp.insertInternalFrame("CreateVendor", false);
                JPanel p = h.showPanel("CreateVendor");
                Object obje = ((BeanTableModel) ((JTable) obj).getModel())
                        .getObject(((JTable) obj).getSelectedRow());
                try {
                    TransactionWorker instance = new EditThreadWorker(
                            this, (ExtJEditPanel) p, obje, mainApp);
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

    public void actionPerformed(ActionEvent e) {
        if(isUpdateInProgress()) return; // z.zt. moeglicherweise optional
        
        Object obj = e.getSource();
        if (obj instanceof JButton) {
            String ac = ((JButton) obj).getName();
            String parentName = ((JButton) obj).getParent().getParent().getParent().getName();
            if (ac.equals("neu")) {
                createVendor.newModelEntity();
                createVendor.clearTextAreas();
                mainApp.setMessage("Neuer Aufsteller");
                mainApp.commitRefresh(this);
            } else if (ac.equals("speichern")) {
                new SaveThreadWorker(createVendor, mainApp).execute();
                mainApp.commitRefresh(this);
            } else if (ac.equals("abbrechen"))
                mainApp.removeInternalFrame(parentName);
            else if (ac.equals("sortieren")) {
                DartComponentRegistry.getInstance()
                        .setTableModel(showVendors.getPanelTable(), showVendors);
                mainApp.setTaskbarTask("Aufsteller sortiert");
            } else if (ac.equals("löschen")) {
                new DeleteThreadWorker(this, showVendors, mainApp).execute();
            }
        }
    }

    public void setCreatePanel(CreateVendor panel) {
        createVendor = panel;
    }

    public void setShowPanel(ShowVendors panel) {
        showVendors = panel;
    }

    public void initiateRefreshEvent() {
        Collection<ExtendedJPanelImpl> panels = mainApp.getPanelMap().values();
        for (ExtendedJPanelImpl panel : panels) {

            if (panel instanceof ExtJTablePanel) {
                panel.getPanelController().refreshAndWait(panel);
            }
        }
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
}
