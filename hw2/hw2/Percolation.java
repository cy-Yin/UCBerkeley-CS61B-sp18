package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    int n;
    boolean[][] grid;
    int numOpenSites;
    int virtualTopSite;
    int virtualBottomSite;
    WeightedQuickUnionUF topSet;
    WeightedQuickUnionUF bottomSet;
    WeightedQuickUnionUF gridSet;

    /** Creates N-by-N grid, with all sites initially blocked. */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        n = N;
        grid = new boolean[n][n];
        for (int i = 0; i < n; i += 1) {
            for (int j = 0; j < n; j += 1) {
                grid[i][j] = false;
            }
        }
        numOpenSites = 0;

        virtualTopSite = n * n;
        virtualBottomSite = n * n + 1;
        int totalNumSites = n * n + 2; // all grids with 2 virtual sites
        topSet = new WeightedQuickUnionUF(totalNumSites);
        bottomSet = new WeightedQuickUnionUF(totalNumSites);
        gridSet = new WeightedQuickUnionUF(totalNumSites);
    }

    /** Coverts the 2D (row, col) coordinate to the int offset. */
    int xyTo1D(int row, int col) {
        return row * n + col;
    }

    /** Open the site (row, col) if it is not open already. */
    public void open(int row, int col) {
        if (row < 0 || row > n - 1 || col < 0 || col > n - 1) {
            throw new IndexOutOfBoundsException();
        }

        if (isOpen(row, col)) {
            return;
        }

        grid[row][col] = true;

        if (row == 0) {
            topSet.union(virtualTopSite, xyTo1D(row, col));
            gridSet.union(virtualTopSite, xyTo1D(row, col));
        } else if (row == n - 1) {
            bottomSet.union(virtualBottomSite, xyTo1D(row, col));
            gridSet.union(virtualBottomSite, xyTo1D(row, col));
        }
        for (int neighbor : neighbors(row, col)) { // num of neighbors can be 2, 3 or 4
            // n1D = nRow * N + nCol, nCol always larger than 0 and smaller than N - 1: remainder
            int neighborCol = neighbor % n;
            int neighborRow = (neighbor - neighborCol) / n;
            if (isOpen(neighborRow, neighborCol)) {
                gridSet.union(neighbor, xyTo1D(row, col));
                topSet.union(neighbor, xyTo1D(row, col));
            }
        }
    }

    /** Returns the neighbors of the (row, col).
     *  For example,
     *  * Considering n = 5
     *  *    0  1  2  3  4
     *  * 0 00 01 02 03 04
     *  * 1 05 06 07 08 09
     *  * 2 10 11 12 13 14
     *  * 3 15 16 17 18 19
     *  * 4 20 21 22 23 24
     *  the neighbors of 12 are 7, 11, 13, 17
     *  the neighbors of 14 are 9, 13, 19
     *  the neighbors of 20 are 15, 21
     */
    int[] neighbors(int row, int col) {
        int upperNeighbor = xyTo1D(row - 1, col);
        int lowerNeighbor = xyTo1D(row + 1, col);
        int leftNeighbor = xyTo1D(row, col - 1);
        int rightNeighbor = xyTo1D(row, col + 1);
        if (row == 0 && col == 0) { // 0
            return new int[]{lowerNeighbor, rightNeighbor};
        } else if (row == 0 && col == n - 1) { // 4
            return new int[]{lowerNeighbor, leftNeighbor};
        } else if (row == n - 1 && col == 0) { // 20
            return new int[]{upperNeighbor, rightNeighbor};
        } else if (row == n - 1 && col == n - 1) { // 24
            return new int[]{upperNeighbor, leftNeighbor};
        } else if (row == 0) {
            return new int[]{lowerNeighbor, leftNeighbor, rightNeighbor};
        } else if (row == n - 1) {
            return new int[]{upperNeighbor, leftNeighbor, rightNeighbor};
        } else if (col == 0) {
            return new int[]{upperNeighbor, lowerNeighbor, rightNeighbor};
        } else if (col == n - 1) {
            return new int[]{upperNeighbor, lowerNeighbor, leftNeighbor};
        } else {
            return new int[]{upperNeighbor, lowerNeighbor, leftNeighbor, rightNeighbor};
        }
    }

    /** Returns true if the site (row, col) is open, and false otherwise. */
    public boolean isOpen(int row, int col) {
        if (row < 0 || row > n - 1 || col < 0 || col > n - 1) {
            throw new IndexOutOfBoundsException();
        }

        return grid[row][col];
    }

    /** Returns true if the site (row, col) is full, and false otherwise. */
    public boolean isFull(int row, int col) {
        if (row < 0 || row > n - 1 || col < 0 || col > n - 1) {
            throw new IndexOutOfBoundsException();
        }

        return isOpen(row, col) && topSet.connected(virtualTopSite, xyTo1D(row, col));
    }

    /** Returns the number of open sites. */
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    /** Returns true if the system percolates, and false otherwise. */
    public boolean percolates() {
        return gridSet.connected(virtualTopSite, virtualBottomSite);
    }
}
