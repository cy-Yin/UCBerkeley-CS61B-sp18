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

        sortHelperLSD(sortedAsciis, maxLength);

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

        int radix = 256; // Assuming ASCII characters

        for (int i = index; i >= 0; i--) {
            // Perform counting sort based on the character at the current index
            int[] count = new int[radix + 1];
            int n = asciis.length;

            // Count frequencies of characters
            for (String s : asciis) {
                char c = (i < s.length()) ? s.charAt(i) : 0;
                count[c + 1]++;
            }

            // Compute cumulative counts
            for (int j = 0; j < radix; j++) {
                count[j + 1] += count[j];
            }

            // Copy to auxiliary array according to counts
            String[] aux = new String[n];
            for (String s : asciis) {
                char c = (i < s.length()) ? s.charAt(i) : 0;
                aux[count[c]++] = s;
            }

            // Copy sorted strings back to original array
            System.arraycopy(aux, 0, asciis, 0, n);
        }
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
            char c = (index < asciis[i].length()) ? asciis[i].charAt(index) : 0;
            count[c]++;
        }

        int[] starts = new int[256];
        int pos = 0;
        for (int i = 0; i < 256; i++) {
            starts[i] = pos;
            pos += count[i];
        }

        String[] aux = new String[end - start];
        for (int i = start; i < end; i++) {
            char c = (index < asciis[i].length()) ? asciis[i].charAt(index) : 0;
            int place = starts[c];
            aux[place] = asciis[i];
            starts[c]++;
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
//        String[] arr = new String[]
//                {"trh5qwe", "121234", "985634", "25sf", "a", "0", "", "sda413263"};
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
