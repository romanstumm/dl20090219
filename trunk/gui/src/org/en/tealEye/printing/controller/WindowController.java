package org.en.tealEye.printing.controller;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Description:   <br/>
 * User: Stephan
 * Date: 16.11.2007, 22 :35:48
 */
public class WindowController implements WindowListener {
    protected static final Log log = LogFactory.getLog(WindowController.class);

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
