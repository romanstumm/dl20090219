package org.en.tealEye.guiExt.ExtPanel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Description:   <br/>
 * User: roman
 * Date: 10.01.2009, 12:47:40
 */
public abstract class ShowTablePanel extends ExtJTablePanel implements ActionListener {
    /////// allgemeine Widgets und Actions

    private javax.swing.JButton btShowGroupsSort;
    private javax.swing.JButton btShowGroupsDelete;
    private javax.swing.JCheckBox toggleDelete;

    protected ShowTablePanel() {
        initComponents();
    }

    private void initComponents() {
        btShowGroupsSort = new javax.swing.JButton();
        btShowGroupsDelete = new javax.swing.JButton();
        toggleDelete = new javax.swing.JCheckBox();

        btShowGroupsSort.setName("sortieren");

        btShowGroupsSort.setText("Neu Sortieren");
        btShowGroupsSort.setMnemonic(KeyEvent.VK_S);

        btShowGroupsDelete.setText("Auswahl L\u00f6schen");
        btShowGroupsDelete.setMnemonic(KeyEvent.VK_L);
        btShowGroupsDelete.setDisplayedMnemonicIndex(8);

        toggleDelete.setText("L\u00f6schen erlauben");
        toggleDelete.setMnemonic(KeyEvent.VK_E);
        toggleDelete.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        toggleDelete.setMargin(new java.awt.Insets(0, 0, 0, 0));
        
        toggleDelete.addActionListener(this);
        btShowGroupsDelete.setEnabled(false);
        btShowGroupsDelete.setName("löschen");
    }

    public JButton getBtShowGroupsDelete() {
        return btShowGroupsDelete;
    }

    public JCheckBox getToggleDelete() {
        return toggleDelete;
    }

    public JButton getBtShowGroupsSort() {
        return btShowGroupsSort;
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj instanceof JCheckBox) {
            JCheckBox box = (JCheckBox) obj;
            if (box.isSelected()) {
                btShowGroupsDelete.setEnabled(true);
            } else {
                btShowGroupsDelete.setEnabled(false);
            }
        }
    }

    public void setToggleDeleteState(boolean state) {
        getToggleDelete().setSelected(state);
        if (getToggleDelete().isSelected()) {
            btShowGroupsDelete.setEnabled(true);
        } else {
            btShowGroupsDelete.setEnabled(false);
        }
    }
}
