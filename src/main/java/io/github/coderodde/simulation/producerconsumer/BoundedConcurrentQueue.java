package io.github.coderodde.simulation.producerconsumer;

import java.util.concurrent.Semaphore;

/**
 * This class implements a bounded concurrent queue.
 * 
 * @param <E> the queue element type.
 * @version 1.0.0
 * @since 1.0.0
 */
public final class BoundedConcurrentQueue<E, R> {
    
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
    private AbstractQueueNotifier<E, R> queueNotifier;
    
    /**
     * The action performed on each popped element.
     */
    private ConsumerAction<E, R> action;
    
    public BoundedConcurrentQueue(final int capacity) {
        checkCapacity(capacity);
        semaphoreFreeSpots = new Semaphore(0,        true);
        semaphoreFillSpots = new Semaphore(capacity, true);
        mutex              = new Semaphore(1,        true);
        array              = (E[]) new Object[capacity];
    }
    
    /**
     * Pushes the {@code element} to the end of this queue.
     * 
     * @param element the element to push.
     * @param thread  the thread pushing.
     */
    public void push(final E element,
                     final AbstractSimulationThread<E, R> thread) {
        
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
    
    /**
     * Pops the head element from this queue.
     * 
     * @param thread the thread popping.
     * 
     * @return the popped element.
     */
    public E pop(final ConsumerThread<E, R> thread) {
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
    
    /**
     * Peeks at the head element of this queue.
     * 
     * @return the head element.
     */
    public E top() {
        semaphoreFreeSpots.acquireUninterruptibly();
        mutex.acquireUninterruptibly();
        final E topElement = array[headIndex];
        mutex.release();
        semaphoreFreeSpots.release();
        return topElement;
    }
    
    /**
     * Return the length of the queue.
     * 
     * @return the length of the queue.
     */
    public int size() {
        mutex.acquireUninterruptibly();
        final int sz = size;
        mutex.release();
        return sz;
    }
    
    /**
     * Return the textual representation of this queue.
     * 
     * @return the textual representation.
     */
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
    
    /**
     * Sets the queue notifier.
     * 
     * @param queueNotifier the queue notifier to set.
     */
    public void setQueueNotifier(
            final AbstractQueueNotifier<E, R> queueNotifier) {
        
        this.queueNotifier = queueNotifier;
    }
    
    /**
     * Sets the action.
     * 
     * @param action the consumer action.
     */
    public void setAction(final ConsumerAction<E, R> action) {
        this.action = action;
    }
    
    /**
     * Converts logical index of an element to physical.
     * 
     * @param index the logical index of an element.
     * 
     * @return the physical index of an element. 
     */
    private int logicalIndexToPhysicalIndex(final int index) {
        return (headIndex + index) % array.length;
    }
    
    /**
     * Checks that the input capacity is sensible (at least 1).
     */
    private static void checkCapacity(final int capacity) {
        if (capacity < 1) {
            final String exceptionMessage = 
                    String.format("capacity(%d) < 1", capacity);
            
            throw new IllegalArgumentException(exceptionMessage);
        }
    }
}
