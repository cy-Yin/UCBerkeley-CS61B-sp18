package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // Hint: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        TETile[][] finalWorld = new TETile[WIDTH][HEIGHT];

        input = input.toLowerCase();
        char firstOperation = input.charAt(0); // 'n' for new or 'l' for load or 's' for save.
        if (firstOperation == 'n') {
            finalWorld = newGame(input);
        } else if (firstOperation == 'l') {
            finalWorld = loadGame(input);
        } else if (firstOperation == 'q') {
            System.exit(0);
        } else {
            finalWorld = newGame(input);
        }

        return finalWorld;
    }

    /** Creates a new game when char n which means demanding for a new game is given.
     * Called if playing with input string.
     */
    private static TETile[][] newGame(String input) {
        long seed = getSeed(input);

        MapGenerationParameters mgp = new MapGenerationParameters(WIDTH, HEIGHT, seed);
        TETile[][] world = MapGenerator.generateMap(mgp);
        return world;
    }

    /** Loads the former game when char l which means load is given.
     * Called if playing with input string.
     */
    private static TETile[][] loadGame(String input) {
        return null;
    }

    /** Returns the long type seed from the input string.
     *  Called if playing with input string.
     *  @param input the input string
     */
    private static long getSeed(String input) {
        /* input string have a format of "N#####S"
         * where "######" are digits for example "1234"
         * we need to convert to digits-string to a long type seed.
         */
        int indexS = input.indexOf('s');
        long seed = Long.parseLong(input.substring(1, indexS)); // seed string start from 2 to n-1
        return seed;
    }
}
