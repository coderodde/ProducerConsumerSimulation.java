package io.github.coderodde.simulation.producerconsumer.impl;

import io.github.coderodde.simulation.producerconsumer.AbstractQueueNotifier;
import io.github.coderodde.simulation.producerconsumer.AbstractSimulationThread;
import io.github.coderodde.simulation.producerconsumer.BoundedConcurrentQueue;
import io.github.coderodde.simulation.producerconsumer.ConsumerThread;
import java.math.BigInteger;

/**
 *
 * @author rodio
 */
public final class LongQueueNotifier 
        extends AbstractQueueNotifier<Long, BigInteger> {
    
    private final String pushFormat;
    private final String popFormat;
    
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
