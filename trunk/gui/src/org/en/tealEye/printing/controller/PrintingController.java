package org.en.tealEye.printing.controller;

import de.liga.dart.common.service.ServiceFactory;
import org.en.tealEye.guiExt.ExtPanel.ExtJTablePanel;
import org.en.tealEye.guiMain.MainAppFrame;
import org.en.tealEye.printing.gui.*;

import javax.swing.*;
import java.awt.event.*;

/**
 * Description:   <br/>
 * User: Stephan
 * Date: 16.11.2007, 18:09:43
 */
public class PrintingController implements ActionListener, MouseListener, WindowListener {

    /*private static final int PRINT_ALL = 0;
    private static final int PRINT_SELECTED = 1;
    private static final int PRINT_GROUPS = 2;
    private static final int PRINT_LABEL = 3;*/
    private final PrintingDialogFrame pframe;
//    private Vector<String[]> printWork;
    private final MainAppFrame mainApp;
//    private JInternalFrame activeFrame;
    private TablePrintingFrame tablePrintFrame;
    private JFrame baseFrame;
    private final WindowController winCon = new WindowController();

    public PrintingController(MainAppFrame mainApp) {
        this.mainApp = mainApp;
        pframe = new PrintingDialogFrame(this);
    }

    public void actionPerformed(final ActionEvent e) {
        final String action = e.getActionCommand();
        Object obj = e.getSource();



                if (action.equals("Drucken")) {
                    String mode = pframe.getSelectedRadioBox();
                    if (mode.equals("alle")) tablePrintFrame =
                                new TablePrintingFrame(PrintingController.this, getPrintableTable(),
                                        mode, winCon);
                    if (mode.equals("auswahl"))
                        tablePrintFrame =
                                new TablePrintingFrame(PrintingController.this, getPrintableTable(),
                                        mode, winCon);
                    if (mode.equals("gruppen"))
                        tablePrintFrame =
                                new TablePrintingFrame(PrintingController.this, getPrintableTable(),
                                        mode, winCon);

                    if (mode.equals("etikett"))
                        tablePrintFrame =
                                new TablePrintingFrame(PrintingController.this, getPrintableTable(),
                                        mode, winCon);
                    if (mode.equals("envelope")){
                        //new FieldMapper(EnvelopePrintFrame.class, EnvelopePrintFrameMethods.class, getPrintableTable());
                        GlobalListenerService gls = new GlobalListenerService();
                        CacheStack cache = new CacheStack();
                        CentralDispatch.setListenerService(gls);
                        CentralDispatch.setupCacheStack(cache);
                        CentralDispatch.storeBoundMethodClass(this);
                        CentralDispatch.storeClassBundle(EnvelopePrintFrame.class,EnvelopePrintFrameMethods.class);
                        CentralDispatch.invokeCustomMethods();
                        CentralDispatch.addListener();

                    }
                    pframe.dispose();
                }
                               

                 if (action.equals("Abbrechen")) {
                    pframe.dispose();
                } else if (action.equals("forward")) {
                    tablePrintFrame.turnPageForw();
                } else if (((JButton) obj).getActionCommand().equals("home")) {
                    tablePrintFrame.turnPageToStart();
                } else if (((JButton) obj).getActionCommand().equals("backward")) {
                    tablePrintFrame.turnPageBack();
                } else if (((JButton) obj).getActionCommand().equals("print")) {

                } else if (((JButton) obj).getActionCommand().equals("decline")) {

                } else if (((JButton) obj).getActionCommand().equals("zoomIn")) {

                } else if (((JButton) obj).getActionCommand().equals("normalize")) {

                } else if (((JButton) obj).getActionCommand().equals("zoomOut")) {

                } /*else if (obj instanceof JComboBox) {
//                    Object o = ((JComboBox) obj).getSelectedItem();
                    tablePrintFrame.rotateAndUpdate();
                }*/

    }

    public JTable getPrintableTable() {
        ExtJTablePanel panel = mainApp.getActiveFramePanel();
        return panel.getPanelTable();
    }


    public void mouseClicked(MouseEvent e) {
//        Object obj = e.getSource();

        // do nothing
    }

    public void mousePressed(MouseEvent e) {
        // do nothing
    }

    public void mouseReleased(MouseEvent e) {
        // do nothing
    }

    public void mouseEntered(MouseEvent e) {
        // do nothing
    }

    public void mouseExited(MouseEvent e) {
        // do nothing
    }

    public void windowOpened(WindowEvent e) {
        // do nothing
    }

    public void windowClosing(WindowEvent e) {
        tablePrintFrame.dispose();
    }

    public void windowClosed(WindowEvent e) {
        tablePrintFrame.dispose();
    }

    public void windowIconified(WindowEvent e) {
        // do nothing
    }

    public void windowDeiconified(WindowEvent e) {
        // do nothing
    }

    public void windowActivated(WindowEvent e) {
        // do nothing
    }

    public void windowDeactivated(WindowEvent e) {
        // do nothing
    }
}
