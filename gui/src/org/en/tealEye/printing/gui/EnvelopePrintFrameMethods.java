package org.en.tealEye.printing.gui;

import org.en.tealEye.printing.controller.FieldMapper;
import org.en.tealEye.printing.controller.GenericThread;
import org.en.tealEye.printing.service.EnvelopePrintService;
import org.en.tealEye.guiServices.GlobalGuiService;
import org.apache.poi.hssf.record.formula.functions.T;

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
    private String[] sender = new String[5];
    private String graphicPath;


    public EnvelopePrintFrameMethods(Map<String, Component> components, Object form, Object parentObject){
        this.form = (JFrame) form;
        this.components = components;
        this.parentObject = parentObject;
    }

    //CustomMethods will be invoked at startup__________________________________

    public void initCustom(){
        getFontProperties();
        loadInitialGraphics();
    }

    public void getFontProperties(){
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
        ((JComboBox)components.get("epEnvelopeType")).setSelectedIndex(Integer.parseInt(service.getProperty("epEnvelopeType")));
        ((JSpinner)components.get("epCount")).setValue(Integer.parseInt(service.getProperty("epCount")));

        if(service.getProperty("ShowSender").equals("true"))
        ((JCheckBox)components.get("epSenderCheckBox")).setSelected(true);

        if(service.getProperty("ShowGraphic").equals("true"))
        ((JCheckBox)components.get("epGraphicCheckBox")).setSelected(true);

        if(service.getProperty("SenderInCorner").equals("true"))
            ((JRadioButton)components.get("epSenderLocCornerRB")).setSelected(true);
        else
            ((JRadioButton)components.get("epSenderLocLineRB")).setSelected(true);

        sender[0] = service.getProperty("senderCorp");
        sender[1] = service.getProperty("senderName");
        sender[2] = service.getProperty("senderStreet");
        sender[3] = service.getProperty("senderPLZ");
        sender[4] = service.getProperty("senderLocation");
        graphicPath = service.getProperty("ImagePath");
    }

    public void loadInitialGraphics(){

        eps = new EnvelopePrintService(parentObject, ((JCheckBox)components.get("epGraphicCheckBox")).isSelected(),
                                                         ((JCheckBox)components.get("epSenderCheckBox")).isSelected(),
                                                         ((JComboBox)components.get("epEnvelopeType")).getSelectedIndex(),
                                                         ((JComboBox)components.get("epEnvelopeAxis")).getSelectedIndex(),
                                                         ((JSpinner)components.get("epCount")).getValue(),
                                                         ((JRadioButton)components.get("epOrderTable")).isSelected(),
                                                         ((JRadioButton)components.get("epSenderLocCornerRB")).isSelected(),
                                                         components.get("epAddressFontPreviewTB").getFont(),
                                                         components.get("epSenderFontPreviewTB").getFont(),
                                                        ((JTextField)components.get("epGraphicPathTB")).getText(),
                                                         sender, graphicPath                                                    );
        eps.setXAxisAddress(Integer.parseInt(service.getProperty("XAxisAddress")));
        eps.setYAxisAddress(Integer.parseInt(service.getProperty("YAxisAddress")));
        eps.setXAxisSender(Integer.parseInt(service.getProperty("XAxisSender")));
        eps.setYAxisSender(Integer.parseInt(service.getProperty("YAxisSender")));
        eps.setXAxisGraphic(Integer.parseInt(service.getProperty("XAxisGraphic")));
        eps.setYAxisGraphic(Integer.parseInt(service.getProperty("YAxisGraphic")));
        eps.setAddressFont(((JTextField)components.get("epAddressFontPreviewTB")).getFont());
        eps.setSenderFont(((JTextField)components.get("epSenderFontPreviewTB")).getFont());




        panel = ((JPanel)components.get("previewPanel"));
        panel.setVisible(false);
        panel.setLayout(new BorderLayout());
        ImageIcon icon = eps.getGraphic(pageIndex);
        //if(icon != null)
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

    private void updatePanel(){
        panel.setVisible(false);
        panel.removeAll();
        panel.add(new JLabel(eps.getGraphic(pageIndex)),BorderLayout.CENTER);
        panel.repaint();
        panel.setVisible(true);
    }

    //Field Method Assignment___________________________________________________

    public void epPrintBt(){
        eps.startPrinting();
        
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
        if(pageIndex<eps.getMaxPages()-1)
        pageIndex++;

        updatePanel();
    }

    public void epHomeBt(){
        pageIndex = 0;
        updatePanel();
    }

    public void epBackwardBt(){
        if(pageIndex>=1)
        pageIndex--;
        updatePanel();

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

    public void epAddressFontPreviewTB(){
        eps.setAddressFont(((JTextField)components.get("epAddressFontPreviewTB")).getFont());
        updatePanel();
    }

    public void epSenderFontPreviewTB(){
        eps.setSenderFont(((JTextField)components.get("epSenderFontPreviewTB")).getFont());
        updatePanel();
    }

    public void epGraphicPathTB(){
        eps.setGraphicPath(((JTextField)components.get("epGraphicPathTB")).getText());
        updatePanel();
    }

    public void epCount(){
        eps.setPages((Integer) ((JSpinner)components.get("epCount")).getValue());
        updatePanel();
    }

    public void epGraphicCheckBox(){
        eps.setWithGraphic(((JCheckBox)components.get("epGraphicCheckBox")).isSelected());
        updatePanel();
    }

    public void epSenderCheckBox(){
        eps.setWithSender(((JCheckBox)components.get("epSenderCheckBox")).isSelected());
        updatePanel();
    }

    public void epEnvelopeType(){
        eps.setFormat(((JComboBox)components.get("epEnvelopeType")).getSelectedIndex());
        pageIndex = 0;
        updatePanel();
    }

    public void epEnvelopeAxis(){
        eps.setOrientation(((JComboBox)components.get("epEnvelopeAxis")).getSelectedIndex());
    }

    public void epSenderLocCornerRB(){
        if(((JRadioButton)components.get("epSenderLocCornerRB")).isSelected())
        eps.setSenderPosition(true);
        updatePanel();
    }

    public void epSenderLocLineRB(){
        if(((JRadioButton)components.get("epSenderLocLineRB")).isSelected())
        eps.setSenderPosition(false);
        updatePanel();
    }

    public void epOrderTable(){
        if(((JRadioButton)components.get("epOrderTable")).isSelected())
        eps.setSenderPosition(true);
    }

    public void epOrderTeam(){
        if(((JRadioButton)components.get("epOrderTeam")).isSelected())
        eps.setSenderPosition(false);
    }

    public void epAddressUpBt(){
        eps.setYAxisAddress(-1);
        updatePanel();
    }

    public void epAddressRightBt(){
        eps.setXAxisAddress(1);
        updatePanel();
    }

    public void epAddressLeftBt(){
        eps.setXAxisAddress(-1);
        updatePanel();
    }

    public void epAddressDownBt(){
        eps.setYAxisAddress(1);
        updatePanel();
    }

    public void epSenderUpBt(){
        eps.setYAxisSender(-1);
        updatePanel();
    }

    public void epSenderRightBt(){
        eps.setXAxisSender(1);
        updatePanel();
    }

    public void epSenderLeftBt(){
        eps.setXAxisSender(-1);
        updatePanel();
    }

    public void epSenderDownBt(){
        eps.setYAxisSender(1);
        updatePanel();
    }

    public void epGraphicUpBt(){
        eps.setYAxisGraphic(-1);
        updatePanel();
    }

    public void epGraphicRightBt(){
        eps.setXAxisGraphic(1);
        updatePanel();
    }

    public void epGraphicLeftBt(){
        eps.setXAxisGraphic(-1);
        updatePanel();
    }

    public void epGraphicDownBt(){
        eps.setYAxisGraphic(1);
        updatePanel();
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
        props.setProperty("epEnvelopeType",String.valueOf(((JComboBox)components.get("epEnvelopeType")).getSelectedIndex()));
        props.setProperty("epCount",String.valueOf(((JSpinner)components.get("epCount")).getValue()));
        props.setProperty("XAxisAddress",String.valueOf(eps.getXAxisAddress()));
        props.setProperty("YAxisAddress",String.valueOf(eps.getYAxisAddress()));
        props.setProperty("XAxisSender",String.valueOf(eps.getXAxisSender()));
        props.setProperty("YAxisSender",String.valueOf(eps.getYAxisSender()));
        props.setProperty("XAxisGraphic",String.valueOf(eps.getXAxisGraphic()));
        props.setProperty("YAxisGraphic",String.valueOf(eps.getYAxisGraphic()));
        if(((JCheckBox)components.get("epSenderCheckBox")).isSelected())
        props.setProperty("ShowSender","true");
        else
        props.setProperty("ShowSender","false");
        if(((JCheckBox)components.get("epGraphicCheckBox")).isSelected())
        props.setProperty("ShowGraphic","true");
        else
        props.setProperty("ShowGraphic","false");
        if(((JRadioButton)components.get("epSenderLocCornerRB")).isSelected())
            props.setProperty("SenderInCorner","true");
        else
            props.setProperty("SenderInCorner","false");
        //props.setProperty("SenderInCorner",String.valueOf(((JCheckBox)components.get("epGraphicCheckBox()")).isSelected()));


        service.updateProps(props);    
    }

}
