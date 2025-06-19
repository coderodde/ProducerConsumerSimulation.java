package io.github.coderodde.simulation.producerconsumer;

import java.util.Objects;

/**
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ConsumerThread<E, R> extends AbstractSimulationThread<E, R> {
    
    private final ConsumerAction<E, R> action;
    
    public ConsumerThread(final E haltingElement,
                          final BoundedConcurrentQueue<E, R> queue,
                          final SharedProducerThreadState sharedState,
                          final ConsumerAction<E, R> action) {
        
        super(haltingElement,
              queue, 
              sharedState);
        
        this.action = Objects.requireNonNull(action, "Input action is null");
    }
    
    @Override
    public void run() {
        while (true) {
            
            E element = queue.top();
            
            if (element.equals(haltingElement)) {
                System.out.println("return " + this.getThreadId());
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
