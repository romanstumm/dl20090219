package org.en.tealEye.framework;

import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.gruppen.CheckResult;
import de.liga.dart.gruppen.check.ProgressIndicator;
import de.liga.dart.gruppen.service.GruppenService;
import de.liga.dart.gruppen.service.GruppenSortierer;
import de.liga.util.thread.ManagedThread;
import junit.framework.TestCase;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 25.11.2007
 * Time: 17:43:43
 */
public class SwingUtilsTest extends TestCase {

    public void testYesNo() {
        JFrame parent = new JFrame();
        parent.setVisible(true);
        parent.validate();
        SwingUtils.createOkDialog(parent, "Wollen Sie das wirklich?", "Gruppen sortieren");
        //System.out.println("result = " + result );
    }

    public void testCancelWorker() throws InterruptedException {
        JFrame parent = new JFrame();
        parent.setVisible(true);
        parent.validate();

        final GruppenSortierer sortierer = ServiceFactory.get(GruppenService.class).createSortierer(null);


        Thread thread = new ManagedThread(new Runnable() {
            public void run() {
                sortierer.start();
            }
        });
        thread.start();

        final ProgressDialog progressDialog = new ProgressDialog();

        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortierer.stop();
            }
        };
        progressDialog.open(al, "es wird berechnet...", "Gruppenaufstellung sortieren");
        try {
            sortierer.setProgressIndicator(new ProgressIndicator() {

                public void showProgress(int percent, String message) {
                    progressDialog.showProgress(percent, message);
//                    System.out.println(message + ": " + percent + "...");
                }
            });
            thread.join();
        } finally {
            progressDialog.dispose();
        }
//                    sortierer.stop();
        CheckResult result = sortierer.getCheckResult();
        result.print(System.out, 0);
        if (SwingUtils.createYesNoDialog(parent, "Aufstellung speichern?", "Sortierung beendet")) {
            sortierer.saveAufstellung();
        }
    }
}
