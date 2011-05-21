package de.liga.util.thread;


/**
 * This class provides a <code>Thread</code> with extensions for usage in a
 * managed environment.
 *
 * @author Roman  Stumm
 */
public class ManagedThread extends Thread {
    public ManagedThread(Runnable target, String name) {
        super(target, name);
        manage();
    }

    /**
     * Create instance and automatically add to management.
     */
    public ManagedThread() {
        super();
        manage();
    }

    /**
     * Create instance and automatically add to management.
     *
     * @param name String with object name
     */
    public ManagedThread(String name) {
        super(name);
        manage();
    }

    /**
     * Create instance and automatically add to management.
     *
     * @param run
     */
    public ManagedThread(Runnable run) {
        super(run);
        manage();
    }

    public ManagedThread(ThreadGroup group, Runnable target) {
        super(group, target);
        manage();
    }

    public ManagedThread(ThreadGroup group, String name) {
        super(group, name);
        manage();
    }

    public ManagedThread(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
        manage();
    }

    public ManagedThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, target, name, stackSize);
        manage();
    }

    protected final void manage() {
        ThreadManager.getInstance().addThread(this);
    }

    /**
     * call after run is complete when you subclass this class and overwrite the run-method
     */
    protected final void finished() {
        // After run is completed remove thread from list
        ThreadManager.getInstance().removeThread(this);
    }

    public void run() {
        try {
            super.run();
        } finally {
            finished();
        }
    }
}
