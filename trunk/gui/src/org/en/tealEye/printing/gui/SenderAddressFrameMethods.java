package org.en.tealEye.printing.gui;

import org.en.tealEye.guiServices.GlobalGuiServiceImpl;
import org.en.tealEye.guiServices.GlobalGuiService;

import java.awt.*;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 12.09.2009
 * Time: 14:34:56
 * To change this template use File | Settings | File Templates.
 */
public class SenderAddressFrameMethods {

    private Map<String, Component> components = new HashMap<String, Component>();
    private Component targetComponent;
    private JFrame form;
    private GlobalGuiService service;

    public SenderAddressFrameMethods(Map<String, Component> components, Object form, Component targetComponent) {
        this.components = components;
        this.targetComponent = targetComponent;
        this.form = (JFrame) form;
        service = new GlobalGuiService();
    }

    public void senderFillTextCustom(){
            ((JTextField)components.get("senderAddressFrameCorpTF")).setText(service.getProperty("senderCorp"));
            ((JTextField)components.get("senderAddressFrameNameTF")).setText(service.getProperty("senderName"));
            ((JTextField)components.get("senderAddressFrameStreetTF")).setText(service.getProperty("senderStreet"));
            ((JTextField)components.get("senderAddressFramePLZTF")).setText(service.getProperty("senderPLZ"));
            ((JTextField)components.get("senderAddressFrameLocationTF")).setText(service.getProperty("senderLocation"));
    }

    public void senderAddressFrameAcceptBt(){
            ((JButton)targetComponent).setToolTipText(((JTextField)components.get("senderAddressFrameCorpTF")).getText()+" | "+
                                                  ((JTextField)components.get("senderAddressFrameNameTF")).getText()+" | "+
                                                  ((JTextField)components.get("senderAddressFrameStreetTF")).getText()+" | "+
                                                  ((JTextField)components.get("senderAddressFramePLZTF")).getText()+" | "+
                                                  ((JTextField)components.get("senderAddressFrameLocationTF")).getText());
            Properties props = new Properties();

            props.setProperty("senderCorp", ((JTextField)components.get("senderAddressFrameCorpTF")).getText());
            props.setProperty("senderName",((JTextField)components.get("senderAddressFrameNameTF")).getText());
            props.setProperty("senderStreet",String.valueOf(((JTextField)components.get("senderAddressFrameStreetTF")).getText()));
            props.setProperty("senderPLZ", String.valueOf(((JTextField)components.get("senderAddressFramePLZTF")).getText()));
            props.setProperty("senderLocation", ((JTextField)components.get("senderAddressFrameLocationTF")).getText());

            service.updateProps(props);
        //form.dispose();
    }
    
    public void senderAddressFrameDeclineBt(){
        form.dispose();
    }

    public void DisposeMethod(){

    }
}