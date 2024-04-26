package lab11.graphs;

import edu.princeton.cs.algs4.MinPQ;

import java.util.Comparator;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target.
     *  Uses the manhattan distance to estimate the distance:
     *  abs(vX - tX) + abs(vY - tY)
     *  @param v the current position
     */
    private int h(int v) {
        int vX = maze.toX(v);
        int vY = maze.toY(v);
        int tX = maze.toX(t);
        int tY = maze.toY(t);
        return Math.abs(vX - tX) + Math.abs(vY - tY);
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int source) {
        MinPQ<Integer> pq = new MinPQ<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                // if o1's A* distance is smaller than o2's, then o1 has a higher priority than o2
                // which means o1 will be polled earlier than o2 in minPQ.
                return (distTo[o1] + h(o1)) - (distTo[o2] + h(o2));
            }
        });

        pq.insert(source);
        marked[source] = true;
        announce();

        if (source == t) {
            targetFound = true;
        }

        if (targetFound) {
            return;
        }

        while (!pq.isEmpty() && !targetFound) {
            int v = pq.delMin();
            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    announce();
                    if (w == t) {
                        targetFound = true;
                    }
                    pq.insert(w);
                }
            }
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

