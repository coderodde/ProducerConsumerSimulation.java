package io.github.coderodde.simulation.producerconsumer;

import java.util.Objects;

/**
 *
 * @author rodio
 */
public final class IntegerQueueNotifier extends AbstractQueueNotifier<Integer> {
    
    private final String pushFormat;
    private final String popFormat;
    
    public IntegerQueueNotifier(final BoundedConcurrentQueue<Integer> queue,
                                final int consumerCount,
                                final int producerCount,
                                final int haltingElement) {
        super(queue);
        
        this.pushFormat = "%s %" 
                        + Integer.toString(producerCount).length()
                        + "d produced %" 
                        + Integer.toString(haltingElement).length() 
                        + "d: %s\n";
        
        this.popFormat = "%s %" 
                        + Integer.toString(consumerCount).length()
                        + "d consumed %" 
                        + Integer.toString(haltingElement).length() 
                        + "d: %s\n";
    }
    
    @Override
    public void onPush(final AbstractSimulationThread<Integer> thread,
                       final Integer element) {
        
        System.out.printf(pushFormat,   
                          thread.toString(),
                          thread.getThreadId(), 
                          element, 
                          queue);
    }
    
    @Override
    public void onPop(final ConsumerThread<Integer> thread,
                      final Integer element) {
        
        System.out.printf(popFormat, 
                          thread.toString(),
                          thread.getThreadId(),
                          element,
                          queue);
    }
}
