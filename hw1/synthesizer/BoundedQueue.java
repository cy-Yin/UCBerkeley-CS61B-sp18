package synthesizer;

import java.util.Iterator;

public interface BoundedQueue<T> extends Iterable<T> {
    /** Returns size of the buffer. */
    int capacity();

    /** Returns the number of items currently in the buffer. */
    int fillCount();

    /** Adds item x to the end. */
    void enqueue(T x);

    /** Deletes and returns item from the front. */
    T dequeue();

    /** Returns (but do not delete) item from the front. */
    T peek();

    /** Returns true if the buffer is empty (fillCount equals zero)
     *  and false otherwise.
     */
    default boolean isEmpty() {
        return fillCount() == 0;
    }

    /** Returns true if the buffer is full (fillCount equals capacity)
     *  and false otherwise.
     */
    default boolean isFull() {
        return fillCount() == capacity();
    }

    /** Adds the ability to iterate. */
    Iterator<T> iterator();
}
