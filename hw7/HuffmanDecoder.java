public class HuffmanDecoder {
    /** open the file given as the 0th command line argument {@code args[0]},
     * decode it, and write a new file with the name given as {@code args[1]}.
     * For example
     * {@code java HuffmanDecoder watermelonsugar.txt.huf originalwatermelon.txt}
     * should decode the contents of watermelonsugar.txt.huf
     * and write them into originalwatermelon.txt. */
    public static void main(String[] args) {
        /* Huffman decoding process:
         * 1: Read the Huffman coding trie.
         * 2: If applicable, read the number of symbols.
         * 3: Read the massive bit sequence corresponding to the original txt.
         * 4: Repeat until there are no more symbols:
         *      4a: Perform a longest prefix match on the massive sequence.
         *      4b: Record the symbol in some data structure.
         *      4c: Create a new bit sequence containing the remaining unmatched bits.
         * 5: Write the symbols in some data structure to the specified file.
         */
        ObjectReader objectReader = new ObjectReader(args[0]);
        BinaryTrie binaryDecodeTrie = (BinaryTrie) objectReader.readObject();
        int charsLength = (int) objectReader.readObject();
        BitSequence hugeBitSequence = (BitSequence) objectReader.readObject();

        char[] symbols = new char[charsLength];
        for (int i = 0; i < charsLength; i += 1) {
            Match match = binaryDecodeTrie.longestPrefixMatch(hugeBitSequence);
            symbols[i] = match.getSymbol();
            hugeBitSequence = hugeBitSequence.allButFirstNBits(match.getSequence().length());
        }

        FileUtils.writeCharArray(args[1], symbols);
    }
}
