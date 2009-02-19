package org.en.tealEye.guiMain;

import de.liga.dart.fileimport.DataExchanger;
import de.liga.dart.fileimport.DbfExporter;
import de.liga.dart.fileimport.DbfImporter;
import de.liga.dart.fileimport.FileImporter;
import de.liga.dart.gruppen.check.ProgressIndicator;
import de.liga.util.thread.ThreadManager;
import org.en.tealEye.framework.LigaChooser;
import org.en.tealEye.framework.SwingUtils;

import javax.swing.*;
import java.io.File;

/**
 * Description: <br/>
 * User: roman.stumm <br/>
 * Date: 06.12.2007 <br/>
 * Time: 18:01:41 <br/>
 * Copyright: Agimatec GmbH
 */
public class ImportThreadWorker extends SwingWorker implements ProgressIndicator {
    private static File REMEMBER_DIR = new File(".");

    private final IO_ACTION action;

    private final MainAppFrame mainAppFrame;

    public ImportThreadWorker(MainAppFrame mainAppFrame, IO_ACTION syncDBase) {
        this.mainAppFrame = mainAppFrame;
        this.action = syncDBase;
    }

    protected Object doInBackground() throws Exception {
        ThreadManager.getInstance().addThread(Thread.currentThread());
        try {
            switch (action) {
                case CsvImport:
                    return doCsvImport();
                case DBaseImport:
                    return doDBaseImport();
                case DBaseExport:
                    return doDBaseExport();
                default:
                    return null; // unknown action
            }
        } finally {
            ThreadManager.getInstance().removeThread(Thread.currentThread());
        }
    }

    private Object doDBaseExport() {
        LigaChooser chooser = new LigaChooser("Export Teams und Gruppen aus DBase Dateien", mainAppFrame);
        if (!chooser.choose()) {
            mainAppFrame.setMessage("Datenexport - Abbruch!");
            return null;
        }
        DataExchanger importer = new DbfExporter();
        importer.setProgressIndicator(this);
        mainAppFrame.setMessage("Datenexport gestartet...");
        boolean success;
        if (chooser.getSelectedLiga() != null) {
            success = importer.start(chooser.getSelectedLiga().getLigaName());
            if (success) {
                mainAppFrame.setMessage("Datenexport für Liga " +
                        chooser.getSelectedLiga().getLigaName() + " beendet");
            } else {
                mainAppFrame.setMessage("Datenexport - nicht möglich (Fehler)!");
            }
        } else {
            success = importer.start();
            if (success) {
                mainAppFrame.setMessage("Datenexport für alle Ligen beendet");
            } else {
                mainAppFrame.setMessage("Datenexport - nicht möglich (Fehler)!");
            }
        }
        return null;
    }

    private Object doDBaseImport() {
        LigaChooser chooser = new LigaChooser("Import Spielorte und Aufsteller aus DBase Dateien", mainAppFrame);
        if (!chooser.choose()) {
            mainAppFrame.setMessage("Datenimport - Abbruch!");
            return null;
        }
        DataExchanger importer = new DbfImporter();
        importer.setProgressIndicator(this);
        mainAppFrame.setMessage("Datenimport gestartet...");
        boolean success;
        if (chooser.getSelectedLiga() != null) {
            success = importer.start(chooser.getSelectedLiga().getLigaName());
            if (success) {
                mainAppFrame.setMessage("Datenimport für Liga " +
                        chooser.getSelectedLiga().getLigaName() + " beendet");
            } else {
                mainAppFrame.setMessage("Datenimport - nicht möglich (Fehler)!");
            }
        } else {
            success = importer.start();
            if (success) {
                mainAppFrame.setMessage("Datenimport für alle Ligen beendet");
            } else {
                mainAppFrame.setMessage("Datenimport - nicht möglich (Fehler)!");
            }
        }
        return null;
    }

    private Object doCsvImport() {
        try {
            final JFileChooser fc = new JFileChooser(REMEMBER_DIR);
            fc.setDialogTitle("Verzeichnis mit LITAUF.csv und LITLOK.csv auswählen...");
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setApproveButtonToolTipText("Spalten durch ; getrennt?");
            int returnVal = fc.showOpenDialog(mainAppFrame);
            if (fc.getSelectedFile() != null) REMEMBER_DIR = fc.getSelectedFile();
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File checkit1 = new File(fc.getSelectedFile(), "LITAUF.csv");
                File checkit2 = new File(fc.getSelectedFile(), "LITLOK.csv");
                if (!checkit1.exists() || !checkit2.exists()) {
                    importAll(fc.getSelectedFile());
                } else {
                    importSingle(fc.getSelectedFile());
                }
            } else {
                mainAppFrame.setMessage("Nicht importiert.");
            }
        } catch (Exception e1) {
            mainAppFrame.setMessage("Fehler: " + e1.getMessage());
        }
        return null;
    }

    private void importSingle(File file) {
        String liga = file.getName();
        liga = SwingUtils
                .showInputDialog("Name der zugehörigen Liga bestätigen", liga);
        if (liga != null && SwingUtils.createYesNoDialog(mainAppFrame,
                "Jetzt Dateien importieren für Liga " + liga + "?", "Datenimport")) {
            mainAppFrame.setMessage("Import läuft...");
            new FileImporter(file).start(liga);
            mainAppFrame.setMessage("Import aus " + file + " beendet.");
        } else {
            mainAppFrame.setMessage("Nicht importiert.");
        }
    }

    private void importAll(File file) throws Exception {
        if (SwingUtils.createYesNoDialog(mainAppFrame,
                "Alle Ligen in Unterverzeichnissen importieren?", "Datenimport")) {
            mainAppFrame.setMessage("Import läuft...");
            new FileImporter(file).start();
            mainAppFrame.setMessage("Import aus " + file + " beendet.");
        } else {
            mainAppFrame.setMessage("Datei LITAUF.csv und/oder LITLOK.csv nicht in " +
                    file + " vorhanden");
        }
    }

    public void showProgress(int percent, String message) {
        mainAppFrame.showProgress(percent, message);
    }
}
