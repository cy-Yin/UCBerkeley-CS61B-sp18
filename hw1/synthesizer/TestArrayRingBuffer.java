package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer arb = new ArrayRingBuffer(10);

        assertEquals(arb.capacity(), 10);
        assertTrue(arb.isEmpty());

        for (int i = 0; i < arb.capacity(); i += 1) {
            arb.enqueue(i);
        } // arb : 0, 1, 2, 3, 4, 5, 6, 7, 8, 9
          //       ^l                         ^f

        assertTrue(arb.isFull());
        assertEquals(0, arb.peek());

        arb.dequeue();
        arb.dequeue(); // arb : null, null, 2, 3, 4, 5, 6, 7, 8, 9
                       //        ^l         ^f

        assertEquals(2, arb.peek());
        assertEquals(8, arb.fillCount());
        assertFalse(arb.isFull());
        assertFalse(arb.isEmpty());
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
