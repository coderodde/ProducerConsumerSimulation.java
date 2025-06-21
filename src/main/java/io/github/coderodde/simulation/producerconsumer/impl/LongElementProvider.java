package io.github.coderodde.simulation.producerconsumer.impl;

import io.github.coderodde.simulation.producerconsumer.ElementProvider;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class implements an element provider.
 * 
 * @version 1.0.0
 * @since 1.0.0
 */
public final class LongElementProvider implements ElementProvider<Long> {

    /**
     * Maximum element value.
     */
    private final long bound;
    
    /**
     * The number of elements to provide before requesting halting.
     */
    private final long totalNumberOfOperations;
    
    /**
     * The number of elements provided so far.
     */
    private long numberOfOperationsPerformed = 0;
    
    /**
     * The halting element indicating that consumer threads reading this from
     * the queue must exit.
     */
    private final long haltingElement;
    
    /**
     * Constructs this element provider.
     * 
     * @param bound                   the bound value (maximum).
     * @param totalNumberOfOperations the total number of elements to provide.
     * @param haltingElement          the halting element.
     */
    public LongElementProvider(final long bound,
                               final long totalNumberOfOperations,
                               final long haltingElement) {
        this.bound = bound;
        this.totalNumberOfOperations = totalNumberOfOperations;
        this.haltingElement = haltingElement;
    }
    
    /**
     * Returns the halting element.
     * 
     * @return the halting element.
     */
    @Override
    public Long getHaltingElement() {
        return haltingElement;
    }
    
    /**
     * Provides a randomly chosen element.
     * 
     * @return a randomly chose element.
     */
    @Override
    public Long provide() {
        if (numberOfOperationsPerformed++ < totalNumberOfOperations) {
            return ThreadLocalRandom.current().nextLong(bound);
        } else {
            return haltingElement;
        }
    }
}
