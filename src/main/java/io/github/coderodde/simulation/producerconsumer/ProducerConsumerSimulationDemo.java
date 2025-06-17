package io.github.coderodde.simulation.producerconsumer;

/**
 * This class implements the producer/consumer simulation demonstration.
 * 
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ProducerConsumerSimulationDemo {

    private static final int DEFAULT_NUMBER_OF_PRODUCERS = 20;
    private static final int DEFAULT_NUMBER_OF_CONSUMERS = 15;
    private static final int DEFAULT_HALTING_INTEGER = 300;
    private static final int DEFAULT_QUEUE_CAPACITY = 10;
    
    public static void main(String[] args) {
        final IntegerElementProvider elementProvider = 
                new IntegerElementProvider(DEFAULT_HALTING_INTEGER);
        
        final BoundedConcurrentQueue<Integer> queue = 
                new BoundedConcurrentQueue<>(DEFAULT_QUEUE_CAPACITY);
        
        final IntegerQueueNotifier queueNotifier = 
                new IntegerQueueNotifier(queue,
                                         DEFAULT_NUMBER_OF_CONSUMERS, 
                                         DEFAULT_NUMBER_OF_PRODUCERS,
                                         DEFAULT_HALTING_INTEGER);
        
        
        final Simulator simulator = new Simulator(DEFAULT_NUMBER_OF_PRODUCERS,
                                                  DEFAULT_NUMBER_OF_CONSUMERS);
        
        simulator.setQueue(queue);
        simulator.setElementProvider(elementProvider);
        simulator.setQueueNotifier(queueNotifier);
        simulator.run();
        
        System.out.println("[STATUS] Simulation done!");
    }
}
