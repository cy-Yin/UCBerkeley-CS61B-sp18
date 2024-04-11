package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class Room {
    int height;
    int width;
    Position pos;

    Room(int roomWidth, int roomHeight, Position p) {
        width = roomWidth;
        height = roomHeight;
        pos = p;
    }

    /** Generates a room with floor and make it surrounded by the walls. */
    public void generateRoom(TETile[][] world, MapGenerationParameters mgp) {
        /* fill the whole room with walls and replace the inner walls with floor. */
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                // This if condition is used not to generate a wall to cover the floor of hallway.
                if (world[pos.x + x][pos.y + y] != Tileset.FLOOR) {
                    world[pos.x + x][pos.y + y] = Tileset.WALL;
                }
            }
        }
        for (int x = 1; x < width - 1; x += 1) {
            for (int y = 1; y < height - 1; y += 1) {
                world[pos.x + x][pos.y + y] = Tileset.FLOOR;
            }
        }
    }

    /** Returns false if this room and exist rooms overlap each other, and true otherwise. */
    public boolean isOverlap(TETile[][] world, MapGenerationParameters mgp) {
        // consider the new room out of the world bound as room-world overlap.
        if (pos.x + width > mgp.width || pos.y + height > mgp.height) {
            return true;
        }
        for (int i = pos.x; i < pos.x + width; i += 1) {
            for (int j = pos.y; j < pos.y + height; j += 1) {
                if (world[i][j] == Tileset.FLOOR) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Returns a random position "in" the room (a FLOOR).
     * @param mgp the mgp class includes the width, height and seed of the generated map/world.
     * @return
     */
    public Position innerPosRandom(MapGenerationParameters mgp) {
        Random random = new Random(mgp.seed);
        int innerX = pos.x + 1 + RandomUtils.uniform(random, width - 2);  // from 1 to width - 2
        int innerY = pos.y + 1 + RandomUtils.uniform(random,height - 2); // from 1 to height - 2
        return new Position(innerX, innerY);
    }
}
