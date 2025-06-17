package io.github.coderodde.simulation.producerconsumer;

import java.util.Objects;

/**
 *
 * @author rodio
 */
public abstract class AbstractQueueNotifier<E> {
    
    protected final BoundedConcurrentQueue<E> queue;
    
    public AbstractQueueNotifier(final BoundedConcurrentQueue<E> queue) {
        this.queue = Objects.requireNonNull(queue, "The input queue is null");
    }
    
    public abstract void onPush(final ProducerThread<E> thread, 
                                final E element);
    
    public abstract void onPop(final ConsumerThread<E> thread, 
                                final E element);
}
