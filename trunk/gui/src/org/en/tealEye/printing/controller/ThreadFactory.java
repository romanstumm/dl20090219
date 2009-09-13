package org.en.tealEye.printing.controller;

import org.en.tealEye.printing.gui.GenericLoadingBarFrame;

import javax.swing.*;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 03.09.2009
 * Time: 01:37:37
 * To change this template use File | Settings | File Templates.
 */
public class ThreadFactory extends SwingWorker {

    private Method threadMethod;
    private Object parentClass;
    private GenericLoadingBarFrame frame;

    public ThreadFactory(Method threadMethod, Object parentClass, GenericLoadingBarFrame frame, List components){
         this.threadMethod = threadMethod;
         this.parentClass = parentClass;
         this.frame = frame;
    }

    public ThreadFactory(Method threadMethod, Object parentClass){
             this.threadMethod = threadMethod;
             this.parentClass = parentClass;
    }

    @Override
    protected Object doInBackground() throws Exception {
        threadMethod.invoke(parentClass);
        return null;
    }

    @Override
    protected void done(){
        frame.dispose();                
    }
}
