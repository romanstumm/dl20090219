package org.en.tealEye.printing.gui;

import de.liga.dart.common.service.ServiceFactory;
import org.en.tealEye.printing.controller.PrintingController;
import org.en.tealEye.printing.controller.WindowController;
import org.en.tealEye.printing.service.*;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class TablePrintingFrame extends JFrame implements ActionListener, KeyListener, ChangeListener {


    private JTable sourceTable;
    private TablePrinting tablePrintService = null;
    private final PrintingController pControl;
    private int pageIndex = 0;
    private LabelPrintingService labelPrinting = null;
    private int pagesNeeded = 0;
    private final WindowController windowController;
    private final String mode;

    private JTextField leftSide;
    private JTextField upperSide;
    private JCheckBox printPageNb;
    private JComboBox jComboBox2;
    private JSpinner etiZeilenAbstandSpinner;
    private int initialLineSpaceInt = 5;
    private JLabel etiZeilenAbstandSpinnerLabel;


    public TablePrintingFrame(PrintingController pControl, JTable sourceTable,
                              String mode, WindowController winCon) {
        this.sourceTable = sourceTable;
        this.mode = mode;
        initComponents();
        this.pControl = pControl;
        this.windowController = winCon;

        if (mode.equals("alle")) {
            tablePrintService = new TablePrintingService(sourceTable);
            //pagesNeeded = tablePrintService.getPageCount();
        }
        if (mode.equals("auswahl"))
            tablePrintService = new TableSelectionPrintingService(sourceTable);
        if (mode.equals("gruppen"))
            tablePrintService = new GroupTablePrintingService(sourceTable);
        if (mode.equals("etikett")) {
            labelPrinting = new LabelPrintingService(sourceTable,
                  Integer.parseInt(etiBreite.getValue().toString()),
                  Integer.parseInt(etiHoehe.getValue().toString()),
                  Integer.parseInt(etiRandSeite.getValue().toString()),
                  Integer.parseInt(etiRandOben.getValue().toString()),
                  Integer.parseInt(etiAnzahl.getValue().toString()),
                  Integer.parseInt(etiZeilenAbstandSpinner.getValue().toString())
            );
            setLabelValues();
            pagesNeeded = labelPrinting.getAllPagesNeeded();
            labelPrinting.setGuides(true);
            labelPrinting.repaintCanvas(pageIndex);
        }

        loadPreview();
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        setTitle("Druckvorschau");
        setName("TablePrintingFrame");
        jToolBar1 = new javax.swing.JToolBar();
        jToolBar3 = new javax.swing.JToolBar();
        jPanel2 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        jPanel3 = new javax.swing.JPanel();
        jButton8 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jToolBar8 = new javax.swing.JToolBar();
        jPanel4 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        contentPanel = new javax.swing.JPanel();
        jToolBar4 = new javax.swing.JToolBar();
        jToolBar5 = new javax.swing.JToolBar();
        jPanel5 = new javax.swing.JPanel();
        etiHoeheLabel = new javax.swing.JLabel();
        etiBreiteLabel = new javax.swing.JLabel();
        etiHoehe = new javax.swing.JSpinner();
        etiRandObenLabel = new javax.swing.JLabel();
        etiBreite = new javax.swing.JSpinner();
        etiRandOben = new javax.swing.JSpinner();
        etiRandUntenLabel = new javax.swing.JLabel();
        etiRandSeite = new javax.swing.JSpinner();
        jToolBar6 = new javax.swing.JToolBar();
        jPanel6 = new javax.swing.JPanel();
        etiAnzahlLabel = new javax.swing.JLabel();
        etiAnzahl = new javax.swing.JSpinner();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox(new String[]{"Hochformat", "Querformat"});
        jToolBar7 = new javax.swing.JToolBar();
        jPanel7 = new javax.swing.JPanel();
        fontFarbeLabel = new javax.swing.JLabel();
        fontType = new javax.swing.JButton();
        guides = new javax.swing.JCheckBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu1.add(new JMenuItem("Beenden"));
        jMenu1.getItem(0).setActionCommand("Beenden");
        jMenu1.getItem(0).addActionListener(this);
        teamCount = new JCheckBox();

        leftSide = new JTextField("10", 3);
        upperSide = new JTextField("10", 3);
        printPageNb = new JCheckBox("Seitenzahl drucken");
        jComboBox2.setActionCommand("orientation");
        printPageNb.setActionCommand("printPageNum");
        leftSide.addKeyListener(this);
        upperSide.addKeyListener(this);
        jComboBox2.addActionListener(this);
        printPageNb.addActionListener(this);

        etiHoehe.setModel(new SpinnerNumberModel(37,1,100,1));
        etiBreite.setModel(new SpinnerNumberModel(70,1,100,1));
        etiRandSeite.setModel(new SpinnerNumberModel(10,1,100,1));
        etiRandOben.setModel(new SpinnerNumberModel(10,1,100,1));
        etiAnzahl.setModel(new SpinnerNumberModel(18,1,100,1));

        etiHoehe.addChangeListener(this);
        etiBreite.addChangeListener(this);
        etiRandSeite.addChangeListener(this);
        etiRandOben.addChangeListener(this);
        etiAnzahl.addChangeListener(this);

        etiZeilenAbstandSpinner = new JSpinner(new SpinnerNumberModel(initialLineSpaceInt,1,100,1));
        etiZeilenAbstandSpinnerLabel = new JLabel("Zeilenabstand");
        etiZeilenAbstandSpinner.addChangeListener(this);

        etiHoehe.addKeyListener(this);
        etiBreite.addKeyListener(this);
        etiRandSeite.addKeyListener(this);
        etiRandOben.addKeyListener(this);
        etiAnzahl.addKeyListener(this);

        guides.setSelected(true);
        guides.addActionListener(this);
        fontType.addActionListener(this);
        teamCount.setSelected(true);
        teamCount.addActionListener(this);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jToolBar1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 20, 1, 1,
              new java.awt.Color(153, 153, 153)));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 2);
        gridBagConstraints.gridx = 0;
        jPanel2.add(new JLabel("Abstand Links"), gridBagConstraints);


        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.gridx = 1;
        jPanel2.add(leftSide, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.gridx = 2;
        jPanel2.add(new JLabel("Abstand Oben"), gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridx = 0;
        jPanel2.add(jComboBox2, gridBagConstraints);




        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.gridx = 3;
        jPanel2.add(upperSide, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.gridx = 4;
        jPanel2.add(printPageNb, gridBagConstraints);

        if (!mode.equals("etikett")) jToolBar3.add(jPanel2);

        jToolBar1.add(jToolBar3);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton8.setText("Vorw�rts");
        jButton8.setActionCommand("forward");
        jButton8.addActionListener(this);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jButton8, gridBagConstraints);

        jButton7.setText("Home");
        jButton7.setActionCommand("home");
        jButton7.addActionListener(this);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jButton7, gridBagConstraints);

        jButton6.setText("Zur�ck");
        jButton6.setActionCommand("backward");
        jButton6.addActionListener(this);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 2);
        jPanel3.add(jButton6, gridBagConstraints);

        jToolBar2.add(jPanel3);

        jToolBar1.add(jToolBar2);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton2.setText("Abbrechen");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel4.add(jButton2, gridBagConstraints);

        jButton1.setText("Drucken");
        jButton1.setBorderPainted(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 2);
        jPanel4.add(jButton1, gridBagConstraints);

        jToolBar8.add(jPanel4);

        jToolBar1.add(jToolBar8);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.NORTH);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(contentPanel);
        contentPanel.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
              jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 775, Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(
              jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 368, Short.MAX_VALUE));
        jScrollPane1.setViewportView(contentPanel);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jToolBar4.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 20, 1, 1,
              new java.awt.Color(153, 153, 153)));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        etiHoeheLabel.setText("H\u00f6he");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 10);
        jPanel5.add(etiHoeheLabel, gridBagConstraints);

        etiBreiteLabel.setText("Breite");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 10);
        jPanel5.add(etiBreiteLabel, gridBagConstraints);

        etiHoehe.setPreferredSize(new java.awt.Dimension(40, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel5.add(etiHoehe, gridBagConstraints);

        etiRandObenLabel.setText("Oberer Rand");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 10);
        jPanel5.add(etiRandObenLabel, gridBagConstraints);

        etiBreite.setPreferredSize(new java.awt.Dimension(40, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanel5.add(etiBreite, gridBagConstraints);

        etiRandOben.setPreferredSize(new java.awt.Dimension(40, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        jPanel5.add(etiRandOben, gridBagConstraints);

        etiRandUntenLabel.setText("Seitlicher Rand");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 10);
        jPanel5.add(etiRandUntenLabel, gridBagConstraints);

        etiRandSeite.setPreferredSize(new java.awt.Dimension(40, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        jPanel5.add(etiRandSeite, gridBagConstraints);

        etiZeilenAbstandSpinner.setPreferredSize(new java.awt.Dimension(40, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        //gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridy = 3;
        jPanel5.add(etiZeilenAbstandSpinner, gridBagConstraints);

        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridy = 3;
        jPanel5.add(etiZeilenAbstandSpinnerLabel, gridBagConstraints);

        jToolBar5.add(jPanel5);

        jToolBar4.add(jToolBar5);

        jPanel6.setLayout(new java.awt.GridBagLayout());

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        etiAnzahlLabel.setText("Anzahl der Etiketten");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 10);
        jPanel6.add(etiAnzahlLabel, gridBagConstraints);

        etiAnzahl.setPreferredSize(new java.awt.Dimension(40, 20));
        jPanel6.add(etiAnzahl, new java.awt.GridBagConstraints());

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(
              new String[]{"Hochformat", "Querformat"}));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        //gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel6.add(jComboBox1, gridBagConstraints);
        jComboBox1.addActionListener(this);
        jComboBox1.setActionCommand("orientation");

        teamCount.setText("Gruppenanzeige");
        teamCount.setActionCommand("teamCount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridx = 1;
        jPanel6.add(teamCount, gridBagConstraints);

        jToolBar6.add(jPanel6);

        jToolBar4.add(jToolBar6);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        fontFarbeLabel.setText("Schrift");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 13);
        jPanel7.add(fontFarbeLabel, gridBagConstraints);

        fontType.setText("Schrift");
        fontType.setActionCommand("fontConfig");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(fontType, gridBagConstraints);

        guides.setText("Guides anzeigen");
        guides.setActionCommand("guides");
        guides.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        guides.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(guides, gridBagConstraints);

        jToolBar7.add(jPanel7);

        jToolBar4.add(jToolBar7);

        if (mode.equals("etikett"))
            getContentPane().add(jToolBar4, java.awt.BorderLayout.SOUTH);

        jMenu1.setText("Datei");
        jMenuBar1.add(jMenu1);


        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setJMenuBar(jMenuBar1);
        setVisible(true);
        pack();
        jButton1.setActionCommand("print");
        jButton1.addActionListener(this);
        jButton2.addActionListener(this);

    }// </editor-fold>

    /** @param args the command line arguments */

    // Variablendeklaration - nicht modifizieren
    private JSpinner etiAnzahl;
    private javax.swing.JLabel etiAnzahlLabel;
    private JSpinner etiBreite;
    private javax.swing.JLabel etiBreiteLabel;
    private JSpinner etiHoehe;
    private javax.swing.JLabel etiHoeheLabel;
    private JSpinner etiRandOben;
    private JLabel etiRandObenLabel;
    private JSpinner etiRandSeite;
    private JLabel etiRandUntenLabel;
    private javax.swing.JButton fontType;
    private javax.swing.JLabel fontFarbeLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBar5;
    private javax.swing.JToolBar jToolBar6;
    private javax.swing.JToolBar jToolBar7;
    private javax.swing.JToolBar jToolBar8;
    private javax.swing.JCheckBox guides;
    private javax.swing.JCheckBox teamCount;

    public void rotateAndUpdate() {
        if (tablePrintService != null) {
            tablePrintService.rotate();
            setTableValues();
            tablePrintService.repaintCanvas();
        } else if (labelPrinting != null) {
            labelPrinting.rotate();
        }
        loadPreview();
    }

    private void setTableValues() {
        pagesNeeded = tablePrintService.getPageCount();
        tablePrintService.setLeftBorder(Integer.parseInt(leftSide.getText()));
        tablePrintService.setUpperBorder(Integer.parseInt(upperSide.getText()));
    }

    public void updateImage() {
        loadPreview();
    }

    public void turnPageForw() {
        getPageCount();
        if (pageIndex + 1 < pagesNeeded) pageIndex++;
        loadPreview();
    }

    public void turnPageBack() {
        if (pageIndex != 0) pageIndex--;
        loadPreview();
    }

    public void turnPageToStart() {
        pageIndex = 0;
        loadPreview();
    }

    public void actionPerformed(final ActionEvent e) {
        final String ac = e.getActionCommand();
        final TablePrintingFrame ich = this;
        ServiceFactory.runAsTransaction(new Runnable() {
            public void run() {
                if (ac.equals("print")) {
                    if (tablePrintService != null) tablePrintService.generateDoc();
                    if (labelPrinting != null) {
                        labelPrinting.flushStringIndex();
                        labelPrinting.setLabelValues();
                        labelPrinting.generateDoc();
                    }
                }

                if (ac.equals("forward")) {
                    turnPageForw();
                } else if (ac.equals("home")) {
                    turnPageToStart();
                } else if (ac.equals("backward")) {
                    turnPageBack();
                } else if (ac.equals("orientation")) {
                    rotateAndUpdate();
                } else if (ac.equals("Abbrechen")) {
                    TablePrintingFrame.this.dispose();

                } else if (ac.equals("Beenden")) {
                    TablePrintingFrame.this.dispose();
                } else if (ac.equals("fontConfig")) {
                    new LabelFontFrame(ich);
//                    System.out.println("FontCOnfig");
                } else if (ac.equals("guides")) {
                    if (guides.isSelected()) labelPrinting.setGuides(true);
                    else labelPrinting.setGuides(false);
                    setLabelValues();
                    labelPrinting.repaintCanvas(pageIndex);
                    loadPreview();
                } else if (ac.equals("teamCount")) {
                    if (teamCount.isSelected()) labelPrinting.setTeamsInSpielort(true);
                    else labelPrinting.setTeamsInSpielort(false);
                    setLabelValues();
                    labelPrinting.repaintCanvas(pageIndex);
                    loadPreview();
                } else if (ac.equals("printPageNum")) {
                    if (printPageNb.isSelected()) tablePrintService.setPrintPageNum(true);
                    else tablePrintService.setPrintPageNum(false);
                } else if (ac.equals("orientation")) {
//                    System.out.printf("boom!!!");
                    rotateAndUpdate();
                }
            }
        });
    }

    private void loadPreview() {
        JLabel contentLabel = null;
        if (tablePrintService != null && labelPrinting == null) {
            tablePrintService.setTableValues();
            contentLabel = new JLabel(
                  new ImageIcon(tablePrintService.getPaintingCanvas(pageIndex)));
        }
        if (labelPrinting != null && tablePrintService == null) {
            labelPrinting.setLabelCount(Integer.parseInt(etiAnzahl.getValue().toString()));
            BufferedImage canvas = labelPrinting.getPaintingCanvas(pageIndex);
            if (canvas != null) {
                contentLabel = new JLabel(new ImageIcon(canvas));
                contentPanel.add(contentLabel, BorderLayout.CENTER);
            }
//               System.out.println(etiAnzahl.getText());
        }

        contentPanel.setLayout(new BorderLayout());
        contentPanel.setVisible(false);
        contentPanel.removeAll();
        contentPanel.add(contentLabel, BorderLayout.CENTER);
        contentPanel.setVisible(true);

    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        // do nothing
    }

    public void keyReleased(KeyEvent e) {
        int kc = e.getKeyCode();
//        System.out.println("Key typed!");

        if (kc == KeyEvent.VK_ENTER) {
            Object source = e.getSource();
            if (source.equals(etiAnzahl)) {
                setLabelValues();
                pagesNeeded = 0;
                pagesNeeded = labelPrinting.getAllPagesNeeded();
                labelPrinting.repaintCanvas(pageIndex);
                pageIndex = 0;
                loadPreview();
            } else if (source.equals(etiRandSeite)) {
                setLabelValues();
                labelPrinting.repaintCanvas(pageIndex);
                loadPreview();
            } else if (source.equals(etiRandOben)) {
                setLabelValues();
                labelPrinting.repaintCanvas(pageIndex);
                loadPreview();
            } else if (source.equals(etiHoehe)) {
                setLabelValues();
                labelPrinting.repaintCanvas(pageIndex);
                loadPreview();
            } else if (source.equals(etiBreite)) {
                setLabelValues();
                labelPrinting.repaintCanvas(pageIndex);
                loadPreview();
            } else if (source.equals(leftSide)) {
                setTableValues();
                tablePrintService.repaintCanvas();
                loadPreview();
            } else if (source.equals(upperSide)) {
                setTableValues();
                tablePrintService.repaintCanvas();
                loadPreview();
            }
        }
    }

    private void setLabelValues() {
        labelPrinting.setLabelCount(Integer.parseInt(etiAnzahl.getValue().toString()));
        labelPrinting.setLabelSideBorder(Integer.parseInt(etiRandSeite.getValue().toString()));
        labelPrinting.setLabelUpperBorder(Integer.parseInt(etiRandOben.getValue().toString()));
        labelPrinting.setLabelHeight(Integer.parseInt(etiHoehe.getValue().toString()));
        labelPrinting.setLabelWidth(Integer.parseInt(etiBreite.getValue().toString()));
        labelPrinting.setEtiZeilenAbstand(Integer.parseInt(etiZeilenAbstandSpinner.getValue().toString()));
        pagesNeeded = labelPrinting.getAllPagesNeeded();

    }

    public void setLabelFont(Font font) {
        labelPrinting.setLabelFont(font);
        setLabelValues();
        labelPrinting.repaintCanvas(pageIndex);
        loadPreview();
    }

    public void getPageCount() {
        if (tablePrintService != null) {
            pagesNeeded = tablePrintService.getPageCount();
        } else if (labelPrinting != null) {
            pagesNeeded = labelPrinting.getAllPagesNeeded();
        }
    }

    public void stateChanged(ChangeEvent e) {
        JSpinner spinner = (JSpinner) e.getSource();
        int i = Integer.parseInt(spinner.getValue().toString());

            if(spinner.equals(etiAnzahl)){
             setLabelValues();
                pagesNeeded = 0;
                pagesNeeded = labelPrinting.getAllPagesNeeded();
                labelPrinting.repaintCanvas(pageIndex);
                pageIndex = 0;
                loadPreview();
            }else if(spinner.equals(etiBreite)){
                setLabelValues();
                labelPrinting.repaintCanvas(pageIndex);
                loadPreview();
            }else if(spinner.equals(etiHoehe)){
                setLabelValues();
                labelPrinting.repaintCanvas(pageIndex);
                loadPreview();
            }else if(spinner.equals(etiRandOben)){
                setLabelValues();
                labelPrinting.repaintCanvas(pageIndex);
                loadPreview();
            }else if(spinner.equals(etiRandSeite)){
                setLabelValues();
                labelPrinting.repaintCanvas(pageIndex);
                loadPreview();
            }else if(spinner.equals(etiZeilenAbstandSpinner)){
                setLabelValues();
                labelPrinting.repaintCanvas(pageIndex);
                loadPreview();                
            }else{
            }

    }

}
