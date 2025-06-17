package io.github.coderodde.simulation.producerconsumer;

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
        
        this.pushFormat = "Producer %" 
                        + producerCount 
                        + "d produced %" 
                        + Integer.toString(haltingElement).length() 
                        + "d: %s\n";
        
        this.popFormat = "Consumer %" 
                        + consumerCount 
                        + "d consumed %" 
                        + Integer.toString(haltingElement).length() 
                        + "d: %s\n";
    }
    
    @Override
    public void onPush(final ProducerThread<Integer> thread,
                       final Integer element) {
        
        System.out.printf(pushFormat,   
                          thread == null ? -1 : thread.getThreadId(), 
                          element, 
                          queue);
    }
    
    @Override
    public void onPop(final ConsumerThread<Integer> thread,
                      final Integer element) {
        
        System.out.printf(popFormat,  
                          thread == null ? -1 : thread.getThreadId(),
                          element, 
                          queue);
    }
}
