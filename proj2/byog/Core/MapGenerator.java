package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

public class MapGenerator {
    /*
     * Maximum of the number of trying to generate a room.
     * Some generated rooms may be overlapped with the existed rooms.
     * Therefore, the final total number of rooms may be smaller than MAX_NUM_ROOMS
     * since the overlapped ones will be dropped.
     */
    public static final int MAX_NUM_ROOM = 40;

    public static final int MIN_ROOM_WIDTH = 4;
    public static final int MIN_ROOM_HEIGHT = 4;
    public static final int MAX_ROOM_WIDTH = 9;
    public static final int MAX_ROOM_HEIGHT = 7;

    /**
     * The map generator. the main method for the MapGenerator. generator the final map.
     */
    public static TETile[][] generateMap(MapGenerationParameters mgp) {
        // initialize tiles
        TETile[][] world = new TETile[mgp.width][mgp.height];
        for (int x = 0; x < mgp.width; x += 1) {
            for (int y = 0; y < mgp.height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        ArrayList<Room> rooms = generateRooms(world, mgp);
        ArrayList<Room> sortedRooms = sortRooms(rooms);

        generateHallways(world, mgp, sortedRooms);

        /* Redraw the rooms on the map, so some ends of the hallways can be covered and hided. */
        for (int i = 0; i < sortedRooms.size(); i += 1) {
            sortedRooms.get(i).generateRoom(world, mgp);
        }

        return world;
    }

    /**
     * Returns a list including all the non-overlapped rooms generated and drew on the map.
     */
    public static ArrayList<Room> generateRooms(TETile[][] world, MapGenerationParameters mgp) {
        Random random = new Random(mgp.seed);

        ArrayList<Room> rooms = new ArrayList<>();
        for (int i = 0; i < MAX_NUM_ROOM; i += 1) {
            int roomWidth = RandomUtils.uniform(random, MIN_ROOM_WIDTH, MAX_ROOM_WIDTH);
            int roomHeight = RandomUtils.uniform(random, MIN_ROOM_HEIGHT, MAX_ROOM_HEIGHT);
            int randPosX = RandomUtils.uniform(random, mgp.width);
            int randPosY = RandomUtils.uniform(random, mgp.height);
            Position roomPos = new Position(randPosX, randPosY);

            Room newRoom = new Room(roomWidth, roomHeight, roomPos);
            if (!newRoom.isOverlap(world, mgp)) {
                rooms.add(newRoom);
                newRoom.generateRoom(world, mgp);
            }
        }
        return rooms;
    }

    /**
     * Sorts the rooms for the further connection by the hallways in ascending order.
     * Uses minimumRoom() method to find the "minimum" room in rooms.
     *
     * @param rooms the generated rooms on the map
     * @return sorted rooms
     */
    public static ArrayList<Room> sortRooms(ArrayList<Room> rooms) {
        ArrayList<Room> sortedRooms = new ArrayList<>();
        int roomsSize = rooms.size();
        for (int i = 0; i < roomsSize; i += 1) {
            int minRoomIndex = minimumRoom(rooms);
            sortedRooms.add(rooms.remove(minRoomIndex));
        }
        return sortedRooms;
    }

    /**
     * Returns the index of the "minimum" room in the rooms list.
     * Helper function for sortRooms() method.
     * <p>
     * Here we use the sum of the room position's x and y as a characteristic for
     * measuring the distance between (0, 0) and the room.
     * And though sorting the rooms, we can connect them if they are neighbors in the list.
     * <p>
     * Of course in a square-shape map, the top-left and the bottom-right rooms
     * will be considered as neighbors, which are not neighbors in reality.
     * However, firstly the map is 80x30, so the pos.x outweighs the pos.y,
     * and it's more likely for two rooms which have smaller distance in the horizontal direction
     * (namely, smaller |r1.pos.x - r2.pos.x|) to become neighbors.
     * What's more, we do not strictly demand that only the neighbors are connected.
     * It is fine to connect two rooms that are not so close.
     *
     * @param rooms the generated rooms on the map
     * @return the "minimum" room in rooms
     */
    private static int minimumRoom(ArrayList<Room> rooms) {
        int comparatorMin = rooms.get(0).pos.x + rooms.get(0).pos.y;
        int minIndex = 0;
        for (int i = 0; i < rooms.size(); i += 1) {
            int comparator = rooms.get(i).pos.x + rooms.get(i).pos.y;
            if (comparator < comparatorMin) {
                comparatorMin = comparator;
                minIndex = i;
            }
        }
        return minIndex;
    }


    /**
     * After all the rooms generated, draws all the hallways to connect the rooms.
     */
    public static void generateHallways(TETile[][] world, MapGenerationParameters mgp,
                                    ArrayList<Room> rooms) {
        ArrayList<Room> newRooms = new ArrayList<>();
        for (Room room : rooms) {
            newRooms.add(room);
        }
        while  (newRooms.size() != 1 && !newRooms.isEmpty()) {
            Room room0 = newRooms.get(0);
            Room room1 = newRooms.get(1);
            Position room0InnerPosRand = room0.innerPosRandom(mgp);
            Position room1InnerPosRand = room1.innerPosRandom(mgp);
            if (room0InnerPosRand.x == room1InnerPosRand.x) {
                Hallway.generateVerticalHallway(world, room0InnerPosRand, room1InnerPosRand);
            } else if (room0InnerPosRand.y == room1InnerPosRand.y) {
                Hallway.generateHorizontalHallway(world, room0InnerPosRand, room1InnerPosRand);
            } else {
                Random random = new Random(mgp.seed);
                int decideUpperConvex = RandomUtils.uniform(random, 0, 2);
                if (decideUpperConvex == 0) {
                    Hallway.generateLHallway(world, room0InnerPosRand, room1InnerPosRand, false);
                } else { // decideUpperConvex == 1
                    Hallway.generateLHallway(world, room0InnerPosRand, room1InnerPosRand, true);
                }
            }
            newRooms.remove(0);
        }
    }

    /** Returns the player after generate one.
     *  The player should be generated at the floor, rather than walls, nothing or something else.
     */
    public static Player generatePlayer(TETile[][] world, MapGenerationParameters mgp) {
        Random rand = new Random(mgp.seed);
        int playerPosX = RandomUtils.uniform(rand, 0, Game.WIDTH);
        int playerPosY = RandomUtils.uniform(rand, 0, Game.HEIGHT);
        while (world[playerPosX][playerPosY] != Tileset.FLOOR) {
            playerPosX = RandomUtils.uniform(rand, 0, Game.WIDTH);
            playerPosY = RandomUtils.uniform(rand, 0, Game.HEIGHT);
        }
        Position playerPos = new Position(playerPosX, playerPosY);
        world[playerPos.x][playerPos.y] = Tileset.PLAYER;
        return new Player(playerPos);
    }

    /** Returns the locked door after generate one.
     *  The door's position is originally wall, and it has four neighbor tiles,
     *  with one floor and one nothing and rest of two both walls.
     */
    public static LockedDoor generateLockedDoor(TETile[][] world, Player player) {
        int doorPosX = 0;
        int doorPosY = 0;
        final int xScale = 10;
        final int yScale = 10;
        for (int x = Math.max(0, player.pos.x - xScale) + 1;
             x < Math.min(player.pos.x + xScale, Game.WIDTH) - 1; x += 1) {
            for (int y = Math.max(0, player.pos.y - yScale) + 1;
                 y < Math.min(player.pos.y + yScale, Game.HEIGHT) - 1; y += 1) {
                Position doorPos = new Position(x, y);
                if (isLockedDoorPosValid(world, doorPos)) {
                    doorPosX = x;
                    doorPosY = y;
                }
            }
        }
        Position doorPos = new Position(doorPosX, doorPosY);
        world[doorPos.x][doorPos.y] = Tileset.LOCKED_DOOR;
        return new LockedDoor(doorPos);
    }

    /** Returns true if the position of the locked door is valid.
     *  When I say "valid", I mean the door's position is originally wall,
     *  and it must have four neighbor tiles,
     *  with one floor and one nothing and rest of two both walls.
     *  Helper function for the generateLockedDoor() method
     */
    private static boolean isLockedDoorPosValid(TETile[][] world, Position doorPos) {
        if (world[doorPos.x][doorPos.y] != Tileset.WALL) {
            return false;
        }
        int numWalls = 0;
        int numNothing = 0;
        int numFloor = 0;
        Position[] neighbors = {
            new Position(doorPos.x - 1, doorPos.y),
            new Position(doorPos.x + 1, doorPos.y),
            new Position(doorPos.x, doorPos.y + 1),
            new Position(doorPos.x, doorPos.y - 1)
        };
        for (Position neighbor : neighbors) {
            if (world[neighbor.x][neighbor.y] == Tileset.WALL) {
                numWalls += 1;
            } else if (world[neighbor.x][neighbor.y] == Tileset.FLOOR) {
                numFloor += 1;
            } else if (world[neighbor.x][neighbor.y] == Tileset.NOTHING) {
                numNothing += 1;
            }
        }
        return numFloor == 1 && numNothing == 1 && numWalls == 2;
    }
}
