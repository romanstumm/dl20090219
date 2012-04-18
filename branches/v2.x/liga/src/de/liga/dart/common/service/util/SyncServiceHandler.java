package de.liga.dart.common.service.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class SyncServiceHandler implements InvocationHandler {
    final Object semaphore;
    final Object objectToSync;

    public SyncServiceHandler(Object semaphore, Object objectToSync) {
        this.semaphore = semaphore;
        this.objectToSync = objectToSync;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        synchronized(semaphore)
        {
            return method.invoke(objectToSync, args);
        }
    }
}
