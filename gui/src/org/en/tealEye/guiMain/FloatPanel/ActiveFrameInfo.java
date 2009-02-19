package org.en.tealEye.guiMain.FloatPanel;

import org.en.tealEye.guiExt.TitleBarPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.Time;
import java.util.LinkedList;

public class ActiveFrameInfo extends TitleBarPanel {
    private final LinkedList messages = new LinkedList();
    private static final int MAX_MESSAGES = 3;

    private JTextArea tArea;

    public ActiveFrameInfo() {
        super();
        initComponents();
    }

    private void initComponents() {
        this.setMinimumSize(new Dimension(200, 250));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 0, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        tArea = new JTextArea();
        tArea.setLineWrap(true);
        this.add(new JScrollPane(tArea), gbc);

        this.setTitle("Informationen");
    }

    public void setActiveFrameTitle(String title) {
        this.setTitle(title);
    }

    public void setMessage(String message) {
        message = new Time(System.currentTimeMillis()) + ": " + message;
        messages.add(message);
        if (messages.size() > MAX_MESSAGES) {
            messages.removeFirst();
        }
        StringBuilder buf = new StringBuilder();
        for (int i = messages.size() - 1; i >= 0; i--) {
            buf.append(messages.get(i)).append("\n");
        }
        tArea.setText(buf.toString());
    }


}
