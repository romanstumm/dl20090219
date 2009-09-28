package org.en.tealEye.printing.service;

import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.gruppen.check.model.OSetting;
import de.liga.dart.gruppen.service.GruppenService;
import de.liga.dart.model.Ligagruppe;
import de.liga.dart.model.Ligateam;
import de.liga.dart.model.Spielort;
import de.liga.dart.spielort.service.SpielortService;
import de.liga.util.CalendarUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.en.tealEye.framework.BeanTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Date;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 17.11.2007
 * Time: 12:43:23
 */
public class GroupTablePrintingService extends JPanel implements TablePrinting {
    private static final Log log = LogFactory.getLog(GroupTablePrintingService.class);

    /* private static final Logger log;

  static {
      log = Logger.getLogger(TablePrintingService.class);
  }  */

    private final JTable jTable;
    private final PrintingUtils printingUtils;
    private int mediaWidth;
    private int mediaHeight;

    private final int padX = 20;
    private final int padY = 20;

//    private int pageIndex = 0;
//    private double resizeFactor;
    private String pageOrientation = "Hochformat";

    private boolean touched = false;
    private final Vector<String[]> printVec;
    private final Vector<BufferedImage> im = new Vector<BufferedImage>();

    public GroupTablePrintingService(JTable objects) {
        this.jTable = objects;
        this.printingUtils = new PrintingUtils();
        aquireValues();
        this.setBackground(Color.WHITE);
        printVec = generatePrintableData(jTable);

        paintTable();
    }

    private void aquireValues() {
        mediaWidth = (int) printingUtils.getMediaWidth();
        mediaHeight = (int) printingUtils.getMediaHeight();
//        resizeFactor = printingUtils.getColumnWidthFactors(jTable, mediaWidth);
    }

    private void paintTable() {

        for (int i = 0; i <= getPageCount()+1; i++) {
            BufferedImage paintCanvas =
                    new BufferedImage(mediaWidth, mediaHeight, BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D g2 = paintCanvas.createGraphics();
            drawTable(g2, i);
            im.addElement(paintCanvas);
        }

    }

    public BufferedImage getPaintingCanvas(int pageNum) {
//        pageIndex = pageNum;
        return im.get(pageNum);
    }

    private void drawTable(Graphics2D g2, int pageNum) {
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, mediaWidth, mediaHeight);
        int index = printVec.size() - (45 * pageNum);
        boolean trigger = false;
        double columnFactor = printingUtils.getGroupColumnWidthFactors(mediaWidth);
        int[] columnWidth = printingUtils.getGroupColumnWidths();

        drawTableGrid(g2, index, printVec.size());
        g2.setColor(Color.BLACK);

        for (int i = 0; i < 45; i++) {
            if (trigger) break;
            double iteratedWidths = 0;
            int fieldIndex = ((i) + printVec.size() - (index));
            String[] lg = null;
            if (fieldIndex < printVec.size()) {
                lg = printVec.get(fieldIndex);
            }
            String[] rowStr;
            if (lg != null) {
                rowStr = lg;
                for (int j = 0; j < rowStr.length; j++) {
                    if (i <= index) {
                        g2.setColor(Color.BLACK);
                        String str = rowStr[j];
                        if (!(str == null)) {
                            while (g2.getFontMetrics().stringWidth(str) > printingUtils
                                    .getGroupColumnWidths(j) *
                                    printingUtils.getGroupColumnWidthFactors(mediaWidth) - 2 &&
                                    !(i % 9 == 0)) {
                                CharSequence strTemp = str.subSequence(0, str.length() - 1);
                                str = strTemp.toString();
                                log.info(str);
                                touched = true;
                            }
                            String tStr;
                            if (touched) {
                                tStr = str.substring(0, str.length() - 3);
                                str = tStr + "...";
                            }
                            g2.drawString(str, (int) (iteratedWidths + padX + 3),
                                    jTable.getRowHeight() * (i + 1) + padY - 3);
                        } else {
                        }
                        if (fieldIndex == printVec.size() ) trigger = true;
                        iteratedWidths = (iteratedWidths +
                                ((double) columnWidth[j] * columnFactor));
                    }
                    touched = false;
                }
            }
        }
    }

