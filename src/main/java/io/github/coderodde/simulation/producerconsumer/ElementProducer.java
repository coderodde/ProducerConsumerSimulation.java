package io.github.coderodde.simulation.producerconsumer;

/**
 *
 * @author rodio
 */
public interface ElementProducer<E> {
    
    public E produce();
    
    public E getHaltingElement();
}
