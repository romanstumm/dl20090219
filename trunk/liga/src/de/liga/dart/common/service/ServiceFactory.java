package de.liga.dart.common.service;

//import de.liga.dart.common.service.util.DummyServiceHandler;

import de.liga.dart.common.service.util.SyncServiceHandler;
import de.liga.dart.common.service.util.TransactServiceHandler;
import de.liga.dart.exception.DartTechnicalException;

import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Stellt Zugriff auf alle Service implementierungen bereit.
 * <br/>
 * Jeder Aufruf eines Service wird - falls vorher keine
 * Transaction gestartet wurde - als EIGENE Transaction
 * ausgeführt.
 */
public class ServiceFactory {

    private static final ServiceFactory instance = new ServiceFactory();
    private static Map<Class, Object> serviceCache =
            Collections.synchronizedMap(new HashMap());

    private ServiceFactory() {
    }

    // ======== BEGIN HACK =====
    // register the DartService as the only one without a Transaction-Proxy-Decorator
    // Reason: shutdown, commit etc. do not work within a transaction!
    static {
        instance.putService(DartService.class,
                (instance.decorateSync(new DartServiceImpl(),
                        DartService.class)));

    }
    // ======== END HACK =====

    /**
     * Gib eine Serviceimplementierung zurück.
     * Beispiel fuer Benutzung:
     * <p/>
     * LigaService ligaService = ServiceFactory.get(LigaService.class);
     *
     * @param serviceType
     * @return
     */
    public static <T extends Service> T get(Class<T> serviceType) {
        return instance.getService(serviceType);
    }

    /**
     * Fuehre das Runnable as eine Transaktion aus!
     *
     * @param runnable
     */
    public static void runAsTransaction(Runnable runnable) {
        decorateAsTransaction(runnable).run();
    }

    public static Runnable decorateAsTransaction(Runnable runable) {
        return instance.decorateTransact(runable, Runnable.class);
    }

    public static <T> T decorateAsTransaction(T target, Class<T> tType) {
        return instance.decorateTransact(target, tType);
    }

    /**
     * not public! *
     */
    static ServiceFactory getInstance() {
        return instance;
    }

    public <T> void putService(Class<T> serviceType, T service) {
        serviceCache.put(serviceType, service);
    }

    private <T extends Service> T getService(Class<T> serviceType) {
        T service = (T) serviceCache.get(serviceType);
        if (service != null) return service;

        try {
//            try {
                Class<T> implClass = (Class<T>) Class
                        .forName(serviceType.getName() + "Impl");
                service = implClass.newInstance();
            /*} catch (ClassNotFoundException e) {
                service = createDummy(serviceType);
            }*/
            service = decorateTransact(service, serviceType);
//            service = decorateSync(service, serviceType);
            serviceCache.put(serviceType, service);
            return service;
        } catch (Exception e) {
            throw new DartTechnicalException(e);
        }
    }
/*
    private <T extends Service> T createDummy(Class<T> serviceType)
            throws IllegalAccessException, InstantiationException {
        try {
            return ((Class<T>) Class
                    .forName(serviceType.getName() + "Dummy")).newInstance();
        } catch (ClassNotFoundException e) {
            return (T) Proxy.newProxyInstance(getClass().getClassLoader(),
                    new Class[]{serviceType},
                    new DummyServiceHandler(serviceType));
        }
    }*/

    private <T> T decorateSync(T service, Class<T> serviceType) {
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{serviceType},
                new SyncServiceHandler(this, service));
    }

    private <T> T decorateTransact(T service, Class<T> serviceType) {
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{serviceType},
                new TransactServiceHandler(service));
    }

}
