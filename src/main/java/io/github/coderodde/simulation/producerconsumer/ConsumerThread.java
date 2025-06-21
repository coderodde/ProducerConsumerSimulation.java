package io.github.coderodde.simulation.producerconsumer;

/**
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ConsumerThread<E, R> extends AbstractSimulationThread<E, R> {
    
    public ConsumerThread(final E haltingElement,
                          final BoundedConcurrentQueue<E, R> queue,
                          final SharedProducerThreadState sharedState,
                          final ConsumerAction<E, R> action) {
        
        super(haltingElement,
              queue, 
              sharedState,
              action);
    }
    
    @Override
    public void run() {
        while (true) {
            
            E element = queue.top();
            
            if (element.equals(haltingElement)) {
                System.out.printf("[STATUS] Consumer %d exited.\n", threadId);
                return;
            }
            
            queue.pop(this);
        }
        
    }
    
    @Override
    public String toString() {
        return "Consumer thread";
    }
}
