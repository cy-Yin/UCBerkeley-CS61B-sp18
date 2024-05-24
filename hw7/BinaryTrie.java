import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/** BinaryTrie is built from a character sequence.
 *  For example, now there is a sequence in English: "abbccccdddddeeeeee".
 *  This file has 1 a, 2 b’s, 4 c’s, 5 d’s, and 6 e’s.
 *  The unique Huffman decoding trie for this file is as shown below.
 *  For example, the letter b corresponds to the binary sequence 001.
 *
 *            18
 *          0/  \1
 *          7    \
 *        0/ \1   \
 *        3   \    11
 *      0/ \1  \  0/ \1
 *      1  2   3  4   5
 *      a  b   c  d   e
 */
public class BinaryTrie implements Serializable {
    /** the root of the binary trie. */
    BinaryTrieNode root;

    /** Given a frequency table which maps symbols of type V to their relative frequencies,
     *  the constructor should build a Huffman decoding trie
     *  according to the procedure discussed in class.
     */
    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        PriorityQueue<BinaryTrieNode> pq = new PriorityQueue<>(new Comparator<BinaryTrieNode>() {
            @Override
            public int compare(BinaryTrieNode o1, BinaryTrieNode o2) {
                return Integer.compare(o1.frequency, o2.frequency);
            }
        });
        for (Character c : frequencyTable.keySet()) {
            pq.add(new BinaryTrieNode(frequencyTable.get(c), c, null, null));
        }

        while (pq.size() > 1) {
            BinaryTrieNode left = pq.poll();
            BinaryTrieNode right = pq.poll();
            int parentFrequency = left.frequency + right.frequency;
            BinaryTrieNode parent = new BinaryTrieNode(parentFrequency, null, left, right);
            pq.add(parent);
        }
        root = pq.poll();
    }

    private class BinaryTrieNode implements Serializable{
        int frequency;
        Character character;
        BinaryTrieNode left;
        BinaryTrieNode right;

        BinaryTrieNode(int frequency, Character character,
                       BinaryTrieNode left, BinaryTrieNode right) {
            this.frequency = frequency;
            this.character = character;
            this.left = left;
            this.right = right;
        }

        /** Returns true if the node is a leaf of the binary trie, and false otherwise.
         *  Only a leaf of a binary trie has a "meaningful" character and can be obtained.
         */
        private boolean isLeaf() {
            return left == null && right == null;
        }
    }

    /** Finds the longest prefix that matches the given {@code querySequence}
     *  and returns a Match object for that Match.
     *
     * @source querySequence which is an argument objects of type BitSequence <br>
     * For example, for the example Trie given in the introduction,
     * if we call trie.longestPrefixMatch(new BitSequence("0011010001")),
     * then we will get back a Match object containing b as the symbol
     * and 001 as the BitSequence.
     * The method is called longestPrefixMatch
     * because 001 is the longest prefix of 0011010001
     * that is a match inside our decoding binary trie.
     */
    public Match longestPrefixMatch(BitSequence querySequence) {
        if (querySequence == null) {
            return null;
        }

        BinaryTrieNode queryNode = root;
        BitSequence returnedSeq = new BitSequence();
        for (int i = 0; i < querySequence.length(); i += 1) {
            if (queryNode.isLeaf()) {
                break;
            } else {
                if (querySequence.bitAt(i) == 0) {
                    queryNode = queryNode.left;
                    returnedSeq = returnedSeq.appended(0);
                } else if (querySequence.bitAt(i) == 1) {
                    queryNode = queryNode.right;
                    returnedSeq = returnedSeq.appended(1);
                } else { // For a binary composing system, bits will only be 0 or 1.
                    throw new IllegalArgumentException();
                }
            }
        }
        return new Match(returnedSeq, queryNode.character);
    }

    /** Returns the inverse of the coding trie. */
    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> returnedMap = new HashMap<>();
        BitSequence bitSequence = new BitSequence();
        buildLookupTableHelper(returnedMap, root, bitSequence);

        return returnedMap;
    }

    /** Searches the binary tire from the current {@code node}
     *  and adds the characters mapped with its bitSequence to the {@code map}.
     *  Helper function for the {@code buildLookupTable()} method.
     */
    private void buildLookupTableHelper(Map<Character, BitSequence> map, BinaryTrieNode node,
                                                                    BitSequence bitSequence) {
        if (node.isLeaf()) {
            map.put(node.character, bitSequence);
        } else {
            /* When the node search for its left child, the left child bitSequence will add 0;
             * When the node search for its right child, the right child bitSequence will add 1;
             */
            buildLookupTableHelper(map, node.left, bitSequence.appended(0));
            buildLookupTableHelper(map, node.right, bitSequence.appended(1));
        }
    }
}
