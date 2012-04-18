package org.en.tealEye.printing.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.en.tealEye.printing.gui.GenericLoadingBarFrame;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

public class FieldMapper implements ActionListener, ChangeListener, ListSelectionListener, PropertyChangeListener, WindowListener, MouseListener {
    protected static final Log log = LogFactory.getLog(FieldMapper.class);
    private Object formObject;
    private Object methodObject;
    private List<Field> fields = new ArrayList();
    private Map<String,Component> components = new HashMap<String,Component>();
    private Map<String, Method> methodMap = new HashMap<String, Method>();
    private Map<String, SwingWorker> threadMap = new HashMap<String, SwingWorker>();

    public FieldMapper(Class formClass, Class methodClass, Object parentObject) {
        try {
            formObject = formClass.newInstance();
            if(formObject instanceof JFrame){
            ((JFrame)formObject).addWindowListener(this);
            }
        } catch (InstantiationException e) {
             log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
             log.error(e.getMessage(), e);
        }
            registerFields();
        try {
            methodObject = methodClass.getDeclaredConstructors()[0].newInstance(components, formObject, parentObject);
        } catch (InstantiationException e) {
             log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
             log.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
             log.error(e.getMessage(), e);
        }
            registerMethods();
            evokeCustomMethods();
            addListener();
        }

    public FieldMapper(Class formClass, Class methodClass, Object parentObject, Component targetComponent) {
        try {
            formObject = formClass.newInstance();
            if(formObject instanceof JFrame){
            ((JFrame)formObject).addWindowListener(this);
            }
        } catch (InstantiationException e) {
             log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
             log.error(e.getMessage(), e);
        }
            registerFields();
        try {
            methodObject = methodClass.getDeclaredConstructors()[0].newInstance(components, formObject, parentObject, targetComponent);
        } catch (InstantiationException e) {
             log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
             log.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
             log.error(e.getMessage(), e);
        }
            registerMethods();
            evokeCustomMethods();
            addListener();
        }

