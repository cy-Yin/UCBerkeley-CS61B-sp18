package synthesizer;

public abstract class AbstractBoundedQueue<T> implements BoundedQueue<T> {
    protected int fillCount;
    protected int capacity;

    /** Returns size of the buffer. */
    @Override
    public int capacity() {
        return capacity;
    }

    /** Returns the number of items currently in the buffer. */
    @Override
    public int fillCount() {
        return fillCount;
    }
}
