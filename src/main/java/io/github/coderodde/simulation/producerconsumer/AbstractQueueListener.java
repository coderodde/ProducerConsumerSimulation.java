package io.github.coderodde.simulation.producerconsumer;

import java.util.Objects;

/**
 * This abstract class defines the API for queue listeners.
 * 
 * @param <E> the queue element type.
 * @param <R> the result element type.
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractQueueListener<E, R> {
    
    /**
     * The target queue.
     */
    protected final BoundedConcurrentQueue<E, R> queue;
    
    /**
     * The action to perform on each queue element popped in order to produce a 
     * result.
     */
    protected final ConsumerAction<E, R> action;
    
    /**
     * Constructs this abstract queue listener.
     * 
     * @param queue  the target queue to listen for.
     * @param action the action to perform on the queue elements.
     */
    public AbstractQueueListener(final BoundedConcurrentQueue<E, R> queue,
                                 final ConsumerAction<E, R> action) {
        
        this.queue  = Objects.requireNonNull(queue,  "Input queue is null");
        this.action = Objects.requireNonNull(action, "Input action is null");
    }
    
    /**
     * This callback is called on each push operation to the queue.
     * 
     * @param thread the target thread pushing to the queue.
     * @param element the element to push to queue.
     */
    public abstract void onPush(final AbstractSimulationThread<E, R> thread, 
                                final E element);
    
    /**
     * This callback is called on each pop operation from the queue.
     * 
     * @param thread the target thread popping from the queue.
     * @param element the element popped from the queue.
     */
    public abstract void onPop(final ConsumerThread<E, R> thread, 
                               final E element);
}
