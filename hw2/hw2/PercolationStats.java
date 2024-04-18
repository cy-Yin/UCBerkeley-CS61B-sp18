package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int n;
    private int times;
    private double[] thresholds;

    /** perform T independent experiments on an N-by-N grid */
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }

        n = N;
        times = T;
        thresholds = new double[times];
        for (int i = 0; i < times; i += 1) {
            Percolation per = pf.make(n);
            while (!per.percolates()) {
                int row = StdRandom.uniform(n);
                int col = StdRandom.uniform(n);
                per.open(row, col);
            }
            thresholds[i] = (double) per.numberOfOpenSites() / (n * n);
        }
    }

    /** Returns the sample mean of percolation threshold. */
    public double mean() {
        return StdStats.mean(thresholds);
    }

    /** Returns the sample standard deviation of percolation threshold. */
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    /** Returns the low endpoint of 95% confidence interval. */
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(times);
    }

    /** Returns the high endpoint of 95% confidence interval. */
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(times);
    }
}
