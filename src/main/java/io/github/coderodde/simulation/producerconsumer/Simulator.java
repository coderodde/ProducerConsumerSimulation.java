package io.github.coderodde.simulation.producerconsumer;

import io.github.coderodde.simulation.producerconsumer.impl.FibonacciConsumerAction;
import io.github.coderodde.simulation.producerconsumer.impl.LongQueueNotifier;
import io.github.coderodde.simulation.producerconsumer.impl.LongElementProvider;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements a simulator that orchestrates the threads.
 */
public final class Simulator {
    
    /**
     * The number of producer threads.
     */
    private final int numberOfProducerThreads;
    
    /**
     * The number of consumer threads.
     */
    private final int numberOfConsumerThreads;
    
    /**
     * The target queue.
     */
    private BoundedConcurrentQueue<Long, BigInteger> queue;
    
    /**
     * The element provider.
     */
    private LongElementProvider elementProvider;
    
    /**
     * The queue notifier.
     */
    private LongQueueNotifier queueNotifier;
    
    /**
     * The action performed on each element popped from {@link #queue}.
     */
    private FibonacciConsumerAction action;
    
    /**
     * Constructs this simulator.
     * 
     * @param numberOfProducerThreads the number of producers.
     * @param numberOfConsumerThreads the number of consumers.
     */
    public Simulator(final int numberOfProducerThreads,
                     final int numberOfConsumerThreads) {
        
        this.numberOfProducerThreads = 
                checkNumberOfProducerThreads(numberOfProducerThreads);
        
        this.numberOfConsumerThreads =
                checkNumberOfConsumerThreads(numberOfConsumerThreads);
    }
    
    /**
     * Sets the queue.
     * 
     * @param queue the target queue to set.
     */
    public void setQueue(
            final BoundedConcurrentQueue<Long, BigInteger> queue) {
        
        this.queue = queue;
    }
    
    /**
     * Sets the element provider.
     * 
     * @param elementProducer the element provider to set.
     */
    public void setElementProvider(
            final LongElementProvider elementProducer) {
        
        this.elementProvider = elementProducer;
    }
    
    /**
     * Sets the queue notifier.
     * 
     * @param queueNotifier the queue notifier to set.
     */
    public void setQueueNotifier(final LongQueueNotifier queueNotifier) {
        this.queueNotifier = queueNotifier;
    }
    
    /**
     * Sets the consumer action.
     * 
     * @param action the action to set. 
     */
    public void setAction(final FibonacciConsumerAction action) {
        this.action = action;
    }
    
    /**
     * Runs the actual simulation.
     */
    public void run() {
        
        queue.setQueueNotifier(queueNotifier);
        
        final List<ConsumerThread<Long, BigInteger>> consumerThreadList;
        final List<ProducerThread<Long, BigInteger>> producerThreadList;
        
        final SharedProducerThreadState sharedState =
          new SharedProducerThreadState();
        
        consumerThreadList = new ArrayList<>(numberOfConsumerThreads);
        producerThreadList = new ArrayList<>(numberOfProducerThreads);
        
        for (int i = 0; i < numberOfConsumerThreads; ++i) {
            final ConsumerThread<Long, BigInteger> thread = 
                    new ConsumerThread<>(elementProvider.getHaltingElement(), 
                                         queue,
                                         action);
            
            consumerThreadList.add(thread);
            thread.start();
        }
        
        for (int i = 0; i < numberOfProducerThreads; ++i) {
            final ProducerThread<Long, BigInteger> thread = 
                    new ProducerThread<>(elementProvider, 
                                         queue,
                                         sharedState,
                                         action);
            
            producerThreadList.add(thread);
            thread.start();
        }
        
        for (final ConsumerThread<Long, BigInteger> thread
                : consumerThreadList) {
            
            try {
                thread.join();
            } catch (final InterruptedException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
        
        for (final ProducerThread<Long, BigInteger> thread
                : producerThreadList) {
            
            try {
                thread.join();
            } catch (final InterruptedException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
    }
    
    /**
     * Checks that there is at least one producer thread requested.
     * 
     * @param numberOfProducerThreads the number of producer threads to create.
     * 
     * @return the same as input.
     */
    private static int 
        checkNumberOfProducerThreads(final int numberOfProducerThreads) {
     
        if (numberOfProducerThreads < 1) {
            final String exceptionMessage = 
                    String.format(
                            "numberOfProducerThreads(%d) < 1", 
                            numberOfProducerThreads);
            
            throw new IllegalArgumentException(exceptionMessage);
        }
        
        return numberOfProducerThreads;
    }
    
    /**
     * Checks that there is at least one consumer thread requested.
     * 
     * @param numberOfConsumerThreads the number of consumer threads to create.
     * 
     * @return the same as input.
     */
    private static int 
        checkNumberOfConsumerThreads(final int numberOfConsumerThreads) {
     
        if (numberOfConsumerThreads < 1) {
            final String exceptionMessage = 
                    String.format(
                            "numberOfConsumerThreads(%d) < 1", 
                            numberOfConsumerThreads);
            
            throw new IllegalArgumentException(exceptionMessage);
        }
        
        return numberOfConsumerThreads;
    }
}
