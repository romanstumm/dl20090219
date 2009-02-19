package org.en.tealEye.guiMain;

import org.en.tealEye.guiExt.ExtPanel.ExtendedJPanelImpl;

import javax.swing.*;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 28.02.2008
 * Time: 16:25:35
 */
public class ConstrWorkerThread extends SwingWorker implements ConstrWorkerThreadAPI {
    private final ExtendedJPanelImpl threadedClass;
    private final MainAppFrame maf;
    private final ThreadSupervisor ts;

    public ConstrWorkerThread(ExtendedJPanelImpl c, MainAppFrame maf, ThreadSupervisor ts) {
        this.threadedClass = c;
        this.maf = maf;
        this.ts = ts;
    }

    protected Object doInBackground() throws Exception {
        maf.showProgress(10, null);
        maf.setTaskbarTask("Lade \"" + threadedClass.getTitle() + "\"");
        threadedClass.setIconMap(maf.getGuiService().getIconMap());
        maf.showProgress(20, null);

        maf.addController(threadedClass);
        maf.showProgress(30, null);
        threadedClass.updatePanelLayout(maf.getFontMap());
        maf.showProgress(45, null);
        threadedClass.updatePanel();
        maf.showProgress(100, "Laden Beendet");
        return threadedClass;
    }

    public String getClassName() {
        return threadedClass.getName();
    }

    public ExtendedJPanelImpl getClassOutput() {
        return threadedClass;  // do nothing
    }

    public void done() {
        maf.showProgress(0, "");
        maf.setTaskbarTask("Laden von \"" + threadedClass.getTitle() + "\" abgeschlossen.");
        threadedClass.validate();
        threadedClass.setVisible(true);
        ts.returnPanel(threadedClass);
    }
}
