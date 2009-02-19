package de.liga.util.thread;

/**
 * Listener interface for thread shutdown events, triggered when the
 * {@link ThreadManager} is shut down.<br>
 * 
 * @author Roman Stumm
 */
public interface ThreadShutdownListener {

    /**
     * Called when a thread shutdown event has happened.
     */
    void shutdownEvent();

}
