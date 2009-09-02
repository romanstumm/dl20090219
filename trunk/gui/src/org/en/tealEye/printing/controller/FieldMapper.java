package org.en.tealEye.printing.controller;

import org.en.tealEye.printing.gui.EnvelopePrintFrame;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.*;
import java.util.List;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 02.09.2009
 * Time: 10:06:48
 * To change this template use File | Settings | File Templates.
 */
public class FieldMapper {

//    private HashMap<String, Field> fieldMap = new HashMap<String, Field>();
    private Component form;
    //    private Class methods;
    private List<Field> fields = new ArrayList();
//    private Method[] meths;
//    private Map methodMap;


    public FieldMapper(Component form) {
        this.form = form;
//        this.methods = methods;

        registerFields();
//        registerMethods();
        addListener();
    }

    private void addListener() {
        for (Field f : fields) {
//            f.setAccessible(true);
            Object o = null;
            try {
                o = f.get(form);
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            if (o instanceof Component) {
                Component c = (Component) o;
                System.out.println(c.getName());
            }


        }
    }

/*    private void registerMethods() {
//        methods = form.getClass().getDeclaredMethods();
    }*/

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


}
