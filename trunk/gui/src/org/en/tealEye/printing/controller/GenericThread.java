package org.en.tealEye.printing.controller;

import de.liga.util.thread.ThreadManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.en.tealEye.printing.gui.GenericLoadingBarFrame;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 03.09.2009
 * Time: 01:37:37
 * To change this template use File | Settings | File Templates.
 */
public class GenericThread extends Thread {
    protected static final Log log = LogFactory.getLog(GenericThread.class);
    private Method threadMethod;
    private Object parentClass;
    private GenericLoadingBarFrame frame;

    public volatile boolean done = false;

    public GenericThread(Method threadMethod, Object parentClass, GenericLoadingBarFrame frame, List components) {
        this.threadMethod = threadMethod;
        this.parentClass = parentClass;
        this.frame = frame;
        ThreadManager.getInstance().addThread(this);
    }

    public GenericThread(Method threadMethod, Object parentClass) {
        this.threadMethod = threadMethod;
        this.parentClass = parentClass;
    }

    public void run() {
        while (!done) {
            try {
                threadMethod.invoke(parentClass, this);
            } catch (IllegalAccessException e) {
                 log.error(e.getMessage(), e);
            } catch (InvocationTargetException e) {
                 log.error(e.getMessage(), e);
            }
        }
    }

    public void setDone() {
        done = true;
//        System.out.println("GenericThread done!");
        frame.setVisible(false);
        try {
            this.finalize();
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
        }
    }
}
