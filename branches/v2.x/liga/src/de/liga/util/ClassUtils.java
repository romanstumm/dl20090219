package de.liga.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: Some useful methods to act with classes and class loaders<br/>
 * User: roman.stumm <br/>
 * Date: 14.02.2007 <br/>
 * Time: 10:13:21 <br/>
 * Copyright: Agimatec GmbH
 */
public final class ClassUtils extends org.apache.commons.lang.ClassUtils {
    private ClassUtils() {
    }

    /**
     * @return contextclassloader or the caller classLoader
     */
    public static ClassLoader getClassLoader() {
        final ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return cl == null ? ClassUtils.class.getClassLoader() : cl;
        // if Reflection.getCallerClass(3) is not the correct stuff, just drop it.
//        return cl == null ? sun.reflect.Reflection.getCallerClass(3).getClassLoader() : cl;
    }

    /**
     * Returns an appropriate default value for primitives
     *
     * @param aClass - a primitive class
     * @return FALSE for boolean, 0 else
     * @throws IllegalArgumentException - if aClass is not of primitive type
     */
    public static Object primitiveDefault(Class aClass) {
        if (aClass.equals(Boolean.TYPE)) {
            return Boolean.FALSE;
        }
        if (aClass.equals(Short.TYPE)) {
            return (short) 0;
        }
        if (aClass.equals(Integer.TYPE)) {
            return 0;
        }
        if (aClass.equals(Byte.TYPE)) {
            return (byte) 0;
        }
        if (aClass.equals(Long.TYPE)) {
            return (long) 0;
        }
        if (aClass.equals(Float.TYPE)) {
            return (float) 0;
        }
        if (aClass.equals(Double.TYPE)) {
            return (double) 0;
        }
        if (aClass.equals(Character.TYPE)) {
            return '\u0000';
        }
        throw new IllegalArgumentException("Only works for primitives");
    }

    /**
     * This method calls the valueOf() Method of the adequate class.
     * <p/>
     * It can handle all primitive types, Wrapperclasses, BigDecimal, BigInteger,
     * String and null.
     *
     * @param class - the class e.g. int.class, Integer.class, Boolean.class
     * @param s     - the string representation
     * @return the wrapper object type
     * @see Boolean#valueOf(String)
     * @see Long#valueOf(String)
     * @see Integer#valueOf(String)
     * @see Float#valueOf(String)
     * @see Double#valueOf(String)
     * @see Byte#valueOf(String)
     * @see Short#valueOf(String)
     */
    private static final Map<Class, Method> valueOfMethods;

    static {
        valueOfMethods = new HashMap(15);
        try {
            valueOfMethods.put(Integer.TYPE, valueOfMethod(Integer.class));
            valueOfMethods.put(Boolean.TYPE, valueOfMethod(Boolean.class));
            valueOfMethods.put(Long.TYPE, valueOfMethod(Long.class));
            valueOfMethods.put(Short.TYPE, valueOfMethod(Short.class));
            valueOfMethods.put(Float.TYPE, valueOfMethod(Float.class));
            valueOfMethods.put(Double.TYPE, valueOfMethod(Double.class));
            valueOfMethods.put(Byte.TYPE, valueOfMethod(Byte.class));

            valueOfMethods.put(Integer.class, valueOfMethod(Integer.class));
            valueOfMethods.put(Boolean.class, valueOfMethod(Boolean.class));
            valueOfMethods.put(Long.class, valueOfMethod(Long.class));
            valueOfMethods.put(Short.class, valueOfMethod(Short.class));
            valueOfMethods.put(Float.class, valueOfMethod(Float.class));
            valueOfMethods.put(Double.class, valueOfMethod(Double.class));
            valueOfMethods.put(Byte.class, valueOfMethod(Byte.class));
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(
                    "could not initialize ClassUtils: " + ex.getMessage());
        }
    }

    private static Method valueOfMethod(Class cls) throws NoSuchMethodException {
        return cls.getMethod("valueOf", String.class);
    }

    /**
     * erzeuge eine Instanz aus einem String.
     * Geht prinzipiell für alle Klassen mit einer static valueOf(String) methode,
     * und einigen weiteren z.b. Date, BigDecimal und BigInteger
     * @param clazz  - gewuenschter Zieltyp oder null (String = default)
     * @param s      - Formatstring, "null" wird zu null
     * @return null oder das Objekt
     */
    public static Object valueOf(Class clazz, String s) {
        if ("null".equals(s)) {
            return null;
        } else if(clazz == null || String.class.equals(clazz)) {
            return s;
        }
        try {
            Method valueOfMethod = valueOfMethods.get(clazz);
            if (valueOfMethod == null) {
                if (Time.class.equals(clazz)) {
                    return CalendarUtils.stringToTime(s);
                }
                if (BigDecimal.class.equals(clazz)) {
                    return new BigDecimal(s);
                }
                if (BigInteger.class.equals(clazz)) {
                    return new BigInteger(s);
                }
                return s;
            }
            return valueOfMethod.invoke(null, s);
        } catch (Exception ex) {
            throw new RuntimeException("cannot convert string " + s + " to type " +
                    clazz + ". Message: " + ex.getMessage());
        }
    }

}
