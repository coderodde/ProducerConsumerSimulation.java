package io.github.coderodde.simulation.producerconsumer;

import java.util.Objects;
import java.util.Random;

/**
 * This class implements producer threads.
 * 
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ProducerThread<E> extends AbstractSimulationThread<E> {
    
    private final ElementProducer<E> elementProducer;
    
    public ProducerThread(final ElementProducer<E> elementProducer,
                          final BoundedConcurrentQueue<E> queue) {
        
        super(elementProducer.getHaltingElement(), queue);
        this.elementProducer = elementProducer;
    }
    
    @Override
    public void run() {
        while (true) {
            final E element = elementProducer.produce();

            if (Objects.equals(element, 
                               elementProducer.getHaltingElement())) {
                return;
            }

            queue.push(element);
        }
    }
}
