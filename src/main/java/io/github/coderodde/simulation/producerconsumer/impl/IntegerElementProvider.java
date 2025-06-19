package io.github.coderodde.simulation.producerconsumer.impl;

import io.github.coderodde.simulation.producerconsumer.ElementProvider;
import java.util.Objects;
import java.util.Random;

/**
 * @version 1.0.0
 * @since 1.0.0
 */
public final class IntegerElementProvider implements ElementProvider<Long> {

    private final long bound;
    
    /**
     * The halting element signalling the end of element production.
     */
    private final long totalNumberOfOperations;
    
    private long numberOfOperationsPerformed = 0;
    
    private final long haltingElement;
    
    private final Random random = new Random();
    
    public IntegerElementProvider(final long bound,
                                  final long totalNumberOfOperations,
                                  final long haltingElement) {
        this.bound = bound;
        this.totalNumberOfOperations = totalNumberOfOperations;
        this.haltingElement = haltingElement;
    }
    
    @Override
    public Long getHaltingElement() {
        return haltingElement;
    }
    
    @Override
    public synchronized Long produce() {
        if (numberOfOperationsPerformed++ < totalNumberOfOperations) {
            return random.nextLong(bound);
        } else {
            return haltingElement;
        }
    }
}
