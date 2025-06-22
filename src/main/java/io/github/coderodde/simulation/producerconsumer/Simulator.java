package io.github.coderodde.simulation.producerconsumer;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements a simulator that orchestrates the threads.
 * 
 * @param <E> the queue element type.
 * @param <R> the result element type.
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Simulator<E, R> {
    
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
    private BoundedConcurrentQueue<E, R> queue;
    
    /**
     * The element provider.
     */
    private ElementProvider<E> elementProvider;
    
    /**
     * The queue notifier.
     */
    private AbstractQueueListener<E, R> queueListener;
    
    /**
     * The action performed on each element popped from {@link #queue}.
     */
    private ConsumerAction<E, R> action;
    
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
    public void setQueue(final BoundedConcurrentQueue<E, R> queue) {
        this.queue = queue;
    }
    
    /**
     * Sets the element provider.
     * 
     * @param elementProducer the element provider to set.
     */
    public void setElementProvider(final ElementProvider<E> elementProducer) {
        this.elementProvider = elementProducer;
    }
    
    /**
     * Sets the queue notifier.
     * 
     * @param queueListener the queue notifier to set.
     */
    public void setQueueListener(
            final AbstractQueueListener<E, R> queueListener) {
        
        this.queueListener = queueListener;
    }
    
    /**
     * Sets the consumer action.
     * 
     * @param action the action to set. 
     */
    public void setAction(final ConsumerAction<E, R> action) {
        this.action = action;
    }
    
    /**
     * Runs the actual simulation.
     */
    public void run() {
        
        queue.setQueueNotifier(queueListener);
        
        final List<ConsumerThread<E, R>> consumerThreadList;
        final List<ProducerThread<E, R>> producerThreadList;
        
        final SharedProducerThreadState sharedState =
          new SharedProducerThreadState();
        
        consumerThreadList = new ArrayList<>(numberOfConsumerThreads);
        producerThreadList = new ArrayList<>(numberOfProducerThreads);
        
        for (int i = 0; i < numberOfConsumerThreads; ++i) {
            final ConsumerThread<E, R> thread = 
                    new ConsumerThread<>(i,
                                         elementProvider.getHaltingElement(), 
                                         queue,
                                         action);
            
            consumerThreadList.add(thread);
            thread.start();
        }
        
        for (int i = 0; i < numberOfProducerThreads; ++i) {
            final ProducerThread<E, R> thread = 
                    new ProducerThread<>(i,
                                         elementProvider, 
                                         queue,
                                         sharedState,
                                         action);
            
            producerThreadList.add(thread);
            thread.start();
        }
        
        for (final ConsumerThread<E, R> thread
                : consumerThreadList) {
            
            try {
                thread.join();
            } catch (final InterruptedException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
        
        for (final ProducerThread<E, R> thread
                : producerThreadList) {
            
            try {
                thread.join();
            } catch (final InterruptedException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
        
        System.out.println("[STATUS] Simulation done!");
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