    private void drawTableGrid(Graphics2D g2, int index, int i) {
        int rowsPP = 45;
        int[] columnWidth = printingUtils.getGroupColumnWidths();
        double cFactor = printingUtils.getGroupColumnWidthFactors(mediaWidth);

        g2.setColor(Color.LIGHT_GRAY);
        //g2.fillRect(padX,padY,mediaWidth - (padX*2),jTable.getRowHeight());
        g2.setColor(Color.BLACK);
        double iteratedWidths;
        iteratedWidths = 0;
        g2.setStroke(new BasicStroke(0.5f));

        g2.drawLine(padX, padY, mediaWidth - padX, padY);
        g2.drawLine(padX, padY, padX, rowsPP * jTable.getRowHeight() + padY);
        //g2.drawLine(mediaWidth-padX,0+padY,mediaWidth-padX,rowsToPaint*jTable.getRowHeight());
        for (int y = 0; y < 45 + 1; y++) {
//            int fieldIndex = ((y) + i) - (index);
            g2.drawLine(padX, jTable.getRowHeight() * y + padY, mediaWidth - padX,
                    jTable.getRowHeight() * y + padY);
        }

        for (int cWidth : columnWidth) {
            iteratedWidths = (iteratedWidths + ((double) cWidth * cFactor));
            g2.drawLine(((int) iteratedWidths + padX), padY, ((int) iteratedWidths + padX),
                    rowsPP * jTable.getRowHeight() + padY);
        }
        g2.drawLine(padX, (45 * jTable.getRowHeight() + padY) * columnWidth.length,
                mediaWidth - padX, (45 * jTable.getRowHeight() + padY) * columnWidth.length);

        for (int y = 0; y < 36 + 1; y++) {
            int fieldIndex = ((y) + i) - (index);
            if ((fieldIndex == 0) || (fieldIndex % 9) == 0) {
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillRect((padX), jTable.getRowHeight() * y + padY, mediaWidth - (padX * 2),
                        jTable.getRowHeight());
                g2.setColor(Color.BLACK);
                g2.drawRect(padX, jTable.getRowHeight() * y + padY, mediaWidth - (padX * 2),
                        jTable.getRowHeight());
            }
        }
    }
/*
    private void drawTableHeader(Graphics2D g2, int page) {

    }

    private void drawTableStrings(Graphics2D g2, int j, int widthIteration, String str) {
        g2.setColor(Color.BLACK);
        if (str == null) {
        } else
            g2.drawString(str, widthIteration, j);
    }

    private void drawTablePageNum(Graphics2D g2, boolean pNum, int mode) {

    }*/

    private int round(double d) {
        int i = (int) d;
        double rest = d - i;
        if (rest != 0) {
            return i + 1;
        } else return i;
    }

    private Vector<String[]> generatePrintableData(JTable jTable) {
        final Vector<String[]> lgv = new Vector<String[]>();
        final JTable jTable1 = jTable;
                ServiceFactory.runAsTransaction(new Runnable() {
                    public void run() {

        int rowC = jTable1.getRowCount();
//        int cCount = jTable.getColumnModel().getColumnCount();

        GruppenService gs = ServiceFactory.get(GruppenService.class);
//        int rpp = printingUtils.getRowsPerPage(jTable.getRowHeight(), mediaHeight);
        Spielort service;
        int laufendeNummer = 0;
        Map<Long, OSetting> settingCache = new HashMap();
//        Vector<String> c = new Vector<String>();
        if (jTable1.getName().equals("Table_Ligagruppe")) {
            for (int i = 0; i < rowC; i++) {
                Object obj = ((BeanTableModel) jTable1.getModel()).getObject(i);
                Ligagruppe lg = ((Ligagruppe) obj);
                // hole Setting für diese Liga...
                OSetting oset = settingCache.get(lg.getLiga().getLigaId());
                if (oset == null) { // wenn noch nicht geholt...
                    oset = gs.getAufstellungsStatus(lg.getLiga()); // jetzt holen...
                    settingCache.put(lg.getLiga().getLigaId(), oset); // und cachen.
                }
                String[] strAr;
                for (int j = 0; j <= 8; j++) {
                    strAr = new String[7];
                    if (j == 0) {
                        strAr[0] = lg.getGruppenName();
                        strAr[1] = null;
                        strAr[2] = null;
                        strAr[3] = null;
                        strAr[4] = null;
                        strAr[5] = null;
                        strAr[6] = null;
                        lgv.addElement(strAr);
                    } else {

                        Ligateam lt;
                        try {
                            lt = lg.findSpiel(gs.findSpieleInGruppe(lg), j).getLigateam();
                        } catch (NullPointerException e) {
                            lt = null;
                        }
                        if (lt != null) {
                            service = ServiceFactory.get(SpielortService.class)
                                    .findSpielortById(lt.getSpielort().getSpielortId());

                            laufendeNummer++;
                            strAr[0] = String.valueOf(laufendeNummer);
                            strAr[1] = String.valueOf(lt.getLigateamspiel().getPlatzNr());
                            strAr[2] = lt.getTeamName();
                            strAr[3] = service.getSpielortName();
                            strAr[4] = oset.getTeam(lt.getLigateamId()).getStatus().getInfo();
                            strAr[5] = lt.getWochentagName();
                            strAr[6] = CalendarUtils.timeToString(lt.getSpielzeit());
                            lgv.addElement(strAr);

                        } else {
                            laufendeNummer++;
                            strAr[0] = String.valueOf(laufendeNummer);
                            strAr[1] = "";
                            strAr[2] = "";
                            strAr[3] = "";
                            strAr[4] = "";
                            strAr[5] = "";
                            strAr[6] = "";
                            lgv.addElement(strAr);
                        }
                    }
                }
            }
        } else {

        }
        //return lgv;
    }});
        return lgv;
   }

