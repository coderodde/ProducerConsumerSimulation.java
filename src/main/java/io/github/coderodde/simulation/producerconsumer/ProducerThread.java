package io.github.coderodde.simulation.producerconsumer;

import java.util.Objects;

/**
 * This class implements producer threads.
 * 
 * @param <E> the element type.
 * @param <R> the result type.
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ProducerThread<E, R> extends AbstractSimulationThread<E, R> {
    
    /**
     * The element provider.
     */
    private final ElementProvider<E> elementProvider;
    
    /**
     * Constructs this producer thread.
     * 
     * @param id              the ID of this producer thread.
     * @param elementProvider the element provider.
     * @param queue           the target queue.
     * @param sharedState     the shared state.
     * @param action          the action.
     */
    public ProducerThread(final int id,
                          final ElementProvider<E> elementProvider,
                          final BoundedConcurrentQueue<E, R> queue,
                          final SharedProducerThreadState sharedState,
                          final ConsumerAction<E, R> action) {
        
        super(id,
              elementProvider.getHaltingElement(),
              queue, 
              sharedState,
              action);
        
        this.elementProvider = elementProvider;
    }
    
    /**
     * Runs the simulation.
     */
    @Override
    public void run() {
        while (true) {
            
            if (sharedState.isHaltRequested()) {
                System.out.printf("[STATUS] Producer %d exited.\n", threadId);
                return;
            }
            
            final E element = elementProvider.provide();
            
            if (Objects.equals(element, haltingElement)) {
                sharedState.requestHalt();
                queue.push(haltingElement, this);
                return;
            }
            
            queue.push(element, this);
        }
    }
    
    /**
     * Returns the name of this thread.
     * 
     * @return the name of this thread.
     */
    @Override
    public String toString() {
        return "Producer thread";
    }
}
