package io.github.coderodde.simulation.producerconsumer.impl;

import io.github.coderodde.simulation.producerconsumer.AbstractQueueNotifier;
import io.github.coderodde.simulation.producerconsumer.AbstractSimulationThread;
import io.github.coderodde.simulation.producerconsumer.BoundedConcurrentQueue;
import io.github.coderodde.simulation.producerconsumer.ConsumerThread;
import java.math.BigInteger;

/**
 * This class implements a queue notifier.
 * 
 * @version 1.0.0
 * @since 1.0.0
 */
public final class LongQueueNotifier 
        extends AbstractQueueNotifier<Long, BigInteger> {
    
    /**
     * Push message format.
     */
    private final String pushFormat;
    
    /**
     * Pop message format.
     */
    private final String popFormat;
    
    /**
     * Constructs this queue notifier.
     * 
     * @param queue          the target queue.
     * @param action         the consumer action.
     * @param consumerCount  the number of consumers.
     * @param producerCount  the number of producers.
     * @param haltingElement the halting element.
     */
    public LongQueueNotifier(
            final BoundedConcurrentQueue<Long, BigInteger> queue,
            final FibonacciConsumerAction action,
            final int consumerCount,
            final int producerCount,
            final int haltingElement) {
        
        super(queue, action);
        
        this.pushFormat = "%s %" 
                        + Integer.toString(producerCount).length()
                        + "d produced %" 
                        + Integer.toString(haltingElement).length() 
                        + "d: %s\n";
        
        this.popFormat = "%s %" 
                        + Integer.toString(consumerCount).length()
                        + "d consumed %" 
                        + Integer.toString(haltingElement).length() 
                        + "d: %s: Result = %s\n";
    }
    
    /**
     * {@inheritDoc }
     */
    
    @Override
    public void onPush(
            final AbstractSimulationThread<Long, BigInteger> thread,
            final Long element) {
        
        System.out.printf(pushFormat,   
                          thread.toString(),
                          thread.getThreadId(), 
                          element, 
                          queue);
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public void onPop(final ConsumerThread<Long, BigInteger> thread,
                      final Long element) {
        
        System.out.printf(popFormat, 
                          thread.toString(),
                          thread.getThreadId(),
                          element,
                          queue,
                          action == null ? "null" : action.apply(element));
    }
}
