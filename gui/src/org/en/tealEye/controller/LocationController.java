package org.en.tealEye.controller;

import org.en.tealEye.framework.BeanTableModel;
import org.en.tealEye.framework.TransactionWorker;
import org.en.tealEye.guiExt.ExtPanel.ExtJEditPanel;
import org.en.tealEye.guiExt.ExtPanel.ExtJTablePanel;
import org.en.tealEye.guiExt.ExtPanel.ExtendedJPanelImpl;
import org.en.tealEye.guiMain.*;
import org.en.tealEye.guiPanels.applicationLogicPanels.CreateLocation;
import org.en.tealEye.guiPanels.applicationLogicPanels.ShowLocations;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Collection;

public class LocationController extends PanelController {

    private CreateLocation createLocation;
    private ShowLocations showLocations;
    private final Hypervisor h;

    public LocationController(MainAppFrame mainApp, Hypervisor h) {
        super(mainApp);
//        this.mainApp.addPropertyChangeListener(this); // // RSt: entfernt
        this.h = h;
    }

    public void mouseClicked(MouseEvent e) {
        Object obj = e.getSource();
        if (obj instanceof JTable) {
            if (e.getClickCount() == 2) {
                //mainApp.insertInternalFrame("CreateLocation", false);
                JPanel p = h.showPanel("CreateLocation");
                Object obje = ((BeanTableModel) ((JTable) obj).getModel()).getObject(((JTable) obj).getSelectedRow());
                try {
                    TransactionWorker instance = new EditThreadWorker(
                            this, (ExtJEditPanel) p, obje, mainApp);
                    instance.addPropertyChangeListener(this);
                    instance.execute();
                } catch (Exception e1) {
                    mainApp.setTaskbarTask("WindowFehler: " + e1.getMessage());
                    //log.error(e1.getMessage(), e1);
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if(isUpdateInProgress()) return; // z.zt. optional 

        Object obj = e.getSource();
        if (obj instanceof JButton) {

            String ac = ((JButton) obj).getName();
            String parentName = ((JButton) obj).getParent().getParent().getParent().getName();
            if (ac.equals("neu")) {
                createLocation.newModelEntity();
                createLocation.clearTextAreas();

                mainApp.setMessage("Neuer Spielort");
                mainApp.commitRefresh(this);
            } else if (ac.equals("speichern")) {
                new SaveThreadWorker(createLocation, mainApp).execute();
                mainApp.commitRefresh(this);
            } else if (ac.equals("abbrechen"))
                mainApp.removeInternalFrame(parentName);
            else if (ac.equals("sortieren")) {
                DartComponentRegistry.getInstance().setTableModel(showLocations.getPanelTable(), showLocations);
                mainApp.setTaskbarTask("Spielorte sortiert");
            } else if (ac.equals("löschen")) {
                new DeleteThreadWorker(this, showLocations, mainApp).execute();
            }
        }
    }

    public void setCreatePanel(CreateLocation panel) {
        this.createLocation = panel;
    }

    public void setShowPanel(ShowLocations panel) {
        this.showLocations = panel;
    }

    public void initiateRefreshEvent() {
        Collection<ExtendedJPanelImpl> panels = mainApp.getPanelMap().values();
        for (ExtendedJPanelImpl panel : panels) {
            if (panel instanceof ExtJTablePanel)
                panel.getPanelController().refreshAndWait(panel);
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
