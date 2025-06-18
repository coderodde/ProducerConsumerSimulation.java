package io.github.coderodde.simulation.producerconsumer;

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
    
    /**
     * Possible queue notifier object.
     */
    private AbstractQueueNotifier<E> queueNotifier;
    
    public BoundedConcurrentQueue(final int capacity) {
        checkCapacity(capacity);
        semaphoreFreeSpots = new Semaphore(0,        true);
        semaphoreFillSpots = new Semaphore(capacity, true);
        mutex              = new Semaphore(1,        true);
        array              = (E[]) new Object[capacity];
    }
    
    public void push(final E element,
                     final AbstractSimulationThread<E> thread) {
        
        semaphoreFreeSpots.release();
        semaphoreFillSpots.acquireUninterruptibly();
        mutex.acquireUninterruptibly();
        array[logicalIndexToPhysicalIndex(size++)] = element;
        
        if (queueNotifier != null) {
            queueNotifier.onPush(thread, 
                                 element);
        }
        
        mutex.release();
    }
    
    public E pop(final ConsumerThread<E> thread) {
        semaphoreFillSpots.release();
        semaphoreFreeSpots.acquireUninterruptibly();
        mutex.acquireUninterruptibly();
        final E element = array[headIndex];
        headIndex = (headIndex + 1) % array.length;
        --size;
        
        if (queueNotifier != null) {
            queueNotifier.onPop(thread,
                                element);
        }
        
        mutex.release();
        return element;
    }
    
    public int size() {
        mutex.acquireUninterruptibly();
        final int sz = size;
        mutex.release();
        return sz;
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
    
    public void setQueueNotifier(final AbstractQueueNotifier<E> queueNotifier) {
        this.queueNotifier = queueNotifier;
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
