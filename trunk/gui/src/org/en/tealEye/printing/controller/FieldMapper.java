package org.en.tealEye.printing.controller;

import org.en.tealEye.printing.gui.GenericLoadingBarFrame;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class FieldMapper implements ActionListener, ChangeListener, ListSelectionListener, PropertyChangeListener, WindowListener, MouseListener {

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
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
            registerFields();
        try {
            methodObject = methodClass.getDeclaredConstructors()[0].newInstance(components, formObject, parentObject);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
            registerFields();
        try {
            methodObject = methodClass.getDeclaredConstructors()[0].newInstance(components, formObject, parentObject, targetComponent);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
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
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
                registerFields();
            try {
                methodObject = methodClass.getDeclaredConstructors()[0].newInstance(components, formObject);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
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
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (InvocationTargetException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
                    e.printStackTrace();
                }
                if(o instanceof Component){
                    Component c = (Component)o;
                    components.put(c.getName(),c);
                }
            }
            each = each.getSuperclass();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        String compName = ((Component) o).getName();
        Method m = methodMap.get(compName);
        if (m == null){
            m = methodMap.get(compName+"asThread");
        }
            System.out.println(m.getName());
        if(m.getName().contains("asThread")){
                Point point = ((JFrame)formObject).getLocationOnScreen();
                GenericLoadingBarFrame frame = new GenericLoadingBarFrame(point);
                new GenericThread(m, methodObject, frame, fields).start();

            }else
            {
                try {
                    m.invoke(methodObject);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                }
            }
        }

    @Override
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
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                }
            }
    }

    @Override
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
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        evt.getPropagationId();
    }


    @Override
    public void windowOpened(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("Dispose");
        Method dispose = methodMap.get("DisposeMethod");
        if(dispose != null)
            try {
                dispose.invoke(methodObject);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InvocationTargetException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void windowActivated(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    //Mouse Listener
    @Override
    public void mouseClicked(MouseEvent e) {
        Component o = e.getComponent();
        String compName = o.getName();
        System.out.println(compName);
        Method m = methodMap.get(compName);
            if(m.getName().contains("asThread")){
                new GenericThread(m, methodObject);
            }else
            {
                try {
                    m.invoke(methodObject);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                }
            }    }

    @Override
    public void mousePressed(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Component o = e.getComponent();
        String compName = o.getName();
        System.out.println(compName);
        Method m = methodMap.get(compName);
            if(m.getName().contains("asThread")){
                new GenericThread(m, methodObject);
            }else
            {
                try {
                    m.invoke(methodObject);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                }
            }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
