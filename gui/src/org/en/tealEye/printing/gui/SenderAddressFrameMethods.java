package org.en.tealEye.printing.gui;

import java.awt.*;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

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

    public SenderAddressFrameMethods(Map<String, Component> components, Object form, Component targetComponent) {
        this.components = components;
        this.targetComponent = targetComponent;
        this.form = (JFrame) form;
    }

    public void senderFillTextCustom(){
        String str = ((JButton)targetComponent).getToolTipText();
        String[] tA = str.split(" | ");
        String[] t = new String[5];
        int i = 0;
        for(String s: tA){
            if(!s.contains("|")){
            t[i] = s.trim();
            i++;
            }
        }

        for(String s: t){
            ((JTextField)components.get("senderAddressFrameCorpTF")).setText(t[0]);
            ((JTextField)components.get("senderAddressFrameNameTF")).setText(t[1]);
            ((JTextField)components.get("senderAddressFrameStreetTF")).setText(t[2]);
            ((JTextField)components.get("senderAddressFramePLZTF")).setText(t[3]);
            ((JTextField)components.get("senderAddressFrameLocationTF")).setText(t[4]);
        }
    }

    public void senderAddressFrameAcceptBt(){
        ((JButton)targetComponent).setToolTipText(((JTextField)components.get("senderAddressFrameCorpTF")).getText()+" | "+
                                                  ((JTextField)components.get("senderAddressFrameNameTF")).getText()+" | "+
                                                  ((JTextField)components.get("senderAddressFrameStreetTF")).getText()+" | "+
                                                  ((JTextField)components.get("senderAddressFramePLZTF")).getText()+" | "+
                                                  ((JTextField)components.get("senderAddressFrameLocationTF")).getText());
        form.dispose();
    }
    
    public void senderAddressFrameDeclineBt(){
        form.dispose();
    }
}
