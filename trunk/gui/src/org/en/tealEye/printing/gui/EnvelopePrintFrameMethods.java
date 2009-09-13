package org.en.tealEye.printing.gui;

import org.en.tealEye.printing.controller.FieldMapper;
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

    public EnvelopePrintFrameMethods(Map<String, Component> components, Object form, Object parentObject){
        this.form = (JFrame) form;
        this.components = components;
        this.parentObject = parentObject;
    }

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

    public void loadInitialGraphicsCustomAsThread(){
        
    }

    private String getFontStyleName(Font font) {
            switch (font.getStyle()){
                case 0: return "normal";
                case 1: return "fett";
                case 2: return "kursiv";
                case 3: return "fett-kursiv";
            }
            return null;    }

    public void epPrintBtasThread(){
        System.out.println("PrintBT");                 
    }

    public void epDeclineBt(){
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

    public void epAddressFontBt(){
        new FieldMapper(GenericFontFrame.class, GenericFontFrameMethods.class, components.get("epAddressFontPreviewTB"));
    }

    public void epSenderFontBt(){
        new FieldMapper(GenericFontFrame.class, GenericFontFrameMethods.class, components.get("epSenderFontPreviewTB"));
    }
    public void epSenderValueBt(){
        new FieldMapper(SenderAddressFrame.class, SenderAddressFrameMethods.class, components.get("epSenderValueBt"));
    }

    public JFrame getForm(){
        return form;
    }

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
