import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class MyTrie {
    private TrieNode root;
    private int size;

    private class TrieNode {
        private Map<Character, TrieNode> next;
        private boolean isTerminal; // isTerminal is true if the node is the end of name.
        private List<String> names;

        TrieNode() {
            next = new HashMap<>();
            isTerminal = false;
            names = new ArrayList<>();
        }
    }

    MyTrie() {
        root = new TrieNode();
        size = 0;
    }

    /** Returns how many names the trie contains. */
    public int size() {
        return size;
    }

    /** Adds the name to the trie. */
    public void add(String name) {
        TrieNode current = root;
        current.names.add(name);
        String cleanedName = GraphDB.cleanString(name);
        for (char c : cleanedName.toCharArray()) {
            if (!current.next.containsKey(c)) {
                current.next.put(c, new TrieNode());
            }
            current = current.next.get(c);
            current.names.add(name);

        }
        if (!current.isTerminal) {
            current.isTerminal = true;
            size += 1;
        }
    }

    /** Returns true if the trie contains the name, and false otherwise. */
    public boolean contain(String name) {
        TrieNode current = root;
        String cleanedName = GraphDB.cleanString(name);
        for (char c : cleanedName.toCharArray()) {
            if (!current.next.containsKey(c)) {
                return false;
            }
            current = current.next.get(c);
        }
        return current.isTerminal;
    }

    /** Returns true if there is at least one name having the given prefix. */
    public boolean isPrefix(String prefix) {
        TrieNode current = root;
        String cleanedPrefix = GraphDB.cleanString(prefix);
        for (char c : cleanedPrefix.toCharArray()) {
            if (!current.next.containsKey(c)) {
                return false;
            }
            current = current.next.get(c);
        }
        return true;
    }

    /** Returns the list of names which have the given prefix. */
    public List<String> namesWithPrefix(String prefix) {
        if (!isPrefix(prefix)) {
            return new ArrayList<>();
        }
        TrieNode current = root;
        String cleanedPrefix = GraphDB.cleanString(prefix);
        for (char c : cleanedPrefix.toCharArray()) {
            current = current.next.get(c);
        }

        List<String> names = current.names;
        Collections.sort(names);
        return names;
    }
}
