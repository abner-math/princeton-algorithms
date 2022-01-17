import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.StdRandom;

public class Board {

    private int[][] tiles;
    private int manhattanDistance;
    private int hammingDistance; 
    private Board twin;
    
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = tiles;
        this.tiles = this.cloneTiles();
        this.manhattanDistance = this.manhattanInternal();
        this.hammingDistance = this.hammingInternal();
    }
    
    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.tiles.length).append(System.lineSeparator());
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                if (j > 0) sb.append(" ");
                sb.append(this.tiles[i][j]);
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return this.tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        return this.hammingDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return this.manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || !(y instanceof Board)) return false;
        Board anotherBoard = (Board) y;
        if (this.dimension() != anotherBoard.dimension()) return false;
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                if (this.tiles[i][j] != anotherBoard.tiles[i][j]) return false;
            }
        }
	    
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int x = this.currentBlankPositionX();
        int y = this.currentBlankPositionY();
        return this.neighborsInternal(x, y);
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (this.twin == null) this.twin = this.twinInternal();
        return this.twin;
    }
    
    private Board twinInternal() {
        int x0, y0;
        do
        {
            x0 = StdRandom.uniform(this.dimension());
            y0 = StdRandom.uniform(this.dimension());
        } while (this.tiles[y0][x0] == 0);
        
        int x1, y1;
        do
        {
            x1 = StdRandom.uniform(this.dimension());
            y1 = StdRandom.uniform(this.dimension());
        } while (this.tiles[y1][x1] == 0 || this.tiles[y1][x1] == this.tiles[y0][x0]);
     
        return this.swapAndCreateNewBoard(x0, y0, x1, y1);
    }

    private int hammingInternal() {
        int countMisplacedTiles = 0;
        int correctPosition = 1;
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                if (this.tiles[i][j] != 0 && this.tiles[i][j] != correctPosition) {
                    ++countMisplacedTiles;
                } 
                ++correctPosition;
            }
        }
	    
        return countMisplacedTiles;
    }

    private int manhattanInternal() {
        int sumDistances = 0;
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                int expectedX, expectedY;
                if (this.tiles[i][j] != 0) {
                    expectedX = (this.tiles[i][j] - 1) % this.dimension();
                    expectedY = (this.tiles[i][j] - 1) / this.dimension();
                    sumDistances += Math.abs(j - expectedX) + Math.abs(i - expectedY);
                }
            }
        }
	    
        return sumDistances;
    }
    
    private int currentBlankPositionX() {
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                if (this.tiles[i][j] == 0) return j;
            }
        }
	    
        return -1;
    }
    
    private int currentBlankPositionY() {
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                if (this.tiles[i][j] == 0) return i;
            }
        }
	    
        return -1;
    }
    
    private int[][] cloneTiles() {
        int[][] copy = new int[this.dimension()][this.dimension()];
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                copy[i][j] = this.tiles[i][j];
            }
        }
	    
        return copy;
    }
    
    private void swapTiles(int[][] tiles, int x0, int y0, int x1, int y1) {
        int temp = tiles[y0][x0];
        tiles[y0][x0] = tiles[y1][x1];
        tiles[y1][x1] = temp;
    }
    
    private Board swapAndCreateNewBoard(int x0, int y0, int x1, int y1) {
        int[][] copy = this.cloneTiles();
        this.swapTiles(copy, x0, y0, x1, y1);
        return new Board(copy);
    }
    
    private List<Board> neighborsInternal(int x, int y) {
        List<Board> neighbors = new ArrayList<>();
        if (x > 0) neighbors.add(this.swapAndCreateNewBoard(x, y, x - 1, y));
        if (y > 0) neighbors.add(this.swapAndCreateNewBoard(x, y, x, y - 1));
        if (x < this.dimension() - 1) neighbors.add(this.swapAndCreateNewBoard(x, y, x + 1, y));
        if (y < this.dimension() - 1) neighbors.add(this.swapAndCreateNewBoard(x, y, x, y + 1));
        return neighbors;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        
    }

}
