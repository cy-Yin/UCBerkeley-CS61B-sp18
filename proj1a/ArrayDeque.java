public class ArrayDeque<T> {

    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    private static final double FACTOR = 0.25;

    /** Creates an empty Array Deque. */
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 0;
        nextLast = 1;
    }

    /** resize the Array Deque to the new size. */
    private void resize(int newLength) {
        if (newLength > items.length) {
            resizeLarger(newLength);
        } else if (newLength < items.length) {
            resizeSmaller(newLength);
        }
    }

    /** Make the array length larger when the array is full. */
    private void resizeLarger(int newLength) {
        T[] newArrayDeque = (T[]) new Object[newLength];
        /* When size equals to the array length,
         * there are two cases:
         * 1. nextFirst = nextLast - 1,
         *    0 <= nextFirst < nextLast <= array.length - 1
         * 2. nextFirst = array.length - 1, nextLast = 0
         */
        if (nextFirst == nextLast - 1) {
            // [0, 1, 2, 3] --> [0, 1, _, _, _, _, 2, 3]
            //     ^f ^l               ^l       ^f
            // copy 1: items[0:1] -> newArrayDeque[0:1]
            // copy 2: items[2:3] -> newArrayDeque[6:7]
            //
            // nextFirst = 1, nextLast = 2
            // lengthBefore = 2, lengthAfter = 4 - 2 = 2
            // newLength - lengthAfter = 8 - 2 = 6
            int lengthBefore = nextFirst + 1;
            int lengthAfter = items.length - nextLast;
            java.lang.System.arraycopy(items, 0, newArrayDeque, 0, lengthBefore);
            java.lang.System.arraycopy(items, nextLast,
                    newArrayDeque, newLength - lengthAfter, lengthAfter);
            nextFirst = nextFirst + (newLength - items.length);
            nextLast = nextLast;
            items = newArrayDeque;
        } else if (nextFirst == items.length - 1 && nextLast == 0) {
            // [0, 1, 2, 3] --> [0, 1, 2, 3, _, _, _, _]
            //  ^l       ^f                  ^l       ^f
            java.lang.System.arraycopy(items, 0, newArrayDeque, 0, items.length);
            nextLast = nextLast + (newLength - items.length);
            nextFirst = newLength - 1;
            items = newArrayDeque;
        }
    }

    /** Make the array length smaller when not many elements in the array. */
    private void resizeSmaller(int newLength) {
        T[] newArrayDeque = (T[]) new Object[newLength];
        /*
         * Two cases:
         * 1. [a, _, _, _, _, _, _, a] --> [a, _, _, a]
         *        ^l             ^f            ^l ^f
         * 2. [_, _, _, a, a, _, _, _] --> [a, a, _, _]
         *           ^f       ^l                  ^l ^f
         */
        if (items[0] != null || items[items.length - 1] != null) { // case 1
            int lengthLeft = nextLast;
            int lengthRight = items.length - 1 - nextFirst;
            java.lang.System.arraycopy(items, 0, newArrayDeque, 0, lengthLeft);
            java.lang.System.arraycopy(items, nextFirst + 1,
                    newArrayDeque, newLength - lengthRight, lengthRight);
            nextFirst = nextFirst - (items.length - newLength);
            nextLast = nextLast;
            items = newArrayDeque;
        } else {
            java.lang.System.arraycopy(items, nextFirst + 1, newArrayDeque, 0, size);
            nextLast = size;
            nextFirst = newLength - 1;
            items = newArrayDeque;
        }
    }

    /** Adds an item of type T to the front of the Deque */
    public void addFirst(T item) {
        if (size == items.length) {
            resize(items.length * 2);
        }
        if (nextFirst == 0) {
            items[nextFirst] = item;
            nextFirst = items.length - 1;
        } else {
            items[nextFirst] = item;
            nextFirst = nextFirst - 1;
        }
        size = size + 1;
    }

    /** Adds an item of type T to the back of the Deque. */
    public void addLast(T item) {
        if (size == items.length) {
            resize(items.length * 2);
        }
        if (nextLast == items.length - 1) {
            items[nextLast] = item;
            nextLast = 0;
        } else {
            items[nextLast] = item;
            nextLast = nextLast + 1;
        }
        size = size + 1;
    }

    /** Returns true if deque is empty, false otherwise. */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns the number of items in the Deque. */
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last, separated by a space. */
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(items[(i + nextFirst + 1) % items.length] + " ");
        }
        System.out.println();
    }

    /** Removes and returns the item at the front of the deque.
     *  If no such item exists, returns null.
     */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        } else {
            T itemFirst = items[(nextFirst + 1) % items.length];
            items[(nextFirst + 1) % items.length] = null;
            nextFirst = (nextFirst + 1) % items.length;
            size = size - 1;
            if (items.length >= 16 && (double) size / items.length < FACTOR) {
                resize(items.length / 2);
            }
            return itemFirst;
        }
    }

    /** Removes and returns the item at the back of the deque.
     *  If no such item exists, returns null.
     */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        } else {
            if (nextLast == 0) {
                T itemLast = items[items.length - 1];
                items[items.length - 1] = null;
                nextLast = items.length - 1;
                size = size - 1;
                if (items.length >= 16 && (double) size / items.length < FACTOR) {
                    resize(items.length / 2);
                }
                return itemLast;
            } else {
                T itemLast = items[nextLast - 1];
                items[nextLast - 1] = null;
                nextLast = nextLast - 1;
                size = size - 1;
                if (items.length >= 16 && (double) size / items.length < FACTOR) {
                    resize(items.length / 2);
                }
                return itemLast;
            }
        }
    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null.
     * Must not alter the Deque!
     */
    public T get(int index) {
        return items[(index + nextFirst + 1) % items.length];
    }
}
