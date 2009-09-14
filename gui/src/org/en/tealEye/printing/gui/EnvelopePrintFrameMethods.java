package org.en.tealEye.printing.gui;

import org.en.tealEye.printing.controller.FieldMapper;
import org.en.tealEye.printing.controller.GenericThread;
import org.en.tealEye.printing.service.EnvelopePrintService;
import org.en.tealEye.guiServices.GlobalGuiService;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 02.09.2009
 * Time: 10:42:29
 * To change this template use File | Settings | File Templates.
 */
public class EnvelopePrintFrameMethods {

    private Map<String, Component> components = new HashMap<String, Component>();
    private JFrame form;
    private GlobalGuiService service = null;
    private Object parentObject;
    private EnvelopePrintService eps;
    private GenericThread tf;
    private int pageIndex = 0;
    private JPanel panel;


    public EnvelopePrintFrameMethods(Map<String, Component> components, Object form, Object parentObject){
        this.form = (JFrame) form;
        this.components = components;
        this.parentObject = parentObject;
    }

    //CustomMethods will be invoked at startup__________________________________

    public void getFontPropertiesCustom(){
        service = new GlobalGuiService();
        components.get("epAddressFontPreviewTB").setFont(service.getFontMap().get("AddressFont"));
        ((JTextField)components.get("epAddressFontPreviewTB")).setToolTipText(service.getFontMap().get("AddressFont").getFamily()+", "+getFontStyleName(service.getFontMap().get("AddressFont"))+", "+service.getFontMap().get("AddressFont").getSize());
        components.get("epSenderFontPreviewTB").setFont(service.getFontMap().get("SenderFont"));
        ((JTextField)components.get("epSenderFontPreviewTB")).setToolTipText(service.getFontMap().get("SenderFont").getFamily()+", "+getFontStyleName(service.getFontMap().get("SenderFont"))+", "+service.getFontMap().get("SenderFont").getSize());
        ((JTextField)components.get("epGraphicPathTB")).setText(service.getProperty("ImagePath"));
        ((JButton)components.get("epSenderValueBt")).setToolTipText(service.getProperty("senderCorp")+ " | "+
                                                                    service.getProperty("senderName")+ " | "+
                                                                    service.getProperty("senderStreet")+ " | "+
                                                                    service.getProperty("senderPLZ")+ " | "+
                                                                    service.getProperty("senderLocation"));
    }

    public void loadInitialGraphicsCustomAsThread(GenericThread tf){
        this.tf = tf;
        eps = new EnvelopePrintService(parentObject, tf, ((JCheckBox)components.get("epGraphicCheckBox")).isSelected(),
                                                         ((JCheckBox)components.get("epSenderCheckBox")).isSelected(),
                                                         ((JComboBox)components.get("epEnvelopeType")).getSelectedIndex(),
                                                         ((JComboBox)components.get("epEnvelopeAxis")).getSelectedIndex(),
                                                         ((JSpinner)components.get("epCount")).getValue(),
                                                         ((JRadioButton)components.get("epOrderTable")).isSelected(),
                                                         ((JRadioButton)components.get("epSenderLocCornerRB")).isSelected(),
                                                         components.get("epAddressFontPreviewTB").getFont(),
                                                         components.get("epSenderFontPreviewTB").getFont(),
                                                         ((JTextField)components.get("epGraphicPathTB")).getText());

        panel = ((JPanel)components.get("previewPanel"));
        panel.setVisible(false);
        panel.setLayout(new BorderLayout());
        ImageIcon icon = eps.getGraphic(pageIndex);
        if(icon != null)
        panel.add(new JLabel(icon),BorderLayout.CENTER);
        panel.repaint();
        panel.setVisible(true);

    }

    private String getFontStyleName(Font font) {
            switch (font.getStyle()){
                case 0: return "normal";
                case 1: return "fett";
                case 2: return "kursiv";
                case 3: return "fett-kursiv";
            }
            return null;    }

    //Field Method Assignment___________________________________________________

    public void epPrintBt(){
        eps.startPrinting();
        tf.setDone();
        System.out.println("PrintBT");                 
    }

    public void epDeclineBt(){
        form.dispose();
        System.out.println("DeclineBT");
    }

