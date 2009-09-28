package org.en.tealEye.printing.gui;

import org.en.tealEye.guiServices.GlobalGuiServiceImpl;
import org.en.tealEye.guiServices.GlobalGuiService;
import org.en.tealEye.printing.controller.annotationClasses.CustomMethod;
import org.en.tealEye.printing.controller.CentralDispatch;

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
    private String[] targetComponent = new String[5];
    private Component component;
    private JFrame form;
    private GlobalGuiService service;

    public SenderAddressFrameMethods() {
        this.components = CentralDispatch.getComponents();
        this.targetComponent = targetComponent;
        this.form = (JFrame) CentralDispatch.getComponentClassObject("org.en.tealEye.printing.gui.SenderAddressFrame");
        this.component = component;
        service = new GlobalGuiService();
    }
    @CustomMethod
    public void senderFillTextCustom(){
            ((JTextField)components.get("senderAddressFrameCorpTF")).setText(service.getProperty("senderCorp"));
            ((JTextField)components.get("senderAddressFrameNameTF")).setText(service.getProperty("senderName"));
            ((JTextField)components.get("senderAddressFrameStreetTF")).setText(service.getProperty("senderStreet"));
            ((JTextField)components.get("senderAddressFramePLZTF")).setText(service.getProperty("senderPLZ"));
            ((JTextField)components.get("senderAddressFrameLocationTF")).setText(service.getProperty("senderLocation"));
    }

    public void senderAddressFrameAcceptBt(){
            targetComponent[0] = ((JTextField)components.get("senderAddressFrameCorpTF")).getText();
            targetComponent[1] = ((JTextField)components.get("senderAddressFrameNameTF")).getText();
            targetComponent[2] = ((JTextField)components.get("senderAddressFrameStreetTF")).getText();
            targetComponent[3] = ((JTextField)components.get("senderAddressFramePLZTF")).getText();
            targetComponent[4] = ((JTextField)components.get("senderAddressFrameLocationTF")).getText();

            ((JButton)components.get("epSenderValueBt")).setToolTipText (((JTextField)components.get("senderAddressFrameCorpTF")).getText()+" | "+
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
        CentralDispatch.invokeMethod("epSetStringArray", targetComponent);
        form.dispose();
    }

    public void senderAddressFrameDeclineBt(){
        form.dispose();
    }

    public void DisposeMethod(){

    }
}
