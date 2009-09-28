package org.en.tealEye.printing.controller;



import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionListener;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import static java.lang.Class.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import org.en.tealEye.printing.controller.annotationClasses.*;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 27.09.2009
 * Time: 18:54:29
 * To change this template use File | Settings | File Templates.
 */
public abstract class CentralDispatch {

    private static Map<String, Object> componentClassObjects = new HashMap<String, Object>();
    private static Map<String, Object> methodClassObjects = new HashMap<String, Object>();
    private static Map<String, Component> componentMap = new HashMap<String, Component>();
    private static Map<String, Method> methodMap = new HashMap<String, Method>();
    private static Map<Method, Object> methodMapReverse = new HashMap<Method, Object>();
    private static Map<Object, Method> globalMethodMap = new HashMap<Object, Method>();
    private static Map<Object, Method> customListenerMethodMap = new HashMap<Object, Method>();
    private static Map<String, Method> customMethodMap = new HashMap<String, Method>();
    private static List<Component> listenerList = new ArrayList<Component>();
    private static GlobalListenerService glsS;
    private static Map<String,String> mainMap = new HashMap<String,String>();
    //private static CacheStack cache;


    public static void storeComponentClass(Class c){
        try {
            componentClassObjects.put(c.getName(), c.newInstance());
            Field[] fields = c.getDeclaredFields();
            for(Field f: fields){
                f.setAccessible(true);
                Object o = f.get(componentClassObjects.get(c.getName()));
                if(o instanceof Component)
                    componentMap.put(((Component)o).getName(),(Component)o);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        addContainerListener(componentClassObjects.get(c.getName()));
    }

    private static void addContainerListener(Object o) {
      if(!o.getClass().isAnnotationPresent(CustomContainerListener.class)){
        if(o instanceof JFrame){
            ((JFrame)o).addWindowListener(glsS);
        }else if(o instanceof JPanel){
            ((JPanel)o).addContainerListener(glsS);
        }else if(o instanceof JInternalFrame){
            ((JInternalFrame)o).addInternalFrameListener(glsS);
        }
      }else{
            addCustomContainerListener((JFrame)o);
      }

    }

    public static void storeClassBundle(Class componentClass, Class methodClass){
        mainMap.put(componentClass.getName(), methodClass.getName());
            storeComponentClass(componentClass);
            storeBoundMethodClass(methodClass);
    }

    private static void addCustomContainerListener(Object o) {
        CustomContainerListener anno = o.getClass().getAnnotation(CustomContainerListener.class);
        String type = anno.listenerType();
        String section = anno.listenerSection();
        boolean cclass = anno.customClass();
        Class c = anno.customListenerClass();
        if(o instanceof JFrame){
            if(cclass){

            }else{
                if(type.equals("WindowListener")){

                }else if(type.equals("ContainerListener")){

                }else if(type.equals("FocusListener")){

                }
            }
        }
    }

    public static void storeBoundMethodClass(Class c){
        try {
            methodClassObjects.put(c.getName(),c.newInstance());

            Method[] methods = c.getDeclaredMethods();
            for(Method m: methods){
                if(m.isAnnotationPresent(GlobalMethod.class)){
                    globalMethodMap.put(methodClassObjects.get(c.getName()),m);
                    methodMap.put(m.getName(),m);
                    methodMapReverse.put(m,methodClassObjects.get(c.getName()));
                }else if(m.isAnnotationPresent(CustomListener.class)){
                    customListenerMethodMap.put(methodClassObjects.get(c.getName()),m);
                    methodMap.put(m.getName(),m);
                    methodMapReverse.put(m,methodClassObjects.get(c.getName()));
                }else if(m.isAnnotationPresent(CustomMethod.class)){
                    methodMap.put(m.getName(),m);
                    methodMapReverse.put(m,methodClassObjects.get(c.getName()));
                }else {
                    methodMap.put(m.getName(),m);
                    methodMapReverse.put(m,methodClassObjects.get(c.getName()));
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void storeBoundMethodClass(Object o){
        methodClassObjects.put(o.getClass().getName(),o);
        Method[] methods = o.getClass().getDeclaredMethods();
        for(Method m: methods){
            if(m.isAnnotationPresent(GlobalMethod.class)){
                globalMethodMap.put(methodClassObjects.get(o.getClass().getName()),m);
                methodMap.put(m.getName(),m);
                methodMapReverse.put(m,methodClassObjects.get(o.getClass().getName()));
            }else if(m.isAnnotationPresent(CustomListener.class)){
                customListenerMethodMap.put(methodClassObjects.get(o.getClass().getName()),m);
                methodMap.put(m.getName(),m);
                methodMapReverse.put(m,methodClassObjects.get(o.getClass().getName()));
            }else if(m.isAnnotationPresent(CustomMethod.class)){
                methodMap.put(m.getName(),m);
                methodMapReverse.put(m,methodClassObjects.get(o.getClass().getName()));
            }else {
                methodMap.put(m.getName(),m);
                methodMapReverse.put(m,methodClassObjects.get(o.getClass().getName()));
            }
        }
    }

    public static void removeComponentClass(Object cClass){
        invokeDisposeMethods(mainMap.get(cClass.getClass().getName()));
        Field[] fields = cClass.getClass().getDeclaredFields();
        for(Field f: fields){
            listenerList.remove(componentMap.get(f.getName()));
            componentMap.remove(f.getName());
        }
        Method[] methods = methodClassObjects.get(mainMap.get(cClass.getClass().getName())).getClass().getDeclaredMethods();
        for(Method m : methods){
            methodMap.remove(m.getName());
        }
        globalMethodMap.remove(cClass);
        String cClassStr = cClass.getClass().getName();
        componentClassObjects.remove(cClassStr);
        mainMap.remove(cClass.getClass().getName());

        cClass.getClass().getClassLoader().clearAssertionStatus();
    }

    private static void invokeDisposeMethods(String s) {
        Collection<Method> entries = methodMap.values();
            for(Method m : entries){
                try {
                    if(m.isAnnotationPresent(DisposeMethod.class)){
                    m.invoke(methodMapReverse.get(m));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (InvocationTargetException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
    }

    public static void setupCacheStack(CacheStack stack){
        //cache = stack;
    }

    public static void registerGlobalMethods(){

    }

    public static void invokeCustomMethods(){
        Collection<Method> entries = methodMap.values();
            for(Method m : entries){
                try {
                    if(m.isAnnotationPresent(CustomMethod.class)){
                    m.invoke(methodMapReverse.get(m));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (InvocationTargetException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
    }

    public static void invokeCustomMethod(String name){
        Set entries = methodMap.entrySet();
            for(Object m : entries){
                if(((Method) ((Map.Entry)m).getValue()).getName().equals(name)){
                    try {
                        if(((Method) ((Map.Entry)m).getValue()).isAnnotationPresent(CustomMethod.class))
                        ((Method)((Map.Entry)m).getValue()).invoke(((Map.Entry)m).getKey());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
               }
           }
    }

    public static void invokeGlobalMethod(String name){
         Set entries = globalMethodMap.entrySet();
             for(Object m : entries){
                 if(((Method) ((Map.Entry)m).getValue()).getName().equals(name)){
                     try {
                         ((Method)((Map.Entry)m).getValue()).invoke(((Map.Entry)m).getKey());
                     } catch (IllegalAccessException e) {
                         e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                     } catch (InvocationTargetException e) {
                         e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                     }
                }
            }
     }

    public static void invokeGlobalMethod(String name, Object parameter){
         Set entries = globalMethodMap.entrySet();
             for(Object m : entries){
                 if(((Method) ((Map.Entry)m).getValue()).getName().equals(name)){
                     try {
                         ((Method)((Map.Entry)m).getValue()).invoke(((Map.Entry)m).getKey(), parameter);
                     } catch (IllegalAccessException e) {
                         e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                     } catch (InvocationTargetException e) {
                         e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                     }
                }
            }
     }

    public static void invokeGlobalMethod(String name, Object parameter, Object parameter2){
         Set entries = globalMethodMap.entrySet();
             for(Object m : entries){
                 if(((Method) ((Map.Entry)m).getValue()).getName().equals(name)){
                     try {
                         ((Method)((Map.Entry)m).getValue()).invoke(((Map.Entry)m).getKey(), parameter, parameter2);
                     } catch (IllegalAccessException e) {
                         e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                     } catch (InvocationTargetException e) {
                         e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                     }
                }
            }
     }

    public static Object getComponentClassObject(String name){
        return componentClassObjects.get(name);
    }

    public static Object getComponent(String name){
        return componentMap.get(name);
    }

    public static boolean isMethodAllowed(String s, String name) {
        if(methodMap.containsKey(name)){
        Method m = methodMap.get(name);
        Object methodObject = methodMapReverse.get(m);
           if(m.isAnnotationPresent(CustomListener.class)){
            CustomListener anno = m.getAnnotation(CustomListener.class);
            return anno.listenerSection().equals(s);
           }else{
               return true;
           }
        }else{
            return false;
        }
    }

    private static String getListenerType(String name){
        Method m = methodMap.get(name);
        Object methodObject = methodMapReverse.get(m);
            CustomListener anno = m.getAnnotation(CustomListener.class);
                return anno.listenerType();
    }

    private static boolean hasAnnotation(String name){
        if(methodMap.containsKey(name))
        return methodMap.get(name).isAnnotationPresent(CustomListener.class);
        return false;
    }

    private static boolean hasCustomListenerClass(String name){
        Method m = methodMap.get(name);
            CustomListener anno = m.getAnnotation(CustomListener.class);
                return anno.customClass();
    }

    private static String getCustomListenerClass(String name){
        Method m = methodMap.get(name);
            CustomListener anno = m.getAnnotation(CustomListener.class);
                return anno.customClassName();
    }

    public static void addListener(){
        for(Component c: componentMap.values()){
            if(methodMap.containsKey(c.getName())&&!listenerList.contains(c)){
                listenerList.add(c);
            if(c instanceof JButton){
                JButton comp = (JButton) c;
                if(hasAnnotation(comp.getName())){
                    if(!hasCustomListenerClass(comp.getName()))
                    addCustomListener(comp);
                    else{
                        addCustomListenerClass(comp);
                    }
                }else{
                    comp.addActionListener(glsS);
                }
            }else if(c instanceof JSpinner){
                JSpinner comp = (JSpinner) c;
                if(hasAnnotation(comp.getName())){
                    addCustomListener(comp);
                }else{
                    comp.addChangeListener(glsS);
                }
            }else if(c instanceof JList){
                JList comp = (JList) c;
                if(hasAnnotation(comp.getName())){
                    addCustomListener(comp);
                }else{
                    comp.addListSelectionListener(glsS);
                }
            }else if(c instanceof JTable){
                JTable comp = (JTable) c;
                if(hasAnnotation(comp.getName())){
                    addCustomListener(comp);
                }else{
                    comp.addMouseListener(glsS);
                }
            }else if(c instanceof JLabel){
                JLabel comp = (JLabel) c;
                if(hasAnnotation(comp.getName())){
                    addCustomListener(comp);
                }else{
                    comp.addMouseListener(glsS);
                }
            }else if(c instanceof JTree){
                JTree comp = (JTree) c;
                if(hasAnnotation(comp.getName())){
                    addCustomListener(comp);
                }else{
                    comp.addTreeSelectionListener(glsS);
                }
            }else if(c instanceof JMenuItem){
                JMenuItem comp = (JMenuItem) c;
                if(hasAnnotation(comp.getName())){
                    addCustomListener(comp);
                }else{
                    comp.addActionListener(glsS);
                }
            }else if(c instanceof JSlider){
                JSlider comp = (JSlider) c;
                if(hasAnnotation(comp.getName())){
                    addCustomListener(comp);
                }else{
                    comp.addChangeListener(glsS);
                }
            }else if(c instanceof JCheckBox){
                JCheckBox comp = (JCheckBox) c;
                if(hasAnnotation(comp.getName())){
                    addCustomListener(comp);
                }else{
                    comp.addActionListener(glsS);
                }
            }else if(c instanceof JRadioButton){
                JRadioButton comp = (JRadioButton) c;
                if(hasAnnotation(comp.getName())){
                    addCustomListener(comp);
                }else{
                    comp.addActionListener(glsS);
                }
            }else if(c instanceof JToggleButton){
                JToggleButton comp = (JToggleButton) c;
                if(hasAnnotation(comp.getName())){
                    addCustomListener(comp);
                }else{
                    comp.addActionListener(glsS);
                }
            }else if(c instanceof JComboBox){
                JComboBox comp = (JComboBox) c;
                if(hasAnnotation(comp.getName())){
                    addCustomListener(comp);
                }else{
                    comp.addActionListener(glsS);
                }
            }else if(c instanceof JTextField){
                JTextField comp = (JTextField) c;
                if(hasAnnotation(comp.getName())){
                    addCustomListener(comp);
                }else{
                    comp.addPropertyChangeListener(glsS);
                }
            }
        } }
    }

    private static void addCustomListenerClass(Component component) {
                    String type = getListenerType(component.getName());
                    Object listener = null;
                    String className = getCustomListenerClass(component.getName());
                    try {
                        if(!getCustomListenerClass(component.getName()).equals("")){
                            Class c = forName(className);
                            listener = c.newInstance();
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (InstantiationException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
        if(component instanceof JButton){
              if(type.equals("MouseListener"))
                   ((JButton)component).addMouseListener((MouseListener) listener);
              else if(type.equals("ActionListener")){
                  ((JButton)component).addActionListener((ActionListener) listener);
              }else if(type.equals("KeyListener")){
                      ((JButton)component).addKeyListener((KeyListener) listener);
              }else if(type.equals("MouseMotionListener")){
                      ((JButton)component).addMouseMotionListener((MouseMotionListener) listener);
              }else if(type.equals("MouseWheelListener")){
                      ((JButton)component).addMouseWheelListener((MouseWheelListener) listener);
              }else if(type.equals("ChangeListener")){
                      ((JButton)component).addChangeListener((ChangeListener) listener);
              }
        }else if(component instanceof JList){
               if(type.equals("MouseListener"))
                   ((JList)component).addMouseListener((MouseListener) listener);
              else if(type.equals("ListSelectionListener")){
                  ((JList)component).addListSelectionListener((ListSelectionListener) listener);
              }else if(type.equals("KeyListener")){
                      ((JList)component).addKeyListener((KeyListener) listener);
              }else if(type.equals("MouseMotionListener")){
                      ((JList)component).addMouseMotionListener((MouseMotionListener) listener);
              }else if(type.equals("MouseWheelListener")){
                      ((JList)component).addMouseWheelListener((MouseWheelListener) listener);
              }
        }else if(component instanceof JTree){
              if(type.equals("MouseListener"))
                   ((JTree)component).addMouseListener((MouseListener) listener);
              else if(type.equals("TreeSelectionListener")){
                  ((JTree)component).addTreeSelectionListener((TreeSelectionListener) listener);
              }else if(type.equals("KeyListener")){
                      ((JTree)component).addKeyListener((KeyListener) listener);
              }else if(type.equals("MouseMotionListener")){
                      ((JTree)component).addMouseMotionListener((MouseMotionListener) listener);
              }else if(type.equals("MouseWheelListener")){
                      ((JTree)component).addMouseWheelListener((MouseWheelListener) listener);
              }
        }else if(component instanceof JTable){
              if(type.equals("MouseListener"))
                   ((JTable)component).addMouseListener((MouseListener) listener);
              else if(type.equals("KeyListener")){
                      ((JTable)component).addKeyListener((KeyListener) listener);
              }else if(type.equals("MouseMotionListener")){
                      ((JTable)component).addMouseMotionListener((MouseMotionListener) listener);
              }else if(type.equals("MouseWheelListener")){
                      ((JTable)component).addMouseWheelListener((MouseWheelListener) listener);
              }
        }else if(component instanceof JLabel){
              if(type.equals("MouseListener"))
                   ((JLabel)component).addMouseListener((MouseListener) listener);
              else if(type.equals("KeyListener")){
                      ((JLabel)component).addKeyListener((KeyListener) listener);
              }else if(type.equals("MouseMotionListener")){
                      ((JLabel)component).addMouseMotionListener((MouseMotionListener) listener);
              }else if(type.equals("MouseWheelListener")){
                      ((JLabel)component).addMouseWheelListener((MouseWheelListener) listener);
              }
        }else if(component instanceof JRadioButton){
              if(type.equals("MouseListener"))
                   ((JRadioButton)component).addMouseListener((MouseListener) listener);
              else if(type.equals("ActionListener")){
                  ((JRadioButton)component).addActionListener((ActionListener) listener);
              }else if(type.equals("KeyListener")){
                      ((JRadioButton)component).addKeyListener((KeyListener) listener);
              }else if(type.equals("MouseMotionListener")){
                      ((JRadioButton)component).addMouseMotionListener((MouseMotionListener) listener);
              }else if(type.equals("MouseWheelListener")){
                      ((JRadioButton)component).addMouseWheelListener((MouseWheelListener) listener);
              }else if(type.equals("ChangeListener")){
                      ((JRadioButton)component).addChangeListener((ChangeListener) listener);
              }
        }else if(component instanceof JCheckBox){
              if(type.equals("MouseListener"))
                   ((JCheckBox)component).addMouseListener((MouseListener) listener);
              else if(type.equals("ActionListener")){
                  ((JCheckBox)component).addActionListener((ActionListener) listener);
              }else if(type.equals("KeyListener")){
                      ((JCheckBox)component).addKeyListener((KeyListener) listener);
              }else if(type.equals("MouseMotionListener")){
                      ((JCheckBox)component).addMouseMotionListener((MouseMotionListener) listener);
              }else if(type.equals("MouseWheelListener")){
                      ((JCheckBox)component).addMouseWheelListener((MouseWheelListener) listener);
              }else if(type.equals("ChangeListener")){
                      ((JCheckBox)component).addChangeListener((ChangeListener) listener);
              }
        }else if(component instanceof JSpinner){
              if(type.equals("MouseListener"))
                   ((JSpinner)component).addMouseListener((MouseListener) listener);
              else if(type.equals("KeyListener")){
                      ((JSpinner)component).addKeyListener((KeyListener) listener);
              }else if(type.equals("MouseMotionListener")){
                      ((JSpinner)component).addMouseMotionListener((MouseMotionListener) listener);
              }else if(type.equals("MouseWheelListener")){
                      ((JSpinner)component).addMouseWheelListener((MouseWheelListener) listener);
              }else if(type.equals("ChangeListener")){
                      ((JSpinner)component).addChangeListener((ChangeListener) listener);
              }
        }else if(component instanceof JMenuItem){
              if(type.equals("MouseListener"))
                   ((JMenuItem)component).addMouseListener((MouseListener) listener);
              else if(type.equals("ActionListener")){
                  ((JMenuItem)component).addActionListener((ActionListener) listener);
              }else if(type.equals("KeyListener")){
                      ((JMenuItem)component).addKeyListener((KeyListener) listener);
              }else if(type.equals("MouseMotionListener")){
                      ((JMenuItem)component).addMouseMotionListener((MouseMotionListener) listener);
              }else if(type.equals("MouseWheelListener")){
                      ((JMenuItem)component).addMouseWheelListener((MouseWheelListener) listener);
              }else if(type.equals("ChangeListener")){
                      ((JMenuItem)component).addChangeListener((ChangeListener) listener);
              }
        }else if(component instanceof JSlider){
              if(type.equals("MouseListener"))
                   ((JSlider)component).addMouseListener((MouseListener) listener);
              else if(type.equals("KeyListener")){
                      ((JSlider)component).addKeyListener((KeyListener) listener);
              }else if(type.equals("MouseMotionListener")){
                      ((JSlider)component).addMouseMotionListener((MouseMotionListener) listener);
              }else if(type.equals("MouseWheelListener")){
                      ((JSlider)component).addMouseWheelListener((MouseWheelListener) listener);
              }else if(type.equals("ChangeListener")){
                      ((JSlider)component).addChangeListener((ChangeListener) listener);
              }
        }else if(component instanceof JToggleButton){
              if(type.equals("MouseListener"))
                   ((JToggleButton)component).addMouseListener((MouseListener) listener);
              else if(type.equals("ActionListener")){
                  ((JToggleButton)component).addActionListener((ActionListener) listener);
              }else if(type.equals("KeyListener")){
                      ((JToggleButton)component).addKeyListener((KeyListener) listener);
              }else if(type.equals("MouseMotionListener")){
                      ((JToggleButton)component).addMouseMotionListener((MouseMotionListener) listener);
              }else if(type.equals("MouseWheelListener")){
                      ((JToggleButton)component).addMouseWheelListener((MouseWheelListener) listener);
              }else if(type.equals("ChangeListener")){
                      ((JToggleButton)component).addChangeListener((ChangeListener) listener);
              }
        }
    }

    private static void addCustomListener(Component component) {
        String type = getListenerType(component.getName());
        if(component instanceof JButton){
                if(type.equals("MouseListener"))
                    addCustomMouseListener(component);
                else if(type.equals("ActionListener")){
                        addCustomActionListener(component);
                }else if(type.equals("KeyListener")){
                        addCustomKeyListener(component);
                }else if(type.equals("MouseMotionListener")){
                        addCustomMouseMotionListener(component);
                }else if(type.equals("MouseWheelListener")){
                        addCustomMouseWheelListener(component);
                }else if(type.equals("ChangeListener")){
                        addCustomChangeListener(component);
                }
        }else if(component instanceof JList){
            if(type.equals("MouseListener"))
                addCustomMouseListener(component);
            else if(type.equals("ActionListener")){
                    addCustomActionListener(component);
            }else if(type.equals("KeyListener")){
                    addCustomKeyListener(component);
            }else if(type.equals("MouseMotionListener")){
                    addCustomMouseMotionListener(component);
            }else if(type.equals("MouseWheelListener")){
                    addCustomMouseWheelListener(component);
            }else if(type.equals("ChangeListener")){
                    addCustomChangeListener(component);
            }

        }else if(component instanceof JTable){
            if(type.equals("MouseListener"))
                addCustomMouseListener(component);
            else if(type.equals("ActionListener")){
                    addCustomActionListener(component);
            }else if(type.equals("KeyListener")){
                    addCustomKeyListener(component);
            }else if(type.equals("MouseMotionListener")){
                    addCustomMouseMotionListener(component);
            }else if(type.equals("MouseWheelListener")){
                    addCustomMouseWheelListener(component);
            }else if(type.equals("ChangeListener")){
                    addCustomChangeListener(component);
            }

        }else if(component instanceof JLabel){
            if(type.equals("MouseListener"))
                addCustomMouseListener(component);
            else if(type.equals("ActionListener")){
                    addCustomActionListener(component);
            }else if(type.equals("KeyListener")){
                    addCustomKeyListener(component);
            }else if(type.equals("MouseMotionListener")){
                    addCustomMouseMotionListener(component);
            }else if(type.equals("MouseWheelListener")){
                    addCustomMouseWheelListener(component);
            }else if(type.equals("ChangeListener")){
                    addCustomChangeListener(component);
            }

        }else if(component instanceof JTree){
            if(type.equals("MouseListener"))
                addCustomMouseListener(component);
            else if(type.equals("ActionListener")){
                    addCustomActionListener(component);
            }else if(type.equals("KeyListener")){
                    addCustomKeyListener(component);
            }else if(type.equals("MouseMotionListener")){
                    addCustomMouseMotionListener(component);
            }else if(type.equals("MouseWheelListener")){
                    addCustomMouseWheelListener(component);
            }else if(type.equals("ChangeListener")){
                    addCustomChangeListener(component);
            }

        }else if(component instanceof JSpinner){
            if(type.equals("MouseListener"))
                addCustomMouseListener(component);
            else if(type.equals("ActionListener")){
                    addCustomActionListener(component);
            }else if(type.equals("KeyListener")){
                    addCustomKeyListener(component);
            }else if(type.equals("MouseMotionListener")){
                    addCustomMouseMotionListener(component);
            }else if(type.equals("MouseWheelListener")){
                    addCustomMouseWheelListener(component);
            }else if(type.equals("ChangeListener")){
                    addCustomChangeListener(component);
            }
        }else if(component instanceof JMenuItem){
            if(type.equals("MouseListener"))
                addCustomMouseListener(component);
            else if(type.equals("ActionListener")){
                    addCustomActionListener(component);
            }else if(type.equals("KeyListener")){
                    addCustomKeyListener(component);
            }else if(type.equals("MouseMotionListener")){
                    addCustomMouseMotionListener(component);
            }else if(type.equals("MouseWheelListener")){
                    addCustomMouseWheelListener(component);
            }else if(type.equals("ChangeListener")){
                    addCustomChangeListener(component);
            }
        }else if(component instanceof JRadioButton){
            if(type.equals("MouseListener"))
                addCustomMouseListener(component);
            else if(type.equals("ActionListener")){
                    addCustomActionListener(component);
            }else if(type.equals("KeyListener")){
                    addCustomKeyListener(component);
            }else if(type.equals("MouseMotionListener")){
                    addCustomMouseMotionListener(component);
            }else if(type.equals("MouseWheelListener")){
                    addCustomMouseWheelListener(component);
            }else if(type.equals("ChangeListener")){
                    addCustomChangeListener(component);
            }
        }else if(component instanceof JCheckBox){
            if(type.equals("MouseListener"))
                addCustomMouseListener(component);
            else if(type.equals("ActionListener")){
                    addCustomActionListener(component);
            }else if(type.equals("KeyListener")){
                    addCustomKeyListener(component);
            }else if(type.equals("MouseMotionListener")){
                    addCustomMouseMotionListener(component);
            }else if(type.equals("MouseWheelListener")){
                    addCustomMouseWheelListener(component);
            }else if(type.equals("ChangeListener")){
                    addCustomChangeListener(component);
            }
        }else if(component instanceof JSlider){
            if(type.equals("MouseListener"))
                addCustomMouseListener(component);
            else if(type.equals("ActionListener")){
                    addCustomActionListener(component);
            }else if(type.equals("KeyListener")){
                    addCustomKeyListener(component);
            }else if(type.equals("MouseMotionListener")){
                    addCustomMouseMotionListener(component);
            }else if(type.equals("MouseWheelListener")){
                    addCustomMouseWheelListener(component);
            }else if(type.equals("ChangeListener")){
                    addCustomChangeListener(component);
            }
        }else if(component instanceof JToggleButton){
            if(type.equals("MouseListener"))
                addCustomMouseListener(component);
            else if(type.equals("ActionListener")){
                    addCustomActionListener(component);
            }else if(type.equals("KeyListener")){
                    addCustomKeyListener(component);
            }else if(type.equals("MouseMotionListener")){
                    addCustomMouseMotionListener(component);
            }else if(type.equals("MouseWheelListener")){
                    addCustomMouseWheelListener(component);
            }else if(type.equals("ChangeListener")){
                    addCustomChangeListener(component);
            }
        }
    }

    private static void addCustomMouseListener(Component component) {
        component.addMouseListener(glsS);
    }

    private static void addCustomMouseMotionListener(Component component) {
        component.addMouseMotionListener(glsS);
    }

    private static void addCustomKeyListener(Component component) {
        component.addKeyListener(glsS);
    }

    private static void addCustomChangeListener(Component component) {
        component.addPropertyChangeListener(glsS);
    }

    private static void addCustomMouseWheelListener(Component component) {
        component.addMouseWheelListener(glsS);
    }

    private static void addCustomActionListener(Component component) {
        ((JButton)component).addActionListener(glsS);
    }

    public static void setListenerService(GlobalListenerService gls) {
        glsS = gls;
    }

    public static Object invokeMethod(String name) {
        try {
            if(methodMap.containsKey(name)){
            Method m = methodMap.get(name);
            Class c = null;
                  c = m.getReturnType();

                if(c == null){
                    methodMap.get(name).invoke(methodMapReverse.get(methodMap.get(name)));
                    return null;
                }else{
                    return m.invoke(methodMapReverse.get(m));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public static void addCustomActionListenerByName(String name, Class listener){

    }

    public static Map<String, Component> getComponents() {
        return componentMap;
    }

    public static Object invokeMethod(String name, Object component) {
        try {
            Method m = methodMap.get(name);
            Class c = m.getReturnType();
                if(c == null){
                    methodMap.get(name).invoke(methodMapReverse.get(methodMap.get(name)),component);
                    return null;
                }else{
                    return m.invoke(methodMapReverse.get(m),component);
                }

        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public static void invokeCustomMethods(Class c) {
        Method[] methods = c.getDeclaredMethods();
        for(Method m: methods){
            if(m.isAnnotationPresent(CustomMethod.class))
                try {
                    m.invoke(methodMapReverse.get(m));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (InvocationTargetException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
        }
    }
}
