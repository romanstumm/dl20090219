package org.en.tealEye.printing.controller;

import java.awt.*;
import java.util.Map;
import java.util.Collection;
import java.util.HashMap;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 27.09.2009
 * Time: 19:39:16
 * To change this template use File | Settings | File Templates.
 */
public class CacheStack {

    public  Map<String, Object> componentClassObjects = new HashMap<String, Object>();
    public  Map<String, Object> methodClassObjects = new HashMap<String, Object>();
    public  Map<String, Component> componentMap = new HashMap<String, Component>();
    public  Map<String, Method> methodMap = new HashMap<String, Method>();
    public  Map<Method, Object> methodMapReverse = new HashMap<Method, Object>();
    public  Map<Object, Method> globalMethodMap = new HashMap<Object, Method>();
    public  Map<Object, Method> customListenerMethodMap = new HashMap<Object, Method>();
    public  Map<Object, Method> customMethodMap = new HashMap<Object, Method>();

    public CacheStack(){

    }


}
