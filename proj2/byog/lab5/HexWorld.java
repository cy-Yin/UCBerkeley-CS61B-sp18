package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 80;

    private static final Random RANDOM = new Random();

    /** Position is a class with two variables p.x and p.y.
     *  p specifies the lower left corner of the hexagon.
     */
    public static class Position {
        int x;
        int y;

        public Position(int xPos, int yPos) {
            x = xPos;
            y = yPos;
        }
    }

    /** Adds a hexagon to the world.
     * @param world the world to draw on
     * @param pos the bottom left coordinate of the hexagon
     * @param size the size of the hexagon
     * @param tile the tile to draw
     */
    public static void addHexagon(TETile[][] world, Position pos, int size, TETile tile) {
        if (size < 2) {
            throw new IllegalArgumentException("Hexagon must be at least size 2.");
        }

        // hexagons have 2*s rows. this code iterates up from the bottom row,
        // which we call row 0.
        for (int yi = 0; yi < 2 * size; yi += 1) {
            int thisRowY = pos.y + yi;

            int xRowStart = pos.x + hexRowOffset(size, yi);
            Position rowStartP = new Position(xRowStart, thisRowY);

            int rowWidth = hexRowWidth(size, yi);

            addRow(world, rowStartP, rowWidth, tile);
        }
    }

    /** Adds a row of the same tile.
     * @param world the world to draw on
     * @param pos the leftmost position of the row
     * @param width the number of tiles wide to draw
     * @param tile the tile to draw
     */
    private static void addRow(TETile[][] world, Position pos, int width, TETile tile) {
        for (int xi = 0; xi < width; xi += 1) {
            int xCoord = pos.x + xi;
            int yCoord = pos.y;
            world[xCoord][yCoord] = TETile.colorVariant(tile, 32, 32, 32, RANDOM);
        }
    }

    /**
     * Computes the width of row i for a size s hexagon.
     * @param size The size of the hex.
     * @param i The row number where i = 0 is the bottom row.
     * @return
     */
    private static int hexRowWidth(int size, int i) {
        int effectiveI = size;
        if (i < size) {
            effectiveI += 2 * i;
        } else {        // i >= s
            effectiveI += 2 * (2 * size - i - 1);
        }
        return effectiveI;
    }

    /**
     * Computesrelative x coordinate of the leftmost tile in the ith
     * row of a hexagon, assuming that the bottom row has an x-coordinate
     * of zero. For example, if s = 3, and i = 2, this function
     * returns -2, because the row 2 up from the bottom starts 2 to the left
     * of the start position, e.g.
     *   xxx
     *  xxxxx
     * xxxxxxx
     * xxxxxxx <-- i = 2, starts 2 spots to the left of the bottom of the hex
     *  xxxxx
     *   xxx
     *
     * @param size size of the hexagon
     * @param i row num of the hexagon, where i = 0 is the bottom
     * @return
     */
    private static int hexRowOffset(int size, int i) {
        int effectiveI = 0;
        if (i < size) {
            effectiveI = -i;
        } else {        // i >= s
            effectiveI = -(2 * size - i - 1);
        }
        return effectiveI;
    }

    @Test
    public void testHexRowWidth() {
        assertEquals(3, hexRowWidth(3, 5));
        assertEquals(5, hexRowWidth(3, 4));
        assertEquals(7, hexRowWidth(3, 3));
        assertEquals(7, hexRowWidth(3, 2));
        assertEquals(5, hexRowWidth(3, 1));
        assertEquals(3, hexRowWidth(3, 0));
        assertEquals(2, hexRowWidth(2, 0));
        assertEquals(4, hexRowWidth(2, 1));
        assertEquals(4, hexRowWidth(2, 2));
        assertEquals(2, hexRowWidth(2, 3));
    }

    @Test
    public void testHexRowOffset() {
        assertEquals(0, hexRowOffset(3, 5));
        assertEquals(-1, hexRowOffset(3, 4));
        assertEquals(-2, hexRowOffset(3, 3));
        assertEquals(-2, hexRowOffset(3, 2));
        assertEquals(-1, hexRowOffset(3, 1));
        assertEquals(0, hexRowOffset(3, 0));
        assertEquals(0, hexRowOffset(2, 0));
        assertEquals(-1, hexRowOffset(2, 1));
        assertEquals(-1, hexRowOffset(2, 2));
        assertEquals(0, hexRowOffset(2, 3));
    }

    /** Returns the position of the top-right neighbor of the current hexagon. */
    private static Position topRightNeighbor(Position pos, int size) {
        Position topRightPos = new Position(pos.x + 2 * size - 1, pos.y + size);
        return topRightPos;
    }

    /** Returns the position of the bottom-right neighbor of the current hexagon. */
    private static Position bottomRightNeighbor(Position pos, int size) {
        Position bottomRightPos = new Position(pos.x + 2 * size - 1, pos.y - size);
        return bottomRightPos;
    }

    /** Draws a column of n hexagons.
     * @param world the world to draw on
     * @param pos the position of the bottom hexagon in the column
     * @param size size of the hexagon
     * @param n the number of hexagons in the column
     */
    public static void drawRandomVerticalHexes(TETile[][] world, Position pos, int size, int n) {
        for (int row = 0; row < n; row += 1) {
            addHexagon(world, pos, size, randomTile());
            pos = topNeighbor(pos, size); // add the hexagons from bottom to top
        }
    }

    /** Returns the position of the top neighbor of the current hexagon. */
    private static Position topNeighbor(Position pos, int size) {
        Position topPos = new Position(pos.x, pos.y + 2 * size);
        return topPos;
    }

//    /** Draws the top-right neighbor and bottom-right neighbor of the given current hexagon. */
//    public static void drawTesselationHexes(TETile[][] world, Position pos, int size) {
//        Position topRightPos = topRightNeighbor(pos, size);
//        Position bottomRightPos = bottomRightNeighbor(pos, size);
//        addHexagon(world, topRightPos, size, randomTile());
//        addHexagon(world, bottomRightPos, size, randomTile());
//    }

    /** Randomly returns the tile of the hexagon. */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.SAND;
            case 1: return Tileset.MOUNTAIN;
            case 2: return Tileset.TREE;
            case 3: return Tileset.WATER;
            case 4: return Tileset.FLOWER;
            default: return Tileset.FLOWER;
        }
    }

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        // information about the tesselation hexagons, including their initial position and size.
        int size = 4;
        Position firstHexPos = new Position(10, 30);

        // Draws the hexagons.
        Position pos = firstHexPos;
        int[] numOfHexInCol = {3, 4, 5, 4, 3};
        for (int col = 0; col < numOfHexInCol.length; col += 1) {
            drawRandomVerticalHexes(world, pos, size, numOfHexInCol[col]);
            if (col < numOfHexInCol.length / 2) { // j < 2
                pos = bottomRightNeighbor(pos, size);
            } else {
                pos = topRightNeighbor(pos, size);
            }
        }

        // draws the world to the screen
        ter.renderFrame(world);
    }
}
