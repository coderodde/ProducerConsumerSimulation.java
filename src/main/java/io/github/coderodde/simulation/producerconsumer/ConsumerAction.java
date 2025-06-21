package io.github.coderodde.simulation.producerconsumer;

import java.util.function.Function;

/**
 * This interface merely renames the function of type {@code E -> R}.
 * 
 * @param <E> the argument element type.
 * @param <R> the result element type.
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ConsumerAction<E, R> extends Function<E, R> {
    
}
