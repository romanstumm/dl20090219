package org.en.tealEye.framework;

import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.exception.DartValidationException;
import de.liga.util.thread.ThreadManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;

public abstract class TransactionWorker<T, V> extends SwingWorker<T, V>
        implements TransactionRunnable {
    private static final Log log = LogFactory.getLog(TransactionWorker.class);

    protected T doInBackground() throws Exception {

        try {
            ThreadManager.getInstance().addThread(Thread.currentThread());
            TransactionRunnable<T> transaction = ServiceFactory
                    .decorateAsTransaction(this, TransactionRunnable.class);
            return transaction.executeTransaction();
        } catch (DartValidationException ex) {
            log.error(ex.getMessages(), ex);
            return null;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            displayError(technicalErrorMessage(ex));
            throw ex;
        } finally {
          ThreadManager.getInstance().removeThread(Thread.currentThread());
        }
    }

    protected void displayError(String msg) {
        // do nothing
    }

    protected String technicalErrorMessage(Exception ex) {
        return "Technischer Fehler: " + ex.getMessage() +
                " \n\nBitte wenden Sie sich an den Hersteller, um Hilfe zu erhalten.";
    }

    public abstract T executeTransaction() throws Exception;
}