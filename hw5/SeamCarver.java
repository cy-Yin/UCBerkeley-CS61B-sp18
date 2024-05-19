import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private int width;
    private int height;
    double[][] energy;


    public SeamCarver(Picture picture) {
        this.picture = picture;
        width = picture.width();
        height = picture.height();
        energy = new double[width][height];
        // calculate the energy
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                energy[i][j] = energy(i, j);
            }
        }
    }

    /** Returns the current picture. */
    public Picture picture() {
        return this.picture;
    }

    /** Returns the width of current picture. */
    public int width() {
        return width;
    }

    /** Returns the height of current picture. */
    public int height() {
        return height;
    }

    /** Returns the energy of pixel at column x and row y. */
    public double energy(int x, int y) {
        if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
            throw new IndexOutOfBoundsException();
        }

        double energy;
        double deltaXSquare;
        double deltaYSquare;

        Color left = picture.get(Math.floorMod(x - 1, width), y);
        Color right = picture.get(Math.floorMod(x + 1, width), y);
        deltaXSquare = (left.getRed() - right.getRed()) * (left.getRed() - right.getRed())
                     + (left.getGreen() - right.getGreen()) * (left.getGreen() - right.getGreen())
                     + (left.getBlue() - right.getBlue()) * (left.getBlue() - right.getBlue());

        Color above = picture.get(x, Math.floorMod(y - 1, height));
        Color below = picture.get(x, Math.floorMod(y + 1, height));
        deltaYSquare = (above.getRed() - below.getRed()) * (above.getRed() - below.getRed())
                     + (above.getGreen() - below.getGreen()) * (above.getGreen() - below.getGreen())
                     + (above.getBlue() - below.getBlue()) * (above.getBlue() - below.getBlue());

        energy = deltaXSquare + deltaYSquare;
        return energy;
    }

    /** Returns the sequence of indices for vertical seam. */
    public int[] findVerticalSeam() {
        int[][] edgeTo = new int[width][height];
        double[][] minCost = new double[width][height];

        // minCost[i, 0] = energy[i, 0]
        for (int i = 0; i < width; i += 1) {
            minCost[i][0] = energy[i][0];
            edgeTo[i][0] = i;
        }

        for (int j = 1; j < height; j += 1) {
            for (int i = 0; i < width; i += 1) {
                double minCostLeftUp = minCost[Math.floorMod(i - 1, width)][j - 1];
                double minCostCurrUp = minCost[i][j - 1];
                double minCostRightUp = minCost[Math.floorMod(i + 1, width)][j - 1];
                if (i == 0) {
                    edgeTo[i][j] =
                            minCostCurrUp > minCostRightUp ? Math.floorMod(i + 1, width) : i;
                    minCost[i][j] = energy[i][j] + Math.min(minCostCurrUp, minCostRightUp);
                } else if (i == width - 1) {
                    edgeTo[i][j] =
                            minCostCurrUp > minCostLeftUp ? Math.floorMod(i - 1, width) : i;
                    minCost[i][j] = energy[i][j] + Math.min(minCostCurrUp, minCostLeftUp);
                } else {
                    int iTemp = minCostCurrUp > minCostLeftUp ? Math.floorMod(i - 1, width) : i;
                    double costTemp = Math.min(minCostCurrUp, minCostLeftUp);
                    edgeTo[i][j] = minCostRightUp > costTemp ? iTemp : Math.floorMod(i + 1, width);
                    minCost[i][j] = energy[i][j]
                        + Math.min(Math.min(minCostLeftUp, minCostCurrUp), minCostRightUp);
                }
            }
        }

        // find the smallest of minCost[i][height - 1]
        double minCostTotal = Double.MAX_VALUE;
        int minCostIndex = 0;
        for (int i = 0; i < width; i += 1) {
            if (minCostTotal > minCost[i][height - 1]) {
                minCostIndex = i;
                minCostTotal = minCost[i][height - 1];
            }
        }

        // find the returned seam array
        int[] verticalSeam = new int[height];
        verticalSeam[height - 1] = minCostIndex;
        for (int j = height - 2; j >= 0; j -= 1) {
            verticalSeam[j] = edgeTo[minCostIndex][j + 1];
            minCostIndex = verticalSeam[j];
        }

        return verticalSeam;
    }

    /** Returns the sequence of indices for horizontal seam. */
    public int[] findHorizontalSeam() {
        energy = transposeMatrix(energy);
        int temp1 = width;
        width = height;
        height = temp1;

        int[] horizontalSeam = findVerticalSeam();

        energy = transposeMatrix(energy);
        int temp2 = width;
        width = height;
        height = temp2;

        return horizontalSeam;
    }

    private double[][] transposeMatrix(double[][] matrix) {
        int w = matrix.length;
        int h = matrix[0].length;

        double[][] matrixTranspose = new double[h][w];

        for (int i = 0; i < w; i += 1) {
            for (int j = 0; j < h; j += 1) {
                matrixTranspose[j][i] = matrix[i][j];
            }
        }

        return matrixTranspose;
    }

    /** Removes vertical seam from picture. */
    public void removeVerticalSeam(int[] seam) {
        if (seam.length != height) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length - 1; i += 1) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }

        SeamRemover.removeVerticalSeam(picture, seam);
    }

    /** Removes horizontal seam from picture. */
    public void removeHorizontalSeam(int[] seam) {
        if (seam.length != width) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length - 1; i += 1) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }

        SeamRemover.removeHorizontalSeam(picture, seam);
    }
}
