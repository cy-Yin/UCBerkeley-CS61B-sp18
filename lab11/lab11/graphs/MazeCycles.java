package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private boolean hasCycle = false;
    private Maze maze;
    private int[] parent;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        parent = new int[maze.V()];
    }

    @Override
    public void solve() {
        // Your code here!
        int startX = 1;
        int startY = 1;
        int s = maze.xyTo1D(startX, startY);
//        distTo[s] = 0;
//        edgeTo[s] = s;
        parent[s] = s;

        dfsDetectCycle(s);
    }

    // Helper methods go here
    private void dfsDetectCycle(int v) {
        marked[v] = true;
        announce();

        if (hasCycle) {
            return;
        }

        for (int w : maze.adj(v)) {
            if (!marked[w]) {
                parent[w] = v;
//                edgeTo[w] = v;
//                distTo[w] = distTo[v] + 1;
                dfsDetectCycle(w);
            } else if (marked[w] && w != parent[v]) { // form a cycle
                hasCycle = true;

                parent[w] = v;
                edgeTo[w] = v;
//                distTo[w] = distTo[v] + 1;
                announce();

                // draw the cycle
                int temp = v;
                while (temp != w) {
                    edgeTo[temp] = parent[temp];
                    temp = parent[temp];
                    announce();
                }
            }
            if (hasCycle) {
                return;
            }
        }
    }
}

