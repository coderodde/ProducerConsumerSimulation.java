package io.github.coderodde.simulation.producerconsumer.impl;

import io.github.coderodde.simulation.producerconsumer.ElementProvider;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @version 1.0.0
 * @since 1.0.0
 */
public final class LongElementProvider implements ElementProvider<Long> {

    private final long bound;
    
    /**
     * The halting element signalling the end of element production.
     */
    private final long totalNumberOfOperations;
    
    private long numberOfOperationsPerformed = 0;
    
    private final long haltingElement;
    
    public LongElementProvider(final long bound,
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
    public Long produce() {
        if (numberOfOperationsPerformed++ < totalNumberOfOperations) {
            return ThreadLocalRandom.current().nextLong(bound);
        } else {
            return haltingElement;
        }
    }
}
