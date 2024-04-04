public class OffByOne implements CharacterComparator {

    /** Returns true for characters that are different by exactly one, false otherwise. */
    @Override
    public boolean equalChars(char x, char y) {
        return (x - y == 1) || (x - y == -1);
    }
}
