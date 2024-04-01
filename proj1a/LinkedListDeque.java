public class LinkedListDeque<T> {
    private class LinkedNode {
        public LinkedNode previous;
        public T current;
        public LinkedNode next;

        public LinkedNode(LinkedNode pre, T cur, LinkedNode nex) {
            previous = pre;
            current = cur;
            next = nex;
        }
    }

    private LinkedNode sentinel; // use circular sentinel topology
    private int size; // Store size, so getting size takes constant time.

    /** Create an empty LinkedListDeque */
    public LinkedListDeque() {
        sentinel = new LinkedNode(null, null, null);
        sentinel.previous = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    /** Adds an item of type T to the front of the Deque */
    public void addFirst(T item) {
        LinkedNode newNode = new LinkedNode(sentinel, item, sentinel.next);
        sentinel.next.previous = newNode; // The prev of original first item is the new item.
        sentinel.next = newNode;
        size = size + 1;
    }

    /** Adds an item of type T to the back of the Deque. */
    public void addLast(T item) {
        LinkedNode newNode = new LinkedNode(sentinel.previous, item, sentinel);
        sentinel.previous.next = newNode;
        sentinel.previous = newNode;
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
        LinkedNode p = sentinel;
        while (p.next != sentinel) {
            System.out.print(p.next.current + " ");
            p = p.next;
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
            T item = sentinel.next.current;
            sentinel.next = sentinel.next.next;
            sentinel.next.previous = sentinel;
            size = size - 1;
            return item;
        }
    }

    /** Removes and returns the item at the back of the deque.
     *  If no such item exists, returns null.
     */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        } else {
            T item = sentinel.previous.current;
            sentinel.previous = sentinel.previous.previous;
            sentinel.previous.next = sentinel;
            size = size - 1;
            return item;
        }
    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null.
     * Must not alter the Deque!
     */
    public T get(int index) {
        LinkedNode getNode = sentinel.next;
        if (index > size - 1) {
            return null;
        } else {
            int i = 0;
            while (i < index) {
                getNode = getNode.next;
                i = i + 1;
            }
            return getNode.current;
        }
    }

    /** Gets the item at the given index, same as get().
     *  But use recursive.
     */
    public T getRecursive(int index) {
        if (index > size - 1) {
            return null;
        } else {
            return getRecursiveHelper(sentinel.next, index);
        }
    }

    /** The helper method of getRecursive method. */
    private T getRecursiveHelper(LinkedNode getNode, int index) {
        if (index == 0) {
            return getNode.current;
        } else {
            return getRecursiveHelper(getNode.next, index - 1);
        }
    }
}
