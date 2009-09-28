package org.en.tealEye.printing.gui;

import org.en.tealEye.guiServices.GlobalGuiService;
import org.en.tealEye.printing.controller.CentralDispatch;
import org.en.tealEye.printing.controller.annotationClasses.CustomMethod;

import javax.swing.*;
import java.awt.*;
import java.awt.List;
import java.util.*;
import java.beans.PropertyChangeSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 11.09.2009
 * Time: 18:24:06
 * To change this template use File | Settings | File Templates.
 */
public class GenericFontFrameMethods {

        private Map<String, Component> components;
        private Frame form;
        private Component targetComponent;
        private Font font;

        public GenericFontFrameMethods() {
            this.components = CentralDispatch.getComponents();
            this.form = (JFrame) CentralDispatch.getComponentClassObject("org.en.tealEye.printing.gui.GenericFontFrame");
        }
    
        @CustomMethod
        public void getFontPropertiesCustom(){
            components.get("genericFontFramePreviewArea").setFont(targetComponent.getFont());
            ((JList)components.get("genericFontFrameTypeList")).setSelectedValue(targetComponent.getFont().getFamily(),true);    
            ((JList)components.get("genericFontFrameStyleList")).setSelectedIndex(targetComponent.getFont().getStyle());
            ((JList)components.get("genericFontFrameSizeList")).setSelectedValue(targetComponent.getFont().getSize(),true);    
        }

        public void genericFontFrameHeaderLabel(){

        }
        public void setGenericFontFrameTargetComponent(Component c){
            this.targetComponent = c;
        }

        public void genericFontFrameTypeList(){
            font = new Font(((JList)components.get("genericFontFrameTypeList")).getSelectedValue().toString(),
                                    ((JList)components.get("genericFontFrameStyleList")).getSelectedIndex(),
                                    Integer.parseInt(((JList)components.get("genericFontFrameSizeList")).getSelectedValue().toString()));
            components.get("genericFontFramePreviewArea").setFont(font);
        }

        public void genericFontFrameStyleList(){
            font = new Font(((JList)components.get("genericFontFrameTypeList")).getSelectedValue().toString(),
                                    ((JList)components.get("genericFontFrameStyleList")).getSelectedIndex(),
                                    Integer.parseInt(((JList)components.get("genericFontFrameSizeList")).getSelectedValue().toString()));
            components.get("genericFontFramePreviewArea").setFont(font);
        }

        public void genericFontFrameSizeList(){
            font = new Font(((JList)components.get("genericFontFrameTypeList")).getSelectedValue().toString(),
                                    ((JList)components.get("genericFontFrameStyleList")).getSelectedIndex(),
                                    Integer.parseInt(((JList)components.get("genericFontFrameSizeList")).getSelectedValue().toString()));
            components.get("genericFontFramePreviewArea").setFont(font);
        }

        public void genericFontFramePreviewArea(){

        }

        public void genericFontFrameAcceptBt(){
            targetComponent.setFont(font);
            if(targetComponent instanceof JTextField){
                ((JTextField)targetComponent).setToolTipText(font.getFamily()+", "+getFontStyleName()+", "+font.getSize());
            }
            form.dispose();
        }

        public void genericFontFrameDeclineBt(){
            this.form.dispose();
        }

        private String getFontStyleName(){
            switch (font.getStyle()){
                case 0: return "normal";
                case 1: return "fett";
                case 2: return "kursiv";
                case 3: return "fett-kursiv";
            }
            return null;
        }

        public void DisposeMethod(){
        }

}
