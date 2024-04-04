import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    @Test
    public void testOffByFive() {
        OffByN offBy5 = new OffByN(5);
        assertTrue(offBy5.equalChars('a', 'f')); // 'a' - 'f' = -5
        assertTrue(offBy5.equalChars('f', 'a')); // 'f' - 'a' = 5
        assertFalse(offBy5.equalChars('f', 'h'));
    }
}
