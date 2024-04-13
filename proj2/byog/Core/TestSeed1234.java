package byog.Core;

import byog.TileEngine.TETile;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

public class TestSeed1234 {

    public static void main(String[] args) {
        String input = "N1234S";
        Game game = new Game();
        TETile[][] world = game.playWithInputString(input);
        System.out.println(TETile.toString(world));
    }

    @Test
    public void testPlayWithInputString() {
        TETile[][] world1 = new Game().playWithInputString("N1234SDDDWWWDDD");

        TETile[][] world2Temp1 = new Game().playWithInputString("N1234SDDD:Q");
        TETile[][] world2 = new Game().playWithInputString("LWWWDDD");

        TETile[][] world3Temp1 = new Game().playWithInputString("N1234SDDD:Q");
        TETile[][] world3Temp2 = new Game().playWithInputString("LWWW:Q");
        TETile[][] world3 = new Game().playWithInputString("LDDD:Q");

        TETile[][] world4Temp1 = new Game().playWithInputString("N1234SDDD:Q");
        TETile[][] world4Temp2 = new Game().playWithInputString("L:Q");
        TETile[][] world4Temp3 = new Game().playWithInputString("L:Q");
        TETile[][] world4 = new Game().playWithInputString("LWWWDDD");

        TETile[][] world5Temp1 = new Game().playWithInputString("N1234SAASSSSSS:Q");
        TETile[][] world5Temp2 = new Game().playWithInputString("L:Q");
        TETile[][] world5Temp3 = new Game().playWithInputString("L:Q");
        TETile[][] world5 = new Game().playWithInputString("LAASSSWASD");

        TETile[][] world6Temp1 = new Game().playWithInputString("N999SDD:Q");
        TETile[][] world6Temp2 = new Game().playWithInputString("L:Q");
        TETile[][] world6Temp3 = new Game().playWithInputString("L:Q");
        TETile[][] world6 = new Game().playWithInputString("LWWWDDD");

        assertEquals(TETile.toString(world1), TETile.toString(world2));
        assertEquals(TETile.toString(world1), TETile.toString(world3));
        assertEquals(TETile.toString(world1), TETile.toString(world4));
        assertNotEquals(TETile.toString(world1), TETile.toString(world5));
        assertNotEquals(TETile.toString(world1), TETile.toString(world6));
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
    public void testRandomUniform() {
        Random random = new Random();
        for (int i = 0; i < MAX_TEST_NUM; i += 1) {
            int randInt = RandomUtils.uniform(random, 0, 2);
            assertTrue(randInt == 0 || randInt == 1);
        }
    }

    /** Tests the build-in method string.substring(index). */
    @Test
    public void testSubstring() {
        String input = "N1234SWDASWAS";
        int indexS = input.indexOf('S');
        assertEquals("WDASWAS", input.substring(indexS + 1));
    }
}
