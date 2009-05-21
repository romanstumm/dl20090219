package org.en.tealEye.framework;

import de.liga.dart.exception.DartException;
import de.liga.util.CalendarUtils;
import de.liga.util.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.en.tealEye.guiExt.ExtPanel.JExtTextField;

import javax.swing.*;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

/**
 * Map values between panels and objects by naming conventions
 * User: Stephan
 * Date: 08.11.2007
 * Time: 21:19:01
 */
public class PanelMapper {
    private static final Map<Class, StringConverter> converterMap = new HashMap();

    private static final Log log = LogFactory.getLog(PanelMapper.class);

    private static final PanelMapper instance = new PanelMapper();

    public static PanelMapper getInstance() {
        return instance;
    }

    static {
        // register converters here

        StringConverter converter = new StringConverter() {

            public String toString(Object value) {
                return String.valueOf(value);
            }

            public Object fromString(String value) {
                try {
                    return Integer.valueOf(value);
                } catch (NumberFormatException ex) {
                    throw new DartException(
                            "Anstatt '" + value + "' sind nur Zahlen erlaubt.");
                }
            }
        };
        converterMap.put(int.class, converter);
        converterMap.put(Integer.class, converter);

        converter = new StringConverter() {

            public String toString(Object value) {
                return String.valueOf(value);
            }

            public Object fromString(String value) {
                return Boolean.valueOf(value);
            }
        };
        converterMap.put(boolean.class, converter);
        converterMap.put(Boolean.class, converter);
        converter = new StringConverter() {
            public String toString(Object value) {
                return CalendarUtils.timeToString((Time) value);
            }

            public Object fromString(String value) {
                Time time = CalendarUtils.stringToTime(value);
                if (time == null && StringUtils.isNotEmpty(value)) {
                    throw new DartException(
                            "'" + value +
                                    "' : Zeitformat muss \"MM:ss\" sein, z.B. 20:30");
                }
                return time;
            }
        };
        converterMap.put(Time.class, converter);
    }

    /**
     * API - fill the object with values from the panel
     */
    public void setObjectValues(Object object, JPanel panel) {
        Map<String, Method> getters = findGetters(panel);
        Map<String, Method> setters = findSetters(object);
        for (Map.Entry<String, Method> entry : setters.entrySet()) {
            Method getter = getters.get(entry.getKey());
            Method setter = entry.getValue();
            if (getter != null) {
                try {
                    Object jcomponent = getter.invoke(panel);
                    setObjectValue(setter, object, jcomponent);
                } catch (DartException ex) {
                    throw ex;
                } catch (Exception e) {
                    log.error("cannot access panel value " + entry, e);
                }

            }
        }
    }

    /**
     * API - fill a panel with values from the object
     */
    public void setPanelValues(Object object, JPanel panel) {
        Map<String, Method> panelGetters = findGetters(panel);
        Map<String, Method> objGetters = findGetters(object);
        Map<String, Method> panelSetters = findSetters(panel);
        for (Map.Entry<String, Method> entry : objGetters.entrySet()) {
            Method objGet = entry.getValue();
            Method panGet = panelGetters.get(entry.getKey());
            if (panGet != null) {
                try {
                    Object jcomponent = panGet.invoke(panel);
                    if (!setPanelValue(objGet, object, jcomponent)) {
                        Method panSet = panelSetters.get(entry.getKey());
                        if (panSet != null) {
                            panSet.invoke(panel, object); // invoke plain setter
                        }
                    }
                } catch (DartException ex) {
                    throw ex;
                } catch (Exception e) {
                    log.error("cannot access panel value " + entry, e);
                }

            }
        }
    }

    private void setObjectValue(Method setter, Object object,
                                Object jcomponent) {
        Object jvalue = readJComponentValue(jcomponent);
        try {
            if (jvalue instanceof String) {
                StringConverter converter =
                        converterMap.get(setter.getParameterTypes()[0]);
                if (converter != null) {
                    jvalue = converter.fromString((String) jvalue);
                } else { // aus String, z.B. "-keiner-", wird null, wenn String nicht zuweisbar ist.
                   if(!setter.getParameterTypes()[0].isAssignableFrom(jvalue.getClass())) {
                       // So soll es sein!
                       jvalue = null;
                   }
                }
            }
            if (jvalue == null && setter.getParameterTypes()[0].isPrimitive()) {
                jvalue = ClassUtils.primitiveDefault(setter.getParameterTypes()[0]);
            }
            setter.invoke(object, jvalue);
        } catch (DartException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("cannot set value " + jvalue + " into " + object +
                    " for " + setter.getName(), e);
        }
    }

