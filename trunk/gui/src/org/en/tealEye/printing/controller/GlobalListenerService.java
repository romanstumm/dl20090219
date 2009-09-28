package org.en.tealEye.printing.controller;


import javax.swing.event.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import org.en.tealEye.printing.controller.CentralDispatch;
import org.en.tealEye.printing.controller.GlobalListener;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 25.09.2009
 * Time: 00:11:38
 * To change this template use File | Settings | File Templates.
 */
public class GlobalListenerService implements GlobalListener, MouseMotionListener, KeyListener, PropertyChangeListener, MouseWheelListener, TreeSelectionListener, ListSelectionListener, WindowListener, ContainerListener, InternalFrameListener {

    private Timer timer;


    public GlobalListenerService(){

    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
            CentralDispatch.invokeMethod(((Component)obj).getName());
    }

    public void stateChanged(ChangeEvent e) {
        Object obj = e.getSource();
            CentralDispatch.invokeMethod(((Component)obj).getName());        
    }

    public void mouseClicked(MouseEvent e) {
        Object obj = e.getSource();
        if(CentralDispatch.isMethodAllowed("mouseClicked",((Component)obj).getName())){
            CentralDispatch.invokeMethod(((Component)obj).getName());
        }
    }

    public void mousePressed(MouseEvent e) {
        final Object obj = e.getSource();
        if(CentralDispatch.isMethodAllowed("mousePressed",((Component)obj).getName())){
            timer = new Timer(100, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    CentralDispatch.invokeMethod(((Component)obj).getName());
                }
            });
            timer.start();
            //CentralDispatch.invokeMethod(((Component)obj).getName());
        }
    }

    public void mouseReleased(MouseEvent e) {
            if (timer != null) {
                timer.stop();
            }
    }

    public void mouseEntered(MouseEvent e) {
        Object obj = e.getSource();
        if(CentralDispatch.isMethodAllowed("mouseEntered",((Component)obj).getName())){
            CentralDispatch.invokeMethod(((Component)obj).getName());
        }
    }

    public void mouseExited(MouseEvent e) {
        Object obj = e.getSource();
        if(CentralDispatch.isMethodAllowed("mouseEntered",((Component)obj).getName())){
            CentralDispatch.invokeMethod(((Component)obj).getName());
        }
    }


    public void mouseDragged(MouseEvent e) {
        Object obj = e.getSource();
        if(CentralDispatch.isMethodAllowed("mouseDragged",((Component)obj).getName())){
            CentralDispatch.invokeMethod(((Component)obj).getName());
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseMoved(MouseEvent e) {
        Object obj = e.getSource();
        if(CentralDispatch.isMethodAllowed("mouseMoved",((Component)obj).getName())){
            CentralDispatch.invokeMethod(((Component)obj).getName());
        }
    }

    public void keyTyped(KeyEvent e) {
        Object obj = e.getSource();
        if(CentralDispatch.isMethodAllowed("keyTyped",((Component)obj).getName())){
            CentralDispatch.invokeMethod(((Component)obj).getName());
        }        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void keyPressed(KeyEvent e) {
        Object obj = e.getSource();
        if(CentralDispatch.isMethodAllowed("keyPressed",((Component)obj).getName())){
            CentralDispatch.invokeMethod(((Component)obj).getName());
        }        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void keyReleased(KeyEvent e) {
        Object obj = e.getSource();
        if(CentralDispatch.isMethodAllowed("keyReleased",((Component)obj).getName())){
            CentralDispatch.invokeMethod(((Component)obj).getName());
        }        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void propertyChange(PropertyChangeEvent evt) {
        Object obj = evt.getSource();
            CentralDispatch.invokeMethod(((Component)obj).getName());
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void valueChanged(TreeSelectionEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void valueChanged(ListSelectionEvent e) {
        System.out.println("ValueChange");
        Object obj = e.getSource();
            CentralDispatch.invokeMethod(((Component)obj).getName());
    }

    public void windowOpened(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowClosing(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowClosed(WindowEvent e) {
        System.out.println("WindowClosed!");
        Object obj = e.getSource();
            CentralDispatch.removeComponentClass(obj);
    }

    public void windowIconified(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowDeiconified(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowActivated(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowDeactivated(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void componentAdded(ContainerEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void componentRemoved(ContainerEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void internalFrameOpened(InternalFrameEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void internalFrameClosing(InternalFrameEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void internalFrameClosed(InternalFrameEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void internalFrameIconified(InternalFrameEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void internalFrameDeiconified(InternalFrameEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void internalFrameActivated(InternalFrameEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void internalFrameDeactivated(InternalFrameEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
