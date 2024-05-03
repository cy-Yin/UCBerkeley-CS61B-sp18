import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Solver {
    private double distance;
    private boolean isGoal;
    private List<Long> solution;
    private PriorityQueue<SearchNode> searchNodesPQ;
    private Set<Long> marked;
    private Map<Long, Double> estimatedDistanceFromCurrToGoalCache = new HashMap<>();

    private class SearchNode implements Comparable<SearchNode> {
        private double distanceFromStart; // the distance made to reach this node from start
        private double estimatedDistanceToEnd; // distance from the current node to end
        private SearchNode prevSearchNode; // a reference to the previous search node
        private GraphDB.Node node;

        SearchNode(GraphDB.Node node, double distanceFromStart, SearchNode prevSearchNode,
                                                    GraphDB.Node start, GraphDB.Node end) {
            this.node = node;
            this.prevSearchNode = prevSearchNode;
            this.distanceFromStart = distanceFromStart;

            if (!estimatedDistanceFromCurrToGoalCache.containsKey(node.id)) {
                estimatedDistanceFromCurrToGoalCache.put(node.id,
                        GraphDB.distance(node.lon, node.lat, end.lon, end.lat));
            }
            this.estimatedDistanceToEnd = estimatedDistanceFromCurrToGoalCache.get(node.id);
        }

        /** For the A* algorithm, the priority equals to
         * the sum of
         * the distance made to reach the current node from the start node
         * and the current node's estimated distance to the end node.
         */
        @Override
        public int compareTo(SearchNode o) {
            return Double.compare((this.distanceFromStart + this.estimatedDistanceToEnd),
                                    (o.distanceFromStart + o.estimatedDistanceToEnd));
        }

        /** Returns true if these two share the same node id. */
        @Override
        public boolean equals(Object y) {
            if (this == y) {
                return true;
            }
            if (y == null) {
                return false;
            }
            if (this.getClass() != y.getClass()) {
                return false;
            }
            SearchNode sy = (SearchNode) y;
            return this.node.id == sy.node.id;
        }

        @Override
        public int hashCode() {
            return (int) node.id;
        }
    }

    /** Constructor which finds the shortest path from start to end nodes
     * Find the path using the A* algorithm. Assumes a solution exists.
     */
    public Solver(GraphDB g, GraphDB.Node start, GraphDB.Node end) {
        distance = 0;
        isGoal = false;
        solution = new ArrayList<>();
        searchNodesPQ = new PriorityQueue<>();
        marked = new HashSet<>();

        // initialize the PQ
        SearchNode searchNode = new SearchNode(start, distance, null, start, end);
        searchNodesPQ.add(searchNode);

        if (start.id == end.id) {
            isGoal = true;
        }

        while (!searchNodesPQ.isEmpty() && !isGoal) {
            searchNode = searchNodesPQ.poll();
            marked.add(searchNode.node.id);
            if (searchNode.node.id == end.id) {
                isGoal = true;
            }
            for (long neighborId : searchNode.node.neighbors) {
                if (marked.contains(neighborId)) {
                    continue;
                }
                distance = searchNode.distanceFromStart
                        + g.distance(neighborId, searchNode.node.id);
                SearchNode neighborSearchNode = new SearchNode(g.nodes.get(neighborId),
                                                distance, searchNode, start, end);
                searchNodesPQ.add(neighborSearchNode);
            }
        }

        // After finding the shortest path (isGoal = true, add the path to the solution)
        while (searchNode != null) {
            /* Sets the ArrayList.add() method's index equal to 0 to implement addFirst(),
             * namely always adding the prev node before the current one.
             * So that the iterator will iterate from start node
             * and end with the target/end node.
             */
            solution.add(0, searchNode.node.id);
            searchNode = searchNode.prevSearchNode;
        }
    }

    /** Returns the solution, namely the shortest path from start to end. */
    public List<Long> solver() {
        return solution;
    }
}
