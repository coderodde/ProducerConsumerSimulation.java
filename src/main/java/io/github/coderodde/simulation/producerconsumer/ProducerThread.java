package io.github.coderodde.simulation.producerconsumer;

import java.util.Objects;

/**
 * This class implements producer threads.
 * 
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ProducerThread<E, R> extends AbstractSimulationThread<E, R> {
    
    private final ElementProvider<E> elementProvider;
    
    public ProducerThread(final ElementProvider<E> elementProducer,
                          final BoundedConcurrentQueue<E, R> queue,
                          final SharedProducerThreadState sharedState,
                          final ConsumerAction<E, R> action) {
        
        super(elementProducer.getHaltingElement(),
              queue, 
              sharedState,
              action);
        
        this.elementProvider = elementProducer;
    }
    
    @Override
    public void run() {
        while (true) {
            
            if (sharedState.isHaltRequested()) {
                return;
            }
            
            final E element        = elementProvider.produce();
            final E haltingElement = elementProvider.getHaltingElement();
            
            if (Objects.equals(element, haltingElement)) {
                sharedState.requestHalt();
                System.out.println("element = " + element);
                queue.push(haltingElement, this);
                return;
            } else {
                System.out.println("fail");
            }
            
            queue.push(element, this);
        }
    }
    
    @Override
    public String toString() {
        return "Producer thread";
    }
}
