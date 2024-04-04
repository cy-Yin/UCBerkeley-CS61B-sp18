public class OffByN implements CharacterComparator {
    private int n;

    public OffByN(int N) {
        n = N;
    }

    /** Returns true for characters that are different by exactly N, false otherwise. */
    @Override
    public boolean equalChars(char x, char y) {
        return (x - y == n) || (x - y == -n);
    }
}
