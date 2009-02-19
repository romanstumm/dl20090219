package org.en.tealEye.guiMain;

import de.liga.dart.common.service.DartService;
import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.gruppen.check.ProgressIndicator;
import de.liga.dart.model.LigaPersistence;
import org.en.tealEye.controller.PanelController;
import org.en.tealEye.framework.TransactionWorker;
import org.en.tealEye.guiExt.ExtPanel.ExtJEditPanel;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 12.11.2007
 * Time: 07:57:45
 */
public class EditThreadWorker extends TransactionWorker {
    private final ExtJEditPanel jPanel;
    private Object obj;
    private final PanelController controller;
    private final ProgressIndicator progressIndicator;

    public EditThreadWorker(PanelController controller, ExtJEditPanel jPanel, Object obj,
                            ProgressIndicator progressIndicator) {
        this.jPanel = jPanel;
        this.obj = obj;
        this.controller = controller;
        this.progressIndicator = progressIndicator;
    }

    public Object executeTransaction() throws Exception {
        controller.setUpdateInProgress(true);
        obj = ServiceFactory.get(DartService.class).rejoin((LigaPersistence) obj);
        progressIndicator.showProgress(50, "");
        jPanel.updatePanel(obj);
        progressIndicator.showProgress(100, "");
        return null;
    }

    protected void done() {
        super.done();    // call super!
        progressIndicator.showProgress(0, "");
        controller.setUpdateInProgress(false);
    }
}
