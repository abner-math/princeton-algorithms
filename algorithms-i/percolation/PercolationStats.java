import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

	private static final double CONFIDENCE_INTERVAL = 1.96;
	private final double[] thresholds;

	// perform independent trials on an n-by-n grid
	public PercolationStats(int n, int trials) {
		if (n <= 0) {
			throw new IllegalArgumentException("Grid size cannot be smaller or equal to zero.");
		} else if (trials <= 0) {
			throw new IllegalArgumentException("Number of trials cannot be smaller or equal to zero.");
		}

		this.thresholds = new double[trials];
		for (var i = 0; i < trials; i++) {
			Percolation p = new Percolation(n);
			while (!p.percolates()) {
				int row = StdRandom.uniform(n) + 1;
				int col = StdRandom.uniform(n) + 1;
				p.open(row, col);
			}

			thresholds[i] = p.numberOfOpenSites() / (double) (n * n);
		}
	}

	// sample mean of percolation threshold
	public double mean() {
		return StdStats.mean(this.thresholds);
	}

	// sample standard deviation of percolation threshold
	public double stddev() {
		return StdStats.stddev(this.thresholds);
	}

	// low endpoint of 95% confidence interval
	public double confidenceLo() {
		return this.mean() - CONFIDENCE_INTERVAL * Math.sqrt(this.stddev()) / Math.sqrt(this.thresholds.length);
	}

	// high endpoint of 95% confidence interval
	public double confidenceHi() {
		return this.mean() + CONFIDENCE_INTERVAL * Math.sqrt(this.stddev()) / Math.sqrt(this.thresholds.length);
	}

	// test client (see below)
	public static void main(String[] args) {
		PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		StdOut.println("mean = " + stats.mean());
		StdOut.println("stddev = " + stats.stddev());
		StdOut.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
	}

}