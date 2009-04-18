package org.en.tealEye.guiMain;

import de.liga.dart.fileimport.*;
import de.liga.dart.fileimport.vfs.DbfExporter;
import de.liga.dart.fileimport.vfs.DbfImporter;
import de.liga.dart.gruppen.check.ProgressIndicator;
import de.liga.dart.exception.DartException;
import de.liga.util.thread.ThreadManager;
import de.liga.util.CalendarUtils;
import org.en.tealEye.framework.LigaChooser;
import org.en.tealEye.framework.SwingUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileFilter;
import java.util.Date;

/**
 * Description: <br/>
 * User: roman.stumm <br/>
 * Date: 06.12.2007 <br/>
 * Time: 18:01:41 <br/>
 * Copyright: Agimatec GmbH
 */
public class ImportThreadWorker extends SwingWorker implements ProgressIndicator {
    private static final Log log = LogFactory.getLog(ImportThreadWorker.class);
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
                case ExcelExport:
                    return doExcelExport();
                case ExcelImport:
                    return doExcelImport();
                default:
                    return null; // unknown action
            }
        } finally {
            ThreadManager.getInstance().removeThread(Thread.currentThread());
        }
    }

    private Object doExcelExport() throws IOException {
        final JFileChooser fc = new JFileChooser(REMEMBER_DIR);
        fc.setDialogTitle("Daten in Exceldatei sichern");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setSelectedFile(new File("liga-" +
                CalendarUtils.dateToString(new Date(), "yyyy-MM-dd HH-mm-ss") + ".xls"));
        int returnVal = fc.showSaveDialog(mainAppFrame);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            setRememberDir(file);
            file = ensureSuffix(file, ".xls");
            DataExchanger exporter = new ExcelExporter(file);
            exporter.setProgressIndicator(this);
            try {
                if (exporter.start()) {
                    mainAppFrame.setMessage("Excel-Export " + file.getPath() + " erfolgreich.");
                } else {
                    mainAppFrame.setMessage("Excel-Export - nicht möglich (Fehler)!");
                }
            } catch (DartException ex) {
                log.error(null, ex);
                mainAppFrame.setMessage(ex.getMessage());
                SwingUtils.createOkDialog(mainAppFrame, ex.getMessage(), "Excel-Export");
            }
        }
        return null;
    }

    private File ensureSuffix(File file, String suffix) {
        if (!file.getName().toLowerCase().endsWith(suffix.toLowerCase())) {
            String name = file.getName();
            int suff = name.lastIndexOf('.');
            if(suff>0) {
                name = name.substring(0, suff);
            }
            file = new File(file.getParent(), name + suffix);
        }
        return file;
    }

    private void setRememberDir(File file) {
        if (file != null) {
            REMEMBER_DIR = file;
            REMEMBER_DIR = REMEMBER_DIR.getParentFile();
        }
    }

    private Object doExcelImport() throws FileNotFoundException {
        final JFileChooser fc = new JFileChooser(REMEMBER_DIR);
        fc.setDialogTitle("Daten aus Exceldatei wiederherstellen");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fc.showOpenDialog(mainAppFrame);
        setRememberDir(fc.getSelectedFile());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                DataExchanger exporter = new ExcelImporter(file);
                exporter.setProgressIndicator(this);
                if (exporter.start()) {
                    mainAppFrame.setMessage("Excel-Import " + file.getPath() + " erfolgreich.");
                } else {
                    mainAppFrame.setMessage("Excel-Import - nicht möglich (Fehler)!");
                    SwingUtils.createOkDialog(mainAppFrame, "Import nicht möglich (Fehler)!",
                            "Excel-Import");
                }
            } catch (DartException ex) {
                log.error(null, ex);
                mainAppFrame.setMessage(ex.getMessage());
                SwingUtils.createOkDialog(mainAppFrame, ex.getMessage(), "Excel-Import");
            }
        }
        return null;
    }

    private Object doDBaseExport() {
        LigaChooser chooser =
                new LigaChooser("Export Teams und Gruppen aus DBase Dateien", mainAppFrame);
        if (!chooser.choose()) {
            mainAppFrame.setMessage("Datenexport - Abbruch!");
            return null;
        }
        DataExchanger importer = new DbfExporter();
        importer.setProgressIndicator(this);
        boolean success;
        if (chooser.getSelectedLiga() != null) {
            try {
                success = importer.start(chooser.getSelectedLiga().getLigaName());
                if (success) {
                    mainAppFrame.setMessage("dBase-Export für Liga " +
                            chooser.getSelectedLiga().getLigaName() + " beendet");
                } else {
                    mainAppFrame.setMessage("dBase-Export - nicht möglich (Fehler)!");
                    SwingUtils.createOkDialog(mainAppFrame, "Export nicht möglich (Fehler)!",
                            "dBase-Export");
                }
            } catch (DartException ex) {
                log.error(null, ex);
                mainAppFrame.setMessage(ex.getMessage());
                SwingUtils.createOkDialog(mainAppFrame, ex.getMessage(), "dBase-Export");
            }
        } else {
            success = importer.start();
            if (success) {
                mainAppFrame.setMessage("dBase-Export für alle Ligen beendet");
            } else {
                mainAppFrame.setMessage("dBase-Export - nicht möglich (Fehler)!");
            }
        }
        return null;
    }

    private Object doDBaseImport() {
        LigaChooser chooser =
                new LigaChooser("Import Spielorte und Aufsteller aus DBase Dateien", mainAppFrame);
        if (!chooser.choose()) {
            mainAppFrame.setMessage("dBase-Import - Abbruch!");
            return null;
        }
        DataExchanger importer = new DbfImporter();
        importer.setProgressIndicator(this);
        boolean success;
        if (chooser.getSelectedLiga() != null) {
            success = importer.start(chooser.getSelectedLiga().getLigaName());
            if (success) {
                mainAppFrame.setMessage("dBase-Import für Liga " +
                        chooser.getSelectedLiga().getLigaName() + " beendet");
            } else {
                mainAppFrame.setMessage("dBase-Import - nicht möglich (Fehler)!");
                SwingUtils.createOkDialog(mainAppFrame, "Import nicht möglich (Fehler)!",
                        "dBase-Import");
            }
        } else {
            success = importer.start();
            if (success) {
                mainAppFrame.setMessage("dBase-Import für alle Ligen beendet");
            } else {
                mainAppFrame.setMessage("dBase-Import - nicht möglich (Fehler)!");
                SwingUtils.createOkDialog(mainAppFrame, "Import nicht möglich (Fehler)!",
                        "dBase-Import");
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
            setRememberDir(fc.getSelectedFile());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File checkit1 = new File(fc.getSelectedFile(), "LITAUF.csv");
                File checkit2 = new File(fc.getSelectedFile(), "LITLOK.csv");
                if (!checkit1.exists() || !checkit2.exists()) {
                    importAll(fc.getSelectedFile());
                } else {
                    importSingle(fc.getSelectedFile());
                }
            }
        } catch (Exception e1) {
            log.error(null, e1);
            mainAppFrame.setMessage("Fehler: " + e1.getMessage());
            SwingUtils.createOkDialog(mainAppFrame, "Fehler: " + e1.getMessage(),
                    "csv-Import");
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
