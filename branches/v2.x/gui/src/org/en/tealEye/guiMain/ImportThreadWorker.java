package org.en.tealEye.guiMain;

import de.liga.dart.exception.DartException;
import de.liga.dart.fileimport.*;
import de.liga.dart.fileimport.vfs.DbfExporterSpielorteAufsteller;
import de.liga.dart.fileimport.vfs.DbfExporterTeamsGruppen;
import de.liga.dart.fileimport.vfs.DbfImporterSpielorteAufsteller;
import de.liga.dart.fileimport.vfs.rangliste.RanglisteExporter;
import de.liga.dart.gruppen.check.ProgressIndicator;
import de.liga.dart.model.Liga;
import de.liga.util.CalendarUtils;
import de.liga.util.thread.ThreadManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.en.tealEye.framework.LigaChooser;
import org.en.tealEye.framework.SwingUtils;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
                case DBaseImportSpielorteAufsteller:
                    return doDBaseImport();
                case DBaseExportSpielorteAufsteller:
                    return doDBaseExportSpielorteAufsteller();
                case DBaseExportTeamsGruppen:
                    return doDBaseExportTeamsGruppen();
                case ExcelExport:
                    return doExcelExport();
                case ExcelImport:
                    return doExcelImport();
                case ExcelRangliste:
                    return doExcelRangliste();
                case HTMLRanglisten:
                    return doHtmlRanglisten();
                case HTMLSpielplaene:
                    return doHtmlSpielplaene();
                default:
                    return null; // unknown action
            }
        } finally {
            ThreadManager.getInstance().removeThread(Thread.currentThread());
        }
    }

    private Object doHtmlSpielplaene() {
        LigaChooser chooser =
                new LigaChooser("HTML Upload - Spielpläne", mainAppFrame,
                        LigaChooser.SELECTION_MODE_LIGA, true);

        if (!chooser.choose() || chooser.getSelectedLigas() == null || chooser.getSelectedLigas().length == 0) {
            mainAppFrame.setMessage("HTML Upload Spielpläne - Abbruch, keine Ligas gewählt!");
            return null;
        }
        StringBuilder ligen = new StringBuilder();
        int i = 0;
        for (Liga liga : chooser.getSelectedLigas()) {
            if (i > 0) {
                ligen.append(", ");
            }
            i++;
            ligen.append(liga.getLigaName());
            FtpWebIO ftp = new FtpWebIO(mainAppFrame);
            try {
                mainAppFrame.setMessage("Kopiere Spielpläne für " + liga.getLigaName() + "...");
                ftp.copyPlanHtmlToWebdir(liga);
                mainAppFrame.setMessage("Uploading Spielpläne für " + liga.getLigaName() + "...");
                if (ftp.uploadPlanWebdir(liga)) {
                    mainAppFrame.setMessage("Upload der Spielpläne abgeschlossen.");
                } else {
                    mainAppFrame.setMessage("Upload der Spielpläne nicht möglich, fehlende oder falsche FTP-Konfiguration.");
                }
            } catch (Exception e) {
                log.error("HTML Spielpläne - Fehler in Verarbeitung", e);
                mainAppFrame.setMessage("Fehler in der Verarbeitung: " + e.getMessage());
            } finally {
                mainAppFrame.showProgress(0, "");
            }
        }
        mainAppFrame.setMessage("Spielpläne von " + ligen + " verarbeitet.");
        return null;  //To change body of created methods use File | Settings | File Templates.
    }


    private Object doHtmlRanglisten() {
        LigaChooser chooser =
                new LigaChooser("HTML Upload - Ranglisten", mainAppFrame,
                        LigaChooser.SELECTION_MODE_LIGA, true);
        if (!chooser.choose() || chooser.getSelectedLigas() == null || chooser.getSelectedLigas().length == 0) {
            mainAppFrame.setMessage("HTML Upload Ranglisten - Abbruch, keine Ligas gewählt!");
            return null;
        }
        StringBuilder ligen = new StringBuilder();
        int i = 0;
        for (Liga liga : chooser.getSelectedLigas()) {
            if (i > 0) {
                ligen.append(", ");
            }
            i++;
            ligen.append(liga.getLigaName());
            FtpWebIO ftp = new FtpWebIO(mainAppFrame);
            try {
                mainAppFrame.setMessage("Kopiere Ranglisten für " + liga.getLigaName() + "...");
                ftp.copyRangHtmlToWebdir(liga);
                mainAppFrame.setMessage("Uploading Ranglisten für " + liga.getLigaName() + "...");
                if (ftp.uploadRangWebdir(liga)) {
                    mainAppFrame.setMessage("Upload der Ranglisten abgeschlossen.");
                } else {
                    mainAppFrame.setMessage("Upload der Ranglisten nicht möglich, fehlende oder falsche FTP-Konfiguration.");
                }
            } catch (Exception e) {
                // loggen
                log.error("HTML Ranglisten - Fehler in Verarbeitung bei " + liga.getLigaName(), e);
                mainAppFrame.setMessage("Fehler in Verarbeitung bei " + liga.getLigaName() + ": " + e.getMessage());
            } finally {
                mainAppFrame.showProgress(0, "");
            }
        }
        mainAppFrame.setMessage("Ranglisten von " + ligen + " verarbeitet.");
        return null;
    }

    private Object doExcelRangliste() {
        LigaChooser chooser =
                new LigaChooser("vfs-Rangliste erstellen. 1. Liga wählen", mainAppFrame,
                        LigaChooser.SELECTION_MODE_LIGA);
        if (!chooser.choose() || chooser.getSelectedLiga() == null) {
            mainAppFrame.setMessage("Rangliste - Abbruch, keine Liga gewählt!");
            return null;
        }
        final JFileChooser fc = new JFileChooser(REMEMBER_DIR);
        fc.setDialogTitle("vfs-Rangliste erstellen. 2. Name der Exceldatei angeben");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setSelectedFile(new File("rangliste-" + chooser.getSelectedLiga().getLigaName() + ".xls"));
        int returnVal = fc.showSaveDialog(mainAppFrame);

        File file = null;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            setRememberDir(file);
            file = ensureSuffix(file, ".xls");
        }
        if (file == null) {
            mainAppFrame.setMessage("Rangliste - Abbruch, keine Exceldatei gewählt!");
            return null;
        }
        RanglisteExporter exporter = new RanglisteExporter(file);
        exporter.setProgressIndicator(this);
        boolean success;
        if (chooser.getSelectedLiga() != null) {
            success = exporter.start(chooser.getSelectedLiga().getLigaName());
            if (success) {
                mainAppFrame.setMessage("Rangliste für Liga " +
                        chooser.getSelectedLiga().getLigaName() + " gespeichert in " + file.getPath());
            } else {
                mainAppFrame.setMessage("Rangliste - nicht möglich (Fehler)!");
                SwingUtils.createOkDialog(mainAppFrame, "Rangliste nicht möglich (Fehler)!",
                        "Rangliste-Export");
            }
        }
        return null;
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
            if (suff > 0) {
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

    private Object doDBaseExportSpielorteAufsteller() {
        LigaChooser chooser =
                new LigaChooser("Export Spielorte und Aufsteller (Gaststätten) in dBase Dateien", mainAppFrame);
        if (!chooser.choose()) {
            mainAppFrame.setMessage("Datenexport Spielorte - Abbruch!");
            return null;
        }
        DataExchanger exporter = new DbfExporterSpielorteAufsteller();
        exporter.setProgressIndicator(this);
        boolean success;
        if (chooser.getSelectedLiga() != null) {
            try {
                success = exporter.start(chooser.getSelectedLiga().getLigaName());
                if (success) {
                    mainAppFrame.setMessage("dBase-Export Spielorte/Aufsteller für Liga " +
                            chooser.getSelectedLiga().getLigaName() + " beendet");
                } else {
                    mainAppFrame.setMessage("dBase-Export Spielorte/Aufsteller - nicht möglich (Fehler)!");
                    SwingUtils.createOkDialog(mainAppFrame, "Export nicht möglich (Fehler)!",
                            "dBase-Export");
                }
            } catch (DartException ex) {
                log.error(null, ex);
                mainAppFrame.setMessage(ex.getMessage());
                SwingUtils.createOkDialog(mainAppFrame, ex.getMessage(), "dBase-Export");
            }
        } else {
            success = exporter.start();
            if (success) {
                mainAppFrame.setMessage("dBase-Export für alle Ligen beendet");
            } else {
                mainAppFrame.setMessage("dBase-Export - nicht möglich (Fehler)!");
            }
        }
        return null;
    }


    private Object doDBaseExportTeamsGruppen() {
        LigaChooser chooser =
                new LigaChooser("Export Teams und Gruppen in dBase Dateien", mainAppFrame);
        if (!chooser.choose()) {
            mainAppFrame.setMessage("Datenexport Teams - Abbruch!");
            return null;
        }
        DataExchanger exporter = new DbfExporterTeamsGruppen();
        exporter.setProgressIndicator(this);
        boolean success;
        if (chooser.getSelectedLiga() != null) {
            try {
                success = exporter.start(chooser.getSelectedLiga().getLigaName());
                if (success) {
                    mainAppFrame.setMessage("dBase-Export Teams/Gruppen für Liga " +
                            chooser.getSelectedLiga().getLigaName() + " beendet");
                } else {
                    mainAppFrame.setMessage("dBase-Export Teams/Gruppen - nicht möglich (Fehler)!");
                    SwingUtils.createOkDialog(mainAppFrame, "Export nicht möglich (Fehler)!",
                            "dBase-Export");
                }
            } catch (DartException ex) {
                log.error(null, ex);
                mainAppFrame.setMessage(ex.getMessage());
                SwingUtils.createOkDialog(mainAppFrame, ex.getMessage(), "dBase-Export");
            }
        } else {
            success = exporter.start();
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
                new LigaChooser("Import Spielorte und Aufsteller von dBase Dateien", mainAppFrame);
        if (!chooser.choose()) {
            mainAppFrame.setMessage("dBase-Import - Abbruch!");
            return null;
        }
        DataExchanger importer = new DbfImporterSpielorteAufsteller();
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
