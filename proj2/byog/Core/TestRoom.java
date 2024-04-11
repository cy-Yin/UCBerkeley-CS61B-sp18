package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestRoom {
    MapGenerationParameters mgp = MapGenerationParameters.defaultParameters();

    public static void testGenerateRoom() {
        MapGenerationParameters mgp = MapGenerationParameters.defaultParameters();
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(mgp.width, mgp.height);

        // initialize tiles
        TETile[][] world = new TETile[mgp.width][mgp.height];
        for (int x = 0; x < mgp.width; x += 1) {
            for (int y = 0; y < mgp.height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        Position roomPos = new Position(30, 10);
        Room r = new Room(4, 5, roomPos);
        r.generateRoom(world, mgp);

        // draws the world to the screen
        ter.renderFrame(world);
    }

    @Test
    public void testIsOverlap() {

        // initialize tiles
        TETile[][] world = new TETile[mgp.width][mgp.height];
        for (int x = 0; x < mgp.width; x += 1) {
            for (int y = 0; y < mgp.height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        Room r1 = new Room(3, 4, new Position(2, 3));
        Room r2 = new Room(5, 3, new Position(6, 1));
        r1.generateRoom(world, mgp);
        r2.generateRoom(world, mgp);

        Room r3 = new Room(4, 4, new Position(6, 5));
        assertFalse(r3.isOverlap(world, mgp));
        r3.generateRoom(world, mgp);

        // From now on the basic graphic world has just 3 rectangles: r1, r2 and r3

        Room r4 = new Room(3, 2, new Position(8, 5));
        assertTrue(r4.isOverlap(world, mgp));

        Room r5 = new Room(78, 9, new Position(3, 20));
        assertTrue(r5.isOverlap(world, mgp));

        Room r6 = new Room(80, 30, new Position(0, 0));
        assertTrue(r6.isOverlap(world, mgp));
    }

    public static void main(String[] args) {
        testGenerateRoom();
    }
}
