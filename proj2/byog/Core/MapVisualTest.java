package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

public class MapVisualTest {
    public static void main(String[] args) {
//        MapGenerationParameters mgp = new MapGenerationParameters(Game.WIDTH, Game.HEIGHT, 5651);
        MapGenerationParameters mgp = MapGenerationParameters.defaultParameters();

        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(mgp.width, mgp.height);

        TETile[][] world = MapGenerator.generateMap(mgp);

        // draws the world to the screen
        ter.renderFrame(world);
    }
}
