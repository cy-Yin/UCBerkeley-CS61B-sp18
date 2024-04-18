package hw2;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class TestPercolation {

    Percolation grid5 = new Percolation(5);

//    @Test
//    public void testXYTo1D() {
//        /*
//         * Considering n = 5
//         *    0  1  2  3  4
//         * 0 00 01 02 03 04
//         * 1 05 06 07 08 09
//         * 2 10 11 12 13 14
//         * 3 15 16 17 18 19
//         * 4 20 21 22 23 24
//         */
//        assertEquals(12, grid5.xyTo1D(2, 2));
//        assertEquals(14, grid5.xyTo1D(2, 4));
//        assertEquals(20, grid5.xyTo1D(4, 0));
//        assertEquals(18, grid5.xyTo1D(3, 3));
//        assertEquals(3, grid5.xyTo1D(0, 3));
//        assertEquals(6, grid5.xyTo1D(1, 1));
//        assertEquals(10, grid5.xyTo1D(2, 0));
//    }

//    @Test
//    public void testNeighbors() {
//        /** Considering n = 5
//         *     0  1  2  3  4
//         *  0 00 01 02 03 04
//         *  1 05 06 07 08 09
//         *  2 10 11 12 13 14
//         *  3 15 16 17 18 19
//         *  4 20 21 22 23 24
//         *  the neighbors of 12 are 7, 11, 13, 17
//         *  the neighbors of 14 are 9, 13, 19
//         *  the neighbors of 20 are 15, 21
//         */
//        int[] actual12 = grid5.neighbors(2, 2);
//        Arrays.sort(actual12);
//        assertArrayEquals(new int[]{7, 11, 13, 17}, actual12);
//
//        int[] actual14 = grid5.neighbors(2, 4);
//        Arrays.sort(actual14);
//        assertArrayEquals(new int[]{9, 13, 19}, actual14);
//
//        int[] actual03 = grid5.neighbors(0, 3);
//        Arrays.sort(actual03);
//        assertArrayEquals(new int[]{2, 4, 8}, actual03);
//
//        int[] actual05 = grid5.neighbors(1, 0);
//        Arrays.sort(actual05);
//        assertArrayEquals(new int[]{0, 6, 10}, actual05);
//
//        int[] actual21 = grid5.neighbors(4, 1);
//        Arrays.sort(actual21);
//        assertArrayEquals(new int[]{16, 20, 22}, actual21);
//
//        int[] actual20 = grid5.neighbors(4, 0);
//        Arrays.sort(actual20);
//        assertArrayEquals(new int[]{15, 21}, actual20);
//
//        int[] actual00 = grid5.neighbors(0, 0);
//        Arrays.sort(actual00);
//        assertArrayEquals(new int[]{1, 5}, actual00);
//
//        int[] actual04 = grid5.neighbors(0, 4);
//        Arrays.sort(actual04);
//        assertArrayEquals(new int[]{3, 9}, actual04);
//
//        int[] actual24 = grid5.neighbors(4, 4);
//        Arrays.sort(actual24);
//        assertArrayEquals(new int[]{19, 23}, actual24);
//    }
}
