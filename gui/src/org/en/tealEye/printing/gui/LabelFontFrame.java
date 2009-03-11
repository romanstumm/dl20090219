package org.en.tealEye.printing.gui;

import org.en.tealEye.guiServices.GlobalGuiService;
import org.en.tealEye.guiServices.GuiService;
import org.en.tealEye.guiServices.GlobalGuiServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Properties;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 08.04.2008
 * Time: 19:16:27
 */
public class LabelFontFrame extends JFrame implements ActionListener {

    private JTextArea fontField;
    private JComboBox fontType;
    private JComboBox fontStyle;
    private JComboBox fontSize;

    private JButton okButton;
    private JButton cancelButton;
    private GuiService guiService = new GlobalGuiService();

    private final TablePrintingFrame tpf;

    private Font font = guiService.getFontMap().get("EtiFont");
    public LabelFontFrame(TablePrintingFrame tablePrintingFrame) {
        this.setTitle("Labelschriftart");
        this.setSize(600, 200);
        this.tpf = tablePrintingFrame;
        init();
    }

    private void init() {
        fontField = new JTextArea("The Quick Brown Fox Jumps Over The Lazy Dog!");
        fontType = new JComboBox(getSystemFonts());
            Font font = guiService.getFontMap().get("EtiFont");

        fontType.setSelectedItem(font.getName());
        fontStyle =
              new JComboBox(new String[]{"normal", "fett", "italic", "fett/italic"});
              int style = font.getStyle();
        fontStyle.setSelectedItem(style);
        fontSize = new JComboBox(getSystemFontSizes());
              int size = font.getSize();
        fontSize.setSelectedItem(size);
        okButton = new JButton("OK");
        okButton.setSize(40, 20);

        cancelButton = new JButton("Cancel");
        cancelButton.setSize(40, 20);

        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        fontType.addActionListener(this);
        fontStyle.addActionListener(this);
        fontSize.addActionListener(this);

        fontField.setFont(font);
        fontField.setSize(250, 50);
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc;

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        this.getContentPane().add(fontType, gbc);
        gbc.gridy = 1;
        this.getContentPane().add(fontStyle, gbc);
        gbc.gridy = 2;
        this.getContentPane().add(fontSize, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        this.getContentPane().add(fontField, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridy = 3;
        gbc.weightx = 0.9;
        this.getContentPane().add(okButton, gbc);
        gbc.gridx = 2;
        this.getContentPane().add(cancelButton, gbc);

        this.validate();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    public String[] getSystemFonts() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] f = ge.getAllFonts();
        String[] fNames = new String[f.length];
        int i = 0;
        for (Font fName : f) {
            fNames[i] = fName.getFontName();
            i++;
        }
        return fNames;
    }

    public String[] getSystemFontSizes() {
        String[] sizes = new String[20];
        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = String.valueOf(i + 8);
        }
        return sizes;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(okButton)) {
            tpf.setLabelFont(font);
            Map<String, Font> fontMap = guiService.getFontMap();
             guiService.updateProps(buildNewFontProperties(fontMap));
             this.dispose();
        } else if (e.getSource().equals(cancelButton)) {
            this.dispose();
        } else if (e.getSource().equals(fontType)) {
            Font old = font;
            font = new Font((String) fontType.getSelectedItem(), old.getStyle(),
                  old.getSize());
            fontField.setFont(font);
        } else if (e.getSource().equals(fontStyle)) {
            Font old = font;
            font = new Font(old.getFontName(), fontStyle.getSelectedIndex(),
                  old.getSize());
            fontField.setFont(font);
        } else if (e.getSource().equals(fontSize)) {
            Font old = font;
            font = new Font(old.getFontName(), old.getStyle(),
                  fontSize.getSelectedIndex() + 8);
            fontField.setFont(font);
        }
    }

        private Properties buildNewFontProperties(Map<String, Font> fontMap) {
        Properties props = new Properties();

        props.setProperty("LabelFontType", fontMap.get("LabelFont").getFontName());
        props.setProperty("LabelFontStyle", String.valueOf(fontMap.get("LabelFont").getStyle()));
        props.setProperty("LabelFontSize", String.valueOf(fontMap.get("LabelFont").getSize()));
        props.setProperty("TableFontType", fontMap.get("TableFont").getFontName());
        props.setProperty("TableFontStyle", String.valueOf(fontMap.get("TableFont").getStyle()));
        props.setProperty("TableFontSize", String.valueOf(fontMap.get("TableFont").getSize()));
        props.setProperty("FormFontType", fontMap.get("FormFont").getFontName());
        props.setProperty("FormFontStyle", String.valueOf(fontMap.get("FormFont").getStyle()));
        props.setProperty("FormFontSize", String.valueOf(fontMap.get("FormFont").getSize()));
        props.setProperty("EtiFontType", font.getFontName());
        props.setProperty("EtiFontStyle", String.valueOf(font.getStyle()));
        props.setProperty("EtiFontSize", String.valueOf(font.getSize()));
        return props;
    }
}
