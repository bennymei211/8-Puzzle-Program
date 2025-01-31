import dsa.Inversions;
import dsa.LinkedQueue;
import dsa.Queue;
import stdlib.In;
import stdlib.StdOut;

// A data type to represent a board in the 8-puzzle game or its generalizations.
public class Board {
    private int[][] tiles; // Tiles in the board
    private int n; // Board size
    private int hamming; // Hamming distance to the goal board
    private int manhattan; // Manhattan distance to the goal board
    private int blankPos; // Position of the blank tile in row-major order

    // Constructs a board from an n x n array; tiles[i][j] is the tile at row i and column j, with 0
    // denoting the blank tile.
    public Board(int[][] tiles) {
        this.tiles = tiles; // Initialize this.tiles to tiles
        n = tiles.length; // Initialize n to the number of rows in tiles
        hamming = 0; // Initialize hamming to 0
        manhattan = 0; // Initialize manhattan to 0
        for (int i = 0; i < n; i++) { // From range [0, n)...
            for (int j = 0; j < n; j++) { // and from range [0. n)...
                int t = tiles[i][j]; // set t to the number at tiles[i][j].
                // Set currvert, to the tile's current vertical(row) position, using (t - 1) / n.
                int currvert = (t - 1) / n;
                // Set currhor, to the tile's current horizontal(column) position, using (t - 1) % n
                int currhor = (t - 1) % n;
                if (t != 0) { // If the tile is not blank(==0)...
                    // and if t is not at its goal position in row-major order...
                    if (t != (n * i + j + 1)) {
                        hamming++; // increment hamming by 1.
                    }
                    // Increment manhattan variable by the manhattan distance of this tile's current
                    // position and its goal position by using their vertical and horizontal
                    // positions.
                    manhattan += Math.abs(i - currvert) + Math.abs(j - currhor);
                }
                if (t == 0) { // If the tile is blank...
                    // reassign blankPos to the blank tile in row-major order(found using n*i+j+1).
                    blankPos = n * i + j + 1;
                }
            }
        }
    }

    // Returns the size of this board.
    public int size() {
        return n; // Return the size of the board.
    }

    // Returns the tile at row i and column j of this board.
    public int tileAt(int i, int j) {
        return tiles[i][j]; // Return tiles at index i, j.
    }

    // Returns Hamming distance between this board and the goal board.
    public int hamming() {
        return hamming; // Return the Hamming distance of this board and the goal board.
    }

    // Returns the Manhattan distance between this board and the goal board.
    public int manhattan() {
        return manhattan; // Return the Manhattan distance of this board and the goal board.
    }

    // Returns true if this board is the goal board, and false otherwise.
    public boolean isGoal() {
        // Return whether hamming and manhattan both equal 0.
        return hamming() == 0 && manhattan() == 0;
    }

    // Returns true if this board is solvable, and false otherwise.
    public boolean isSolvable() {
        int[] rowmajor = new int[n * n - 1]; // Create an array of size n^2 - 1
        int index = 0; // Set index to 0
        for (int i = 0; i < n; i++) { // For i in range [0, n)...
            for (int j = 0; j < n; j++) { // For j in range [0, n)...
                if (tiles[i][j] != 0) { // If tiles at index [i][j] is not blank...
                    // set rowmajor at index (before incrementing) to tiles at index [i][j].
                    rowmajor[index++] = tiles[i][j];
                }
            }
        }
        // If the size of the board is even...
        if (n % 2 == 0) {
            // return whether the sum of inversion count(of row major) and the row of
            // the blank position is odd.
            return (Inversions.count(rowmajor) + ((blankPos - 1) / n)) % 2 != 0;
        } else { // Otherwise(if size of the board is odd)...
            // return whether the inversion count of rowmajor is even.
            return Inversions.count(rowmajor) % 2 == 0;
        }
    }

