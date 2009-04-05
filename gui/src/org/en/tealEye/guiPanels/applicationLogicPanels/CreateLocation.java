package org.en.tealEye.guiPanels.applicationLogicPanels;

import de.liga.dart.model.Ligateam;
import de.liga.dart.model.Spielort;
import de.liga.util.CalendarUtils;
import org.en.tealEye.framework.SwingUtils;
import org.en.tealEye.guiExt.ExtPanel.ExtJEditPanel;
import org.en.tealEye.guiMain.util.LigaSelectable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Calendar;


/**
 * @author Stephan
 */
public class CreateLocation extends ExtJEditPanel implements LigaSelectable, ActionListener {
    private JButton locationNew;
    private JComboBox locationLiga;
    private final JLabel locationLigaLabel = new JLabel("Liga");
    private JTextArea labelTeamsAnzahl;

    public CreateLocation() {

        setName("CreateLocation");
        setTitle("Spielort bearbeiten");

        initComponents();
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
        headerLabel = new javax.swing.JLabel();
        formularPanel = new javax.swing.JPanel();
        locationName = new javax.swing.JTextField();
        locationStreet = new javax.swing.JTextField();
        locationZipNum = new javax.swing.JTextField();
        SwingUtils.acceptDigitsOnly(locationZipNum);
        locationCity = new javax.swing.JTextField();
        locationClerk = new javax.swing.JTextField();
        locationPhone = new javax.swing.JTextField();
        locationFax = new javax.swing.JTextField();
        locationEmail = new javax.swing.JTextField();
        locationMobile = new javax.swing.JTextField();
        locationVendorNum = new JComboBox();
        locationDayOff = new javax.swing.JComboBox();
        locationNameLabel = new javax.swing.JLabel();
        locationStreetLabel = new javax.swing.JLabel();
        locationZipNumLabel = new javax.swing.JLabel();
        locationCityLabel = new javax.swing.JLabel();
        locationClerkLabel = new javax.swing.JLabel();
        locationPhoneLabel = new javax.swing.JLabel();
        locationFaxLabel = new javax.swing.JLabel();
        locationEmailLabel = new javax.swing.JLabel();
        locationMobileLabel = new javax.swing.JLabel();
        locationVendorNumLabel = new javax.swing.JLabel();
        locationDayOffLabel = new javax.swing.JLabel();
        fieldAutomatenAnzahl = new JTextField();
        SwingUtils.acceptDigitsOnly(fieldAutomatenAnzahl);
        labelAutomatenAnzahl = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel();
        buttonNestedPanel = new javax.swing.JPanel();
        locationAccept = new javax.swing.JButton();
        locationDecline = new javax.swing.JButton();
        locationNew = new JButton();
        locationLiga = new JComboBox();
        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(400, 400));
        setPreferredSize(new java.awt.Dimension(600, 600));
        headerPanel.setLayout(new java.awt.GridBagLayout());

        headerPanel.setBorder(
                new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        headerLabel.setFont(new java.awt.Font("Tahoma", 1, 18));
        headerLabel.setText(getTitle());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        headerPanel.add(headerLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        add(headerPanel, gridBagConstraints);

        formularPanel.setLayout(new java.awt.GridBagLayout());

        formularPanel.setBorder(
                new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 2);
        formularPanel.add(locationName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 2);
        formularPanel.add(locationStreet, gridBagConstraints);

        locationZipNum.setMinimumSize(new java.awt.Dimension(50, 20));
        locationZipNum.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 2);
        formularPanel.add(locationZipNum, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 2);
        formularPanel.add(locationCity, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 2);
        formularPanel.add(locationClerk, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 2);
        formularPanel.add(locationPhone, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 2);
        formularPanel.add(locationFax, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 2);
        formularPanel.add(locationEmail, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 2);
        formularPanel.add(locationMobile, gridBagConstraints);

        locationVendorNum.setName("Combo_AufstellerMitKeine");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 2);
        formularPanel.add(locationVendorNum, gridBagConstraints);

