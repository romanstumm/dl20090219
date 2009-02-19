package org.en.tealEye.framework;

import de.liga.dart.gruppen.check.ProgressIndicator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 25.11.2007
 * Time: 18:39:54
 */
public class ProgressDialog extends JFrame implements ProgressIndicator {
    private JProgressBar pBar;
    private JLabel textLabel;

    public ProgressDialog() {
        super();
    }

    public void open(ActionListener al, String msg, String title) {
        this.setLayout(new GridBagLayout());
        this.setMinimumSize(new Dimension(300, 300));
        this.setMaximumSize(new Dimension(300, 300));
        this.setPreferredSize(new Dimension(300, 300));
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        pBar = new JProgressBar();
        pBar.setBorderPainted(true);
        pBar.setEnabled(true);
        pBar.setMaximum(100);
        pBar.setMinimum(0);
        pBar.setValue(0);
        JButton cancel = new JButton("Abbrechen");
        cancel.addActionListener(al);
        this.setTitle(title);
        textLabel = new JLabel(msg);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 0.8;
        this.add(textLabel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        this.add(pBar, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        this.add(cancel, gbc);

        this.setAlwaysOnTop(true);
        this.setVisible(true);
        this.pack();

    }

    public void showProgress(int percent, String message) {
        pBar.setValue(percent);
        if(message != null) textLabel.setText(message);
    }
}
