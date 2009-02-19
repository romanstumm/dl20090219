package org.en.tealEye.guiMain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.en.tealEye.framework.TransactionWorker;
import org.en.tealEye.guiExt.ExtPanel.ExtendedJPanelImpl;

import javax.swing.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 28.02.2008
 * Time: 16:18:17
 */
public class ThreadSupervisor {
    private static final Log log = LogFactory.getLog(ThreadSupervisor.class);

//    private Map<String, ExtendedJPanelImpl> panelMapping = new HashMap<String,ExtendedJPanelImpl>();
    private final Map<String, SwingWorker> workerMapping = new HashMap<String, SwingWorker>();
    private final Map<String, TransactionWorker> taWorkerMapping = new HashMap<String, TransactionWorker>();


    private final Hypervisor h;
    private final MainAppFrame maf;
    public ThreadSupervisor(Hypervisor h, MainAppFrame maf){
        this.h = h;
        this.maf = maf;
    }

    protected void startConstrWorkerThread(ExtendedJPanelImpl c) {
        SwingWorker worker = new ConstrWorkerThread(c,maf,this);
        worker.execute();
        try {
            worker.get(); // warten, sonst macht das updatePanel im ConstrWorker
            // ggf. nachfolgende Edit/Refresh updates wieder platt
        } catch (Exception e) {
            log.error("error during ConstrWorkerThread", e);
        }
        workerMapping.put(c.getName(), worker);
    }

    protected void registerBareWorkerThread(TransactionWorker worker){
        taWorkerMapping.put(worker.getClass().getName(),worker);
    }

    protected void startBareWorkerThread(String name){
        try {
            taWorkerMapping.get(name).executeTransaction();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    protected void restartWorkerThread(String name){
        workerMapping.get(name).execute();
    }

    protected void stopWorkerThread(String name){
        workerMapping.get(name).cancel(true);
    }

    protected Collection<SwingWorker> showWorkerThread(){
        return workerMapping.values();
    }

    protected Object getWorkerOutput(String wName){
        try {
            return workerMapping.get(wName).get();
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public void returnPanel(ExtendedJPanelImpl threadedClass) {
//        panelMapping.put(threadedClass.getName(),threadedClass);
        h.registerNewPanel(threadedClass);
    }
}
