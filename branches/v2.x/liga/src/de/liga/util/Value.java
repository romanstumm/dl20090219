package de.liga.util;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

/**
 * Description: This class is a helper for generic access to objects. It uses
 * Java-reflection, handles DynaBeans and java.util.Map. <p>
 * <pre>
 * Examples:
 * Customer customer = aCustomerObject;
 * String loginName = (String)Value.evaluate(customer, "User.Login");
 * <p/>
 * This is the same as:
 * String loginName = null;
 * User user = (User)customer.getUser();
 * if(user != null) { loginName = user.getLogin(); }
 * </pre>
 * You can access any public getter-Method e.g. persistence
 * attribute and navigate through toOne-Associations by separating each
 * attribute name with '.'. This class supports some formatters (called
 * 'reflectorHandler tokens') that offer an extensible customerization of
 * formatting/conversion etc. <p>
 * <pre>
 * Examples: (when dateBirth is 2000-02-20)
 * Integer year =
 * (Integer)Value.evaluate(customer, "DateBirth.$Year"); year is Integer(2000)
 * String monthString =
 * (String)Value.evaluate(customer, "DateBirth.$Month.$##"); monthString is "02"
 * </pre>< Register a
 * custom reflectorHandler, use: Value.registerHandler()<br>
 * User: roman.stumm <br/>
 * Date: 23.05.2007 <br/>
 * Copyright: Agimatec GmbH
 */
public class Value implements java.io.Serializable {
    protected static final Logger log = Logger.getLogger(Value.class);

    protected static final char HANDLER_TOKEN = '$';
    protected static final char LIST_ACCESS = '#';
    protected static final String SEPARATORS = ".";
    protected Map<String, ValueHandler> commandHandlers;

    /** the default instance widely used by Value.get() */
    protected static Value instance;

    static {
        instance = new Value(new Hashtable());
        instance.registerDefaultHandlers();
    }

    // register known command handlers for reflectorHandler tokens
    protected void registerDefaultHandlers() {
        /*registerHandler("DayOfMonth", DateAccessCommandHandler.createDayOfMonthHandler());
      registerHandler("Year", DateAccessCommandHandler.createYearHandler());
      registerHandler("Month", DateAccessCommandHandler.createMonthHandler());
      registerHandler("trim", FormatCommandHandler.trim());
      registerHandler("lower", FormatCommandHandler.createLowerString());
      registerHandler("upper", FormatCommandHandler.createUpperString()); */
    }

    /** @return the default instance */
    public static Value getDefault() {
        return instance;
    }

    /** @param aCommandHandlers - key = handlertoken value= a ValueHandler */
    protected Value(Map<String, ValueHandler> aCommandHandlers) {
        commandHandlers = aCommandHandlers;
    }

    /** create a new instance with default commandHandlers */
    public Value() {
        this(instance.commandHandlers); // use default command handlers
    }

    /**
     * Sets the AttributeReflective attribute of the Value object.
     * (handler tokens are not allowed)
     *
     * @param current The new AttributeReflective value
     * @param token   The new AttributeReflective value
     * @param value   The new AttributeReflective value
     */
    protected void setAttributeReflective(Object current, String token, Object value) {
        try {
            if (token.charAt(0) == LIST_ACCESS) {
                setListValue(current, token, value);
            } else {
                setReflectValue(current, token, value);
            }
        } catch (Exception ex) {
            final String msg =
                    "Error invoking method set" + token + '(' + value + ") on " + current;
            log.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }
    }

    private void setReflectValue(Object current, String token, Object value) throws
            NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method setter = findSetter(current.getClass(), token);

