package io.github.coderodde.simulation.producerconsumer;

import java.util.Objects;

/**
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ConsumerThread<E> extends AbstractSimulationThread<E> {
    
    public ConsumerThread(final E haltingElement,
                          final BoundedConcurrentQueue<E> queue) {
        
        super(haltingElement, queue);
    }
    
    @Override
    public void run() {
        while (true) {
            E element = queue.pop(this);
            
            if (Objects.equals(element, haltingElement)) {
                queue.push(haltingElement, null);
                return;
            }
        }
    }
}
