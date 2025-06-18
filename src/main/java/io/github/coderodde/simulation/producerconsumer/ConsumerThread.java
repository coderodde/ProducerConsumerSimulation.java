package io.github.coderodde.simulation.producerconsumer;

import io.github.coderodde.simulation.producerconsumer.AbstractSimulationThread;
import io.github.coderodde.simulation.producerconsumer.BoundedConcurrentQueue;
import io.github.coderodde.simulation.producerconsumer.SharedProducerThreadState;

/**
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ConsumerThread<E> extends AbstractSimulationThread<E> {
    
    public ConsumerThread(final E haltingElement,
                          final BoundedConcurrentQueue<E> queue,
                          final SharedProducerThreadState sharedState) {
        
        super(haltingElement,
              queue, 
              sharedState);
    }
    
    @Override
    public void run() {
        while (true) {
            if (sharedState.isHaltRequested() && queue.size() == 0) {
                System.out.println("hello");
                return;
            }
            
            E element = queue.pop(this);
            
            if (element.equals(haltingElement)) {
                queue.push(element, this);
                System.out.println("bye");
                return;
            }
        }
    }
    
    @Override
    public String toString() {
        return "Consumer thread";
    }
}
