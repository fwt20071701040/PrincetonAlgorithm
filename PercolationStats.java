import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;
public class PercolationStats {

	private final int trialsNum;
	private final double mu;
	private final double sigma;
	public PercolationStats(int n, int trials) {
		
		if (n <= 0 || trials <= 0) {
			
			throw new IllegalArgumentException();
		}
		trialsNum = trials;
		int[] numOfOpenSites = new int[trials];
		double []propablePerTrial = new double[trials];
		for (int i = 0; i < trials; i++) {
			
			Percolation perco = new Percolation(n);
			int count = 0;
			int[] rand = StdRandom.permutation(n * n);
			while (!perco.percolates()) {
				
				int row = rand[count] / n + 1;
				int col = rand[count] % n + 1;
				perco.open(row, col);
				count++;
			}
			numOfOpenSites[i] = perco.numberOfOpenSites();
			propablePerTrial[i] = numOfOpenSites[i] * 1.0 / (n * n);
		}
		mu = StdStats.mean(propablePerTrial);
		sigma = StdStats.stddev(propablePerTrial);
	}    // perform trials independent experiments on an n-by-n grid
	public double mean() {
		
		return mu;
	}                          // sample mean of percolation threshold
 	public double stddev() {
 		
	 	return sigma;
 	}                        // sample standard deviation of percolation threshold
	public double confidenceLo() {
		
		return mu - 1.96 * sigma / Math.sqrt(trialsNum);
	}                  // low  endpoint of 95% confidence interval
	public double confidenceHi() {
		
		return mu + 1.96 * sigma / Math.sqrt(trialsNum);
	}                  // high endpoint of 95% confidence interval

	public static void main(String[] args) {
		
		Stopwatch sw = new Stopwatch();
		double startTime = sw.elapsedTime();
		PercolationStats percoStats = new PercolationStats(2000, 10);
		StdOut.println("mean                    = " + percoStats.mean());
		StdOut.println("stddev                  = " + percoStats.stddev());
		StdOut.println("95% confidence interval = " + "[" + 
		percoStats.confidenceLo() + "," + percoStats.confidenceHi() + "]");
		double endTime = sw.elapsedTime();
		System.out.println("totalTimeConsume =" + (endTime - startTime) + "s");
	}// test client s(described below)
	
} 