    public void epGraphicBt(){
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Grafikdateien", "jpg","jpeg","png","gif");
        
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(form);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
           ((JTextField)components.get("epGraphicPathTB")).setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    public void epFowardBt(){
        pageIndex++;
        panel.setVisible(false);
        panel.removeAll();
        panel.add(new JLabel(eps.getGraphic(pageIndex)),BorderLayout.CENTER);
        panel.repaint();
        panel.setVisible(true);
        eps.getGraphic(pageIndex);
    }

    public void epHomeBt(){
        pageIndex = 0;
        panel.setVisible(false);
        panel.removeAll();
        panel.add(new JLabel(eps.getGraphic(pageIndex)),BorderLayout.CENTER);
        panel.repaint();
        panel.setVisible(true);
    }

    public void epBackwardBt(){
        if(pageIndex>=1)
        pageIndex--;
        panel.setVisible(false);
        panel.removeAll();
        panel.add(new JLabel(eps.getGraphic(pageIndex)),BorderLayout.CENTER);
        panel.repaint();
        panel.setVisible(true);
    }

    public void epAddressFontBt(){
        new FieldMapper(GenericFontFrame.class, GenericFontFrameMethods.class, components.get("epAddressFontPreviewTB"));
    }

    public void epSenderFontBt(){
        new FieldMapper(GenericFontFrame.class, GenericFontFrameMethods.class, components.get("epSenderFontPreviewTB"));
    }

    public void epSenderValueBt(){
        new FieldMapper(SenderAddressFrame.class, SenderAddressFrameMethods.class, components.get("epSenderValueBt"));
    }

    public void epCount(){
        eps.setPages((Integer) ((JSpinner)components.get("epCount")).getValue());
        panel.setVisible(false);
        panel.removeAll();
        panel.add(new JLabel(eps.getGraphic(pageIndex)),BorderLayout.CENTER);
        panel.repaint();
        panel.setVisible(true);
        eps.getGraphic(pageIndex);
    }

    public void epGraphicCheckBox(){
        eps.setWithGraphic(((JCheckBox)components.get("epGraphicCheckBox")).isSelected());
    }

    public void epSenderCheckBox(){
        eps.setWithSender(((JCheckBox)components.get("epSenderCheckBox")).isSelected());
    }

    public void epEnvelopeType(){
        eps.setFormat(((JComboBox)components.get("epEnvelopeType")).getSelectedIndex());
    }

    public void epEnvelopeAxis(){
        eps.setOrientation(((JComboBox)components.get("epEnvelopeAxis")).getSelectedIndex());
    }

    public void epSenderLocCornerRB(){
        if(((JRadioButton)components.get("epSenderLocCornerRB")).isSelected())
        eps.setSenderPosition(true);
    }

    public void epSenderLocLineRB(){
        if(((JRadioButton)components.get("epSenderLocLineRB")).isSelected())
        eps.setSenderPosition(false);
    }

    public void epOrderTable(){
        if(((JRadioButton)components.get("epOrderTable")).isSelected())
        eps.setSenderPosition(true);

    }

    public void epOrderTeam(){
        if(((JRadioButton)components.get("epOrderTeam")).isSelected())
        eps.setSenderPosition(false);

    }

    //Getter_________________________________________________________

    public JFrame getForm(){
        return form;
    }

    //DisposeMethod will be invoked while closing the parent frame

    public void DisposeMethod(){
        System.out.println("DisposeEnvelop");
        Properties props = new Properties();
        props.setProperty("AddressFontType", components.get("epAddressFontPreviewTB").getFont().getFamily());
        props.setProperty("AddressFontStyle", String.valueOf(components.get("epAddressFontPreviewTB").getFont().getStyle()));
        props.setProperty("AddressFontSize", String.valueOf(components.get("epAddressFontPreviewTB").getFont().getSize()));
        props.setProperty("SenderFontType", components.get("epSenderFontPreviewTB").getFont().getFamily());
        props.setProperty("SenderFontStyle", String.valueOf(components.get("epSenderFontPreviewTB").getFont().getStyle()));
        props.setProperty("SenderFontSize", String.valueOf(components.get("epSenderFontPreviewTB").getFont().getSize()));
        props.setProperty("ImagePath", ((JTextField)components.get("epGraphicPathTB")).getText());
        String toolTipText = ((JButton)components.get("epSenderValueBt")).getToolTipText();
        String[] tA = toolTipText.split(" | ");
        String[] t = new String[5];
        int i = 0;
        for(String s: tA){
            if(!s.contains("|")){
            t[i] = s.trim();
            i++;
            }
        }
        for(String s: t){
            props.setProperty("senderCorp", t[0]);
            props.setProperty("senderName", t[1]);
            props.setProperty("senderStreet", t[2]);
            props.setProperty("senderPLZ", t[3]);
            props.setProperty("senderLocation", t[4]);
        }
        service.updateProps(props);    
    }

}
