package io.github.coderodde.simulation.producerconsumer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class Simulator {
    
    private final int numberOfProducerThreads;
    private final int numberOfConsumerThreads;
    private BoundedConcurrentQueue<Integer> queue;
    private IntegerElementProvider elementProducer;
    private IntegerQueueNotifier queueNotifier;
    
    public Simulator(final int numberOfProducerThreads,
                     final int numberOfConsumerThreads) {
        
        this.numberOfProducerThreads = 
                checkNumberOfProducerThreads(numberOfProducerThreads);
        
        this.numberOfConsumerThreads =
                checkNumberOfConsumerThreads(numberOfConsumerThreads);
    }
    
    public void setQueue(final BoundedConcurrentQueue<Integer> queue) {
        this.queue = queue;
    }
    
    public void setElementProvider(
            final IntegerElementProvider elementProducer) {
        
        this.elementProducer = elementProducer;
    }
    
    public void setQueueNotifier(final IntegerQueueNotifier queueNotifier) {
        this.queueNotifier = queueNotifier;
    }
    
    public void run() {
        
        queue.setQueueNotifier(queueNotifier);
        
        final List<ConsumerThread<Integer>> consumerThreadList;
        final List<ProducerThread<Integer>> producerThreadList;
        
        final SharedProducerThreadState sharedState =
          new SharedProducerThreadState();
        
        consumerThreadList = new ArrayList<>(numberOfConsumerThreads);
        producerThreadList = new ArrayList<>(numberOfProducerThreads);
        
        for (int i = 0; i < numberOfConsumerThreads; ++i) {
            final ConsumerThread<Integer> thread = 
                    new ConsumerThread(elementProducer.getHaltingElement(), 
                                       queue,
                                       sharedState);
            
            consumerThreadList.add(thread);
            thread.start();
        }
        
        for (int i = 0; i < numberOfProducerThreads; ++i) {
            final ProducerThread<Integer> thread = 
                    new ProducerThread(elementProducer, 
                                       queue,
                                       sharedState);
            
            producerThreadList.add(thread);
            thread.start();
        }
        
        for (final ConsumerThread<Integer> thread : consumerThreadList) {
            try {
                thread.join();
            } catch (final InterruptedException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
        
        for (final ProducerThread<Integer> thread : producerThreadList) {
            try {
                thread.join();
            } catch (final InterruptedException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
    }
    
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
    
    private static int checkQueueCapacity(final int queueCapacity) {
     
        if (queueCapacity < 1) {
            final String exceptionMessage = 
                    String.format(
                            "queueCapacity(%d) < 1", 
                            queueCapacity);
            
            throw new IllegalArgumentException(exceptionMessage);
        }
        
        return queueCapacity;
    }
}
