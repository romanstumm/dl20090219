package org.en.tealEye.guiMain;

import de.liga.dart.automatenaufsteller.service.AutomatenaufstellerService;
import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.exception.DartException;
import de.liga.dart.exception.DartValidationException;
import de.liga.dart.gruppen.service.GruppenService;
import de.liga.dart.ligateam.service.LigateamService;
import de.liga.dart.model.Automatenaufsteller;
import de.liga.dart.model.Ligagruppe;
import de.liga.dart.model.Ligateam;
import de.liga.dart.model.Spielort;
import de.liga.dart.spielort.service.SpielortService;
import org.en.tealEye.controller.PanelController;
import org.en.tealEye.framework.BeanTableModel;
import org.en.tealEye.framework.TransactionWorker;
import org.en.tealEye.framework.SwingUtils;
import org.en.tealEye.guiExt.ExtPanel.ExtJTablePanel;
import org.en.tealEye.guiExt.ExtPanel.ShowTablePanel;

import java.util.List;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 18.11.2007
 * Time: 17:43:21
 */
public class DeleteThreadWorker extends TransactionWorker {

    private ExtJTablePanel jPanel = null;
    private final MainAppFrame mainApp;
    private final PanelController controller;

    public DeleteThreadWorker(PanelController controller, ExtJTablePanel jPanel,
                              MainAppFrame mainApp) {
        this.jPanel = jPanel;
        this.mainApp = mainApp;
        this.controller = controller;
    }

    public Object executeTransaction() throws Exception {
        mainApp.showProgress(25, "");
        if (jPanel instanceof ShowTablePanel) {
            ((ShowTablePanel) jPanel).setToggleDeleteState(false);
        }
        try {
            BeanTableModel model =
                    (BeanTableModel) jPanel.getPanelTable().getModel();
            List entries = model.getSelectedObjects(jPanel.getPanelTable());
            if (jPanel.getModelClass().equals(Automatenaufsteller.class)) {
                AutomatenaufstellerService service =
                        ServiceFactory.get(AutomatenaufstellerService.class);
                for (Automatenaufsteller each : (List<Automatenaufsteller>) entries) {
                    service.deleteAutomatenaufsteller(each);
                }
            } else if (jPanel.getModelClass().equals(Spielort.class)) {
                SpielortService service =
                        ServiceFactory.get(SpielortService.class);
                for (Spielort each : (List<Spielort>) entries) {
                    service.deleteSpielort(each);
                }
            } else if (jPanel.getModelClass().equals(Ligateam.class)) {
                LigateamService service =
                        ServiceFactory.get(LigateamService.class);
                try {
                    for (Ligateam each : (List<Ligateam>) entries) {
                        service.deleteLigateam(each, true);
                    }
                } catch (DartValidationException ex) {
                    SwingUtils.createOkDialog(mainApp, ex.getMessage(),
                            "Team kann nicht gelöscht werden");
                    throw ex;
                }
            } else if (jPanel.getModelClass().equals(Ligagruppe.class)) {
                GruppenService service =
                        ServiceFactory.get(GruppenService.class);
                for (Ligagruppe each : (List<Ligagruppe>) entries) {
                    service.deleteLigagruppe(each);
                }
            }
            if (entries.isEmpty()) {
                mainApp.setMessage("Zum Löschen muss etwas selektiert sein.");
            } else {
                mainApp.setMessage(entries + " gelöscht.");
            }
        } catch (
                DartValidationException ex) {
            if (ex.getMessages().isEmpty()) {
                mainApp.setMessage(ex.getMessage());
            } else {
                mainApp.setMessage(ex.getMessages().toString());
            }
        } catch (DartException ex) {
            mainApp.setMessage(ex.getMessage());
        }
        mainApp.showProgress(100, "");
        return null;
    }


    protected void done() {
        super.done();    // call super!
        mainApp.showProgress(0, "");
        mainApp.commitRefresh(controller);
        jPanel.updatePanel();  // sonst bleiben die geloeschten Objekte in der Liste stehen
    }

    protected void displayError(String msg) {
        mainApp.setMessage(msg);
    }

}
