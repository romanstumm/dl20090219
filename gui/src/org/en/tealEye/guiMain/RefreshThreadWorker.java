package org.en.tealEye.guiMain;

import de.liga.dart.gruppen.check.ProgressIndicator;
import org.en.tealEye.controller.PanelController;
import org.en.tealEye.framework.TransactionWorker;
import org.en.tealEye.guiExt.ExtPanel.ExtendedJPanelImpl;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 13.11.2007
 * Time: 23:23:18
 */
public class RefreshThreadWorker extends TransactionWorker {
    private final ExtendedJPanelImpl jPanel;
    private final PanelController controller;
    private final ProgressIndicator progressIndicator;

    public RefreshThreadWorker(PanelController controller, ExtendedJPanelImpl jPanel,
                               ProgressIndicator progressIndicator) {
        this.jPanel = jPanel;
        this.controller = controller;
        this.progressIndicator = progressIndicator;
    }

    public Object executeTransaction() throws Exception {
        controller.setUpdateInProgress(true);
        progressIndicator.showProgress(50, "");
        jPanel.updatePanel();
        progressIndicator.showProgress(100, "");
        return null;
    }

    protected void done() {
        super.done();    // call super!
        progressIndicator.showProgress(0, "");
        controller.setUpdateInProgress(false);
    }
}
