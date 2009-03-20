package de.liga.util.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Description:<br>
 * This service holds a list of generated threads which could all be killed by a
 * single method. Users have to put their threads into this service by an add
 * method.<br>
 *
 * @author Roman Stumm
 */
public class ThreadManager {
    private static final Log log = LogFactory.getLog(ThreadManager.class);

    private static final int SLEEP_MILLIS = 20000;

    // weak list: so monitoredThreads can get garbage collected
    private final List<Thread> threads =
            Collections.synchronizedList(new WeakList<Thread>());

    private final Collection<ThreadShutdownListener> threadShutdownListener =
            Collections.synchronizedCollection(
                    new HashSet<ThreadShutdownListener>());

    private static final ThreadManager INSTANCE = new ThreadManager();

    private ThreadManager() {
        log.debug("Constructing new ThreadManager...");
    }

    public static ThreadManager getInstance() {
        return INSTANCE;
    }

    public List<Thread> getThreads() {
        return Collections.unmodifiableList(threads);
    }

    /**
     * add a thread to management.
     *
     * @see ManagedThread
     *      {@inheritDoc}
     */
    public void addThread(Thread t) {
        if(t == null) return;
        if (!this.threads.contains(t)) {
            log.info("Adding thread " + t.getName() + " to list");
            this.threads.add(t);
        } else {
            log.warn("Thread " + t.getName() + " already in list");
        }
    }

    public void addThreadShutdownListener(ThreadShutdownListener listener) {
        log.info("Adding ThreadShutdownListener " + listener + " to list");
        this.threadShutdownListener.add(listener);
    }

    public void removeThreadShutdownListener(ThreadShutdownListener listener) {
        log.info("Removing ThreadShutdownListener " + listener + " from list");
        this.threadShutdownListener.remove(listener);
    }

    public void shutdown() {
        killAllThreads();
    }

    public void removeThread(Thread t) {
        if (this.threads.contains(t)) {
            log.info("Removing thread " + t.getName() + " from list");
            this.threads.remove(t);
        } else {
            log.debug("Thread " + t.getName() + " not in list");
        }
    }

    private void killAllThreads() {

        // Use Copy of thread list because each MonitoredThread removes itself from
        // this list after interrupt and this would lead to ConcurrentMod
        // Exceptions
        final List<Thread> killList = new ArrayList<Thread>(this.threads);
        log.info("Preparing to kill " + killList.size() + " thread(s)...");

        for (Thread t : killList) {
            if(t == null) continue;
            log.info("Interrupting Thread " + t.getName());

            try {
                // Wait until work of thread is completly done
                t.interrupt();
                while (t.isAlive()) {
                    log.info("Thread " + t.getName() +
                            " is alive. Waiting max. millis: " + SLEEP_MILLIS);
                    t.join(SLEEP_MILLIS);
                }
                if (t.isAlive()) {
                    log.warn("Thread " + t.getName() +
                            " is still alive. I continue nevertheless.");
                } else {
                    log.info("Finished with thread: " + t.getName());
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        log.info("Remaining threads in list (should be 0): " + threads.size());
        this.threads.clear();
        if (log.isDebugEnabled()) {
            printAllActiveThreads();
        }
        notifyAllThreadShutdownListener();
        threadShutdownListener.clear();
    }

    private void printAllActiveThreads() {
        log.debug("List of all active threads here:");
        ThreadGroup g = Thread.currentThread().getThreadGroup();

        try {
            while (g.getParent() != null) {
                log.debug("Threadgroup: " + g.getName());
                g = g.getParent();
            }
            log.debug("Threadgroup: " + g.getName());
            int activeThreadCount = g.activeCount();
            log.debug("Active threads: " + activeThreadCount);
            Thread[] threads = new Thread[activeThreadCount];
            g.enumerate(threads, true);

            for (int i = 0; i < activeThreadCount; i++) {
                log.debug("Thread[" + i + "]=" +
                        (threads[i] != null ? threads[i].getName() : null));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void notifyAllThreadShutdownListener() {
        log.info("Notifying ThreadShutdownListeners (" +
                threadShutdownListener.size() + ")...");
        for (ThreadShutdownListener listener : threadShutdownListener) {
            log.info("Notifying ThreadShutdownListener: " + listener);
            listener.shutdownEvent();
        }
    }
}
