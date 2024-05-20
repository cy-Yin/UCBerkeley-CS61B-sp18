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

import static org.junit.Assert.assertEquals;

public class TestClorus {
    @Test
    public void testBasics() {
        Clorus c = new Clorus(2);
        assertEquals(2, c.energy(), 0.01);
        assertEquals(new Color(34, 0, 231), c.color());
        c.move();
        assertEquals(1.97, c.energy(), 0.01);
        c.move();
        assertEquals(1.94, c.energy(), 0.01);
        c.stay();
        assertEquals(1.93, c.energy(), 0.01);
        c.stay();
        assertEquals(1.92, c.energy(), 0.01);
    }

    @Test
    public void testAttack() {
        Clorus c = new Clorus(2);
        Plip p1 = new Plip(0.5);
        c.attack(p1);
        assertEquals(2.5, c.energy(), 0.01);
        Plip p2 = new Plip(1.2);
        c.attack(p2);
        assertEquals(3.7, c.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        Clorus c = new Clorus(2);

        Clorus babyC = c.replicate();

        assertEquals(1, c.energy(), 0.01);
        assertEquals(1, babyC.energy(), 0.01);

        assertNotSame(c, babyC);

    }

    @Test
    public void testChoose() {
        // test the first action choice
        Clorus c1 = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded1 = new HashMap<Direction, Occupant>();
        surrounded1.put(Direction.TOP, new Impassible());
        surrounded1.put(Direction.BOTTOM, new Impassible());
        surrounded1.put(Direction.LEFT, new Impassible());
        surrounded1.put(Direction.RIGHT, new Impassible());

        Action actual1 = c1.chooseAction(surrounded1);
        Action expected1 = new Action(Action.ActionType.STAY);

        assertEquals(expected1, actual1);

        Clorus c2 = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded2 = new HashMap<Direction, Occupant>();
        surrounded2.put(Direction.TOP, new Impassible());
        surrounded2.put(Direction.BOTTOM, new Plip(1));
        surrounded2.put(Direction.LEFT, new Impassible());
        surrounded2.put(Direction.RIGHT, new Impassible());

        Action actual2 = c2.chooseAction(surrounded2);
        Action expected2 = new Action(Action.ActionType.STAY);

        assertEquals(expected2, actual2);

        // test the second action choice
        Clorus c3 = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded3 = new HashMap<Direction, Occupant>();
        surrounded3.put(Direction.TOP, new Empty());
        surrounded3.put(Direction.BOTTOM, new Plip(1));
        surrounded3.put(Direction.LEFT, new Plip(1));
        surrounded3.put(Direction.RIGHT, new Impassible());

        Action actual3 = c3.chooseAction(surrounded3);
        Action expected3Bottom = new Action(Action.ActionType.ATTACK, Direction.BOTTOM);
        Action expected3Left = new Action(Action.ActionType.ATTACK, Direction.LEFT);

        assertTrue(expected3Bottom.equals(actual3) || expected3Left.equals(actual3));

        //test the third action choice
        Clorus c4 = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded4 = new HashMap<Direction, Occupant>();
        surrounded4.put(Direction.TOP, new Impassible());
        surrounded4.put(Direction.BOTTOM, new Impassible());
        surrounded4.put(Direction.LEFT, new Empty());
        surrounded4.put(Direction.RIGHT, new Impassible());

        Action actual4 = c4.chooseAction(surrounded4);
        Action expected4 = new Action(Action.ActionType.REPLICATE, Direction.LEFT);

        assertEquals(expected4, actual4);

        Clorus c5 = new Clorus(0.2);
        HashMap<Direction, Occupant> surrounded5 = new HashMap<Direction, Occupant>();
        surrounded5.put(Direction.TOP, new Impassible());
        surrounded5.put(Direction.BOTTOM, new Impassible());
        surrounded5.put(Direction.LEFT, new Empty());
        surrounded5.put(Direction.RIGHT, new Impassible());

        Action actual5 = c5.chooseAction(surrounded5);
        Action expected5 = new Action(Action.ActionType.MOVE, Direction.LEFT);

        assertEquals(expected5, actual5);
    }

    public static void main(String[] args) {
        System.exit(jh61b.junit.textui.runClasses(TestClorus.class));
    }
}
