import java.util.Arrays;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // Implement LSD Sort
        String[] sortedAsciis = asciis.clone();

        // find the max length of strings in asciis.
        int maxLength = Integer.MIN_VALUE;
        for (String s : asciis) {
            maxLength = maxLength > s.length() ? maxLength : s.length();
        }

        for (int i = maxLength - 1; i >= 0; i -= 1) {
            sortHelperLSD(sortedAsciis, i);
        }

//        sortHelperMSD(sortedAsciis, 0, asciis.length, 0);

        return sortedAsciis;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        String[] asciisSortedWithIndex = new String[asciis.length];

        // ASCII strings have 256 possible characters: 0 to 255 (with null as 0)
        int totalAsciis = 256;
        // gather all the counts for each value
        int[] counts = new int[totalAsciis];
        for (String s : asciis) {
            int i = (int) getCharAt(s, index);
            counts[i]++;
        }

        // counting sort that uses start position calculation
        int[] starts = new int[totalAsciis];
        int pos = 0;
        for (int i = 0; i < starts.length; i += 1) {
            starts[i] = pos;
            pos += counts[i];
        }

        for (int i = 0; i < asciis.length; i += 1) {
            int itemIndex = (int) getCharAt(asciis[i], index);
            int place = starts[itemIndex];
            asciisSortedWithIndex[place] = asciis[i];
            starts[itemIndex] += 1;
        }

        System.arraycopy(asciisSortedWithIndex, 0, asciis, 0, asciis.length);
    }

    /** Returns the character at the index of the string. */
    private static Character getCharAt(String s, int index) {
        if (index < 0 || index >= s.length()) {
            return 0; // (int) null equals to 0
        }
        return s.charAt(index);
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        int maxLength = Integer.MIN_VALUE;

        for (int i = start; i < end; i++) {
            maxLength = maxLength > asciis[i].length() ? maxLength : asciis[i].length();
        }

        if (end <= start || index >= maxLength) {
            return;
        }

        // Counting sort to sort characters at current index
        int[] count = new int[256]; // Assuming ASCII characters
        for (int i = start; i < end; i++) {
            int charIndex = (int) getCharAt(asciis[i], index);
            count[charIndex]++;
        }

        int[] starts = new int[256];
        int pos = 0;
        for (int i = 0; i < 256; i++) {
            starts[i] = pos;
            pos += count[i];
        }

        String[] aux = new String[end - start];
        for (int i = start; i < end; i++) {
            int charIndex = (int) getCharAt(asciis[i], index);
            int place = starts[charIndex];
            aux[place] = asciis[i];
            starts[charIndex]++;
        }

        // Copy sorted strings back to original array
        System.arraycopy(aux, 0, asciis, start, end - start);

        // Recursively sort each group
        int currentStart = start;
        for (int i = 0; i < 256; i++) {
            int currentEnd = currentStart + count[i];
            sortHelperMSD(asciis, currentStart, currentEnd, index + 1);
            currentStart = currentEnd;
        }
    }

//    public static void main(String[] args) {
//        String[] arr = new String[]{"trh5qwe", "12341234", "985634", "25sf", "a"};
////        String[] arr = new String[]{"       ", "    ", " "};
//        String[] sortedStd = arr.clone();
//        Arrays.sort(sortedStd);
//        System.out.println("Sorted result given by Arrays.sort()");
//        for (String s : sortedStd) {
//            System.out.print(s + " ");
//        }
//        System.out.println();
//
//        String[] mySorted = arr.clone();
//        mySorted = sort(mySorted);
//        System.out.println("Sorted result given by my implemented radix sort");
//        for (String s : mySorted) {
//            System.out.print(s + " ");
//        }
//    }
}
