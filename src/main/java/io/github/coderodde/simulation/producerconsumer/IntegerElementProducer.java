package io.github.coderodde.simulation.producerconsumer;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version 1.0.0
 * @since 1.0.0
 */
public final class IntegerElementProducer implements ElementProducer<Integer> {

    /**
     * The halting element signalling the end of element production.
     */
    private final Integer haltingInteger;
    
    /**
     * The integer element counter.
     */
    private final AtomicInteger atomicInteger = new AtomicInteger(0);
    
    public IntegerElementProducer(final Integer haltingElement) {
        this.haltingInteger = 
                Objects.requireNonNull(
                        haltingElement,
                        "The input haltingInteger is null");
    }
    
    @Override
    public Integer getHaltingElement() {
        return haltingInteger;
    }
    
    @Override
    public Integer produce() {
        final int currentValue = atomicInteger.getAndIncrement();
        
        return currentValue == haltingInteger ? haltingInteger : currentValue;
    }
}
