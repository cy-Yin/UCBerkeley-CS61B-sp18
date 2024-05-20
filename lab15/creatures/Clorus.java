package creatures;
import huglife.Creature;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.HugLifeUtils;
import java.awt.Color;
import java.util.Map;
import java.util.List;

public class Clorus extends Creature {
    /** red color. */
    private int r;
    /** green color. */
    private int g;
    /** blue color. */
    private int b;

    /** Cloruses will lose 0.03 units of energy on a MOVE action. */
    private static double energyLostWithMove = 0.03;
    /** Cloruses will lose 0.01 units of energy on a STAY action. */
    private static double energyLostWithStay = 0.01;

    /* When a Clorus replicates, it keeps 50% of its energy.
     * The other 50% goes to its offspring.
     * No energy is lost in the replication process.
     */
    /** fraction of energy to retain when replicating. */
    private double repEnergyRetained = 0.5;
    /** fraction of energy to bestow upon offspring. */
    private double repEnergyGiven = 0.5;

    /** creates clorus with energy equal to E. */
    public Clorus(double e) {
        super("clorus");
        r = 0;
        g = 0;
        b = 0;
        energy = e;
    }

    /** creates a clorus with energy equal to 1. */
    public Clorus() {
        this(1);
    }

    /** Should always return the color red = 34, green = 0, blue = 231.
     */
    public Color color() {
        r = 34;
        g = 0;
        b = 231;
        return color(r, g, b);
    }

    /** If a Clorus attacks another creature, it should gain that creature’s energy. */
    public void attack(Creature c) {
        energy += c.energy();
    }

    /** Cloruses should lose 0.03 units of energy when moving. */
    public void move() {
        energy -= energyLostWithMove;
    }


    /** Cloruses should lose 0.01 units of energy when moving. */
    public void stay() {
        energy -= energyLostWithStay;
    }

    /** Cloruses and their offspring each get 50% of the energy, with none
     *  lost to the process. Now that's efficiency! Returns a baby
     *  Clorus.
     */
    public Clorus replicate() {
        double babyEnergy = energy * repEnergyGiven;
        energy = energy * repEnergyRetained;
        return new Clorus(babyEnergy);
    }

    /** Cloruses should obey exactly the following behavioral rules: <br>
     * 1. If there are no empty squares, the Clorus will STAY
     *      (even if there are Plips nearby they could attack).<br>
     * 2. Otherwise, if any Plips are seen, the Clorus will ATTACK one of them randomly.<br>
     * 3. Otherwise, if the Clorus has energy greater than or equal to one,
     *      it will REPLICATE to a random empty square.<br>
     * 4. Otherwise, the Clorus will MOVE to a random empty square.<br>
     */
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        List<Direction> plips = getNeighborsOfType(neighbors, "plip");

        if (empties.isEmpty()) {
            return new Action(Action.ActionType.STAY);
        } else if (!plips.isEmpty()) {
            Direction attackDir = HugLifeUtils.randomEntry(plips);
            return new Action(Action.ActionType.ATTACK, attackDir);
        } else if (energy >= 1.0) {
            Direction repDir = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.REPLICATE, repDir);
        }
        Direction moveDir = HugLifeUtils.randomEntry(empties);
        return new Action(Action.ActionType.MOVE, moveDir);
    }
}
