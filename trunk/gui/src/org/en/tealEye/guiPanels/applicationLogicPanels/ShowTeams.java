package org.en.tealEye.guiPanels.applicationLogicPanels;

import de.liga.dart.model.Ligateam;
import org.en.tealEye.guiExt.ExtPanel.ShowTablePanel;
import org.en.tealEye.guiMain.util.LigaSelectable;
import org.en.tealEye.guiMain.util.LigaklasseSelectable;
import org.en.tealEye.guiMain.util.SpielortSelectable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * @author Stephan
 */
public class ShowTeams extends ShowTablePanel
        implements LigaSelectable, LigaklasseSelectable, SpielortSelectable {
    private JCheckBox keineGruppe;

    /**
     * Creates new form ShowGroups
     */
    public ShowTeams() {
        setName("ShowTeams");
        setTitle("Teams anzeigen");

        initComponents();
    }

    public Class getModelClass() {
        return Ligateam.class;
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Erzeugter Quelltext ">
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        headerPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        formPanel = new javax.swing.JPanel();
        comboPanel = new javax.swing.JPanel();
        showGroupsLeague = new javax.swing.JComboBox();
        showGroupsLeagueClass = new javax.swing.JComboBox();
        showGroupsLocation = new javax.swing.JComboBox();
        showGroupsLeagueLabel = new javax.swing.JLabel();
        showGroupsLeagueClassLabel = new javax.swing.JLabel();
        showGroupsLeague.setName("Combo_LigaMitLeer");
        showGroupsLeagueClass.setName("Combo_LigaklasseFilterOptions");
        showGroupsLocationLabel = new javax.swing.JLabel();
        tablePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        searchTextTF = new JTextField("");
        searchTextTF.setColumns(20);
        fullTextMode = new JCheckBox("Team-Name:");
        fullTextMode.setMnemonic(KeyEvent.VK_T);
        fullTextMode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                disableOrEnable();
            }
        });

        keineGruppe = new javax.swing.JCheckBox();
        keineGruppe.setText("In keiner Gruppe");
        keineGruppe.setMnemonic(KeyEvent.VK_G);
        keineGruppe.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        keineGruppe.setMargin(new java.awt.Insets(0, 0, 0, 0));

        setLayout(new java.awt.GridBagLayout());

        headerPanel.setLayout(new java.awt.GridBagLayout());

        headerPanel.setBorder(
                new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18));
        jLabel1.setText(getTitle());
        headerPanel.add(jLabel1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        add(headerPanel, gridBagConstraints);

        formPanel.setLayout(new java.awt.GridBagLayout());

        formPanel.setBorder(
                new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        comboPanel.setLayout(new java.awt.GridBagLayout());

        comboPanel.setBorder(
                new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 10);
        comboPanel.add(showGroupsLeague, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 10);
        comboPanel.add(showGroupsLeagueClass, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 10);
        comboPanel.add(keineGruppe, gridBagConstraints);

        showGroupsLocation.setName("Combo_SpielortMitLeer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 10);
        comboPanel.add(showGroupsLocation, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        comboPanel.add(getBtShowGroupsDelete(), gridBagConstraints);

        showGroupsLeagueLabel.setText("Liga");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        comboPanel.add(showGroupsLeagueLabel, gridBagConstraints);

        showGroupsLeagueClassLabel.setText("Ligaklasse");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        comboPanel.add(showGroupsLeagueClassLabel, gridBagConstraints);

        showGroupsLocationLabel.setText("Spielort");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        comboPanel.add(showGroupsLocationLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        comboPanel.add(getBtShowGroupsSort(), gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 10);
        comboPanel.add(getToggleDelete(), gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 10);
        comboPanel.add(searchTextTF, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 10);
        comboPanel.add(fullTextMode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        formPanel.add(comboPanel, gridBagConstraints);

        tablePanel.setLayout(new java.awt.GridBagLayout());

        tablePanel.setBorder(
                new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jTable1.setName("Table_Ligateam");
        jScrollPane1.setViewportView(jTable1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        tablePanel.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        formPanel.add(tablePanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(formPanel, gridBagConstraints);
    }// </editor-fold>


    // Variablendeklaration - nicht modifizieren
    private javax.swing.JPanel comboPanel;
    private javax.swing.JPanel formPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox showGroupsLeague;
    private javax.swing.JComboBox showGroupsLeagueClass;
    private javax.swing.JLabel showGroupsLeagueClassLabel;
    private javax.swing.JLabel showGroupsLeagueLabel;
    private javax.swing.JComboBox showGroupsLocation;
    private javax.swing.JLabel showGroupsLocationLabel;
    private javax.swing.JPanel tablePanel;
    private JTextField searchTextTF;

    public JCheckBox getFullTextMode() {
        return fullTextMode;
    }

    private JCheckBox fullTextMode;

    // Ende der Variablendeklaration
    public JComboBox getSpielort() {
        return showGroupsLocation;
    }

    public JComboBox getLigaklasse() {
        return showGroupsLeagueClass;
    }

    public JComboBox getLiga() {
        return showGroupsLeague;
    }

    public JCheckBox getKeineGruppe() {
        return keineGruppe;
    }

    public JTextField getSuchenTextTF() {
        return searchTextTF;
    }

    protected void doUpdatePanel() {
        super.doUpdatePanel();    // call super!
        disableOrEnable();
    }

    private void disableOrEnable() {
        if (fullTextMode.isSelected()) {
            getSuchenTextTF().setEditable(true);
            getKeineGruppe().setEnabled(false);
            getLigaklasse().setEnabled(false);
            getSpielort().setEnabled(false);
        } else {
            getSuchenTextTF().setEditable(false);
            getKeineGruppe().setEnabled(true);
            getLigaklasse().setEnabled(true);
            getSpielort().setEnabled(true);
        }
    }
}