        locationDayOff.setModel(new DefaultComboBoxModel(CalendarUtils.getWeekdays()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 2);
        formularPanel.add(locationDayOff, gridBagConstraints);

        locationLiga.setName("Combo_LigaMitKeine");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 2);
        formularPanel.add(locationLiga, gridBagConstraints);

        locationNameLabel.setText("Spielortname");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        formularPanel.add(locationNameLabel, gridBagConstraints);

        locationStreetLabel.setText("Strasse / Hausnummer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        formularPanel.add(locationStreetLabel, gridBagConstraints);

        locationZipNumLabel.setText("Postleitzahl");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        formularPanel.add(locationZipNumLabel, gridBagConstraints);

        locationCityLabel.setText("Ort");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        formularPanel.add(locationCityLabel, gridBagConstraints);

        locationClerkLabel.setText("Ansprechpartner");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        formularPanel.add(locationClerkLabel, gridBagConstraints);

        locationPhoneLabel.setText("Telefon");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        formularPanel.add(locationPhoneLabel, gridBagConstraints);

        locationFaxLabel.setText("Fax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        formularPanel.add(locationFaxLabel, gridBagConstraints);

        locationEmailLabel.setText("Email");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        formularPanel.add(locationEmailLabel, gridBagConstraints);

        locationMobileLabel.setText("Mobil");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        formularPanel.add(locationMobileLabel, gridBagConstraints);

        locationVendorNumLabel.setText("Aufsteller");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        formularPanel.add(locationVendorNumLabel, gridBagConstraints);

        locationDayOffLabel.setText("Freier Tag");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        formularPanel.add(locationDayOffLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        formularPanel.add(locationLigaLabel, gridBagConstraints);
        fieldAutomatenAnzahl.setMinimumSize(new java.awt.Dimension(30, 20));
        fieldAutomatenAnzahl.setPreferredSize(new java.awt.Dimension(30, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 2);
        formularPanel.add(fieldAutomatenAnzahl, gridBagConstraints);

        labelTeamsAnzahl = new JTextArea();
        labelTeamsAnzahl.setSize(900,350);
//        labelTeamsAnzahl.setRows(10);
//        labelTeamsAnzahl.setColumns(100);
        labelTeamsAnzahl.setWrapStyleWord(true);
        labelTeamsAnzahl.setLineWrap(true);
        labelTeamsAnzahl.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 2);
        formularPanel.add(labelTeamsAnzahl, gridBagConstraints);

        labelAutomatenAnzahl.setText("Anzahl Automaten");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        formularPanel.add(labelAutomatenAnzahl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(formularPanel, gridBagConstraints);

        buttonPanel.setLayout(new java.awt.GridBagLayout());

        buttonPanel.setBorder(
                new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        buttonNestedPanel.setLayout(new java.awt.GridBagLayout());

        locationAccept.setText("Speichern");
        locationAccept.setMnemonic(KeyEvent.VK_S);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        buttonNestedPanel.add(locationAccept, gridBagConstraints);

        locationDecline.setToolTipText("Dieses Fenster schliessen");
        locationDecline.setText("Abbrechen");
        locationDecline.setMnemonic(KeyEvent.VK_A);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        buttonNestedPanel.add(locationDecline, gridBagConstraints);

        locationNew.setText("Neue Daten");
        locationNew.setMnemonic(KeyEvent.VK_N);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 30);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        buttonNestedPanel.add(locationNew, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        buttonPanel.add(buttonNestedPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(buttonPanel, gridBagConstraints);

        locationNew.setName("neu");
        locationAccept.setName("speichern");
        locationDecline.setName("abbrechen");
    }// </editor-fold>


    // Variablendeklaration - nicht modifizieren
    private javax.swing.JPanel buttonNestedPanel;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JPanel formularPanel;
    private javax.swing.JLabel headerLabel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JButton locationAccept;
    private javax.swing.JTextField locationCity;
    private javax.swing.JLabel locationCityLabel;
    private javax.swing.JTextField locationClerk;
    private javax.swing.JLabel locationClerkLabel;
    private javax.swing.JComboBox locationDayOff;
    private javax.swing.JLabel locationDayOffLabel;
    private javax.swing.JButton locationDecline;
    private javax.swing.JTextField locationEmail;
    private javax.swing.JLabel locationEmailLabel;
    private javax.swing.JTextField locationFax;
    private javax.swing.JLabel locationFaxLabel;
    private javax.swing.JTextField locationMobile;
    private javax.swing.JLabel locationMobileLabel;
    private javax.swing.JTextField locationName;
    private javax.swing.JLabel locationNameLabel;
    private javax.swing.JTextField locationPhone;
    private javax.swing.JLabel locationPhoneLabel;
    private javax.swing.JTextField locationStreet;
    private javax.swing.JLabel locationStreetLabel;
    private JComboBox locationVendorNum;
    private javax.swing.JLabel locationVendorNumLabel;
    private JTextField fieldAutomatenAnzahl;
    private javax.swing.JTextField locationZipNum;
    private javax.swing.JLabel locationZipNumLabel;
    private javax.swing.JLabel labelAutomatenAnzahl;
    // Ende der Variablendeklaration

    public JTextField getOrt() {
        return locationCity;
    }

    public JTextField getKontaktName() {
        return locationClerk;
    }

    public JComboBox getFreierTagName() {
        return locationDayOff;
    }

    public JTextField getEmail() {
        return locationEmail;
    }

    public JTextField getFax() {
        return locationFax;
    }

    public JTextField getMobil() {
        return locationMobile;
    }

    public JTextField getSpielortName() {
        return locationName;
    }

    public JTextField getTelefon() {
        return locationPhone;
    }

    public JTextField getStrasse() {
        return locationStreet;
    }

    public JComboBox getAutomatenaufsteller() {
        return locationVendorNum;
    }

    public JTextField getAutomatenAnzahl() {
        return fieldAutomatenAnzahl;
    }

    public JTextField getPlz() {
        return locationZipNum;
    }


    public JComboBox getLiga() {
        return locationLiga;
    }

    public JTextArea getLabelTeamsAnzahl() {
        return labelTeamsAnzahl;
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj instanceof JButton) {
            JButton button = (JButton) obj;
            if (button.equals(locationAccept)) {
                locationNew.setEnabled(true);
            } else if (button.equals(locationDecline)) {
            } else if (button.equals(locationNew)) {
                clearTextAreas();
                locationNew.setEnabled(false);
            }
        }
    }

    public Class getModelClass() {
        return Spielort.class;
    }


    @Override
    protected void doUpdatePanel() {
        super.doUpdatePanel();   // call super!
        Spielort ort = (Spielort) getModelEntity();
        if (ort == null) {
            getLabelTeamsAnzahl().setText("");
        } else {
            StringBuilder buf = new StringBuilder();
            buf.append("Stammkneipe von ").append(ort.getLigateams().size())
                    .append(" Teams.\n");
            int i = 1;
            for (Ligateam team : ort.getLigateams()) {
                buf.append(team.getTeamName());
                buf.append(" (");
                buf.append(team.getLiga().getLigaName());
                buf.append(" ");
                buf.append(team.getGruppeKlasse());
                if (team.getLigateamspiel() != null) {
                    buf.append(" Platz: ").append(team.getLigateamspiel().getPlatzNr());
                }
                buf.append(")");
                i++;
                if (i == ort.getLigateams().size()) {
                    buf.append(" und ");
                } else if (i < ort.getLigateams().size()) {
                    buf.append(", ");
                }
            }
            getLabelTeamsAnzahl().setText(buf.toString());
        }
    }

    @Override
    public void clearTextAreas() {
        super.clearTextAreas();   // call super!
        getLabelTeamsAnzahl().setText("");
        Spielort ort = (Spielort) getModelEntity();
        if (ort != null) {
            getAutomatenAnzahl().setText(String.valueOf(ort.getAutomatenAnzahl()));
            getFreierTagName().setSelectedItem(ort.getFreierTagName());
        }
        getSpielortName().requestFocus();
    }

    @Override
    public void newModelEntity() {
        super.newModelEntity();    // call super!
        // set defaults
        Spielort ort = (Spielort) getModelEntity();
        ort.setAutomatenAnzahl(1);
        ort.setFreierTag(Calendar.MONDAY);
    }
}
