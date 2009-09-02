package org.en.tealEye.printing.controller;

import org.en.tealEye.printing.gui.EnvelopePrintFrame;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Collection;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 02.09.2009
 * Time: 10:06:48
 * To change this template use File | Settings | File Templates.
 */
public class FieldMapper {

    private HashMap<String, Field> fieldMap = new HashMap<String,Field>();
    private Class form;
    private Class methods;
    private Field[] fields;
    private Method[] meths;
    private Map methodMap;


    public FieldMapper(Class form, Class methods) {
        this.form = form;
        this.methods = methods;

        registerFields();
        registerMethods();
        addListener();
    }

    private void addListener() {
        for(Field f: fields){
            f.setAccessible(true);
            Object o = null;
            try {
                o = f.get(form);
                System.out.println(form.getDeclaredField(f.getName()).isAccessible());

                try {
                    o = form.getDeclaredField(f.getName()).get(f);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            if(o instanceof Component) {
                Component c = (Component) o;
                System.out.println(c.getName());
            }


        }
    }

    private void registerMethods() {
        meths = methods.getDeclaredMethods();
    }

    private void registerFields() {
        fields = form.getDeclaredFields();

        for(Field f:fields){
            fieldMap.put(f.getType().toString(),f);
        }

    }




}
