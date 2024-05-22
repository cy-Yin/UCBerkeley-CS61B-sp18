import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Stack;
import java.util.Comparator;

public class Boggle {
    // Words must be at least three letters long.
    private static final int MIN_WORD_LENGTH = 3;
    
    // File path of dictionary file
    static String dictPath = "words.txt";
//    static String dictPath = "trivial_words.txt";

    private static char[][] board;
    private static TrieST dictTrie = new TrieST();

    private static Stack<State> stack = new Stack<>();
    /** The priority queue sort the elements in descending order of length.
     *  If multiple words have the same length, then sort them in ascending alphabetical order.
     */
    private static TreeSet<String> set = new TreeSet<>(new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            if (o1.length() != o2.length()) {
                return -Integer.compare(o1.length(), o2.length());
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

//        /* Words may not use the same letter cube more than once per word
//         * marked 2D array is used to guarantee the has-been-marked cube
//         * will never be considered before completing searching the current word.
//         */
//        boolean[][] marked = new boolean[m][n];

        for (int i = 0; i < m; i += 1) {
            for (int j = 0; j < n; j += 1) {
                // add all words starting with the char board[i][j] in the dictionary
                // to the priority queue.
//                String wordPrefix = "";
//                addWordsContainingGivenCharRecursively(wordPrefix, marked, i, j, m, n);
                addWordWithGivenStart(i, j);
            }
        }

        // returns the first k strings in the sorted priority queue.
        List<String> firstKWordsToReturn = new ArrayList<>();
        while (firstKWordsToReturn.size() < k && !set.isEmpty()) {
            firstKWordsToReturn.add(set.pollFirst());
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
        if (!new File(dictionaryPath).exists()) {
            throw new IllegalArgumentException();
        }
        TrieST dict = new TrieST();
        String[] dictWords = new In(dictionaryPath).readAllLines();
        for (String dictWord : dictWords) {
            dict.add(dictWord);
        }
        return dict;
    }

    private static class State {
        private int x;
        private int y;
        private List<State> markedStates;
        private String prefix;
        private List<State> neighbors;

        private State(int x, int y, List<State> markedStates, String prefix) {
            this.x = x;
            this.y = y;
            // this.markedStates contains all the marked states from the start State,
            // including itself.
            this.markedStates = new ArrayList<>(markedStates);
            this.markedStates.add(this);
            this.prefix = prefix + board[x][y];
            this.neighbors = new ArrayList<>();
        }

        public String getPrefix() {
            return prefix;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            State so = (State) o;
            return x == so.x && y == so.y;
        }

        @Override
        public int hashCode() {
            return x * board.length + y;
        }

        public List<State> getUnMarkedNeighbors() {
            for (int nx = x - 1; nx <= x + 1; nx += 1) {
                for (int ny = y - 1; ny <= y + 1; ny += 1) {
                    if ((nx >= 0 && nx < board.length && ny >= 0 && ny < board[0].length)) {
                        State ns = new State(nx, ny, markedStates, prefix);
                        neighbors.add(ns);
                    }
                }
            }

            // Pruning: we only care about the unmarked neighbors
            List<State> unMarkedNeighbors = new ArrayList<>();
            for (State ns : neighbors) {
                if (!markedStates.contains(ns)) {
                    unMarkedNeighbors.add(ns);
                }
            }
            return unMarkedNeighbors;
        }
    }

    /** Adds the valid (shown in the dictionary) word
     * with the start board[i][j] to the priority queue.
     */
    private static void addWordWithGivenStart(int x, int y) {
        String prefixAtStart = "";
        List<State> markedStatesAtStart = new ArrayList<>();
        stack.add(new State(x, y, markedStatesAtStart, prefixAtStart));
        while (!stack.isEmpty()) {
            State state = stack.pop();
            String prefix = state.getPrefix();
            if (dictTrie.hasPrefix(prefix)) {
                for (State ns : state.getUnMarkedNeighbors()) {
                    stack.push(ns);
                }
                if (prefix.length() >= MIN_WORD_LENGTH && dictTrie.contains(prefix)
                                                                && !set.contains(prefix)) {
                    set.add(prefix);
                }
            }
        }
    }

//    private static void addWordsContainingGivenCharRecursively(String prefix,
//                                                    boolean[][] marked,
//                                                    int x, int y,
//                                                    int boardWidth, int boardHeight) {
//        if ((x < 0 || x > boardWidth - 1 || y < 0 || y > boardHeight - 1)
//            || marked[x][y] || !dictTrie.hasPrefix(prefix)) {
//            return;
//        }
//
//        prefix += board[x][y];
//        String tempWord = prefix;
//        marked[x][y] = true;
//
//        if (tempWord.length() >= MIN_WORD_LENGTH && dictTrie.contains(tempWord)) {
//            // the temp word is valid, add it to the priority queue
//            set.add(tempWord);
//        }
//
//        /* consider the neighbors of board[x, y]
//         * Here neighbors are those horizontally, vertically, and diagonally neighboring.
//         *
//         * *     *     *
//         * *   (i, j)  *
//         * *     *     *
//         * totally 8 neighbors.
//         */
//        for (int i = -1; i <= 1; i += 1) {
//            for (int j = -1; j <= 1; j += 1) {
//                int neighborX = x + i;
//                int neighborY = y + j;
//                addWordsContainingGivenCharRecursively(prefix, marked, neighborX, neighborY,
//                                                            boardWidth, boardHeight);
//            }
//        }
//        marked[x][y] = false;
//    }

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
        assertTrue(dict.contains("American's"));

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
        assertArrayEquals(expected1, actualArray1);
    }

    @Test
    public void testSolveWithTrivialDict() {
        dictPath = "trivial_words.txt";
        List<String> actualList2 = solve(20, "exampleBoard2.txt");
        String[] actualArray2 = actualList2.toArray(new String[actualList2.size()]);
        String[] expected2 = new String[]{"aaaaa", "aaaa"};
        assertArrayEquals(expected2, actualArray2);
    }

    public static void main(String[] args) {
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
