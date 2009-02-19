package org.en.tealEye.framework;

import de.liga.dart.model.Liga;
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
    private boolean cancelled = false;
    private final String titleMessage;

    public LigaChooser(String title, MainAppFrame mainAppFrame) {
        super((Frame) null, "", true);
        setLocationRelativeTo(mainAppFrame);
        titleMessage = title;
    }

    private void initComponents() {

        final JComboBox ligaBox = new JComboBox();
        ligaBox.setName("Combo_LigaMitLeer");
        setSize(200, 300);
        JButton okBt = new JButton("OK");
        okBt.setMnemonic(KeyEvent.VK_O);
        JPanel contentPanel = new JPanel(new GridBagLayout());
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                cancelled = true;
            }
        });
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
        contentPanel.add(ligaBox, gbc);

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

        DartComponentRegistry.getInstance().setComboBoxModel(ligaBox, null);
        ligaBox.setSelectedIndex(0);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        ligaBox.requestFocus();
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
}
