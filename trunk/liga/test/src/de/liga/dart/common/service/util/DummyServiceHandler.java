package de.liga.dart.common.service.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class DummyServiceHandler<T> implements InvocationHandler {
    private static final Log log = LogFactory.getLog(DummyServiceHandler.class);
    private final Class<T> serviceType;

    public DummyServiceHandler(Class<T> serviceType) {
        this.serviceType = serviceType;
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        log.debug("Dummy of " + serviceType.getSimpleName() + " >> " +
                method.getName());
        String mname = method.getName();
        if (mname.startsWith("find")) {
            if (List.class.isAssignableFrom(method.getReturnType())) {
                List list = new ArrayList(20);
                Class entityType = (Class) ((ParameterizedType) method
                        .getGenericReturnType()).getActualTypeArguments()[0];
                for (int i = 0; i < 20; i++) {
                    list.add(entityType.newInstance());
                }
                return list;
            } else {
                return method.getReturnType().newInstance();
            }
        } else {
            return null; // nothing happens
        }
    }
}
