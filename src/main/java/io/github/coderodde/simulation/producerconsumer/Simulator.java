package io.github.coderodde.simulation.producerconsumer;

import io.github.coderodde.simulation.producerconsumer.impl.FibonacciConsumerAction;
import io.github.coderodde.simulation.producerconsumer.impl.LongQueueNotifier;
import io.github.coderodde.simulation.producerconsumer.impl.LongElementProvider;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class Simulator {
    
    private final int numberOfProducerThreads;
    private final int numberOfConsumerThreads;
    private BoundedConcurrentQueue<Long, BigInteger> queue;
    private LongElementProvider elementProducer;
    private LongQueueNotifier queueNotifier;
    private FibonacciConsumerAction action;
    
    public Simulator(final int numberOfProducerThreads,
                     final int numberOfConsumerThreads) {
        
        this.numberOfProducerThreads = 
                checkNumberOfProducerThreads(numberOfProducerThreads);
        
        this.numberOfConsumerThreads =
                checkNumberOfConsumerThreads(numberOfConsumerThreads);
    }
    
    public void setQueue(
            final BoundedConcurrentQueue<Long, BigInteger> queue) {
        
        this.queue = queue;
    }
    
    public void setElementProvider(
            final LongElementProvider elementProducer) {
        
        this.elementProducer = elementProducer;
    }
    
    public void setQueueNotifier(final LongQueueNotifier queueNotifier) {
        this.queueNotifier = queueNotifier;
    }
    
    public void setAction(final FibonacciConsumerAction action) {
        this.action = action;
    }
    
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
                    new ConsumerThread<Long, BigInteger>(elementProducer.getHaltingElement(), 
                                         queue,
                                         sharedState,
                                         action);
            
            consumerThreadList.add(thread);
            thread.start();
        }
        
        for (int i = 0; i < numberOfProducerThreads; ++i) {
            final ProducerThread<Long, BigInteger> thread = 
                    new ProducerThread(elementProducer, 
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
