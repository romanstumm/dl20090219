package org.en.tealEye.framework;

import de.liga.dart.model.Liga;
import org.en.tealEye.guiExt.ExtPanel.JListExt;
import org.en.tealEye.guiMain.DartComponentRegistry;
import org.en.tealEye.guiMain.MainAppFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 24.03.2008
 * Time: 19:58:25
 */
public class LigaChooser extends JDialog {
    private Liga selectedLiga;
    private Liga[] selectedLigas;
    private boolean cancelled = false;
    private final String titleMessage;
    private final String selectionMode;
    public static final String SELECTION_MODE_LIGA_MIT_ALLE = "Combo_LigaMitLeer";
    public static final String SELECTION_MODE_LIGA_MIT_KEINE = "Combo_LigaMitKeine";
    public static final String SELECTION_MODE_LIGA = "Combo_Liga";
    private final boolean multiselect;

    /**
     * default selection mode = LIGA_MIT_ALLE
     *
     * @param title
     * @param mainAppFrame
     */
    public LigaChooser(String title, MainAppFrame mainAppFrame) {
        this(title, mainAppFrame, SELECTION_MODE_LIGA_MIT_ALLE);
    }

    public LigaChooser(String title, MainAppFrame mainAppFrame, boolean multiselect) {
        this(title, mainAppFrame, SELECTION_MODE_LIGA_MIT_ALLE, multiselect);
    }

    /**
     * @param title
     * @param mainAppFrame
     * @param selectionModeName - use one of SELECTION_MODE_*
     */
    public LigaChooser(String title, MainAppFrame mainAppFrame, String selectionModeName, boolean multiselect) {
        super((Frame) null, "", true);
        this.multiselect = multiselect;
        setLocationRelativeTo(mainAppFrame);
        titleMessage = title;
        selectionMode = selectionModeName;
    }

    public LigaChooser(String title, MainAppFrame mainAppFrame, String selectionModeName) {
        this(title, mainAppFrame, selectionModeName, false);
    }

    private void initComponents() {


        final JComboBox ligaBox;
        final JListExt ligaList;
        if (multiselect) {
            ligaBox = null;
            ligaList = new JListExt();
            ligaList.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            ligaList.setName(selectionMode);
        } else {
            ligaList = null;
            ligaBox = new JComboBox();
            ligaBox.setName(selectionMode);
        }

        setSize(200, 300);
        JButton okBt = new JButton("OK");
        okBt.setMnemonic(KeyEvent.VK_O);
        JPanel contentPanel = new JPanel(new GridBagLayout());
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                cancelled = true;
            }
        });
        if (!multiselect) {
            okBt.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Object obj = ligaBox.getSelectedItem();
                    if (obj instanceof Liga) {
                        selectedLiga = (Liga) obj;
                    } else {
                        selectedLiga = null; // d.h. alle
                    }
                    LigaChooser.this.dispose();
                }
            });
        } else {
            okBt.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Object[] obj = ligaList.getSelectedValues();
                    if(obj != null) {
                        selectedLigas = new Liga[obj.length];
                        System.arraycopy(obj, 0, selectedLigas, 0, selectedLigas.length);
                    } else {
                        selectedLigas = new Liga[0];
                    }
                    LigaChooser.this.dispose();
                }
            });
        }
        JButton cancelBt = new JButton("Abbrechen");
        cancelBt.setMnemonic(KeyEvent.VK_A);
        cancelBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelled = true;
                LigaChooser.this.dispose();
            }
        });
        JLabel titleLabel = new JLabel(titleMessage);

        this.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc;

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(titleLabel, gbc);

        JLabel comboLabel = new JLabel("Liga");

        this.getContentPane().setLayout(new GridBagLayout());

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        contentPanel.add(comboLabel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        if (multiselect) {
            contentPanel.add(ligaList, gbc);
        } else {
            contentPanel.add(ligaBox, gbc);
        }

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        contentPanel.add(okBt, gbc);
        getRootPane().setDefaultButton(okBt);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        contentPanel.add(cancelBt, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        this.getContentPane().add(contentPanel, gbc);

        if (multiselect) {
            DartComponentRegistry.getInstance().setListModel(ligaList, null);
            ligaList.getAccessibleContext().getAccessibleSelection().selectAllAccessibleSelection();  // select all
            ligaList.requestFocus();
        } else {
            DartComponentRegistry.getInstance().setComboBoxModel(ligaBox, null);
            ligaBox.setSelectedIndex(0);
            ligaBox.requestFocus();
        }
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.pack();
        this.setVisible(true); // hier wartet der aktuelle Thread...
        this.dispose();
    }


    public boolean choose() {
        initComponents();
        return !cancelled;
    }

    public Liga getSelectedLiga() {
        return selectedLiga;
    }

    public Liga[] getSelectedLigas() {
        return selectedLigas;
    }
}
