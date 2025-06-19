package io.github.coderodde.simulation.producerconsumer;

import java.util.function.Function;

/**
 *
 * @author rodio
 */
public interface ConsumerAction<E, R> extends Function<E, R> {
    
}
