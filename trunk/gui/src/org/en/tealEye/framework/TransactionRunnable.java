package org.en.tealEye.framework;

public interface TransactionRunnable<T> {
    T executeTransaction() throws Exception;
}
