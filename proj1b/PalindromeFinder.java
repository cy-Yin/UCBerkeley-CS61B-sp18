/** This class outputs all palindromes in the words file in the current directory. */
public class PalindromeFinder {

    public static void main(String[] args) {
        int minLength = 4;
        Palindrome palindrome = new Palindrome();

        System.out.println("All the palindromes are as follows.");
        In inEqual = new In("../library-sp18/data/words.txt");
        while (!inEqual.isEmpty()) {
            String word = inEqual.readString();
            if (word.length() >= minLength && palindrome.isPalindrome(word)) {
                System.out.println(word);
            }
        }

        System.out.println();
        System.out.println("All the off-by-1 palindromes are as follows.");
        In inOffByOne = new In("../library-sp18/data/words.txt");
        while (!inOffByOne.isEmpty()) {
            String word = inOffByOne.readString();
            OffByOne obo = new OffByOne();
            if (word.length() >= minLength && palindrome.isPalindrome(word, obo)) {
                System.out.println(word);
            }
        }

        System.out.println();
        System.out.println("All the off-by-4 palindromes are as follows.");
        In inOffBy4 = new In("../library-sp18/data/words.txt");
        while (!inOffBy4.isEmpty()) {
            String word = inOffBy4.readString();
            OffByN ob4 = new OffByN(4);
            if (word.length() >= minLength && palindrome.isPalindrome(word, ob4)) {
                System.out.println(word);
            }
        }
    }
    // Uncomment this class once you've written isPalindrome.
}
