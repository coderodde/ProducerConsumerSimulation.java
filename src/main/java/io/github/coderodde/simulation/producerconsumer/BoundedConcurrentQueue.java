package io.github.coderodde.simulation.producerconsumer;

import java.util.Objects;
import java.util.concurrent.Semaphore;

/**
 * This class implements a bounded concurrent queue.
 * 
 * @param <E> the queue element type.
 * @version 1.0.0
 * @since 1.0.0
 */
public final class BoundedConcurrentQueue<E> {
    
    /**
     * The semaphore for protecting against pops on empty queue.
     */
    private final Semaphore semaphoreFreeSpots;
    
    /**
     * The semaphore for protecting against pushes to a full queue.
     */
    private final Semaphore semaphoreFillSpots;
    
    /**
     * A mutex for synchronizing the operations on this queue.
     */
    private final Semaphore mutex;
    
    /**
     * The array actually holding the elements in this queue.
     */
    private final E[] array;
    
    /**
     * The index of the head element that is the next one that will be removed
     * with {@link #pop()}.
     */
    private int headIndex = 0;
    
    /**
     * The number of elements stored in this queue.
     */
    private int size = 0;
    
    public BoundedConcurrentQueue(final int capacity) {
        checkCapacity(capacity);
        semaphoreFreeSpots = new Semaphore(0,        true);
        semaphoreFillSpots = new Semaphore(capacity, true);
        mutex              = new Semaphore(1,        true);
        array              = (E[]) new Object[capacity];
    }
    
    public void push(final E element) {
        semaphoreFreeSpots.release();
        semaphoreFillSpots.acquireUninterruptibly();
        mutex.acquireUninterruptibly();
        array[logicalIndexToPhysicalIndex(size++)] = element;
        
        System.out.printf(
                "Producer %d pushed %s: %s\n",
                          ((ProducerThread<E>) Thread.currentThread())
                                                     .getThreadId(),
                          Objects.toString(element), 
                          this.toString());
        mutex.release();
    }
    
    public E pop() {
        semaphoreFillSpots.release();
        semaphoreFreeSpots.acquireUninterruptibly();
        mutex.acquireUninterruptibly();
        final E element = array[headIndex++];
        --size;
                
        System.out.printf(
                "Consumer %d popped %s: %s\n",
                          ((ConsumerThread<E>) Thread.currentThread())
                                                     .getThreadId(),
                          Objects.toString(element), 
                          this.toString());

        mutex.release();
        return element;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("[");
        boolean firstIteration = true;
        
        for (int index = 0; index < size; ++index) {
            if (firstIteration) {
                firstIteration = false;
            } else {
                sb.append(", ");
            }
            
            sb.append(array[logicalIndexToPhysicalIndex(index)]);
        }
        
        return sb.append("]").toString();
    }
    
    private int logicalIndexToPhysicalIndex(final int index) {
        return (headIndex + index) % array.length;
    }
    
    private static void checkCapacity(final int capacity) {
        if (capacity < 1) {
            final String exceptionMessage = 
                    String.format("capacity(%d) < 1", capacity);
            
            throw new IllegalArgumentException(exceptionMessage);
        }
    }
}
