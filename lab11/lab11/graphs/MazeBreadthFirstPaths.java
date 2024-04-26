package lab11.graphs;

import java.util.LinkedList;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        // Add more variables here!
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        // Your code here.
        // Don't forget to update distTo, edgeTo, and marked, as well as call announce()
        Queue<Integer> q = new LinkedList<Integer>();

        q.offer(s);
        marked[s] = true;
        announce();

        if (s == t) {
            targetFound = true;
        }

        if (targetFound) {
            return;
        }

        while (!q.isEmpty() && !targetFound) {
            int v = q.poll();
            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    announce();
                    if (w == t) {
                        targetFound = true;
                    }
                    q.offer(w);
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs();
    }
}

