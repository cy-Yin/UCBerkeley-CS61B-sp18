import static org.junit.Assert.*;
import org.junit.Test;

public class FlikTest {

    @Test
    public void testIsSameNumber() {
        // Test if 127 = 127.
        int a1 = 127;
        int b1 = 127;
        boolean actual1 = Flik.isSameNumber(a1, b1);
        assertTrue(a1 + " and " + b1 + " is not the same number in Flik.isSameNumber", actual1);

        // Test if 128 = 128.
        int a2 = 128;
        int b2 = 128;
        boolean actual2 = Flik.isSameNumber(a2, b2);
        assertTrue(a2 + " and " + b2 + " is not the same number in Flik.isSameNumber", actual2);

        // Test if 500 = 500.
        int a3 = 500;
        int b3 = 500;
        boolean actual3 = Flik.isSameNumber(a3, b3);
        assertTrue(a3 + " and " + b3 + " is not the same number in Flik.isSameNumber", actual3);
    }

    public static void main(String... args) {
        jh61b.junit.TestRunner.runTests("all", FlikTest.class);
    }
}
