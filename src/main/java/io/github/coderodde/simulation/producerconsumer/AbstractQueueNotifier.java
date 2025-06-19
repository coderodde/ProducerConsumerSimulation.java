package io.github.coderodde.simulation.producerconsumer;

import java.util.Objects;

/**
 *
 * @author rodio
 */
public abstract class AbstractQueueNotifier<E, R> {
    
    protected final BoundedConcurrentQueue<E, R> queue;
    protected final ConsumerAction<E, R> action;
    
    public AbstractQueueNotifier(final BoundedConcurrentQueue<E, R> queue,
                                 final ConsumerAction<E, R> action) {
        
        this.queue  = Objects.requireNonNull(queue,  "Input queue is null");
        this.action = Objects.requireNonNull(action, "Input action is null");
    }
    
    public abstract void onPush(final AbstractSimulationThread<E, R> thread, 
                                final E element);
    
    public abstract void onPop(final ConsumerThread<E, R> thread, 
                                final E element);
}
