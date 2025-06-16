package io.github.coderodde.simulation.producerconsumer;

import java.util.Objects;
import java.util.Random;

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
            final E element = queue.pop();

            if (Objects.equals(element, haltingElement)) {
                return;
            }
        }
    }
}
