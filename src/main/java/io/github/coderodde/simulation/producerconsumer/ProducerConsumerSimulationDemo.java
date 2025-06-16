package io.github.coderodde.simulation.producerconsumer;

/**
 * This class implements the producer/consumer simulation demonstration.
 * 
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ProducerConsumerSimulationDemo {

    public static void main(String[] args) {
        final IntegerElementProducer elementProducer = 
                new IntegerElementProducer(100);
        
        new Simulator(3, 5, 10, elementProducer).run();
    }
}
