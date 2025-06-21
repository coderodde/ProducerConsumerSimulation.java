package io.github.coderodde.simulation.producerconsumer.impl;

import io.github.coderodde.simulation.producerconsumer.ConsumerAction;
import java.math.BigInteger;

/**
 *
 * @author rodio
 */
public final class FibonacciConsumerAction 
        implements ConsumerAction<Long, BigInteger> {

    @Override
    public BigInteger apply(final Long arg) {
        return fibonacci(arg);
    }
    
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
