package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Hallway {
    Position start;
    Position end;
    Position corner;

    /** Hallway with a corner (for example L-shape) */
    Hallway(Position hallwayStart, Position hallwayEnd, Position hallwayCorner) {
        start = hallwayStart;
        end = hallwayEnd;
        corner = hallwayCorner;
    }

    /** Hallway without corners (just horizontal or vertical direction) */
    Hallway(Position hallwayStart, Position hallwayEnd) {
        start = hallwayStart;
        end = hallwayEnd;
    }

    /** Generates the horizontal hallway if start.y = end.y */
    public static void generateHorizontalHallway(TETile[][] world, Position start, Position end) {
        if (start.y != end.y) {
            return;
        }
        int largeX = Math.max(start.x, end.x);
        int smallerX = Math.min(start.x, end.x);
        for (int x = smallerX; x <= largeX; x += 1) {
            if (world[x][start.y - 1] != Tileset.FLOOR) {
                world[x][start.y - 1] = Tileset.WALL;
            }
            world[x][start.y] = Tileset.FLOOR;
            if (world[x][start.y + 1] != Tileset.FLOOR) {
                world[x][start.y + 1] = Tileset.WALL;
            }
        }
    }

    /** Generates the vertical hallway if start.x = end.x */
    public static void generateVerticalHallway(TETile[][] world, Position start, Position end) {
        if (start.x != end.x) {
            return;
        }
        int largeY = Math.max(start.y, end.y);
        int smallerY = Math.min(start.y, end.y);
        for (int y = smallerY; y <= largeY; y += 1) {
            if (world[start.x - 1][y] != Tileset.FLOOR) {
                world[start.x - 1][y] = Tileset.WALL;
            }
            world[start.x][y] = Tileset.FLOOR;
            if (world[start.x + 1][y] != Tileset.FLOOR) {
                world[start.x + 1][y] = Tileset.WALL;
            }
        }
    }

    /** Generates the L-shape hallway (which has a corner,
     *  or can be considered as grouped by a vertical hallway and a horizontal hallway)
     *  There are four shapes of L-shape hallway:
     *  1. corner at the top-left
     *  2. corner at the top-right
     *  3. corner at the bottom-left
     *  4. corner at the bottom-right
     *  1 and 2 have isUpperConvex equals true, and 3 and 4 have isUpperConvex equals false.
     * @param isUpperConvex decides the shape of the hallway curve. isUpperConvex is
     *      *     true if the corner is at the top-left or top-right
     *      *    false if the corner is at the bottom-left or bottom-right
     */
    public static void generateLHallway(TETile[][] world, Position start, Position end,
                                        boolean isUpperConvex) {
        if (start.x == end.x || start.y == end.y) {
            return;
        }

        Position corner = getCornerPos(start, end, isUpperConvex);

        // generate "four" hallways, but actually only two will be generated.
        // Thanks to the if condition at the beginning of
        // generateVerticalHallway() and generateHorizontalHallway() method.
        generateVerticalHallway(world, start, corner);
        generateVerticalHallway(world, end, corner);
        generateHorizontalHallway(world, start, corner);
        generateHorizontalHallway(world, end, corner);

        dealWithCorner(world, corner, isUpperConvex);
    }

    /** Returns the corner position of the L-shape hallway
     *  according to its start and end position and the demanded shape (isUpperConvex)
     */
    private static Position getCornerPos(Position start, Position end, boolean isUpperConvex) {
        int largeX = Math.max(start.x, end.x);
        int smallerX = Math.min(start.x, end.x);
        int largeY = Math.max(start.y, end.y);
        int smallerY = Math.min(start.y, end.y);

        int cornerX;
        int cornerY;
        if (isUpperConvex) {
            if (largeY == end.y) {
                /*
                 *  c   e   or   e   c
                 *  s                s
                 */
                cornerX = start.x;
                cornerY = end.y;
            } else { // largerY = start.y
                /*
                 *  c   s   or   s   c
                 *  e                e
                 */
                cornerX = end.x;
                cornerY = start.y;
            }
        } else { // generate a lower convex shape hallway
            if (smallerY == end.y) {
                /*
                 *      s   or   s
                 *  e   c        c   e
                 */
                cornerX = start.x;
                cornerY = end.y;
            } else { // smallerY = start.y
                /*
                 *      e   or   e
                 *  s   c        c   s
                 */
                cornerX = end.x;
                cornerY = start.y;
            }
        }
        Position corner = new Position(cornerX, cornerY);
        return corner;
    }

    /** Deals with the walls and floors at the corner to make it look like a L-shape hallway. */
    private static void dealWithCorner(TETile[][] world, Position corner, boolean isUpperConvex) {
        /*
         *  In generateLHallway() method, the last step is to deal with the walls and floors
         *  at the corner.
         *
         *  The previous steps will cause 4 situations: 1, 2 isUpperConvex = true and 3, 4 false
         *
         * 1.   ******  2. ******    3. *-*          4.       *-*
         *     *------     ------*      *-*                   *-*
         *     *******     *******      *-*                   *-*
         *     *-*             *-*      *********       *********
         *     *-*             *-*      *--------       --------*
         *     *-*             *-*       ********       ********
         *
         * In order to create a beautiful corner, we should
         * change (x, y - 1) from wall to floor if the corner is upper convex
         * change (x, y + 1) from wall to floor if the corner is lower convex
         * fill the corner tile with wall
         */
        if (isUpperConvex) {
            world[corner.x][corner.y - 1] = Tileset.FLOOR;
            if (world[corner.x - 1][corner.y + 1] == Tileset.NOTHING) {
                world[corner.x - 1][corner.y + 1] = Tileset.WALL;
            }
            if (world[corner.x + 1][corner.y + 1] == Tileset.NOTHING) {
                world[corner.x + 1][corner.y + 1] = Tileset.WALL;
            }
        } else {
            world[corner.x][corner.y + 1] = Tileset.FLOOR;
            if (world[corner.x - 1][corner.y - 1] == Tileset.NOTHING) {
                world[corner.x - 1][corner.y - 1] = Tileset.WALL;
            }
            if (world[corner.x + 1][corner.y - 1] == Tileset.NOTHING) {
                world[corner.x + 1][corner.y - 1] = Tileset.WALL;
            }
        }
    }
}
