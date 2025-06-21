package io.github.coderodde.simulation.producerconsumer;

import java.util.Objects;

/**
 * This class implements an abstract base class for producer/consumer threads.
 * 
 * @param <E> the queue element type.
 * @param <R> the queue result element type.
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractSimulationThread<E, R> extends Thread {
    
    /**
     * The element that is there for signalling that a consumer thread must 
     * halt.
     */
    protected final E haltingElement;
    
    /**
     * The queue this thread works on.
     */
    protected final BoundedConcurrentQueue<E, R> queue;
    
    /**
     * The ID of this abstract thread.
     */
    protected final int threadId;
    
    /**
     * Counts the total number of threads. Used for {@code threadId}.
     */
    private static int threadIdCounter = 0;
    
    /**
     * The queue action.
     */
    protected final ConsumerAction<E, R> action;
    
    /**
     * The shared producer thread state used for requesting the exit.
     */
    protected final SharedProducerThreadState sharedState;
    
    /**
     * Constructs this thread.
     * 
     * @param haltingElement the halting element.   
     * @param queue          the target queue.
     * @param sharedState    the shared state.
     * @param action         the action.
     */
    public AbstractSimulationThread(
            final E haltingElement,
            final BoundedConcurrentQueue<E, R> queue,
            final SharedProducerThreadState sharedState,
            final ConsumerAction<E, R> action) {
        
        this.threadId = threadIdCounter++;
        
        this.haltingElement =
                Objects.requireNonNull(
                        haltingElement,
                        "The input haltingElement is null");
        
        this.queue = Objects.requireNonNull(queue, "The input queue is null");
        this.sharedState = sharedState;
        this.action = Objects.requireNonNull(action, "action is null");
        
    }
    
    /**
     * Returns the ID of this thread.
     * 
     * @return the ID of this thread.
     */
    public int getThreadId() {
        return threadId;
    }
}
