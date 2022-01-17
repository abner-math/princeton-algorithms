import java.util.Comparator;
import java.util.LinkedList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private LinkedList<Board> solution;
    private boolean isSolvable;
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        MinPQ<LinkedList<Board>> queue = this.getInitialQueue(initial);
        MinPQ<LinkedList<Board>> queueTwin = this.getInitialQueue(initial.twin());
        while (true) {
            if (queue.min().peekLast().isGoal()) {
                this.solution = queue.delMin();
                this.isSolvable = true;
                break;
            } else if (queueTwin.min().peekLast().isGoal()) {
                this.isSolvable = false;
                break;
            }
            
            this.addNeighborsToTheQueue(queue);
            this.addNeighborsToTheQueue(queueTwin);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
         if (!this.isSolvable()) return -1;
         return this.solution.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!this.isSolvable()) return null;
        return this.solution;
    }
    
    private MinPQ<LinkedList<Board>> getInitialQueue(Board board) {
        MinPQ<LinkedList<Board>> queue = new MinPQ<>(new Comparator<LinkedList<Board>>() {
            public int value(LinkedList<Board> o) {
                return o.peekLast().manhattan() + o.size();
            }
            @Override
            public int compare(LinkedList<Board> o1, LinkedList<Board> o2) {
                return this.value(o1) - this.value(o2);
            }
        });
	    
        LinkedList<Board> list = new LinkedList<>();
        list.add(board);
        queue.insert(list);
        return queue;
    }
    
    private Board getPreviousStep(LinkedList<Board> list) {
        LinkedList<Board> copy = (LinkedList<Board>) list.clone();
        copy.removeLast();
        if (copy.size() > 0)  return copy.peekLast();
        return null;
    }
    
    private void addNeighborsToTheQueue(MinPQ<LinkedList<Board>> queue) {
        LinkedList<Board> steps = queue.delMin();
        Board previousStep = this.getPreviousStep(steps);
        for (Board neighbor : steps.peekLast().neighbors()) {
            if (!neighbor.equals(previousStep)) {
                LinkedList<Board> copy = (LinkedList) steps.clone();
                copy.add(neighbor);
                queue.insert(copy);
            }
        }
    }
    
    // test client (see below) 
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
	} else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
	    }
        }
    }

}