    private boolean setPanelValue(Method objGet, Object object,
                                  Object jcomponent) {
        try {
            Object objVal = objGet.invoke(object);
            return setJComponentValue(jcomponent, objVal);
        } catch (DartException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("cannot read value " + objGet.getName() + " from " +
                    object);
        }
        return true;
    }

    private boolean setJComponentValue(Object jcomponent, Object objVal) {
        if (jcomponent instanceof JExtTextField) {
            ((JExtTextField) jcomponent).setObject(objVal);
        } else if (jcomponent instanceof JTextField) {
            ((JTextField) jcomponent).setText(toString(objVal));
        } else if (jcomponent instanceof JComboBox) {
//            System.out.println("[" + Thread.currentThread().getName() + "] " + "Combo " + ((JComboBox)jcomponent).getName() + " = " + objVal);
/*
            if ("Combo_Ligaklasse".equals(((JComboBox) jcomponent).getName())) {
                log.debug("set combo value " + jcomponent + " = " + objVal, new Exception());
            }
*/
            ((JComboBox) jcomponent).setSelectedItem(objVal);
        } else if (jcomponent instanceof JList) {
            ((JList) jcomponent).setSelectedValue(objVal, true);
        } else if (jcomponent instanceof JTable) {
            JTable jtable = ((JTable) jcomponent);
            if (jtable.getModel() instanceof BeanTableModel) {
                int idx = ((BeanTableModel) jtable.getModel()).getObjects()
                        .indexOf(objVal);
                if (idx > -1) { // set selection
                    jtable.setRowSelectionInterval(idx, idx);
                }
            }
        }
        return jcomponent instanceof JComponent;
    }

    private String toString(Object objVal) {
        if (objVal == null) {
            return "";
        }
        if (!(objVal instanceof String)) {
            StringConverter converter = converterMap.get(objVal.getClass());
            if (converter != null) return converter.toString(objVal);
            else return String.valueOf(objVal);
        }
        return (String) objVal;
    }

    private Object readJComponentValue(Object jcomponent) {
        if (jcomponent instanceof JExtTextField) {
            return ((JExtTextField) jcomponent).getObject();
        } else if (jcomponent instanceof JTextField) {
            return ((JTextField) jcomponent).getText();
        } else if (jcomponent instanceof JComboBox) {
            return ((JComboBox) jcomponent).getSelectedItem();
        } else if (jcomponent instanceof JList) {
            return ((JList) jcomponent).getSelectedValue();
        } else if (jcomponent instanceof JTable) {
            JTable jtable = ((JTable) jcomponent);
            if (jtable.getModel() instanceof BeanTableModel) {
                return ((BeanTableModel) jtable.getModel())
                        .getObject(jtable.getSelectedRow());
            } /*else {
                return jtable.getModel().getValueAt(jtable.getSelectedRow(),
                        jtable.getSelectedColumn());
            }*/
        }
        return jcomponent instanceof JComponent ? null : jcomponent;
    }

    private Map<String, Method> findSetters(Object object) {
        Map<String, Method> result = new HashMap();

        try {
            PropertyDescriptor[] props = Introspector
                    .getBeanInfo(object.getClass()).getPropertyDescriptors();
            for (PropertyDescriptor prop : props) {
                if (null != prop.getWriteMethod()) {
                    result.put(prop.getName(), prop.getWriteMethod());
                }
            }
        } catch (IntrospectionException e) {
            log.error("cannot introspect " + object, e);
        }
        return result;
    }

    private Map<String, Method> findGetters(Object object) {
        Map<String, Method> result = new HashMap();

        try {
            PropertyDescriptor[] props = Introspector
                    .getBeanInfo(object.getClass()).getPropertyDescriptors();
            for (PropertyDescriptor prop : props) {
                if (null != prop.getReadMethod()) {
                    result.put(prop.getName(), prop.getReadMethod());
                }
            }
        } catch (IntrospectionException e) {
            log.error("cannot introspect " + object, e);
        }
        return result;
    }
}
