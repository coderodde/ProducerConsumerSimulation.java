package io.github.coderodde.simulation.producerconsumer;

import java.util.Objects;

/**
 * This class implements producer threads.
 * 
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ProducerThread<E> extends AbstractSimulationThread<E> {
    
    private final ElementProvider<E> elementProvider;
    
    public ProducerThread(final ElementProvider<E> elementProducer,
                          final BoundedConcurrentQueue<E> queue) {
        
        super(elementProducer.getHaltingElement(), queue);
        this.elementProvider = elementProducer;
    }
    
    @Override
    public void run() {
        while (true) {
            final E element        = elementProvider.produce();
            final E haltingElement = elementProvider.getHaltingElement();
            
            queue.push(element, this);
            
            if (Objects.equals(element, haltingElement)) {
                return;
            }
        }
    }
}