    @SuppressWarnings({"SuspiciousNameCombination"})
    public void rotate() {
        int mediaHeightT = mediaHeight;
        int mediaWidthT = mediaWidth;
        mediaHeight = 0;
        mediaWidth = 0;
        mediaHeight = mediaWidthT;
        mediaWidth = mediaHeightT;
//        resizeFactor = printingUtils.getColumnWidthFactors(jTable, mediaWidth);
        if (pageOrientation.equals("Hochformat"))
            pageOrientation = "Querformat";
        else
            pageOrientation = "Hochformat";
    }

    public void setTableValues() {
        // do nothing
    }

    public void generateDoc() {

//        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        /*if (pageOrientation.equals("Hochformat")) {
            aset.add(OrientationRequested.PORTRAIT);
        } else if (!pageOrientation.equals("Hochformat")) {
            aset.add(OrientationRequested.LANDSCAPE);
        }
        aset.add(new JobName("Tabelle", Locale.GERMANY));
        MediaPrintableArea pa;
        pa = new MediaPrintableArea(0, 0, mediaWidth, mediaHeight, MediaPrintableArea.MM);
        aset.add(pa); */
        PrinterJob job = printingUtils.getPrinterJob();
        Paper paper = new Paper();
        paper.setImageableArea(0, 0, mediaWidth, mediaHeight);
        job.setPrintable(this);
        job.setJobName("print-liga-group-" + new Date());
        boolean ok = job.printDialog();
        //Attribute[] a = aset.toArray();

        //if(log.isDebugEnabled()) for (Attribute anA : a) log.debug(anA.getName());
        //for (int i = 0; i < job.getPageFormat(aset).getMatrix().length; i++)
        //log.info(job.getPageFormat(aset).getMatrix()[i]);
        if (ok) {
            try {
                job.print();
            } catch (PrinterException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }

    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {

        int noPages = getPageCount();
        if (pageIndex < noPages) {
//            this.pageIndex = pageIndex;

            Graphics2D g2 = (Graphics2D) g;
            drawTable(g2, pageIndex);

            //g2.scale(0.5,0.5);
            return Printable.PAGE_EXISTS;
        } else return Printable.NO_SUCH_PAGE;
    }

    public int getPageCount() {
        return round((double) generatePrintableData(jTable).size() /
                printingUtils.getRowsPerPage(jTable.getRowHeight(), mediaHeight))+1;
    }

    public void repaintCanvas() {
        // do nothing
    }

    public void setLeftBorder(int leftBorder) {
        // do nothing
    }

    public void setUpperBorder(int upperBorder) {
        // do nothing
    }

    public void setPrintPageNum(boolean b) {
        // do nothing
    }

    public void paintCanvas() {
        // do nothing
    }
}
