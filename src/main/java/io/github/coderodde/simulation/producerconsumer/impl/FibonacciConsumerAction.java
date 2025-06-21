package io.github.coderodde.simulation.producerconsumer.impl;

import io.github.coderodde.simulation.producerconsumer.ConsumerAction;
import java.math.BigInteger;

/**
 * This class implements a consumer action that computes Fibonacci numbers.
 * 
 * @version 1.0.0
 * @since 1.0.0
 */
public final class FibonacciConsumerAction 
        implements ConsumerAction<Long, BigInteger> {

    /**
     * Applies the argument.
     * 
     * @param arg the argument to apply.
     * 
     * @return {@code arg}th Fibonacci number.
     */
    @Override
    public BigInteger apply(final Long arg) {
        return fibonacci(arg);
    }
    
    /**
     * Computes {@code index}th Fibonacci number.
     * 
     * @param index the index of the number. Starts from zero (0).
     * 
     * @return {@code index}th Fibonacci number. 
     */
    private static BigInteger fibonacci(final long index) {
        BigInteger a = BigInteger.ZERO;
        BigInteger b = BigInteger.ONE;
        
        for (int i = 0; i < index; ++i) {
            BigInteger c = a.add(b);
            a = b;
            b = c;
        }
        
        return a;
    }
}
