// Accepts filenames (Strings) as command-line arguments; creates an initial board from each
// file; and writes to standard output: the filename, minimum number of moves to reach the goal
// board from the initial board, and the time (in secs) taken. If the initial board is
// unsolvable, a "--" is written for the number of moves and time taken.

import stdlib.In;
import stdlib.StdOut;
import stdlib.Stopwatch;

public class PuzzleChecker {
    // Entry point.
    public static void main(String[] args) {
        StdOut.printf("%-35s %7s %8s\n", "filename", "moves", "time");
        StdOut.println("----------------------------------------------------");
        for (String filename : args) {
            In in = new In(filename);
            int n = in.readInt();
            int[][] blocks = new int[n][n];
            for (int row = 0; row < n; row++) {
                for (int col = 0; col < n; col++) {
                    blocks[row][col] = in.readInt();
                }
            }
            Board initial = new Board(blocks);
            if (initial.isSolvable()) {
                Stopwatch timer = new Stopwatch();
                Solver solver = new Solver(initial);
                double time = timer.elapsedTime();
                int moves = solver.moves();
                StdOut.printf("%-35s %7s %8.2f\n", filename, moves, time);
            } else {
                StdOut.printf("%-35s %7s %8s\n", filename, "--", "--");
            }
        }
    }
}
