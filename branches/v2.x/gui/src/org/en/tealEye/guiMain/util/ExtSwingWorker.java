package org.en.tealEye.guiMain.util;

import javax.swing.*;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 28.03.2008
 * Time: 07:28:27
 */
public abstract class ExtSwingWorker extends SwingWorker{

    protected abstract Object doInBackground() throws Exception;

    protected abstract void done();

    public abstract void stop();
}
