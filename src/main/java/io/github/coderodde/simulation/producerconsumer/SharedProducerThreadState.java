package io.github.coderodde.simulation.producerconsumer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class implements a shared state consisting of a flag indicating whether 
 * the producer threads must exit.
 * 
 * @version 1.0.0
 * @since 1.0.0
 */
public final class SharedProducerThreadState {
    
    /**
     * Indicates whether the halt is requested. Is requested if set to 
     * {@code true}.
     */
    private final AtomicBoolean haltRequestedFlag = new AtomicBoolean(false);
    
    /**
     * Return a boolean indicating whether the producer threads sharing this 
     * state must halt or not.
     * 
     * @return {@code true} if the producer threads must halt. {@code false} 
     *         otherwise.
     */
    public boolean isHaltRequested() {
        return haltRequestedFlag.get();
    }
    
    /**
     * Request the halt status.
     */
    public void requestHalt() {
        haltRequestedFlag.set(true);
    }
}
