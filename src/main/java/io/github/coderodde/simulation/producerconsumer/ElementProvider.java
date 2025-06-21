package io.github.coderodde.simulation.producerconsumer;

/**
 * This interface defines the API for element providers.
 * 
 * @param <E> the element type.
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ElementProvider<E> {
    
    /**
     * Provides an element to push to a queue.
     * 
     * @return an element to push.
     */
    public E provide();
    
    /**
     * Returns the halting element.
     * 
     * @return the halting element.
     */
    public E getHaltingElement();
}
