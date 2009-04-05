package org.en.tealEye.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.en.tealEye.framework.BeanTableModel;
import org.en.tealEye.framework.TransactionWorker;
import org.en.tealEye.guiExt.ExtPanel.ExtJEditPanel;
import org.en.tealEye.guiExt.ExtPanel.ExtendedJPanelImpl;
import org.en.tealEye.guiMain.EditThreadWorker;
import org.en.tealEye.guiMain.MainAppFrame;
import org.en.tealEye.guiMain.RefreshThreadWorker;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 14.11.2007
 * Time: 00:56:28
 */
public abstract class PanelController
        implements MouseListener, ActionListener, PropertyChangeListener, KeyListener {
    private static final Log log = LogFactory.getLog(PanelController.class);
    private boolean updateInProgress;
    protected final MainAppFrame mainApp;

    protected PanelController(MainAppFrame mainApp) {
        this.mainApp = mainApp;
    }

    public void mousePressed(MouseEvent e) {
        // do nothing
    }

    public void mouseClicked(MouseEvent e) {
        // do nothing
    }

    public void mouseReleased(MouseEvent e) {
        // do nothing
    }

    public void mouseEntered(MouseEvent e) {
        // do nothing
    }

    public void mouseExited(MouseEvent e) {
        // do nothing
    }


    public void commitEditTransaction(ExtendedJPanelImpl p, JTable jtable) {

        Object obje = ((BeanTableModel) jtable.getModel())
                .getObject(jtable.getSelectedRow());
        try {
            TransactionWorker instance = new EditThreadWorker(this,
                    (ExtJEditPanel) p, obje, mainApp);
//                    instance.addPropertyChangeListener(this);
            instance.execute();
        } catch (Exception e1) {
            //mainApp.setTaskbarTask("WindowFehler: " + e1.getMessage());
            log.error(e1.getMessage(), e1);
        }
    }


    public final void propertyChange(PropertyChangeEvent evt) {
        if (isInterestedInRefresh(evt)) {
            initiateRefreshEvent();
        }
    }

    protected boolean isInterestedInRefresh(PropertyChangeEvent evt) {
        return "state".equals(evt.getPropertyName()) &&
                SwingWorker.StateValue.DONE.equals(evt.getNewValue());
    }

    protected abstract void initiateRefreshEvent();

    public void refreshAndWait(ExtendedJPanelImpl panel) {
        RefreshThreadWorker worker;
        worker = new RefreshThreadWorker(this, panel, mainApp);
        worker.execute();
        try {
            worker.get();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public void setUpdateInProgress(boolean value) {
        updateInProgress = value;
    }

    public boolean isUpdateInProgress() {
        return updateInProgress;
    }
}
