package org.en.tealEye.guiPanels.applicationLogicPanels;

import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.gruppen.TeamStatusInfo;
import de.liga.dart.gruppen.service.GruppenService;
import de.liga.dart.model.*;
import de.liga.util.CalendarUtils;
import org.en.tealEye.framework.RowTipJTable;
import org.en.tealEye.guiExt.ExtPanel.BeanListCellRenderer;
import org.en.tealEye.guiExt.ExtPanel.ExtJEditPanel;
import org.en.tealEye.guiExt.ExtPanel.JExtTextField;
import org.en.tealEye.guiExt.ExtPanel.JListExt;
import org.en.tealEye.guiMain.DartComponentRegistry;
import org.en.tealEye.guiMain.util.LigaSelectable;
import org.en.tealEye.guiMain.util.LigaklasseSelectable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;


public class CreateGroup extends ExtJEditPanel implements
        LigaSelectable, LigaklasseSelectable {

    public static final int C_NR = 0;
    public static final int C_TEAMNAME = 1; // enthält das Ligateam
    public static final int C_SPIELORT = 2;
    public static final int C_SPIELTAG = 3;
    public static final int C_SPIELZEIT = 4;
    public static final int C_SPIELFREI = 5;
    public static final int C_FIXIERT = 6;
    public static final int C_TEAMSTATUS = 7;

    private RowTipJTable jtable;

    private JButton groupNew;
    private JButton forward;
    private JButton backward;
    private JPanel controlPanel;

    private javax.swing.JPanel buttonPanel;
    private javax.swing.JPanel comboPanel;
    private javax.swing.JPanel formularPanel;
    private javax.swing.JButton groupAccept;
    private javax.swing.JButton groupDecline;
    private javax.swing.JButton groupValidate;
    private javax.swing.JLabel headerLabel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox leagueClassCombo;
    private javax.swing.JLabel leagueClassComboLabel;
    private javax.swing.JComboBox leagueCombo;
    private javax.swing.JLabel leagueComboLabel;
    private JListExt ligateamsNochFrei;
    private javax.swing.JLabel leagueNotInheritedTeamsListLabel;
    private JExtTextField gruppenName;
    private javax.swing.JLabel leagueSuggestedGroupNameLabel;
    private javax.swing.JPanel listPanel;
    private javax.swing.JPanel textfieldPanel;
    private boolean refreshFromEntity;


    @Override
    public void setModelEntity(Object modelEntity) {
        super.setModelEntity(modelEntity);    // call super!
        refreshFromEntity = true;
    }

    public CreateGroup() {
        setName("CreateGroup");
        setTitle("Gruppe bearbeiten");
        initComponents();
    }

    private void initComponents() {

        java.awt.GridBagConstraints gridBagConstraints;

        forward = new JButton("Nächste Gruppe");
        forward.setName("FORWARD");
        forward.setMnemonic('h');
        backward = new JButton("Vorherige Gruppe");
        backward.setMnemonic('o');
        backward.setName("BACKWARD");
        controlPanel = new JPanel(new GridBagLayout());

        jPopupMenu1 = new javax.swing.JPopupMenu();
        headerPanel = new javax.swing.JPanel();
        headerLabel = new javax.swing.JLabel();
        formularPanel = new javax.swing.JPanel();
        comboPanel = new javax.swing.JPanel();
        leagueCombo = new javax.swing.JComboBox();
        leagueClassCombo = new javax.swing.JComboBox();
        gruppenName = new JExtTextField();
        leagueComboLabel = new javax.swing.JLabel();
        leagueClassComboLabel = new javax.swing.JLabel();
        leagueSuggestedGroupNameLabel = new javax.swing.JLabel();
        textfieldPanel = new javax.swing.JPanel();

        gruppenName.setName("groupName");
        gruppenName.setEditable(false);

        jtable = new RowTipJTable();
        jtable.setModel(buildTableModel());
        jtable.setCellEditor(new DefaultCellEditor(new JCheckBox()));
        jtable.setFillsViewportHeight(true);

        listPanel = new javax.swing.JPanel();
        leagueNotInheritedTeamsListLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ligateamsNochFrei = new JListExt();
        buttonPanel = new javax.swing.JPanel();
        groupValidate = new javax.swing.JButton();
        groupAccept = new javax.swing.JButton();
        groupDecline = new javax.swing.JButton();
        groupNew = new JButton();
        setLayout(new java.awt.GridBagLayout());
        ligateamsNochFrei.setName("List_LigateamsNochFrei");
        BeanListCellRenderer renderer = new BeanListCellRenderer();
        renderer.setProperty("teamNameTagName");
        ligateamsNochFrei.setCellRenderer(renderer);
        ligateamsNochFrei.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setMinimumSize(new java.awt.Dimension(400, 400));
        setPreferredSize(new java.awt.Dimension(600, 600));
        headerPanel.setLayout(new java.awt.GridBagLayout());

//        jtable.setDragEnabled(true);                    // RSt: entfernt
//        ligateamsNochFrei.setDragEnabled(true);
//        jtable.setTransferHandler(new TableTransferHandler());
//        ligateamsNochFrei.setTransferHandler(new ListTransferHandler());
        jtable.setEditingColumn(C_SPIELFREI);
        jtable.setEditingColumn(C_FIXIERT);
        jtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        headerPanel.setBorder(new javax.swing.border.LineBorder(
                new java.awt.Color(153, 153, 153), 1, true));
        headerLabel.setFont(new java.awt.Font("Tahoma", 1, 18));
        headerLabel.setText(getTitle());
        headerPanel.add(headerLabel, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        add(headerPanel, gridBagConstraints);

        formularPanel.setLayout(new java.awt.GridBagLayout());

        formularPanel.setBorder(new javax.swing.border.LineBorder(
                new java.awt.Color(153, 153, 153), 1, true));
        comboPanel.setLayout(new java.awt.GridBagLayout());

        leagueCombo.setName("Combo_Liga");
        comboPanel.setBorder(javax.swing.BorderFactory.createLineBorder(
                new java.awt.Color(204, 204, 204)));
        leagueCombo.setMaximumSize(new java.awt.Dimension(200, 20));
        leagueCombo.setMinimumSize(new java.awt.Dimension(200, 20));
        leagueCombo.setOpaque(false);
        leagueCombo.setPreferredSize(new java.awt.Dimension(100, 20));


        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        comboPanel.add(leagueCombo, gridBagConstraints);

        leagueClassCombo.setName("Combo_Ligaklasse");
        leagueClassCombo.setMaximumSize(new java.awt.Dimension(120, 20));
        leagueClassCombo.setMinimumSize(new java.awt.Dimension(120, 20));
        leagueClassCombo.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        comboPanel.add(leagueClassCombo, gridBagConstraints);

        gruppenName.setMaximumSize(new java.awt.Dimension(150, 20));
        gruppenName.setMinimumSize(new java.awt.Dimension(50, 20));
        gruppenName.setPreferredSize(new java.awt.Dimension(110, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        comboPanel.add(gruppenName, gridBagConstraints);

        leagueComboLabel.setText("Liga");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        comboPanel.add(leagueComboLabel, gridBagConstraints);

        leagueClassComboLabel.setText("Ligaklasse");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        comboPanel.add(leagueClassComboLabel, gridBagConstraints);

        leagueSuggestedGroupNameLabel.setText("Gruppenname");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        comboPanel.add(leagueSuggestedGroupNameLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        formularPanel.add(comboPanel, gridBagConstraints);

        textfieldPanel.setLayout(new java.awt.GridBagLayout());

        textfieldPanel.setBorder(javax.swing.BorderFactory.createLineBorder(
                new java.awt.Color(204, 204, 204)));

        JPanel jpanel = new JPanel(new GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.8;

        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        formularPanel.add(jpanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.8;
        jpanel.add(jtable.getTableHeader(), gridBagConstraints);


        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
        jpanel.add(jtable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        //gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        //gridBagConstraints.ipadx = 10;
        //gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.3;
        // gridBagConstraints.weighty = 0.4;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        controlPanel.add(forward, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        //gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        //gridBagConstraints.ipadx = 10;
        //gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.3;
        //gridBagConstraints.weighty = 0.4;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        controlPanel.add(backward, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.weighty = 0.6;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        formularPanel.add(controlPanel, gridBagConstraints);
        listPanel.setLayout(new java.awt.GridBagLayout());

        listPanel.setBorder(new javax.swing.border.LineBorder(
                new java.awt.Color(204, 204, 204), 1, true));
        leagueNotInheritedTeamsListLabel.setText("Noch nicht gruppierte Teams");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
        listPanel.add(leagueNotInheritedTeamsListLabel, gridBagConstraints);

        jScrollPane1.setViewportView(ligateamsNochFrei);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 10);
        listPanel.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        formularPanel.add(listPanel, gridBagConstraints);

        buttonPanel.setLayout(new java.awt.GridBagLayout());

        buttonPanel.setBorder(new javax.swing.border.LineBorder(
                new java.awt.Color(204, 204, 204), 1, true));
        groupValidate.setText("Verifizieren");
        groupValidate.setMnemonic(KeyEvent.VK_V);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 60);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        buttonPanel.add(groupValidate, gridBagConstraints);

        groupAccept.setText("Speichern");
        groupAccept.setMnemonic(KeyEvent.VK_S);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        buttonPanel.add(groupAccept, gridBagConstraints);

        groupDecline.setText("Abbrechen");
        groupDecline.setMnemonic(KeyEvent.VK_A);
        groupDecline.setToolTipText("Dieses Fenster schliessen");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        buttonPanel.add(groupDecline, gridBagConstraints);

        groupNew.setText("Neue Gruppe");
        groupNew.setMnemonic(KeyEvent.VK_N);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 30);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        buttonPanel.add(groupNew, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        formularPanel.add(buttonPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(formularPanel, gridBagConstraints);

        groupAccept.setName("speichern");
        groupDecline.setName("abbrechen");
        groupValidate.setName("verifizieren");
        groupNew.setName("neu");

        setTableLayout();
    }

    @Override
    public void doUpdatePanel() {
        Ligagruppe gruppe = (Ligagruppe) getModelEntity();
        if (refreshFromEntity) {
            refreshFromEntity = false;
            List<Ligateamspiel> spiele = ServiceFactory
                    .get(GruppenService.class).findSpieleInGruppe(gruppe);
            Liga liga = gruppe.getLiga();
            Ligaklasse ligaklasse = gruppe.getLigaklasse();
            DartComponentRegistry.getInstance()
                    .setComboBoxModel(getLiga(), this); // zum Setzen der Selection!
            DartComponentRegistry.getInstance().setComboBoxModel(getLigaklasse(), this);
            getLiga().setSelectedItem(liga);
            getLigaklasse().setSelectedItem(ligaklasse);
            TeamStatusInfo[] status =
                    ServiceFactory.get(GruppenService.class).getTeamStatus(gruppe);
            for (int platzNr = 0; platzNr < 8; platzNr++) {
                Ligateamspiel spiel = gruppe.findSpiel(spiele, platzNr + 1);
                updateSpielInTable(spiel != null ? spiel.getLigateam() : null, spiel, platzNr,
                        status);
            }
        }
        super.doUpdatePanel();    // call super!
        DartComponentRegistry.getInstance()
                .setListModel(getLigateamsNochFrei(), this); // zum Schluss!
    }

    /**
     * @param team    - darf null sein
     * @param spiel   - darf null sein
     * @param platzNr - 0 ... 7
     * @param status  - darf null sein
     */
    public void updateSpielInTable(Ligateam team, Ligateamspiel spiel, int platzNr,
                                   TeamStatusInfo[] status) {
        if (status == null) {
            jtable.setValueAt(null, platzNr, C_TEAMSTATUS);
            jtable.setToolTipText(platzNr, null);
        } else {
            jtable.setValueAt(status[platzNr].getTeamStatus().getInfo(), platzNr, C_TEAMSTATUS);
            // Tooltip spezifisch für die Zeile oder Zelle "Status", nicht fuer Spalte
            jtable.setToolTipText(platzNr, status[platzNr].toString());
        }

        if (team != null) {
            jtable.setValueAt(team, platzNr, C_TEAMNAME);
            if (team.getSpielort() != null) {
                jtable.setValueAt(team.getSpielort().getSpielortName(), platzNr, C_SPIELORT);
            }
            jtable.setValueAt(team.getWochentagName(), platzNr, C_SPIELTAG);
            jtable.setValueAt(CalendarUtils.timeToString(team.getSpielzeit()), platzNr,
                    C_SPIELZEIT);
            jtable.setValueAt(false, platzNr, C_SPIELFREI);
        } else {
            jtable.setValueAt(null, platzNr, C_TEAMNAME);
            jtable.setValueAt(null, platzNr, C_SPIELORT);
            jtable.setValueAt(null, platzNr, C_SPIELTAG);
            jtable.setValueAt(null, platzNr, C_SPIELZEIT);
            jtable.setValueAt(spiel != null, platzNr, C_SPIELFREI);
        }
        jtable.setValueAt(spiel != null && spiel.isFixiert(), platzNr, C_FIXIERT);
    }

    public void removeTeamInTable(int platzNr) {
        jtable.setValueAt(null, platzNr, C_TEAMSTATUS);
        jtable.setToolTipText(platzNr, null);
        jtable.setValueAt(null, platzNr, C_TEAMNAME);
        jtable.setValueAt(null, platzNr, C_SPIELORT);
        jtable.setValueAt(null, platzNr, C_SPIELTAG);
        jtable.setValueAt(null, platzNr, C_SPIELZEIT);
    }

    public JPopupMenu getJPopupMenu1() {
        return jPopupMenu1;
    }

    public JPanel getButtonPanel() {
        return buttonPanel;
    }

    public JExtTextField getGruppenName() {
        return gruppenName;
    }

    public JList getLigateamsNochFrei() {
        return ligateamsNochFrei;
    }

    public JComboBox getLiga() {
        return leagueCombo;
    }

    public JComboBox getLigaklasse() {
        return leagueClassCombo;
    }

    public Class getModelClass() {
        return Ligagruppe.class;
    }

    private DefaultTableModel buildTableModel() {
        TModel tableModel = new TModel();
        tableModel.addColumn("Nr.");
        tableModel.addColumn("Teamname");
        tableModel.addColumn("Spielort");
        tableModel.addColumn("Spieltag");
        tableModel.addColumn("Spielzeit");
        tableModel.addColumn("Spielfrei");
        tableModel.addColumn("Fixiert");
        tableModel.addColumn("Status");

        for (int i = 1; i < 9; i++) {
            tableModel.addRow(new Object[]{i + ".", null, null, null, null, false, false, null});
        }

        return tableModel;
    }

    public class TModel extends DefaultTableModel {

        public Class getColumnClass(int columnIndex) {
            if (columnIndex == C_FIXIERT || columnIndex == C_SPIELFREI)
                return Boolean.class;
            else
                return Object.class;
        }

        public boolean isCellEditable(int rowIndex, int vColIndex) {
            return !(vColIndex == C_NR || vColIndex == C_TEAMNAME
                    || vColIndex == C_SPIELORT || vColIndex == C_SPIELTAG
                    || vColIndex == C_SPIELZEIT || vColIndex == C_TEAMSTATUS);
        }
    }

    protected void createCollectionModel(Component c) {
        if (c == ligateamsNochFrei) return; // muss ganz zum Schluss kommen
        super.createCollectionModel(c);
    }

    public RowTipJTable getTable() {
        return jtable;
    }

    private void setTableLayout(){
         TableColumn column = null;
         for(int i = 0;i<jtable.getColumnCount();i++){
             column = jtable.getColumnModel().getColumn(i);
             switch(i){
                 case 0: column.setMaxWidth(30);column.setMinWidth(25);
                 case 3: column.setMaxWidth(50);column.setMinWidth(30);
                 case 4: column.setMaxWidth(50);column.setMinWidth(30);
                 case 5: column.setMaxWidth(50);column.setMinWidth(30);
                 case 6: column.setMaxWidth(50);column.setMinWidth(30);
                 case 7: column.setMaxWidth(50);column.setMinWidth(30);
                     jtable.setAutoResizeMode(1);
             }
         }
    }
}
