package org.en.tealEye.printing.gui;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EnvelopePrintFrame.java
 *
 * Created on 21.08.2009, 00:57:51
 */


import org.en.tealEye.printing.controller.ExtResourceBundle;
import org.en.tealEye.printing.controller.ResourceBundleExt;

import java.util.ResourceBundle;

/**
 *
 * @author Stephan Pudras
 */
public class EnvelopePrintFrame extends javax.swing.JFrame {

    /** Creates new form EnvelopePrintFrame */
    public EnvelopePrintFrame() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        toolPanel = new javax.swing.JPanel();
        epAddressFontBt = new javax.swing.JButton();
        epSenderFontBt = new javax.swing.JButton();
        epGraphicBt = new javax.swing.JButton();
        epGraphicCheckBox = new javax.swing.JCheckBox();
        epEnvelopeType = new javax.swing.JComboBox();
        epEnvelopeAxis = new javax.swing.JComboBox();
        enSenderCheckBox = new javax.swing.JCheckBox();
        epPrintBt = new javax.swing.JButton();
        epDeclineBt = new javax.swing.JButton();
        epFowardBt = new javax.swing.JButton();
        epHomeBt = new javax.swing.JButton();
        epBackwardBt = new javax.swing.JButton();
        epEnvAxisLabel = new javax.swing.JLabel();
        epEnvTypeLabel = new javax.swing.JLabel();
        epAddressFontPreviewTB = new javax.swing.JTextField();
        epSenderFontPreviewTB = new javax.swing.JTextField();
        epGraphicPathTB = new javax.swing.JTextField();
        epSenderLocCornerRB = new javax.swing.JRadioButton();
        epSenderLocLineRB = new javax.swing.JRadioButton();
        epCount = new javax.swing.JSpinner();
        epCountLabel = new javax.swing.JLabel();
        epOrderTable = new javax.swing.JRadioButton();
        epOrderTeam = new javax.swing.JRadioButton();
        epSenderValueBt = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        epAddressUpBt = new javax.swing.JButton();
        epAddressRightBt = new javax.swing.JButton();
        epAddressLeftBt = new javax.swing.JButton();
        epAddressDownBt = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        epSenderUpBt = new javax.swing.JButton();
        epSenderRightBt = new javax.swing.JButton();
        epSenderLeftBt = new javax.swing.JButton();
        epSenderDownBt = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        epGraphicUpBt = new javax.swing.JButton();
        epGraphicRightBt = new javax.swing.JButton();
        epGraphicLeftBt = new javax.swing.JButton();
        epGraphicDownBt = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        previewPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        ExtResourceBundle resourceMap = new ExtResourceBundle("org.en.tealEye.resources.EnvelopePrintFrame");
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jSplitPane1.setDividerLocation(500);
        jSplitPane1.setDividerSize(10);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.6);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N
        jScrollPane1.setViewportView(toolPanel);

        jTabbedPane1.setMaximumSize(new java.awt.Dimension(1029, 200));
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(1029, 200));
        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        toolPanel.setMinimumSize(new java.awt.Dimension(1024, 200));
        toolPanel.setName("toolPanel"); // NOI18N
        toolPanel.setPreferredSize(new java.awt.Dimension(1024, 200));
        toolPanel.setRequestFocusEnabled(false);
        toolPanel.setLayout(new java.awt.GridBagLayout());

        epAddressFontBt.setText(resourceMap.getString("epAddressFontBt.text")); // NOI18N
        epAddressFontBt.setToolTipText(resourceMap.getString("epAddressFontBt.toolTipText")); // NOI18N
        epAddressFontBt.setName("epAddressFontBt"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        toolPanel.add(epAddressFontBt, gridBagConstraints);

        epSenderFontBt.setText(resourceMap.getString("epSenderFontBt.text")); // NOI18N
        epSenderFontBt.setToolTipText(resourceMap.getString("epSenderFontBt.toolTipText")); // NOI18N
        epSenderFontBt.setName("epSenderFontBt"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        toolPanel.add(epSenderFontBt, gridBagConstraints);

        epGraphicBt.setText(resourceMap.getString("epGraphicBt.text")); // NOI18N
        epGraphicBt.setToolTipText(resourceMap.getString("epGraphicBt.toolTipText")); // NOI18N
        epGraphicBt.setName("epGraphicBt"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        toolPanel.add(epGraphicBt, gridBagConstraints);

        epGraphicCheckBox.setText(resourceMap.getString("epGraphicCheckBox.text")); // NOI18N
        epGraphicCheckBox.setToolTipText(resourceMap.getString("epGraphicCheckBox.toolTipText")); // NOI18N
        epGraphicCheckBox.setName("epGraphicCheckBox"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        toolPanel.add(epGraphicCheckBox, gridBagConstraints);

        epEnvelopeType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "DIN C3   324 � 458", "DIN E4   280 � 400", "DIN B4   250 � 353", "DIN C4   229 � 324", "DIN B5   176 � 250", "DIN C5   162 � 229", "DIN C5/6   114 x 229", "DIN DL C5/6   110 � 220", "DIN B6   125 � 176", "DIN C6   114 � 162" }));
        epEnvelopeType.setMinimumSize(new java.awt.Dimension(150, 20));
        epEnvelopeType.setName("epEnvelopeType"); // NOI18N
        epEnvelopeType.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        toolPanel.add(epEnvelopeType, gridBagConstraints);

        epEnvelopeAxis.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Horizontal", "Vertikal" }));
        epEnvelopeAxis.setToolTipText(resourceMap.getString("epEnvelopeAxis.toolTipText")); // NOI18N
        epEnvelopeAxis.setMinimumSize(new java.awt.Dimension(150, 20));
        epEnvelopeAxis.setName("epEnvelopeAxis"); // NOI18N
        epEnvelopeAxis.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        toolPanel.add(epEnvelopeAxis, gridBagConstraints);

        enSenderCheckBox.setText(resourceMap.getString("enSenderCheckBox.text")); // NOI18N
        enSenderCheckBox.setToolTipText(resourceMap.getString("enSenderCheckBox.toolTipText")); // NOI18N
        enSenderCheckBox.setName("enSenderCheckBox"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        toolPanel.add(enSenderCheckBox, gridBagConstraints);

        epPrintBt.setIcon(resourceMap.getIcon("epPrintBt.icon")); // NOI18N
        epPrintBt.setMnemonic(1);
        epPrintBt.setText(resourceMap.getString("epPrintBt.text")); // NOI18N
        epPrintBt.setToolTipText(resourceMap.getString("epPrintBt.toolTipText")); // NOI18N
        epPrintBt.setName("epPrintBt"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 5;
        toolPanel.add(epPrintBt, gridBagConstraints);

        epDeclineBt.setIcon(resourceMap.getIcon("epDeclineBt.icon")); // NOI18N
        epDeclineBt.setMnemonic(1);
        epDeclineBt.setText(resourceMap.getString("epDeclineBt.text")); // NOI18N
        epDeclineBt.setToolTipText(resourceMap.getString("epDeclineBt.toolTipText")); // NOI18N
        epDeclineBt.setName("epDeclineBt"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 5;
        toolPanel.add(epDeclineBt, gridBagConstraints);

        epFowardBt.setIcon(resourceMap.getIcon("epFowardBt.icon")); // NOI18N
        epFowardBt.setText(resourceMap.getString("epFowardBt.text")); // NOI18N
        epFowardBt.setToolTipText(resourceMap.getString("epFowardBt.toolTipText")); // NOI18N
        epFowardBt.setName("epFowardBt"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        toolPanel.add(epFowardBt, gridBagConstraints);

        epHomeBt.setIcon(resourceMap.getIcon("epHomeBt.icon")); // NOI18N
        epHomeBt.setText(resourceMap.getString("epHomeBt.text")); // NOI18N
        epHomeBt.setToolTipText(resourceMap.getString("epHomeBt.toolTipText")); // NOI18N
        epHomeBt.setName("epHomeBt"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        toolPanel.add(epHomeBt, gridBagConstraints);

        epBackwardBt.setIcon(resourceMap.getIcon("epBackwardBt.icon")); // NOI18N
        epBackwardBt.setText(resourceMap.getString("epBackwardBt.text")); // NOI18N
        epBackwardBt.setToolTipText(resourceMap.getString("epBackwardBt.toolTipText")); // NOI18N
        epBackwardBt.setName("epBackwardBt"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        toolPanel.add(epBackwardBt, gridBagConstraints);

        epEnvAxisLabel.setText(resourceMap.getString("epEnvAxisLabel.text")); // NOI18N
        epEnvAxisLabel.setName("epEnvAxisLabel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        toolPanel.add(epEnvAxisLabel, gridBagConstraints);

        epEnvTypeLabel.setText(resourceMap.getString("epEnvTypeLabel.text")); // NOI18N
        epEnvTypeLabel.setName("epEnvTypeLabel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        toolPanel.add(epEnvTypeLabel, gridBagConstraints);

        epAddressFontPreviewTB.setText(resourceMap.getString("epAddressFontPreviewTB.text")); // NOI18N
        epAddressFontPreviewTB.setMinimumSize(new java.awt.Dimension(250, 30));
        epAddressFontPreviewTB.setName("epAddressFontPreviewTB"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        toolPanel.add(epAddressFontPreviewTB, gridBagConstraints);

        epSenderFontPreviewTB.setText(resourceMap.getString("epSenderFontPreviewTB.text")); // NOI18N
        epSenderFontPreviewTB.setMinimumSize(new java.awt.Dimension(250, 30));
        epSenderFontPreviewTB.setName("epSenderFontPreviewTB"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        toolPanel.add(epSenderFontPreviewTB, gridBagConstraints);

        epGraphicPathTB.setColumns(200);
        epGraphicPathTB.setText(resourceMap.getString("epGraphicPathTB.text")); // NOI18N
        epGraphicPathTB.setMinimumSize(new java.awt.Dimension(250, 30));
        epGraphicPathTB.setName("epGraphicPathTB"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        toolPanel.add(epGraphicPathTB, gridBagConstraints);

        buttonGroup1.add(epSenderLocCornerRB);
        epSenderLocCornerRB.setText(resourceMap.getString("epSenderLocCornerRB.text")); // NOI18N
        epSenderLocCornerRB.setName("epSenderLocCornerRB"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        toolPanel.add(epSenderLocCornerRB, gridBagConstraints);

        buttonGroup1.add(epSenderLocLineRB);
        epSenderLocLineRB.setText(resourceMap.getString("epSenderLocLineRB.text")); // NOI18N
        epSenderLocLineRB.setName("epSenderLocLineRB"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        toolPanel.add(epSenderLocLineRB, gridBagConstraints);

        epCount.setMinimumSize(new java.awt.Dimension(50, 25));
        epCount.setName("epCount"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        toolPanel.add(epCount, gridBagConstraints);

        epCountLabel.setText(resourceMap.getString("epCountLabel.text")); // NOI18N
        epCountLabel.setName("epCountLabel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        toolPanel.add(epCountLabel, gridBagConstraints);

        buttonGroup2.add(epOrderTable);
        epOrderTable.setText(resourceMap.getString("epOrderTable.text")); // NOI18N
        epOrderTable.setName("epOrderTable"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        toolPanel.add(epOrderTable, gridBagConstraints);

        buttonGroup2.add(epOrderTeam);
        epOrderTeam.setText(resourceMap.getString("epOrderTeam.text")); // NOI18N
        epOrderTeam.setName("epOrderTeam"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        toolPanel.add(epOrderTeam, gridBagConstraints);

        epSenderValueBt.setText(resourceMap.getString("epSenderValueBt.text")); // NOI18N
        epSenderValueBt.setName("epSenderValueBt"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        toolPanel.add(epSenderValueBt, gridBagConstraints);

        jTabbedPane1.addTab(resourceMap.getString("toolPanel.TabConstraints.tabTitle"), toolPanel); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), resourceMap.getString("jPanel2.border.title"), javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), resourceMap.getColor("jPanel2.border.titleColor"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(new java.awt.GridBagLayout());

        epAddressUpBt.setIcon(resourceMap.getIcon("epAddressUpBt.icon")); // NOI18N
        epAddressUpBt.setText(resourceMap.getString("epAddressUpBt.text")); // NOI18N
        epAddressUpBt.setMargin(new java.awt.Insets(2, 14, 8, 14));
        epAddressUpBt.setName("epAddressUpBt"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel2.add(epAddressUpBt, gridBagConstraints);

        epAddressRightBt.setIcon(resourceMap.getIcon("epAddressRightBt.icon")); // NOI18N
        epAddressRightBt.setText(resourceMap.getString("epAddressRightBt.text")); // NOI18N
        epAddressRightBt.setAlignmentY(0.0F);
        epAddressRightBt.setIconTextGap(0);
        epAddressRightBt.setMargin(new java.awt.Insets(2, 14, 8, 14));
        epAddressRightBt.setName("epAddressRightBt"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        jPanel2.add(epAddressRightBt, gridBagConstraints);

        epAddressLeftBt.setIcon(resourceMap.getIcon("epAddressLeftBt.icon")); // NOI18N
        epAddressLeftBt.setText(resourceMap.getString("epAddressLeftBt.text")); // NOI18N
        epAddressLeftBt.setMargin(new java.awt.Insets(2, 14, 8, 14));
        epAddressLeftBt.setName("epAddressLeftBt"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel2.add(epAddressLeftBt, gridBagConstraints);

        epAddressDownBt.setIcon(resourceMap.getIcon("epAddressDownBt.icon")); // NOI18N
        epAddressDownBt.setText(resourceMap.getString("epAddressDownBt.text")); // NOI18N
        epAddressDownBt.setMargin(new java.awt.Insets(2, 14, 8, 14));
        epAddressDownBt.setName("epAddressDownBt"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        jPanel2.add(epAddressDownBt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jPanel2, gridBagConstraints);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), resourceMap.getString("jPanel3.border.title"), javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), resourceMap.getColor("jPanel3.border.titleColor"))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setLayout(new java.awt.GridBagLayout());

        epSenderUpBt.setIcon(resourceMap.getIcon("epSenderUpBt.icon")); // NOI18N
        epSenderUpBt.setText(resourceMap.getString("epSenderUpBt.text")); // NOI18N
        epSenderUpBt.setMargin(new java.awt.Insets(2, 14, 8, 14));
        epSenderUpBt.setName("epSenderUpBt"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        jPanel3.add(epSenderUpBt, gridBagConstraints);

        epSenderRightBt.setIcon(resourceMap.getIcon("epSenderRightBt.icon")); // NOI18N
        epSenderRightBt.setText(resourceMap.getString("epSenderRightBt.text")); // NOI18N
        epSenderRightBt.setMargin(new java.awt.Insets(2, 14, 8, 14));
        epSenderRightBt.setName("epSenderRightBt"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        jPanel3.add(epSenderRightBt, gridBagConstraints);

        epSenderLeftBt.setIcon(resourceMap.getIcon("epSenderLeftBt.icon")); // NOI18N
        epSenderLeftBt.setText(resourceMap.getString("epSenderLeftBt.text")); // NOI18N
        epSenderLeftBt.setMargin(new java.awt.Insets(2, 14, 8, 14));
        epSenderLeftBt.setName("epSenderLeftBt"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        jPanel3.add(epSenderLeftBt, gridBagConstraints);

        epSenderDownBt.setIcon(resourceMap.getIcon("epSenderDownBt.icon")); // NOI18N
        epSenderDownBt.setText(resourceMap.getString("epSenderDownBt.text")); // NOI18N
        epSenderDownBt.setMargin(new java.awt.Insets(2, 14, 8, 14));
        epSenderDownBt.setName("epSenderDownBt"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        jPanel3.add(epSenderDownBt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jPanel3, gridBagConstraints);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), resourceMap.getString("jPanel4.border.title"), javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), resourceMap.getColor("jPanel4.border.titleColor"))); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N
        jPanel4.setLayout(new java.awt.GridBagLayout());

        epGraphicUpBt.setIcon(resourceMap.getIcon("epGraphicUpBt.icon")); // NOI18N
        epGraphicUpBt.setText(resourceMap.getString("epGraphicUpBt.text")); // NOI18N
        epGraphicUpBt.setMargin(new java.awt.Insets(2, 14, 8, 14));
        epGraphicUpBt.setName("epGraphicUpBt"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        jPanel4.add(epGraphicUpBt, gridBagConstraints);

        epGraphicRightBt.setIcon(resourceMap.getIcon("epGraphicRightBt.icon")); // NOI18N
        epGraphicRightBt.setText(resourceMap.getString("epGraphicRightBt.text")); // NOI18N
        epGraphicRightBt.setMargin(new java.awt.Insets(2, 14, 8, 14));
        epGraphicRightBt.setName("epGraphicRightBt"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        jPanel4.add(epGraphicRightBt, gridBagConstraints);

        epGraphicLeftBt.setIcon(resourceMap.getIcon("epGraphicLeftBt.icon")); // NOI18N
        epGraphicLeftBt.setText(resourceMap.getString("epGraphicLeftBt.text")); // NOI18N
        epGraphicLeftBt.setMargin(new java.awt.Insets(2, 14, 8, 14));
        epGraphicLeftBt.setName("epGraphicLeftBt"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        jPanel4.add(epGraphicLeftBt, gridBagConstraints);

        epGraphicDownBt.setIcon(resourceMap.getIcon("epGraphicDownBt.icon")); // NOI18N
        epGraphicDownBt.setText(resourceMap.getString("epGraphicDownBt.text")); // NOI18N
        epGraphicDownBt.setMargin(new java.awt.Insets(2, 14, 8, 14));
        epGraphicDownBt.setName("epGraphicDownBt"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        jPanel4.add(epGraphicDownBt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jPanel4, gridBagConstraints);

        jTabbedPane1.addTab(resourceMap.getString("jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        jScrollPane1.setViewportView(jTabbedPane1);

        jSplitPane1.setBottomComponent(jScrollPane1);

        jScrollPane2.setName("jScrollPane2"); // NOI18N
        jScrollPane2.setViewportView(previewPanel);

        previewPanel.setMinimumSize(new java.awt.Dimension(1024, 500));
        previewPanel.setName("previewPanel"); // NOI18N

        javax.swing.GroupLayout previewPanelLayout = new javax.swing.GroupLayout(previewPanel);
        previewPanel.setLayout(previewPanelLayout);
        previewPanelLayout.setHorizontalGroup(
            previewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1099, Short.MAX_VALUE)
        );
        previewPanelLayout.setVerticalGroup(
            previewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 585, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(previewPanel);

        jSplitPane1.setLeftComponent(jScrollPane2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jSplitPane1, gridBagConstraints);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 20, 0);
        getContentPane().add(jLabel1, gridBagConstraints);

        jSeparator1.setName("jSeparator1"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jSeparator1, gridBagConstraints);

        jMenuBar1.setName("jMenuBar1"); // NOI18N

        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N

        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenu1.add(jMenuItem1);

        jSeparator2.setName("jSeparator2"); // NOI18N
        jMenu1.add(jSeparator2);

        jMenuItem2.setText(resourceMap.getString("jMenuItem2.text")); // NOI18N
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText(resourceMap.getString("jMenuItem3.text")); // NOI18N
        jMenuItem3.setName("jMenuItem3"); // NOI18N
        jMenu1.add(jMenuItem3);

        jSeparator3.setName("jSeparator3"); // NOI18N
        jMenu1.add(jSeparator3);

        jMenuItem4.setText(resourceMap.getString("jMenuItem4.text")); // NOI18N
        jMenuItem4.setName("jMenuItem4"); // NOI18N
        jMenu1.add(jMenuItem4);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
        setVisible(true);
    }// </editor-fold>

    /**
    * @param args the command line arguments
    */
    // Variables declaration - do not modify
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JCheckBox enSenderCheckBox;
    private javax.swing.JButton epAddressDownBt;
    private javax.swing.JButton epAddressFontBt;
    private javax.swing.JTextField epAddressFontPreviewTB;
    private javax.swing.JButton epAddressLeftBt;
    private javax.swing.JButton epAddressRightBt;
    private javax.swing.JButton epAddressUpBt;
    private javax.swing.JButton epBackwardBt;
    private javax.swing.JSpinner epCount;
    private javax.swing.JLabel epCountLabel;
    private javax.swing.JButton epDeclineBt;
    private javax.swing.JLabel epEnvAxisLabel;
    private javax.swing.JLabel epEnvTypeLabel;
    private javax.swing.JComboBox epEnvelopeAxis;
    private javax.swing.JComboBox epEnvelopeType;
    private javax.swing.JButton epFowardBt;
    private javax.swing.JButton epGraphicBt;
    private javax.swing.JCheckBox epGraphicCheckBox;
    private javax.swing.JButton epGraphicDownBt;
    private javax.swing.JButton epGraphicLeftBt;
    private javax.swing.JTextField epGraphicPathTB;
    private javax.swing.JButton epGraphicRightBt;
    private javax.swing.JButton epGraphicUpBt;
    private javax.swing.JButton epHomeBt;
    private javax.swing.JRadioButton epOrderTable;
    private javax.swing.JRadioButton epOrderTeam;
    private javax.swing.JButton epPrintBt;
    private javax.swing.JButton epSenderDownBt;
    private javax.swing.JButton epSenderFontBt;
    private javax.swing.JTextField epSenderFontPreviewTB;
    private javax.swing.JButton epSenderLeftBt;
    private javax.swing.JRadioButton epSenderLocCornerRB;
    private javax.swing.JRadioButton epSenderLocLineRB;
    private javax.swing.JButton epSenderRightBt;
    private javax.swing.JButton epSenderUpBt;
    private javax.swing.JButton epSenderValueBt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel previewPanel;
    private javax.swing.JPanel toolPanel;
    // End of variables declaration

}
