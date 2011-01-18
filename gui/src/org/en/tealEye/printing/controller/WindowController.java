package org.en.tealEye.printing.controller;


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
    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        e.getWindow().dispose();
    }

    public void windowClosed(WindowEvent e) {
        if(e.getWindow().getName().equals("TablePrintingFrame")) {
            Method[] methods = e.getWindow().getClass().getDeclaredMethods();
            for(Method m : methods){
                if(m.getName().equals("cleanUp")){
                    try {
                        m.invoke(e.getWindow().getClass());
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (InvocationTargetException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        }
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