    public FieldMapper(Class formClass, Class methodClass) {
            try {
                formObject = formClass.newInstance();
                if(formObject instanceof JFrame){
                ((JFrame)formObject).addWindowListener(this);
                }
            } catch (InstantiationException e) {
                 log.error(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                 log.error(e.getMessage(), e);
            }
                registerFields();
            try {
                methodObject = methodClass.getDeclaredConstructors()[0].newInstance(components, formObject);
            } catch (InstantiationException e) {
                 log.error(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                 log.error(e.getMessage(), e);
            } catch (InvocationTargetException e) {
                 log.error(e.getMessage(), e);
            }
                registerMethods();
                evokeCustomMethods();
                addListener();
        }


    private void evokeCustomMethods() {
       int i = 0;
        Collection<Method> methods = methodMap.values();
        methods.toArray();

        for(Method me: methods){
             if(me.getName().contains("Custom")&&me.getName().contains("AsThread")){
                new GenericThread(me,methodObject,new GenericLoadingBarFrame(((JFrame)formObject).getLocationOnScreen()),fields).start();
                //me.invoke(methodObject);
            }
            else if(me.getName().contains("Custom")&&!(me.getName().contains("AsThread"))){
                try {
                    me.invoke(methodObject);
                } catch (IllegalAccessException e) {
                     log.error(e.getMessage(), e);  //To change body of catch statement use File | Settings | File Templates.
                } catch (InvocationTargetException e) {
                     log.error(e.getMessage(), e);  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }

    public FieldMapper(Object formObject, Object methodObject) {
        this.formObject = formObject;
        this.methodObject = methodObject;
        registerFields();
        registerMethods();
        addListener();
    }

    private void addListener() {

        for(Component c : components.values()) {
                if (c instanceof JButton) {
                    ((JButton) c).addActionListener(this);
                } else if (c instanceof JSpinner) {
                    ((JSpinner) c).addChangeListener(this);
                } else if (c instanceof JComboBox) {
                    ((JComboBox) c).addActionListener(this);
                } else if (c instanceof JCheckBox) {
                    ((JCheckBox) c).addActionListener(this);
                } else if (c instanceof JRadioButton) {
                    ((JRadioButton) c).addActionListener(this);
                } else if (c instanceof JList) {
                    ((JList) c).addListSelectionListener(this);
                } else if (c instanceof JTextField) {
                    ((JTextField)c).addPropertyChangeListener(this);
                }
        }
    }

    private void registerMethods() {
        Method[] meths = methodObject.getClass().getDeclaredMethods();
        for (Method m : meths) {
            methodMap.put(m.getName(), m);
        }
    }

    private void registerFields() {
        Class each = formObject.getClass();
        Object o = null;
        while (each != null) {
            Field[] _fields = each.getDeclaredFields();
            for (Field f : _fields) {
                f.setAccessible(true);
                fields.add(f);
                try {
                    o = f.get(formObject);
                } catch (IllegalAccessException e) {
                     log.error(e.getMessage(), e);
                }
                if(o instanceof Component){
                    Component c = (Component)o;
                    components.put(c.getName(),c);
                }
            }
            each = each.getSuperclass();
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        String compName = ((Component) o).getName();
        Method m = methodMap.get(compName);
        if (m == null){
            m = methodMap.get(compName+"asThread");
        }
//            System.out.println(m.getName());
        if(m.getName().contains("asThread")){
                Point point = ((JFrame)formObject).getLocationOnScreen();
                GenericLoadingBarFrame frame = new GenericLoadingBarFrame(point);
                new GenericThread(m, methodObject, frame, fields).start();

            }else
            {
                try {
                    m.invoke(methodObject);
                } catch (IllegalAccessException e1) {
                     log.error(e1.getMessage(), e1);
                } catch (InvocationTargetException e1) {
                     log.error(e1.getMessage(), e1);
                }
            }
        }

    public void stateChanged(ChangeEvent e) {
        Object o = e.getSource();
        String compName = ((Component) o).getName();
        Method m = methodMap.get(compName);
            if(m.getName().contains("asThread")){
                new GenericThread(m, methodObject);
            }else
            {
                try {
                    m.invoke(methodObject);
                } catch (IllegalAccessException e1) {
                     log.error(e1.getMessage(), e1);
                } catch (InvocationTargetException e1) {
                     log.error(e1.getMessage(), e1);
                }
            }
    }

    public void valueChanged(ListSelectionEvent e) {
        Object o = e.getSource();
        if(!e.getValueIsAdjusting()){
        String compName = ((Component)o).getName();
        Method m = methodMap.get(compName);
            if(m.getName().contains("asThread")){
                new GenericThread(m, methodObject);
            }else
            {
                try {
                    m.invoke(methodObject);
                } catch (IllegalAccessException e1) {
                     log.error(e1.getMessage(), e1);
                } catch (InvocationTargetException e1) {
                     log.error(e1.getMessage(), e1);
                }
            }
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        Object obj = evt.getSource();
        if(obj instanceof JTextField){

        String compName = ((JTextField)obj).getName();
        Method m = methodMap.get(compName);
        if(m != null){
            if(m.getName().contains("asThread")){
                new GenericThread(m, methodObject);
            }else
            {
                try {
                    m.invoke(methodObject);
                } catch (IllegalAccessException e1) {
                     log.error(e1.getMessage(), e1);
                } catch (InvocationTargetException e1) {
                     log.error(e1.getMessage(), e1);
                }
            }
        }
//        System.out.println("PropertyChangeListener!");
        }
    }


    public void windowOpened(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowClosing(WindowEvent e) {
//        System.out.println("Dispose");
        Method dispose = methodMap.get("DisposeMethod");
        if(dispose != null)
            try {
                dispose.invoke(methodObject);
            } catch (IllegalAccessException e1) {
                 log.error(e1.getMessage(), e1);  //To change body of catch statement use File | Settings | File Templates.
            } catch (InvocationTargetException e1) {
                 log.error(e1.getMessage(), e1);  //To change body of catch statement use File | Settings | File Templates.
            }
    }

    public void windowClosed(WindowEvent e) {

    }

    public void windowIconified(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowDeiconified(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowActivated(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowDeactivated(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    //Mouse Listener
    public void mouseClicked(MouseEvent e) {
        Component o = e.getComponent();
        String compName = o.getName();
//        System.out.println(compName);
        Method m = methodMap.get(compName);
            if(m.getName().contains("asThread")){
                new GenericThread(m, methodObject);
            }else
            {
                try {
                    m.invoke(methodObject);
                } catch (IllegalAccessException e1) {
                     log.error(e1.getMessage(), e1);
                } catch (InvocationTargetException e1) {
                     log.error(e1.getMessage(), e1);
                }
            }    }

    public void mousePressed(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseReleased(MouseEvent e) {
        Component o = e.getComponent();
        String compName = o.getName();
//        System.out.println(compName);
        Method m = methodMap.get(compName);
            if(m.getName().contains("asThread")){
                new GenericThread(m, methodObject);
            }else
            {
                try {
                    m.invoke(methodObject);
                } catch (IllegalAccessException e1) {
                     log.error(e1.getMessage(), e1);
                } catch (InvocationTargetException e1) {
                     log.error(e1.getMessage(), e1);
                }
            }
    }

    public void mouseEntered(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseExited(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
