import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }
    // Uncomment this class once you've created your Palindrome class.

    @Test
    public void testisPalindrome() {
        String word1 = "";
        assertTrue("A word with length 0 is a palindrome.", palindrome.isPalindrome(word1));

        String word2 = "a";
        assertTrue("A word with length 1, such as "
                + word2 + ", is a palindrome.", palindrome.isPalindrome(word2));
        String word3 = "A";
        assertTrue("A word with length 1, such as "
                + word3 + ", is a palindrome.", palindrome.isPalindrome(word3));

        String word4 = "rancor";
        assertFalse(word4 + " is not a palindrome.", palindrome.isPalindrome(word4));
        String word5 = "Racecar"; // The uppercase letter is not equal to the lowercase letter.
        assertFalse(word5 + " is not a palindrome.", palindrome.isPalindrome(word5));

        String word6 = "racecar";
        assertTrue(word6 + " is a palindrome.", palindrome.isPalindrome(word6));
        String word7 = "noon";
        assertTrue(word7 + " is a palindrome.", palindrome.isPalindrome(word7));
    }

    @Test
    public void testisOffByOnePalindrome() {
        CharacterComparator obo = new OffByOne();
        assertTrue(palindrome.isPalindrome("", obo));
        assertTrue(palindrome.isPalindrome("a", obo));
        assertTrue(palindrome.isPalindrome("&a%", obo));
        assertTrue(palindrome.isPalindrome("flake", obo));
        assertFalse(palindrome.isPalindrome("racecar", obo));
        assertFalse(palindrome.isPalindrome("noon", obo));
    }
}
