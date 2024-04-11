package byog.Core;

import byog.TileEngine.TETile;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class TestSeed1234 {

    public static void main(String[] args) {
        String input = "N1234S";
        Game game = new Game();
        TETile[][] world = game.playWithInputString(input);
        System.out.println(TETile.toString(world));
    }

    /** Tests the build-in method toLowerCase() */
    @Test
    public void testLowercase() {
        String expected = "n1234s";
        assertEquals(expected, "N1234S".toLowerCase());
        assertEquals(expected, "N1234s".toLowerCase());
        assertEquals(expected, "n1234S".toLowerCase());
        assertEquals(expected, "n1234s".toLowerCase());
    }

    final int MAX_TEST_NUM = 10000;

    /** Tests the build-in method random.nextInt(a, b). */
    @Test
    public void testRandomNextInt() {
        Random random = new Random();
        for (int i = 0; i < MAX_TEST_NUM; i += 1) {
            int randInt = random.nextInt(0, 2);
            assertTrue(randInt == 0 || randInt == 1);
        }
    }
}
