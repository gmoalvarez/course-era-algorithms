/**
 * Created by guillermo on 6/25/15.
 */
public class PercolationStats {
    private double[] fractionOpenSites;
    private double totalNumberOfSites;
    private double numberOfTrials;

    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("N and T should be greater than 0");
        }
        fractionOpenSites = new double[T];
        totalNumberOfSites = N * N;
        numberOfTrials = T;
        runPercolationExperiment(N);
    }

    private void runPercolationExperiment(int N) {
        int randomRow;
        int randomColumn;
        double numberOfOpenSites;
        for (int i = 0; i < numberOfTrials; i++) {
            numberOfOpenSites = 0;
            Percolation percolationExperiment = new Percolation(N);
            while (!percolationExperiment.percolates()) {
                randomRow = StdRandom.uniform(1, N + 1);
                randomColumn = StdRandom.uniform(1, N + 1);
                if (!percolationExperiment.isOpen(randomRow, randomColumn)) {
                    percolationExperiment.open(randomRow, randomColumn);
                    numberOfOpenSites++;
                }
            }
            fractionOpenSites[i] = numberOfOpenSites / totalNumberOfSites;
        }
    }

    public double mean() {
        return StdStats.mean(fractionOpenSites);
    }

    public double stddev() {
        if (numberOfTrials == 1) {
            return Double.NaN;
        }
        return StdStats.stddev(fractionOpenSites);
    }

    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(numberOfTrials);
    }

    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(numberOfTrials);
    }

    public static void main(String [] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats percolationThreshholdCalculator = new PercolationStats(N, T);
        double mean = percolationThreshholdCalculator.mean();
        double stddev = percolationThreshholdCalculator.stddev();
        double confidenceLo = percolationThreshholdCalculator.confidenceLo();
        double confidenceHi = percolationThreshholdCalculator.confidenceHi();
        StdOut.println("mean = " + mean);
        StdOut.println("stddev = " + stddev);
        StdOut.println("95% confidence interval = " + confidenceLo + ", " + confidenceHi);
    }
}