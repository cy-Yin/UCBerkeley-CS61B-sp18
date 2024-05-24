import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {
    /** Maps characters to their counts according to the char[] input. */
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> map = new HashMap<>();
        for (Character symbol : inputSymbols) {
            if (!map.containsKey(symbol)) {
                map.put(symbol, 1);
            } else {
                int count = map.get(symbol);
                map.put(symbol, count + 1);
            }
        }
        return map;
    }

    /** Opens the file given as the 0th command line argument {@code args[0]},
     * and write a new file with the name args[0] + ".huf"
     * that contains a huffman encoded version of the original file.
     * For example {@code java HuffmanEncoder watermelonsugar.txt}
     * should generate a new Huffman encoded version of watermelonsugar.txt
     * that contains watermelonsugar.txt.huf.
     */
    public static void main(String[] args) {
        /* Huffman encoding process:
         * 1: Read the file as 8 bit symbols.
         * 2: Build frequency table.
         * 3: Use frequency table to construct a binary decoding trie.
         * 4: Write the binary decoding trie to the .huf file.
         * 5: (optional: write the number of symbols to the .huf file)
         * 6: Use binary trie to create lookup table for encoding.
         * 7: Create a list of bitSequences.
         * 8: For each 8 bit symbol:
         *      Lookup that symbol in the lookup table.
         *      Add the appropriate bit sequence to the list of bitSequences.
         * 9: Assemble all bit sequences into one huge bit sequence.
         * 10: Write the huge bit sequence to the .huf file.
         */
        char[] inputSymbols = FileUtils.readFile(args[0]);
        Map<Character, Integer> frequencyTable = buildFrequencyTable(inputSymbols);
        BinaryTrie binaryDecodeTrie = new BinaryTrie(frequencyTable);
        Map<Character, BitSequence> bitSequenceTable = binaryDecodeTrie.buildLookupTable();
        List<BitSequence> lstBitSequences = new ArrayList<>();
        for (char symbol : inputSymbols) {
            BitSequence bitSequence = bitSequenceTable.get(symbol);
            lstBitSequences.add(bitSequence);
        }
        BitSequence hugeBitSequence = BitSequence.assemble(lstBitSequences);

        // write 1. binary decoding trie; 2. number of symbols; 3. the huge bit sequence
        // to the ".huf" file.
        ObjectWriter objectWriter = new ObjectWriter(args[0] + ".huf");
        objectWriter.writeObject(binaryDecodeTrie);
        objectWriter.writeObject(inputSymbols.length);
        objectWriter.writeObject(hugeBitSequence);
    }
}
