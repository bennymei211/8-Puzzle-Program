import dsa.LinkedStack;
import dsa.MinPQ;
import stdlib.In;
import stdlib.StdOut;

// A data type that implements the A* algorithm for solving the 8-puzzle and its generalizations.
public class Solver {
    private int moves; // Minimum number of moves needed to solve the initial board

    private LinkedStack<Board> solution; // Sequence of boards in shortest solution of initial board

    // Finds a solution to the initial board using the A* algorithm.
    public Solver(Board board) {
        if (board == null) { // If board is null...
            // throw new NullPointerException.
            throw new NullPointerException("board is null");
        }
        if (!board.isSolvable()) { // If the board is unsolvable...
            // throw new IllegalArgumentException.
            throw new IllegalArgumentException("board is unsolvable");
        }
        // Create a MinPQ<SearchNode> object pq...
        MinPQ<SearchNode> pq = new MinPQ<>();
        // and insert the initial SearchNode into it.
        pq.insert(new SearchNode(board, 0, null));
        // As long as pq is not empty:
        while (!pq.isEmpty()) {
            // Remove the smallest node (call it node) from pq.
            SearchNode node = pq.delMin();
            // If the board in node is the goal board:
            if (node.board.isGoal()) {
                // Extract from the node the number of moves in the solution and store it in
                // the instance variable, moves.
                moves = node.moves;
                // Initialize solution to a new LinkedStack<board> object to store the
                // sequence of boards in the shortest solution of the initial board.
                solution = new LinkedStack<>();
                // Traverse backwards from the goal SearchNode until its previous is null...
                for (SearchNode i = node; i.previous != null; i = i.previous) {
                    // and push the SearchNode's board onto LinkedStack, solution.
                    solution.push(i.board);
                }
                break; // Break
            } else { // Otherwise:
                // Iterate over the neighboring boards of node.board...
                for (Board i : node.board.neighbors()) {
                    // and if node.previous is null or the neighbor is not equal to
                    // its previous node's board...
                    if (node.previous == null || !i.equals(node.previous.board)) {
                        // Insert a new SearchNode object into pq, constructed using
                        // appropriate values.
                        pq.insert(new SearchNode(i, node.moves + 1, node));
                    }
                }
            }
        }
    }

    // Returns the minimum number of moves needed to solve the initial board.
    public int moves() {
        return moves; // Return instance variable, solution.
    }

    // Returns a sequence of boards in a shortest solution of the initial board.
    public Iterable<Board> solution() {
        return solution; // Return instance variable, solution.
    }

    // A data type that represents a search node in the grame tree. Each node includes a
    // reference to a board, the number of moves to the node from the initial node, and a
    // reference to the previous node.
    private class SearchNode implements Comparable<SearchNode> {
        private Board board; // The board represented by this node
        private int moves; // Number of moves it took to get to this node from initial node
        private SearchNode previous; // The previous search node

        // Constructs a new search node.
        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board; // Initialize this.board to board
            this.moves = moves; // Initialize this.moves to moves
            this.previous = previous; // Initialize this.previous to previous
        }

        // Returns a comparison of this node and other based on the following sum:
        //   Manhattan distance of the board in the node + the # of moves to the node
        public int compareTo(SearchNode other) {
            // Set variable thismoves to the manhattan distance of this.board
            // and the minimum number of moves needed to solve board, this.
            int thismoves = this.board.manhattan() + this.moves;
            // Set variable othermoves to the manhattan distance of other.board
            // and the minimum number of moves needed to solve the board, other.
            int othermoves = other.board.manhattan() + other.moves;
            // Use Integer.compare to return a comparison of this node and other based on the
            // following sum of the Manhattan distance of the board in the node and the # of
            // moves to the node.
            return Integer.compare(thismoves, othermoves);
        }
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);
        if (initial.isSolvable()) {
            Solver solver = new Solver(initial);
            StdOut.printf("Solution (%d moves):\n", solver.moves());
            StdOut.println(initial);
            StdOut.println("----------");
            for (Board board : solver.solution()) {
                StdOut.println(board);
                StdOut.println("----------");
            }
        } else {
            StdOut.println("Unsolvable puzzle");
        }
    }
}
