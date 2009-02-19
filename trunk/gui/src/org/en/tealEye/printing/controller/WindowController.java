package org.en.tealEye.printing.controller;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Description:   <br/>
 * User: Stephan
 * Date: 16.11.2007, 22 :35:48
 */
public class WindowController implements WindowListener {
    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        e.getWindow().dispose();
    }

    public void windowClosed(WindowEvent e) {
        e.getWindow().dispose();
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }
}
