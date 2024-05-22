import java.util.HashMap;
import java.util.Map;

/**
 *  The {@code TrieST} class represents an ordered set of strings over
 *  the extended ASCII alphabet.
 *
 *  @source
 *  <a href="https://algs4.cs.princeton.edu/52trie">Section 5.2</a> of
 *  <i>Algorithms in Java, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class TrieST {
    private Node root;      // root of trie

    // R-way trie node
    private static class Node {
        private Map<Character, Node> next = new HashMap<>();
        private boolean isString;
    }

    /**
     * Initializes an empty set of strings.
     */
    public TrieST() {
        root = new Node();
    }

    /**
     * Does the set contain the given key?
     * @param key the key
     * @return {@code true} if the set contains {@code key} and
     *     {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(String key) {
        Node cur = root;
        for (char c : key.toCharArray()) {
            cur = cur.next.get(c);
            if (cur == null) {
                return false;
            }
        }
        return cur.isString;
    }

    /**
     * Adds the key to the set if it is not already present.
     * @param key the key to add
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void add(String key) {
        Node cur = root;
        for (char c : key.toCharArray()) {
            cur.next.putIfAbsent(c, new Node());
            cur = cur.next.get(c);
        }
        cur.isString = true;
    }

    /**
     * Returns true if the string has the {@code prefix}, and false otherwise.
     * @param prefix the prefix
     */
    public boolean hasPrefix(String prefix) {
        Node cur = root;
        for (char c : prefix.toCharArray()) {
            cur = cur.next.get(c);
            if (cur == null) {
                return false;
            }
        }
        return true;
    }
}
