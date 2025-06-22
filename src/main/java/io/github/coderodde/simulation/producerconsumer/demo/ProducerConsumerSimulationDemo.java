package io.github.coderodde.simulation.producerconsumer.demo;

import io.github.coderodde.simulation.producerconsumer.BoundedConcurrentQueue;
import io.github.coderodde.simulation.producerconsumer.Simulator;
import io.github.coderodde.simulation.producerconsumer.impl.FibonacciConsumerAction;
import io.github.coderodde.simulation.producerconsumer.impl.LongQueueListener;
import io.github.coderodde.simulation.producerconsumer.impl.LongElementProvider;
import java.math.BigInteger;

/**
 * This class implements the producer/consumer simulation demonstration.
 * 
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ProducerConsumerSimulationDemo {

    private static final int DEFAULT_NUMBER_OF_PRODUCERS = 3;
    private static final int DEFAULT_NUMBER_OF_CONSUMERS = 5;
    private static final int DEFAULT_QUEUE_CAPACITY = 6;
    private static final long DEFAULT_HALTING_ELEMENT = -1;
    private static final long LONG_BOUND = 31;
    private static final long TOTAL_OPERATIONS = 20;
    
    public static void main(String[] args) {
        final LongElementProvider elementProvider = 
                    new LongElementProvider(LONG_BOUND,
                                            TOTAL_OPERATIONS, 
                                            DEFAULT_HALTING_ELEMENT);
        
        final FibonacciConsumerAction action = new FibonacciConsumerAction();
        
        final BoundedConcurrentQueue<Long, BigInteger> queue = 
                new BoundedConcurrentQueue<>(DEFAULT_QUEUE_CAPACITY);
        
        final LongQueueListener queueNotifier = 
                new LongQueueListener(queue,
                                      action,
                                      DEFAULT_NUMBER_OF_CONSUMERS, 
                                      DEFAULT_NUMBER_OF_PRODUCERS,
                                      DEFAULT_HALTING_ELEMENT);
        
        final Simulator<Long, BigInteger> simulator = 
                new Simulator<>(DEFAULT_NUMBER_OF_PRODUCERS,
                                DEFAULT_NUMBER_OF_CONSUMERS);
        
        simulator.setQueue(queue);
        simulator.setElementProvider(elementProvider);
        simulator.setQueueListener(queueNotifier);
        simulator.setAction(action);
        simulator.run();
    }
}
