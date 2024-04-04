public class Palindrome {
    /** Returns a Deque where the characters appear in the same order as in the String. */
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> wordDeque = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i += 1) {
            // String.charAt(int i) method is able to get the i-th character in a String.
            wordDeque.addLast(word.charAt(i));
        }
        return wordDeque;
    }

    /** Returns true if the given word is a palindrome, and false otherwise.
     *  Uses recursion. Really a beautiful solution compared to a loop!
     */
    public boolean isPalindrome(String word) {
        if (word.isEmpty() || word.length() == 1) {
            // Any word of length 0 or 1 is a palindrome.
            return true;
        } else {
            Deque<Character> wordDeque = wordToDeque(word);
            Character firstChar = wordDeque.removeFirst();
            Character lastChar = wordDeque.removeLast();
            if (!firstChar.equals(lastChar)) {
                return false;
            }
            String wordRemoved = dequeToWord(wordDeque);
            return isPalindrome(wordRemoved);
        }
    }

    /** Returns a String where the characters appear in the same order as in the Deque. */
    private String dequeToWord(Deque<Character> wordDeque) {
        String dequeWord = "";
        while (!wordDeque.isEmpty()) {
            dequeWord += wordDeque.removeFirst();
        }
        return dequeWord;
    }

    /** Returns true if the given word is a palindrome according to the character comparison test
     *  provided by the CharacterComparator passed in as argument cc, and false otherwise.
     */
    public boolean isPalindrome(String word, CharacterComparator cc) {
        if (word.isEmpty() || word.length() == 1) {
            // Any word of length 0 or 1 is a palindrome.
            return true;
        } else {
            Deque<Character> wordDeque = wordToDeque(word);
            Character firstChar = wordDeque.removeFirst();
            Character lastChar = wordDeque.removeLast();
            if (!cc.equalChars(firstChar, lastChar)) {
                return false;
            }
            String wordRemoved = dequeToWord(wordDeque);
            return isPalindrome(wordRemoved, cc);
        }
    }
}