        Class pType = setter.getParameterTypes()[0];
        if (pType.isPrimitive() && value == null) {
            value = ClassUtils.primitiveDefault(pType);
        }
        setter.invoke(current, value);
        if (log.isDebugEnabled()) {
            log.debug("Invoked setter " + setter + " with " + value);
        }
    }

    private void setListValue(Object current, String token, Object value) {
        String indexString = token.substring(1);
        if (indexString.length() > 0) { // (List)set <index> or (Array)set <index>
            int index = Integer.parseInt(indexString);
            // we expect current to be either List or an array
            if (current instanceof List) {
                ((List) current).set(index, value);
                if (log.isDebugEnabled()) {
                    log.debug("Invoked List.set with index " + index + " and value " +
                            value);
                }
            } else {
                ((Object[]) current)[index] = value;
                if (log.isDebugEnabled()) {
                    log.debug("Invoked array[] = .. with index " + index + " and value " +
                            value);
                }
            }
        } else { // (Collection)add
            ((Collection) current).add(value);
            if (log.isDebugEnabled()) {
                log.debug("Invoked List.add with " + value);
            }
        }
    }

    /**
     * API - set a single attribute to the given value.
     *
     * @param token - a single token (property name etc.)
     */
    public void setAttribute(Object current, String token, Object value) {
        if (current instanceof DynaBean) {
            ((DynaBean) current).set(token, value);
        } else if (current instanceof Map) {
            ((Map) current).put(token, value);
        } else {
            setAttributeReflective(current, token, value);
        }
    }

    /**
     * API - compute the value of a single attribute
     *
     * @param token - a single token (property name etc.)
     * @return attribute value from object
     */
    public Object getAttribute(final Object current, final String token) {
        if (current instanceof DynaBean) {
            return ((DynaBean) current).get(token);
        } else if (current instanceof Map) {
            return ((Map) current).get(token);
        } else {
            return getAttributeReflective(current, token);
        }
    }

    /**
     * Return the value of the getter method named get <token>() Find and invoke
     * via Java-reflection
     *
     * @param current Description of Parameter
     * @param token   Description of Parameter
     * @return The AttributeReflective value
     */
    protected Object getAttributeReflective(final Object current, final String token) {
        try {
            return findGetter(current.getClass(), token).invoke(current);
        } catch (Exception ex) {
            final String msg = "Error invoking method get" +
                    StringUtils.capitalize(token) + " on " + current;
            log.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }
    }

    public static Type getGenericType(final Class clazz, final String token) {
        try {
            return clazz.getMethod("get" + StringUtils.capitalize(token))
                    .getGenericReturnType();
        } catch (NoSuchMethodException e) {
            final PropertyDescriptor propDesc =
                    getPropertyDescriptor(clazz, Introspector.decapitalize(token));
            return (propDesc == null) ? null : propDesc.getPropertyType();
        }
    }

    public static Class getPropertyType(final Class clazz, final String token) {
        try {
            return clazz.getMethod("get" + StringUtils.capitalize(token))
                    .getReturnType();
        } catch (NoSuchMethodException e) {
            final PropertyDescriptor propDesc =
                    getPropertyDescriptor(clazz, Introspector.decapitalize(token));
            return (propDesc == null) ? null : propDesc.getPropertyType();
        }
    }

    public static Class getBeanType(final Class clazz, final String token) {
        Type t = getGenericType(clazz, token);

        if (t instanceof GenericArrayType) {
            return getBeanClass(t);
        } else if (t instanceof ParameterizedType) {     // a list of what?
            ParameterizedType p = (ParameterizedType) t;
            Type rt = p.getRawType();
            if (rt instanceof Class && p.getActualTypeArguments().length > 0) {
                Class c = (Class) rt;
                if (Collection.class.isAssignableFrom(c))
                    return getBeanClass(p.getActualTypeArguments()[0]);
            }
            return getBeanClass(rt);
        } else {
            return getBeanClass(t);
        }
    }

    private static Class getBeanClass(Type t) {
        if (t instanceof Class) {
            Class c = (Class) t;
            if (c.isArray()) {
                return c.getComponentType();
            }
            return c;
        } else if (t instanceof ParameterizedType) {
            return getBeanClass(((ParameterizedType) t).getRawType());
        } else if (t instanceof GenericArrayType) {
            return getBeanClass(((GenericArrayType) t).getGenericComponentType());
        } else {
            return null;
        }
    }

    // instance methods

    /**
     * @return null when break, or
     *         Object[0] = current / last object for path,
     *         Object[1] = token / last token for attribute of last object
     */
    private Object[] prepSetValue(final Object obj, final String path,
                                  boolean createAbsentObjects) {
        final StringTokenizer tokenizer = new StringTokenizer(path, SEPARATORS);
        Object current = obj;
        String token = tokenizer.nextToken();
        while (tokenizer.hasMoreTokens()) {
            Object previous = current;
            current = evalToken(current, token);
            if (current == null) {
                if (createAbsentObjects) {
                    current = createAbsentObject(previous, token, current);
                } else {
                    return null;
                }
            }
            token = tokenizer.nextToken();
        }
        return new Object[]{current, token};
    }

    /**
     * Set a new value according to the path, that is a value expression.
     * (handler tokens are not allowed)
     *
     * @param obj                 root object
     * @param path                value path (full syntax supported)
     * @param value               the value to set
     * @param createAbsentObjects true= create new instances for null objects
     * @return true when the value has been set, false when not
     */
    public boolean setPath(final Object obj, final String path, Object value,
                           boolean createAbsentObjects) {
        Object[] results = prepSetValue(obj, path, createAbsentObjects);
        if (results == null) return false;
        setAttribute(results[0], (String) results[1], value);
        return true;
    }

    /**
     * API -
     * Return the result of a path evaluation for a given object
     * <p/>
     * Evaluate the path and return the value described. OneReferences and
     * Attributes allowed. If the object path ends return null.
     *
     * @param obj  SourceObject (may be null)
     * @param path string in path-format (att1.att2.$handlertoken)
     * @return the value after evaluation of the given path for the given
     *         object
     */
    public final Object eval(final Object obj, final String path) {
        if (obj == null) {
            return null;
            // for performance optimization
        }
        Object current = obj;
        final StringTokenizer tokenizer = new StringTokenizer(path, SEPARATORS);
        while (tokenizer.hasMoreTokens()) {
            current = evalToken(current, tokenizer.nextToken());
            if (current == null) return null;
        }
        return current;
    }

    /**
     * evalute the value of the current object for the given token.
     *
     * @param current - the object
     * @param token   - any syntactical single token allowed
     * @return the value or null
     */
    private Object evalToken(Object current, final String token) {
        switch(token.charAt(0)) {
            case HANDLER_TOKEN:
                current = getHandlerValue(current, token);
                break;
            case LIST_ACCESS:
                current = getListValue(current, token);
                break;
            default:
                current = getAttribute(current, token);
        }
        return current;
    }

    private Object getListValue(Object current, final String token) {
        String indexString = token.substring(1);
        if (indexString.length() > 0) {
            int idx = Integer.parseInt(indexString);
            if (current instanceof List) {
                if (idx >= ((List) current).size()) return null;
                current = ((List) current).get(idx);
            } else {
                // assume array
                Object[] castedCurrent = (Object[]) current;
                if (idx >= castedCurrent.length) return null;
                current = castedCurrent[idx];
            }
        } else { // get last element of List or Array
            if (current instanceof List) {
                int sz = ((List) current).size();
                current = (sz == 0) ? null : ((List) current).get(sz - 1);
            } else {
                Object[] castedCurrent = (Object[]) current;
                current = castedCurrent.length == 0 ? null :
                        castedCurrent[castedCurrent.length - 1];
            }
        }
        return current;
    }

    /**
     * Gets the HandlerToken-value attribute of the Value object
     *
     * @param value Description of Parameter
     * @param token Description of Parameter
     * @return The HandlerTokenValue value
     */
    protected Object getHandlerValue(final Object value, final String token) {
        final String command;
        final String param;
        if (token.charAt(token.length() - 1) == ')') { // token has parameters
            int idx = token.lastIndexOf('(');
            if (idx < 2)
                throw new IllegalArgumentException("invalid command token: " + token);
            command = token.substring(1, idx);
            param = token.substring(idx + 1, token.length() - 1);
        } else { // token without parameters
            command = token.substring(1);
            param = null;
        }
        ValueHandler handler = commandHandlers.get(command);
        if (handler == null) {
            throw new RuntimeException("Unknown handler token: " + command);
        }
        return handler.getValue(value, param);
    }

    /**
     * Description of the Method
     *
     * @param theClass class of the bean
     * @param token    name of the attribute
     * @return Description of the Returned Value
     * @throws NoSuchMethodException Description of Exception
     */
    protected Method findGetter(Class theClass, String token)
            throws NoSuchMethodException {
        final PropertyDescriptor propDesc =
                getPropertyDescriptor(theClass, Introspector.decapitalize(token));
        if (propDesc != null && propDesc.getReadMethod() != null) {
            if (!propDesc.getReadMethod().isAccessible()) {
                propDesc.getReadMethod().setAccessible(true);
            }
            return propDesc.getReadMethod();
        } else {
            return null;
        }
    }

    /**
     * @param theClass - javabean class
     * @param name     - javabean property name (already decapitalized)
     * @return javabean propertydescriptor or null
     */
    public static PropertyDescriptor getPropertyDescriptor(final Class theClass,
                                                           final String name) {
        final PropertyDescriptor[] descriptors =
                PropertyUtils.getPropertyDescriptors(theClass);
        for (PropertyDescriptor descriptor : descriptors) {
            if (name.equals(descriptor.getName())) {
                return (descriptor);
            }
        }
        return null;
    }


    /**
     * Find the setter. Caution: This requires a getter method with the same
     * return type as the sole parameter type of the setter to be returned.
     *
     * @param token    bean attribute name
     * @param theClass the bean class
     * @return the method to set the value or null
     * @throws NoSuchMethodException Description of Exception
     */
    protected Method findSetter(Class theClass, String token)
            throws NoSuchMethodException {
        final PropertyDescriptor propDesc =
                getPropertyDescriptor(theClass, Introspector.decapitalize(token));
        return propDesc.getWriteMethod();
    }

    /**
     * API -
     * Evaluate the path and return the value described. OneReferences and
     * Attributs allowed. If the object path ends return 'defaultResult'. Note:
     * This method never returns null, but the given 'defaultResult' instead.
     * handler Tokens (as last one) begin with $
     *
     * @param obj           Description of Parameter
     * @param path          Description of Parameter
     * @param defaultResult Description of Parameter
     * @return Description of the Returned Value
     */
    public Object getPath(Object obj, String path, Object defaultResult) {
        Object val = eval(obj, path);
        return (val == null || "".equals(val)) ? defaultResult : val;
    }

    /**
     * Register a command handler for the given command
     *
     * @param command Description of Parameter
     * @param handler Description of Parameter
     */
    public void registerHandler(String command, ValueHandler handler) {
        useOwnHandlers();
        commandHandlers.put(command, handler);
    }

    private void useOwnHandlers() {
        if (this != instance && commandHandlers == instance
                .commandHandlers) { // decouple from default instance
            commandHandlers = new Hashtable(commandHandlers);
        }
    }

    /**
     * remove the handler for the given handler token
     *
     * @return the formerly registered command handler or null
     */
    public ValueHandler removeHandler(String command) {
        useOwnHandlers();
        return commandHandlers.remove(command);
    }

    /**
     * Description of the Method
     *
     * @param previous Description of Parameter
     * @param token    Description of Parameter
     * @param current  Description of Parameter
     * @return Description of the Returned Value
     */
    private Object createAbsentObject(Object previous, String token, Object current) {
        try {
            if (previous instanceof DynaBean) {
                DynaBean db = (DynaBean) previous;
                return db.getDynaClass().getDynaProperty(token).getType().newInstance();
            } else {
                Class prevClass = previous.getClass();

                Method setter = findSetter(prevClass, token);
                current = setter.getParameterTypes()[0].newInstance();

                setter.invoke(previous, current);

                if (log.isDebugEnabled()) {
                    log.debug("New instance: " + setter + " with " + current);
                }


                return current;
            }
        } catch (Exception ex) {
            final String msg = "Error invoking constructor or setter for " + token +
                    " on " + previous;
            log.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }
    }

    /**
     * write commandHandlers only if they are not default
     * to save space and to keep them identical after deserialization
     *
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        if (commandHandlers == instance.commandHandlers) {
            out.writeObject(null);
        } else {
            out.writeObject(commandHandlers);
        }
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        Object storedHandlers = in.readObject();
        if (storedHandlers instanceof Map) {
            commandHandlers = (Map) storedHandlers;
        } else { // restore defaults
            commandHandlers = instance.commandHandlers;
        }
    }
}
