package io.github.coderodde.simulation.producerconsumer;

/**
 *
 * @author rodio
 */
public interface ElementProvider<E> {
    
    public E produce();
    
    public E getHaltingElement();
}
