package byog.Core;

import byog.TileEngine.TETile;

public class SavedInfo {
    TETile[][] world;
    MapGenerationParameters mgp;
    Player player1;
    LockedDoor lockedDoor;

    SavedInfo(TETile[][] world, MapGenerationParameters mgp,
              Player player1, LockedDoor lockedDoor) {
        this.world = world;
        this.mgp = mgp;
        this.player1 = player1;
        this.lockedDoor = lockedDoor;
    }
}
