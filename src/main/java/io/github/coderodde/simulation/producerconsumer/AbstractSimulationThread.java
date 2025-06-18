package io.github.coderodde.simulation.producerconsumer;

import java.util.Objects;

/**
 * This class implements an abstract base class for producer/consumer threads.
 * 
 * @param <E> the queue element type.
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractSimulationThread<E> extends Thread {
    
    /**
     * The element that is there for signalling that a consumer thread must 
     * halt.
     */
    protected final E haltingElement;
    
    /**
     * The queue this thread works on.
     */
    protected final BoundedConcurrentQueue<E> queue;
    
    /**
     * The ID of this abstract thread.
     */
    protected final int threadId;
    
    /**
     * Counts the total number of threads. Used for {@code threadId}.
     */
    private static int threadIdCounter = 0;
    
    protected final SharedProducerThreadState sharedState;
    
    public AbstractSimulationThread(
            final E haltingElement,
            final BoundedConcurrentQueue<E> queue,
            final SharedProducerThreadState sharedState) {
        
        this.threadId = threadIdCounter++;
        
        this.haltingElement =
                Objects.requireNonNull(
                        haltingElement,
                        "The input haltingElement is null");
        
        this.queue = Objects.requireNonNull(queue, "The input queue is null");
        this.sharedState = 
                Objects.requireNonNull(
                        sharedState, 
                        "The input sharedState is null");
    }
    
    public int getThreadId() {
        return threadId;
    }
}
