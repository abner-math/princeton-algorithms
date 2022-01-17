import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

	// size of the grid
	private final int gridSize;

	// the union-find implementation
	private final WeightedQuickUnionUF unionFind;

	// number of open sites
	private int numberOfOpenSites;

	// boolean array to indicate whether a site is open or not
	private boolean[] openSites;

	// creates n-by-n grid, with all sites initially blocked
	public Percolation(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("Grid size cannot be smaller or equal to zero.");
		}

		this.gridSize = n;
		this.numberOfOpenSites = 0;
		this.openSites = new boolean[n * n];
		for (int i = 0; i < n * n; i++) {
			this.openSites[i] = false;
		}

		// connect first and last row to virtual cells
		this.unionFind = new WeightedQuickUnionUF(n * n + 2);
		for (int i = 0; i < n; i++) {
			this.unionFind.union(this.getIndex(1, i + 1), n * n);
			this.unionFind.union(this.getIndex(n, i + 1), n * n + 1);
		}
	}

	// opens the site (row, col) if it is not open already
	public void open(int row, int col) {
		if (this.isOpen(row, col)) {
			return;
		}

		int index = this.getIndex(row, col);
		this.openSites[index] = true;
		++this.numberOfOpenSites;

		if (row > 1 && this.isOpen(row - 1, col)) {
			this.unionFind.union(index, this.getIndex(row - 1, col));
		}

		if (col > 1 && this.isOpen(row, col - 1)) {
			this.unionFind.union(index, this.getIndex(row, col - 1));
		}

		if (row < this.gridSize && this.isOpen(row + 1, col)) {
			this.unionFind.union(index, this.getIndex(row + 1, col));
		}

		if (col < this.gridSize && this.isOpen(row, col + 1)) {
			this.unionFind.union(index, this.getIndex(row, col + 1));
		}
	}

	// is the site (row, col) open?
	public boolean isOpen(int row, int col) {
		return this.openSites[this.getIndex(row, col)];
	}

	// is the site (row, col) full?
	public boolean isFull(int row, int col) {
		return this.isOpen(row, col) && this.unionFind.find(this.getIndex(row, col)) == this.unionFind.find(this.gridSize * this.gridSize);
	}

	// returns the number of open sites
	public int numberOfOpenSites() {
		return this.numberOfOpenSites;
	}

	// does the system percolate?
	public boolean percolates() {
		return this.unionFind.find(this.gridSize * this.gridSize) == this.unionFind
				.find(this.gridSize * this.gridSize + 1);
	}

	private int getIndex(int row, int col) {
		if (row < 1 || col < 1 || row > this.gridSize || col > this.gridSize) {
			throw new IllegalArgumentException("Row and col must be between 1 and " + this.gridSize + ".");
		}

		return (col - 1) + (row - 1) * this.gridSize;
	}
}