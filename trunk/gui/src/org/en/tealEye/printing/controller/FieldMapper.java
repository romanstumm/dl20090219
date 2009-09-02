package org.en.tealEye.printing.controller;

import org.en.tealEye.printing.gui.EnvelopePrintFrame;
import org.en.tealEye.printing.gui.EnvelopePrintFrameMethods;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 02.09.2009
 * Time: 10:06:48
 * To change this template use File | Settings | File Templates.
 */
public class FieldMapper implements ActionListener, ChangeListener {

    private HashMap<String, Field> fieldMap = new HashMap<String, Field>();
    private Object form;
    private Object methods;
    private List<Field> fields = new ArrayList();
    private Method[] meths;
    private Map<String, Method> methodMap = new HashMap<String, Method>();
    private List<Method> methodList;


    public FieldMapper(Object form, Object aClass) {
        this.form = form;
        this.methods = aClass;

        registerFields();
        registerMethods();
        addListener();
    }

    private void addListener() {
        for (Field f : fields) {
            f.setAccessible(true);
            Object o = null;
            try {
                o = f.get(form);
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            if (o instanceof Component) {
                Component c = (Component) o;
                if(c instanceof JButton){
                    ((JButton)c).addActionListener(this);
                }else if(c instanceof JSpinner){
                    ((JSpinner)c).addChangeListener(this);
                }else if(c instanceof JComboBox){
                    ((JComboBox)c).addActionListener(this);
                }else if(c instanceof JCheckBox){
                    ((JCheckBox)c).addActionListener(this);
                }else if(c instanceof JRadioButton){
                    ((JRadioButton)c).addActionListener(this);
                }
            }


        }
    }

    private void registerMethods() {
        meths = methods.getClass().getDeclaredMethods();
        for(Method m: meths){
            methodMap.put(m.getName(),m);
        }
    }

    private void registerFields() {
        Class each = form.getClass();
        while (each != null) {
            Field[] _fields = each.getDeclaredFields();
            for (Field f : _fields) {
                f.setAccessible(true);
                fields.add(f);
            }
            each = each.getSuperclass();
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        String compName = ((Component)o).getName();
        Method m = methodMap.get(compName);
        try {
            m.invoke(methods);
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
