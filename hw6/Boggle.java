import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Boggle {
    // Words must be at least three letters long.
    private static final int MIN_WORD_LENGTH = 3;
    
    // File path of dictionary file
    static String dictPath = "words.txt";

    private static char[][] board;
    private static TrieST dictTrie = new TrieST();

    /** The priority queue sort the elements in descending order of length.
     *  If multiple words have the same length, then sort them in ascending alphabetical order.
     */
    private static PriorityQueue<String> pq = new PriorityQueue<>(new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            if (o1.length() != o2.length()) {
                if (o1.length() > o2.length()) {
                    return -1;
                } else { // o1.length() < o2.length()
                    return 1;
                }
            } else { // o1 and o2 have the same length
                return o1.compareTo(o2);
            }
        }
    });

    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        // YOUR CODE HERE
        if (k <= 0) {
            throw new IllegalArgumentException();
        }

        board = readBoardFromFile(boardFilePath);
        dictTrie = readDictFromFile(dictPath);
        int m = board.length;
        int n = board[0].length;

        /* Words may not use the same letter cube more than once per word
         * marked 2D array is used to guarantee the has-been-marked cube
         * will never be considered before completing searching the current word.
         */
        boolean[][] marked = new boolean[m][n];

        for (int i = 0; i < m; i += 1) {
            for (int j = 0; j < n; j += 1) {
                // add all words starting with the char board[i][j] in the dictionary
                // to the priority queue.
                String wordPrefix = "";
                addWordsContainingGivenChar(wordPrefix, marked, i, j, m, n);
            }
        }

        // returns the first k strings in the sorted priority queue.
        List<String> firstKWordsToReturn = new ArrayList<>();
        while (firstKWordsToReturn.size() < k && !pq.isEmpty()) {
            firstKWordsToReturn.add(pq.poll());
        }

        return firstKWordsToReturn;
    }

    /** Reads and returns the 2-dimensional board from the file path. */
    private static char[][] readBoardFromFile(String boardFilePath) {
        if (!new File(boardFilePath).exists()) {
            throw new IllegalArgumentException();
        }
        String[] lines = new In(boardFilePath).readAllLines();
        int m = lines.length;
        int n = lines[0].length();
        char[][] boardFromFile = new char[m][n];
        for (int i = 0; i < m; i += 1) {
            if (lines[i].length() != n) {
                throw new IllegalArgumentException();
            }
            for (int j = 0; j < n; j += 1) {
                boardFromFile[i][j] = lines[i].charAt(j);
            }
        }
        return boardFromFile;
    }

    /** Reads and returns the dictionary trie from the dictionary file path. */
    private static TrieST readDictFromFile(String dictionaryPath) {
        TrieST dict = new TrieST();
        String[] dictWords = new In(dictionaryPath).readAllLines();
        for (String dictWord : dictWords) {
            dictWord = dictWord.toLowerCase();
            dict.add(dictWord);
        }
        return dict;
    }

    private static void addWordsContainingGivenChar(String prefix,
                                                    boolean[][] marked,
                                                    int x, int y,
                                                    int boardWidth, int boardHeight) {
        if ((x < 0 || x > boardWidth - 1 || y < 0 || y > boardHeight - 1)
            || marked[x][y] || !dictTrie.hasPrefix(prefix)) {
            return;
        }

        prefix += board[x][y];
        String tempWord = prefix;
        marked[x][y] = true;

        if (tempWord.length() > MIN_WORD_LENGTH && dictTrie.contains(tempWord)
                                                        && !pq.contains(tempWord)) {
            // the temp word is valid, add it to the priority queue
            pq.add(tempWord);
        }

        /* consider the neighbors of board[x, y]
         * Here neighbors are those horizontally, vertically, and diagonally neighboring.
         *
         * *     *     *
         * *   (i, j)  *
         * *     *     *
         * totally 8 neighbors.
         */
        for (int i = -1; i <= 1; i += 1) {
            for (int j = -1; j <= 1; j += 1) {
                int neighborX = x + i;
                int neighborY = y + j;
                addWordsContainingGivenChar(prefix, marked, neighborX, neighborY,
                                                            boardWidth, boardHeight);
            }
        }
        marked[x][y] = false;
    }

    @Test
    public void testReadBoardFromFile() {
        char[][] excepted1 = {
                {'n', 'e', 's', 's'},
                {'t', 'a', 'c', 'k'},
                {'b', 'm', 'u', 'h'},
                {'e', 's', 'f', 't'}
        };
        char[][] actual1 = readBoardFromFile("exampleBoard.txt");
        assertArrayEquals(excepted1, actual1);

        char[][] excepted2 = {
                {'b', 'a', 'a'},
                {'a', 'b', 'a'},
                {'a', 'a', 'b'},
                {'b', 'a', 'a'}
        };
        char[][] actual2 = readBoardFromFile("exampleBoard2.txt");
        assertArrayEquals(excepted1, actual1);
    }

    @Test
    public void testReadDictFromFile() {
        TrieST dict = readDictFromFile("words.txt");
        assertTrue(dict.contains("thumbtacks"));
        assertTrue(dict.contains("british"));
        assertFalse(dict.contains("BirthDay")); // all words in dictTrie must be lowercase.
        assertTrue(dict.contains("american's"));

        TrieST trivialDict = readDictFromFile("trivial_words.txt");
        assertTrue(trivialDict.contains("aaaaa"));
        assertTrue(trivialDict.contains("aaaa"));
        assertFalse(trivialDict.contains("bbb"));
        assertTrue(trivialDict.hasPrefix("aaa"));
        assertTrue(trivialDict.hasPrefix("aaaa"));
    }

    @Test
    public void testSolve() {
        dictPath = "words.txt";
        List<String> actualList1 = solve(7, "exampleBoard.txt");
        String[] actualArray1 = actualList1.toArray(new String[actualList1.size()]);
        String[] expected1 = new String[]
            {"thumbtacks", "thumbtack", "setbacks", "setback", "ascent", "humane", "smacks"};
        assertArrayEquals(actualArray1, expected1);

        dictPath = "trivial_words.txt";
        List<String> actualList2 = solve(7, "exampleBoard2.txt");
        String[] actualArray2 = actualList1.toArray(new String[actualList2.size()]);
        String[] expected2 = new String[]
            {"thumbtacks", "thumbtack", "setbacks", "setback", "ascent", "humane", "smacks"};
        assertArrayEquals(actualArray2, expected2);
    }

    public static void main(String[] args) {
        dictPath = "words.txt";

        // test the run time
        char[][] board1 = readBoardFromFile("smallBoard.txt");
        System.out.println("Size of board: " + board1.length + " x " + board1[0].length);
        long start1 = System.currentTimeMillis();
        List<String> res1 = solve(7, "smallBoard.txt");
        long end1 = System.currentTimeMillis();
        long timeSpan1 = end1 - start1;
        System.out.println(res1.toString());
        System.out.println("time to solve the boggle: " + timeSpan1);
        System.out.println();

        char[][] board2 = readBoardFromFile("smallBoard2.txt");
        System.out.println("Size of board: " + board2.length + " x " + board2[0].length);
        long start2 = System.currentTimeMillis();
        List<String> res2 = solve(7, "smallBoard2.txt");
        long end2 = System.currentTimeMillis();
        System.out.println(res2.toString());
        long timeSpan2 = end2 - start2;
        System.out.println("time to solve the boggle: " + timeSpan2);
        System.out.println();

        int sizeRatio = (board1.length / board2.length) * (board1[0].length / board2[0].length);
        System.out.println("board1 is about x" + sizeRatio + " larger(smaller) than board2");
        System.out.println("Linearly extrapolating from the board2 runtime, "
                + "we would expect a runtime of "
                + timeSpan2 + " x " + sizeRatio + " = " + (timeSpan2 * sizeRatio));
    }
}
