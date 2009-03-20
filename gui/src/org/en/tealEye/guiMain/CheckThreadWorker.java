package org.en.tealEye.guiMain;

import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.gruppen.CheckResult;
import de.liga.dart.gruppen.service.GruppenService;
import de.liga.dart.gruppen.service.GruppenSortierer;
import de.liga.dart.model.Liga;
import de.liga.dart.model.Ligagruppe;
import de.liga.util.thread.ManagedThread;
import org.en.tealEye.framework.ProgressDialog;
import org.en.tealEye.framework.SwingUtils;
import org.en.tealEye.framework.TransactionWorker;
import org.en.tealEye.guiPanels.applicationLogicPanels.CreateGroup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 18.11.2007
 * Time: 20:09:34
 */
public class CheckThreadWorker extends TransactionWorker {
    private final CreateGroup editPanel;
    private final MainAppFrame mainApp;

    public CheckThreadWorker(CreateGroup jPanel, MainAppFrame mainApp) {
        editPanel = jPanel;
        this.mainApp = mainApp;
    }

    public Object executeTransaction() throws Exception {
        Liga liga = (Liga) editPanel.getLiga().getSelectedItem();
        if (liga != null) {
            CheckResult result =
                    ServiceFactory.get(GruppenService.class).checkGruppen(liga);
            mainApp.setMessage(result.toString());

            if (result.getOutcome() == CheckResult.Outcome.KEINE_KONFLIKTE) {
                SwingUtils.createOkDialog(editPanel, result.toStringWithLimit(30),
                        "Verifizierung beendet");
            } else {
                if (SwingUtils.createYesNoDialog(editPanel,
                        result.toStringWithLimit(30) + " \nJetzt automatisch sortieren?",
                        "Verifizierung beendet")) {
                    optimize(liga);
                }

            }
        } else {
            mainApp.setMessage("Zum Verifzieren bitte zuerst eine Liga wählen.");
        }
        return null;
    }

    private void optimize(Liga liga) throws InterruptedException {
        final GruppenSortierer sortierer =
                ServiceFactory.get(GruppenService.class).createSortierer(liga);

        Thread thread = new ManagedThread(new Runnable() {
            public void run() {
                try {
                    sortierer.start();
                } catch (RuntimeException ex) {  // NullPointerEx, ClassCastEx, ...
                    sortierer.getCheckResult().setFatalError(ex.getLocalizedMessage());
                    throw ex;
                } catch(Error ex) {  // StackOverflow, OutofMemory ...
                    sortierer.getCheckResult().setFatalError(ex.getLocalizedMessage());
                    throw ex;
                }
            }
        });
        thread.start();

        final ProgressDialog progressDialog = new ProgressDialog();

        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortierer.stop();
            }
        };
        progressDialog.open(al, "bitte warten...", "Sortierung");
        try {
            sortierer.setProgressIndicator(progressDialog);
            thread.join();
        } finally {
            progressDialog.dispose();
        }
        CheckResult result = sortierer.getCheckResult();
        mainApp.setMessage(result.toString());
        if (SwingUtils.createYesNoDialog(editPanel,
                result.toStringWithLimit(30) + " \nAufstellung speichern?",
                "Sortierung beendet")) {
            sortierer.saveAufstellung();
            Ligagruppe gruppe = (Ligagruppe) editPanel.getModelEntity();
            mainApp.setMessage("Aufstellung \"" + gruppe + "\" gespeichert.");
            if (gruppe.getGruppenId() > 0) {
                gruppe = ServiceFactory.get(GruppenService.class)
                        .findLigagruppeById(gruppe.getGruppenId());
                editPanel.updatePanel(gruppe);
            }
        }
    }

    protected void displayError(String msg) {
        mainApp.setMessage(msg);
    }

}
