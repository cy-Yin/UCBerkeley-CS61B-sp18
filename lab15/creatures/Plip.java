package creatures;
import huglife.Creature;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.HugLifeUtils;
import java.awt.Color;
import java.util.Map;
import java.util.List;

/** An implementation of a motile pacifist photosynthesizer.
 *  @author Josh Hug
 */
public class Plip extends Creature {

    /** red color. */
    private int r;
    /** green color. */
    private int g;
    /** blue color. */
    private int b;

    /** probability of taking a move when ample space available. */
    private double moveProbability = 0.5;

    /** Plips will lose 0.15 units of energy on a MOVE action. */
    private static double energyLostWithMove = 0.15;
    /** Plips will gain 0.2 units of energy on a STAY action. */
    private static double energyGainWithStay = 0.2;
    /** Plips should never have energy greater than 2.
     *  If an action would cause the Plip to have energy greater than 2,
     *  then it should be set to 2 instead.
     */
    private static double maxEnergy = 2.0;

    /* When a Plip replicates, it keeps 50% of its energy.
     * The other 50% goes to its offspring.
     * No energy is lost in the replication process.
     */
    /** fraction of energy to retain when replicating. */
    private double repEnergyRetained = 0.5;
    /** fraction of energy to bestow upon offspring. */
    private double repEnergyGiven = 0.5;

    /** creates plip with energy equal to E. */
    public Plip(double e) {
        super("plip");
        r = 0;
        g = 0;
        b = 0;
        energy = e;
    }

    /** creates a plip with energy equal to 1. */
    public Plip() {
        this(1);
    }

    /** Should return a color with red = 99, blue = 76, and green that varies
     *  linearly based on the energy of the Plip. If the plip has zero energy,
     *  it should have a green value of 63. If it has max energy, it should
     *  have a green value of 255. The green value should vary with energy
     *  linearly in between these two extremes. It's not absolutely vital
     *  that you get this exactly correct.
     */
    public Color color() {
        r = 99;
        b = 76;
        g = 63 + (int) (energy / maxEnergy * (255 - 63));
        return color(r, g, b);
    }

    /** Do nothing with C, Plips are pacifists. */
    public void attack(Creature c) {
    }

    /** Plips should lose 0.15 units of energy when moving. If you want to
     *  avoid the magic number warning, you'll need to make a
     *  private static final variable. This is not required for this lab.
     */
    public void move() {
        energy -= energyLostWithMove;
    }


    /** Plips gain 0.2 energy when staying due to photosynthesis. */
    public void stay() {
        energy += energyGainWithStay;
        if (energy > maxEnergy) {
            energy = maxEnergy;
        }
    }

    /** Plips and their offspring each get 50% of the energy, with none
     *  lost to the process. Now that's efficiency! Returns a baby
     *  Plip.
     */
    public Plip replicate() {
        double babyEnergy = energy * repEnergyGiven;
        energy = energy * repEnergyRetained;
        return new Plip(babyEnergy);
    }

    /** Plips take exactly the following actions based on NEIGHBORS:
     *  1. If no empty adjacent spaces, STAY.
     *  2. Otherwise, if energy >= 1, REPLICATE.
     *  3. Otherwise, if any Cloruses, MOVE with 50% probability.
     *  4. Otherwise, if nothing else, STAY
     *
     *  Returns an object of type Action. See Action.java for the
     *  scoop on how Actions work. See SampleCreature.chooseAction()
     *  for an example to follow.
     */
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        List<Direction> cloruses = getNeighborsOfType(neighbors, "clorus");

        if (empties.isEmpty()) { // If there are no empty spaces, the Plip should STAY.
            return new Action(Action.ActionType.STAY);
        } else if (energy >= 1.0) {
            /* If the Plip has energy greater than 1.0,
             * it should replicate to an available space.
             */
            Direction repDir = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.REPLICATE, repDir);
        } else if (!cloruses.isEmpty()) {
            /* if it sees a neighbor with name() equal to "clorus",
             * it will move to any available empty square with probability 50%.
             * It should choose the empty square randomly.
             *
             * As an example, if it sees a Clorus to the LEFT and to the BOTTOM,
             * and "empty" to the TOP and RIGHT, there is a 50% chance it will move
             * (due to fear of Cloruses), and if it does move,
             * it will pick randomly between RIGHT and TOP.
             */
            if (HugLifeUtils.random() < moveProbability) {
                Direction moveDir = HugLifeUtils.randomEntry(empties);
                return new Action(Action.ActionType.MOVE, moveDir);
            }
        }
        return new Action(Action.ActionType.STAY);
    }
}
