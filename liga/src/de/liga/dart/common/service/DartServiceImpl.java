package de.liga.dart.common.service;

import de.liga.dart.common.service.util.HibernateUtil;
import de.liga.dart.model.LigaPersistence;
import de.liga.util.thread.ThreadManager;

public class DartServiceImpl extends AbstractService implements DartService {
    public void shutdown() {
        ThreadManager.getInstance().shutdown();
        if (!HibernateUtil.getSessionFactory().isClosed()) {
            HibernateUtil.getSessionFactory().close();
        }
    }

    public LigaPersistence rejoin(LigaPersistence obj) {
        return (LigaPersistence) getSession()
                .get(obj.getModelClass(), obj.getKeyValue());
    }
}
