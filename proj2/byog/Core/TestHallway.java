package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class TestHallway {
    public static void testGenerateLHallway() {
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

        Hallway.generateLHallway(world,
                new Position(2, 11), new Position(6, 21), true);

        Hallway.generateLHallway(world,
                new Position(14, 11), new Position(9, 21), true);

        Hallway.generateLHallway(world,
                new Position(18, 11), new Position(24, 21), false);

        Hallway.generateLHallway(world,
                new Position(34, 11), new Position(29, 21), false);

        Hallway.generateLHallway(world,
                new Position(60, 11), new Position(61, 21), false);

        // draws the world to the screen
        ter.renderFrame(world);
    }

    public static void main(String[] args) {
        testGenerateLHallway();
    }
}
