package io.github.coderodde.simulation.producerconsumer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @version 1.0.0
 * @since 1.0.0
 */
public final class SharedProducerThreadState {
    
    private final AtomicBoolean haltRequestedFlag = new AtomicBoolean(false);
    
    public boolean isHaltRequested() {
        return haltRequestedFlag.getAcquire();
    }
    
    public void requestHalt() {
        haltRequestedFlag.setRelease(true);
    }
}
