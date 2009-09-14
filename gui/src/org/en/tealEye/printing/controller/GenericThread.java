package org.en.tealEye.printing.controller;

import org.en.tealEye.printing.gui.GenericLoadingBarFrame;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import de.liga.util.thread.ThreadManager;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 03.09.2009
 * Time: 01:37:37
 * To change this template use File | Settings | File Templates.
 */
public class GenericThread extends Thread {

    private Method threadMethod;
    private Object parentClass;
    private GenericLoadingBarFrame frame;

    private volatile boolean done = false;
    
    public GenericThread(Method threadMethod, Object parentClass, GenericLoadingBarFrame frame, List components){
         this.threadMethod = threadMethod;
         this.parentClass = parentClass;
         this.frame = frame;
        ThreadManager.getInstance().addThread(this);
    }

    public GenericThread(Method threadMethod, Object parentClass){
             this.threadMethod = threadMethod;
             this.parentClass = parentClass;
    }

    public void run(){
        while(!done){
        try {
            threadMethod.invoke(parentClass, this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        }
    }

    public void setDone(){
        done = true;
        System.out.println("GenericThread done!");
        frame.setVisible(false);
    }
}
