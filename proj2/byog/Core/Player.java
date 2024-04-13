package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 123123123123123L;
    Position pos;

    Player(Position playerPosition) {
        pos = playerPosition;
    }

    /** When playing with keyboard, user is allowed to move the character "@"
     * through "WASD" keys.
     */
    public TETile[][] move(TETile[][] world, String moveInput) {
        while (!moveInput.isEmpty()) {
            char c = moveInput.charAt(0);
            if ((c == 'W' || c == 'w') && (world[pos.x][pos.y + 1].equals(Tileset.FLOOR)
                    || world[pos.x][pos.y + 1].equals(Tileset.LOCKED_DOOR))) {
                world[pos.x][pos.y] = Tileset.FLOOR;
                world[pos.x][pos.y + 1] = Tileset.PLAYER;
                pos = new Position(pos.x, pos.y + 1);
            } else if ((c == 'S' || c == 's') && (world[pos.x][pos.y - 1].equals(Tileset.FLOOR)
                    || world[pos.x][pos.y - 1].equals(Tileset.LOCKED_DOOR))) {
                world[pos.x][pos.y] = Tileset.FLOOR;
                world[pos.x][pos.y - 1] = Tileset.PLAYER;
                pos = new Position(pos.x, pos.y - 1);
            } else if ((c == 'A' || c == 'a') && (world[pos.x - 1][pos.y].equals(Tileset.FLOOR)
                    || world[pos.x - 1][pos.y].equals(Tileset.LOCKED_DOOR))) {
                world[pos.x][pos.y] = Tileset.FLOOR;
                world[pos.x - 1][pos.y] = Tileset.PLAYER;
                pos = new Position(pos.x - 1, pos.y);
            } else if ((c == 'D' || c == 'd') && (world[pos.x + 1][pos.y].equals(Tileset.FLOOR)
                    || world[pos.x + 1][pos.y].equals(Tileset.LOCKED_DOOR))) {
                world[pos.x][pos.y] = Tileset.FLOOR;
                world[pos.x + 1][pos.y] = Tileset.PLAYER;
                pos = new Position(pos.x + 1, pos.y);
            }
            moveInput = moveInput.substring(1);
        }
        return world;
    }
}