    // Returns an iterable object containing the neighboring boards of this board.
    public Iterable<Board> neighbors() {
        Queue<Board> q = new LinkedQueue<>(); // Create a queue q of Board objects.
        // Set int variable i, to be the blank tile's current vertical(row) position.
        int i = (blankPos - 1) / n;
        // Set int variable j, to be the blank tile's current horizontal(column) position.
        int j = (blankPos - 1) % n;
        // If there is a tile to the South of the blank tile(the blank position is not in
        // the last row)...
        if (i < n - 1) {
            int[][] s = cloneTiles(); // Clone the tiles of the board.
            // Set variable temp to hold the value at s[i][j](blank tile = 0).
            int temp = s[i][j];
            // Set the current blank position's value to be the value of the South neighbor.
            s[i][j] = s[i + 1][j];
            // Set the value of the South neighbor's position to be the blank tile's value(0).
            s[i + 1][j] = temp;
            Board newboard = new Board(s); // Construct a Board object from the clone...
            q.enqueue(newboard); // and enqueue it into q.
        }
        // If there is a tile to the North of the blank tile(the blank position is not in
        // the first row)...
        if (i > 0) {
            int[][] n = cloneTiles(); // Clone the tiles of the board.
            // Set variable temp to hold the value at n[i][j](blank tile = 0).
            int temp = n[i][j];
            // Set the current blank position's value to be the value of its North neighbor.
            n[i][j] = n[i - 1][j];
            // Set the value of the North neighbor's position to be the blank tile's value(0).
            n[i - 1][j] = temp;
            Board newboard = new Board(n); // Construct a Board object from the clone...
            q.enqueue(newboard); // and enqueue it into q.
        }
        // If there is a tile to the East of the blank tile(the blank position is not in
        // the last column)...
        if (j < n - 1) {
            int[][] e = cloneTiles(); // Clone the tiles of the board.
            // Set variable temp to hold the value at e[i][j](blank tile = 0).
            int temp = e[i][j];
            // Set the current blank position's value to be the value of its East neighbor.
            e[i][j] = e[i][j + 1];
            // Set the value of the East neighbor's position to be the blank tile's value(0).
            e[i][j + 1] = temp;
            Board newboard = new Board(e); // Construct a Board object from the clone...
            q.enqueue(newboard); // and enqueue it into q.
        }
        // If there is a tile to the West of the blank tile(the blank position is not in
        // the first column)...
        if (j > 0) {
            int[][] w = cloneTiles(); // Clone the tiles of the board.
            // Set variable temp to hold the value at w[i][j](blank tile = 0).
            int temp = w[i][j];
            // Set the current blank position's value to be the value of its West neighbor.
            w[i][j] = w[i][j - 1];
            // Set the value of the West neighbor's position to be the blank tile's value(0).
            w[i][j - 1] = temp;
            Board newboard = new Board(w); // Construct a Board object from the clone...
            q.enqueue(newboard); // and enqueue it into q.
        }
        return q;
    }

    // Returns true if this board is the same as other, and false otherwise.
    public boolean equals(Object other) {
        if (other == null) { // If other is null...
            return false; // return false.
        }
        if (other == this) { // If other is equal to this...
            return true; // return true.
        }
        // If the class of other doesn't equal the class of this...
        if (other.getClass() != this.getClass()) {
            return false; // return false.
        }
        for (int i = 0; i < n; i++) { // For i in range[0, n)...
            for (int j = 0; j < n; j++) { // for j in range[0, n)...
                // If the tile at index [i][j] of this(Board) doesn't equal the tile at
                // index [i][j] of other(Board)...
                if (this.tiles[i][j] != ((Board) other).tiles[i][j]) {
                    return false; // return false.
                }
            }
        }
        return true; // Otherwise return true.
    }

    // Returns a string representation of this board.
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2s", tiles[i][j] == 0 ? " " : tiles[i][j]));
                if (j < n - 1) {
                    s.append(" ");
                }
            }
            if (i < n - 1) {
                s.append("\n");
            }
        }
        return s.toString();
    }

    // Returns a defensive copy of tiles[][].
    private int[][] cloneTiles() {
        // Create a new 2d array of integers to store the defensive copy.
        int[][] clone = new int[n][n];
        for (int i = 0; i < n; i++) { // For i in range [0, n)...
            for (int j = 0; j < n; j++) { // for j in range [0. n)...
                clone[i][j] = tiles[i][j]; // assign clone at index [i][j] to tiles at index [i][j].
            }
        }
        return clone; // Return the defensive copy array.
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
        Board board = new Board(tiles);
        StdOut.printf("The board (%d-puzzle):\n%s\n", n, board);
        String f = "Hamming = %d, Manhattan = %d, Goal? %s, Solvable? %s\n";
        StdOut.printf(f, board.hamming(), board.manhattan(), board.isGoal(), board.isSolvable());
        StdOut.println("Neighboring boards:");
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
            StdOut.println("----------");
        }
    }
}
