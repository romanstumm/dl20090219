package org.en.tealEye.framework;

import de.liga.dart.common.service.ServiceFactory;

import javax.swing.*;

/**
 * Description:   <br/>
 * User: roman
 * Date: 09.11.2007, 20:32:29
 */
public abstract class TransactedModelFactory<T> extends ModelFactory<T> {
    T result;

    public T create(final JPanel panel) {
        ServiceFactory.runAsTransaction(new Runnable() {
            public void run() {
                result = createTransacted(panel);
            }
        });
        return result;
    }

    public abstract T createTransacted(JPanel panel);
}
