package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solver {
    private int moves;
    private List<WorldState> solution;
    private MinPQ<SearchNode> searchNodesPQ;
//    private int totalEnqueue;

    private Map<WorldState, Integer> estimatedDistanceFromStateToGoalCache = new HashMap<>();

    /** Constructor which solves the puzzle, computing
     * everything necessary for moves() and solution() to
     * not have to solve the problem again. Solves the
     * puzzle using the A* algorithm. Assumes a solution exists.
     */
    public Solver(WorldState initial) {
        moves = 0;
        solution = new ArrayList<>();
        searchNodesPQ = new MinPQ<>();

        // initialize the PQ
        SearchNode initialSearchNode = new SearchNode(initial, 0, null);
        searchNodesPQ.insert(initialSearchNode);

//        totalEnqueue = 1;

        SearchNode x = searchNodesPQ.delMin();
        while (!x.worldState.isGoal()) {
            for (WorldState neighbor : x.worldState.neighbors()) {
                // Optimizations: do not re-add the parent which is also one of the neighbors
                if (x.prevSearchNode != null && neighbor.equals(x.prevSearchNode.worldState)) {
                    continue;
                }
                SearchNode neighborSearchNode = new SearchNode(neighbor, x.steps + 1, x);
                searchNodesPQ.insert(neighborSearchNode);
//                totalEnqueue += 1;
            }
            x = searchNodesPQ.delMin();
        }

        moves = x.steps;
        while (x != null) {
            /* Sets the ArrayList.add() method's index equal to 0 to implement addFirst(),
             * namely always adding the prev world state before the current one.
             * So that the iterator will iterate from initial world state
             * and end with the goal world state.
             */
            solution.add(0, x.worldState);
            x = x.prevSearchNode;
        }
    }

    private class SearchNode implements Comparable<SearchNode> {
        private WorldState worldState;
        private int steps; // the number of moves made to reach this state from the initial state
        private SearchNode prevSearchNode; // a reference to the previous search node

        SearchNode(WorldState ws, int s, SearchNode prevSN) {
            worldState = ws;
            steps = s;
            prevSearchNode = prevSN;
        }

        /** For the A* algorithm, the priority equals to
         * the sum of
         * the number of moves made to reach the current world state from the initial state
         * and the world state's estimated distance to the goal world state.
         */
        @Override
        public int compareTo(SearchNode o) {
            return (steps + getEstimatedDistanceToGoal())
                    - (o.steps + o.getEstimatedDistanceToGoal());
        }

        /** Returns the estimated distance from the current state to the goal state
         *
         *  To avoid recomputing the estimatedDistanceToGoal() result from scratch each time
         *  during various priority queue operations, compute it at most once per object;
         *  save its value in a HashMap estimatedDistanceFromStateToGoalCache
         */
        private int getEstimatedDistanceToGoal() {
            if (!estimatedDistanceFromStateToGoalCache.containsKey(worldState)) {
                estimatedDistanceFromStateToGoalCache.put(worldState,
                                            worldState.estimatedDistanceToGoal());
            }
            return estimatedDistanceFromStateToGoalCache.get(worldState);
        }
    }

    /** Returns the minimum number of moves to solve the puzzle starting
     *  at the initial WorldState.
     */
    public int moves() {
        return moves;
    }

    /** Returns a sequence of WorldStates from the initial WorldState
     * to the solution.
     */
    public Iterable<WorldState> solution() {
        return solution;
    }

//    /** Returns the total number of items enqueued by solver
//     *  API for hw4.puzzle.CommonBugDetector.
//     */
//    public int getTotalEnqueue() {
//        return totalEnqueue;
//    }
}
