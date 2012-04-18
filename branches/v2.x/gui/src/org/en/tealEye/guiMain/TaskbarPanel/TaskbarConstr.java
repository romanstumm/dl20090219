package org.en.tealEye.guiMain.TaskbarPanel;

import javax.swing.*;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 06.11.2007
 * Time: 20:08:14
 */
public class TaskbarConstr extends TaskbarImpl {

    private JProgressBar jProgressBar;
    private JLabel taskLabel;
    private JLabel taskMessage;


    public TaskbarConstr() {
        super();
        setSize(getWidth(), 25);
        setName("Taskbar");
        initComponents();
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        taskLabel = new javax.swing.JLabel();
        taskMessage = new javax.swing.JLabel();
        jProgressBar = new javax.swing.JProgressBar();

        setLayout(new java.awt.GridBagLayout());
        this.setBorder(BorderFactory.createEtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(taskLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(taskMessage, gridBagConstraints);

        jProgressBar.setMinimumSize(new java.awt.Dimension(50, 19));
        jProgressBar.setPreferredSize(new java.awt.Dimension(100, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(jProgressBar, gridBagConstraints);

        jProgressBar.setBorderPainted(true);
        jProgressBar.setEnabled(true);
        jProgressBar.setStringPainted(true);
        jProgressBar.setMaximum(100);
        jProgressBar.setMinimum(0);
    }// </editor-fold>

    public void setProgress(int value) {
        if (value < 100) {
            jProgressBar.setString("Verarbeite Daten: " + value + "%");
            jProgressBar.setStringPainted(true);
        } else {
            jProgressBar.setString("Vorgang abgeschlossen");
            jProgressBar.setStringPainted(true);
        }
        jProgressBar.setValue(value);
    }

    public void setTaskbarTask(String task) {
        taskLabel.setText(task);
    }

    public JProgressBar getProgBar() {
        return jProgressBar;
    }


}
