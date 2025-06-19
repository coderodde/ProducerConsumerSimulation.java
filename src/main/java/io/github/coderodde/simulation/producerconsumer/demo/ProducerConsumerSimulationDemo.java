package io.github.coderodde.simulation.producerconsumer.demo;

import io.github.coderodde.simulation.producerconsumer.BoundedConcurrentQueue;
import io.github.coderodde.simulation.producerconsumer.Simulator;
import io.github.coderodde.simulation.producerconsumer.impl.FibonacciConsumerAction;
import io.github.coderodde.simulation.producerconsumer.impl.LongQueueNotifier;
import io.github.coderodde.simulation.producerconsumer.impl.IntegerElementProvider;
import java.math.BigInteger;

/**
 * This class implements the producer/consumer simulation demonstration.
 * 
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ProducerConsumerSimulationDemo {

    private static final int DEFAULT_NUMBER_OF_PRODUCERS = 15;
    private static final int DEFAULT_NUMBER_OF_CONSUMERS = 20;
    private static final int DEFAULT_HALTING_INTEGER = 300;
    private static final int DEFAULT_QUEUE_CAPACITY = 10;
    private static final long LONG_BOUND = 31;
    private static final long TOTAL_OPERATIONS = 300;
    
    public static void main(String[] args) {
        final IntegerElementProvider elementProvider = 
                    new IntegerElementProvider(LONG_BOUND,
                                               TOTAL_OPERATIONS, 
                                               Long.MIN_VALUE);
        
        final FibonacciConsumerAction action = new FibonacciConsumerAction();
        
        final BoundedConcurrentQueue<Long, BigInteger> queue = 
                new BoundedConcurrentQueue<>(DEFAULT_QUEUE_CAPACITY);
        
        final LongQueueNotifier queueNotifier = 
                new LongQueueNotifier(queue,
                                         action,
                                         DEFAULT_NUMBER_OF_CONSUMERS, 
                                         DEFAULT_NUMBER_OF_PRODUCERS,
                                         DEFAULT_HALTING_INTEGER);
        
        
        final Simulator simulator = new Simulator(DEFAULT_NUMBER_OF_PRODUCERS,
                                                  DEFAULT_NUMBER_OF_CONSUMERS);
        
        simulator.setQueue(queue);
        simulator.setElementProvider(elementProvider);
        simulator.setQueueNotifier(queueNotifier);
        simulator.setAction(action);
        simulator.run();
        
        System.out.println("[STATUS] Simulation done!");
    }
}
