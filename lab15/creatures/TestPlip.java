package creatures;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.awt.Color;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.Impassible;
import huglife.Empty;

/** Tests the plip class   
 *  @authr FIXME
 */

public class TestPlip {

    /* Replace with the magic word given in lab.
     * If you are submitting early, just put in "early" */
    public static final String MAGIC_WORD = "early";

    @Test
    public void testBasics() {
        Plip p = new Plip(2);
        assertEquals(2, p.energy(), 0.01);
        assertEquals(new Color(99, 255, 76), p.color());
        p.move();
        assertEquals(1.85, p.energy(), 0.01);
        p.move();
        assertEquals(1.70, p.energy(), 0.01);
        p.stay();
        assertEquals(1.90, p.energy(), 0.01);
        p.stay();
        assertEquals(2.00, p.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        Plip p = new Plip(2);
        assertEquals(new Color(99, 255, 76), p.color());

        Plip babyP = p.replicate();

        assertEquals(1, p.energy(), 0.01);
        assertEquals(1, babyP.energy(), 0.01);

        assertEquals(new Color(99, 159, 76), p.color());
        assertEquals(new Color(99, 159, 76), babyP.color());

        assertNotSame(p, babyP);

    }

    @Test
    public void testChoose() {
        Plip p = new Plip(1.2);
        // test the first action choice
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        //You can create new empties with new Empty();
        //Despite what the spec says, you cannot test for Cloruses nearby yet.
        //Sorry!  

        Action actual = p.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);

        // test the second action choice
        HashMap<Direction, Occupant> surrounded2 = new HashMap<Direction, Occupant>();
        surrounded2.put(Direction.TOP, new Impassible());
        surrounded2.put(Direction.BOTTOM, new Empty());
        surrounded2.put(Direction.LEFT, new Empty());
        surrounded2.put(Direction.RIGHT, new Impassible());

        Action actual2 = p.chooseAction(surrounded2);
        Action expected2Bottom = new Action(Action.ActionType.REPLICATE, Direction.BOTTOM);
        Action expected2Left = new Action(Action.ActionType.REPLICATE, Direction.LEFT);
        assertTrue(expected2Bottom.equals(actual2) || expected2Left.equals(actual2));


        Plip pWithLowEnergy = new Plip(0.2);
        Action actual3 = pWithLowEnergy.chooseAction(surrounded2);
        Action expected3 = new Action(Action.ActionType.STAY);
        assertEquals(expected3, actual3);

        // test the third action choice
        Plip pWithLowEnergy2 = new Plip(0.2);
        HashMap<Direction, Occupant> surrounded4 = new HashMap<Direction, Occupant>();
        surrounded4.put(Direction.TOP, new Clorus(2));
        surrounded4.put(Direction.BOTTOM, new Empty());
        surrounded4.put(Direction.LEFT, new Empty());
        surrounded4.put(Direction.RIGHT, new Impassible());
        Action actual4 = pWithLowEnergy2.chooseAction(surrounded4);
        Action expected4Bottom = new Action(Action.ActionType.MOVE, Direction.BOTTOM);
        Action expected4Left = new Action(Action.ActionType.MOVE, Direction.LEFT);
        Action expected4Stay = new Action(Action.ActionType.STAY);
        assertTrue(expected4Bottom.equals(actual4) || expected4Left.equals(actual4)
                || expected4Stay.equals(actual4));
    }

    public static void main(String[] args) {
        System.exit(jh61b.junit.textui.runClasses(TestPlip.class));
    }
} 
