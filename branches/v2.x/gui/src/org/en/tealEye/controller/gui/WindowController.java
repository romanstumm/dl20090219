package org.en.tealEye.controller.gui;

import de.liga.dart.common.service.DartService;
import de.liga.dart.common.service.ServiceFactory;
import org.en.tealEye.guiMain.MainAppFrame;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 14.11.2007
 * Time: 03:15:55
 */
public class WindowController implements InternalFrameListener, WindowListener, GuiController {

    private final MainAppFrame mainApp;

    public WindowController(MainAppFrame mainApp) {
        this.mainApp = mainApp;
    }

    public void internalFrameOpened(InternalFrameEvent e) {
    }

    public void internalFrameClosing(InternalFrameEvent e) {
        //mainApp.activeFrameMenu.removeFrameButton(e.getInternalFrame().getName());
        //mainApp.getFrameMap().remove(e.getInternalFrame().getName());
        //mainApp.getPanelMap().remove(e.getInternalFrame().getName());
        mainApp.removeInternalFrame(e.getInternalFrame().getName());
    }

    public void internalFrameClosed(InternalFrameEvent e) {
        // do nothing
    }

    public void internalFrameIconified(InternalFrameEvent e) {
        // do nothing
    }

    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    public void internalFrameActivated(InternalFrameEvent e) {
        mainApp.setActiveFrame(e.getInternalFrame());

    }

    public void internalFrameDeactivated(InternalFrameEvent e) {
        // do nothing
    }

    public void windowOpened(WindowEvent e) {
        // do nothing
    }

    public void windowClosing(WindowEvent e) {
        ServiceFactory.get(DartService.class).shutdown();
        System.exit(0);
    }

    public void windowClosed(WindowEvent e) {
        // do nothing
    }

    public void windowIconified(WindowEvent e) {
        // do nothing
    }

    public void windowDeiconified(WindowEvent e) {
        // do nothing
    }

    public void windowActivated(WindowEvent e) {
        // do nothing
    }

    public void windowDeactivated(WindowEvent e) {
        // do nothing
    }
}
