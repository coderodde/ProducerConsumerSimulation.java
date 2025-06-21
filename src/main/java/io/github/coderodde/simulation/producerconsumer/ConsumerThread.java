package io.github.coderodde.simulation.producerconsumer;

/**
 * This class implements a generic consumer thread.
 * 
 * @param <E> the element type.
 * @param <R> the result type.
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ConsumerThread<E, R> extends AbstractSimulationThread<E, R> {
    
    /**
     * Constructs this consumer thread.
     * 
     * @param haltingElement the element indicating this thread should exit.
     * @param queue          the target queue.
     * @param action         the pop action.
     */
    public ConsumerThread(final E haltingElement,
                          final BoundedConcurrentQueue<E, R> queue,
                          final ConsumerAction<E, R> action) {
        
        super(haltingElement,
              queue, 
              null,
              action);
    }
    
    /**
     * Enters to thread.
     */
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
    
    /**
     * Returns the name of this thread.
     * 
     * @return the name of thsi thread. 
     */
    @Override
    public String toString() {
        return "Consumer thread";
    }
}
