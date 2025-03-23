import java.util.Random;

public class DiscreteGaussianSampler {

    private final int q;          // Modulus
    private final double sigma;   // Standard deviation
    private final double tau;     // Truncation parameter
    private final double fmax;    // Maximum value of the Gaussian PDF
    private final double[] lookupTable; // Precomputed Gaussian probabilities
    private final int bound;      // Integer bound for truncation
    private final Random random;  // Random number generator

    /**
     * Construct a new sampler for a discrete Gaussian distribution.
     * @param q Modulus (must be an integer > 1)
     * @param sigma Standard deviation
     */
    public DiscreteGaussianSampler(int q, double sigma) {
        this.q = q;
        this.sigma = sigma;
        this.tau = 6.0;
        this.random = new Random();

        // Compute the range [-bound, bound]
        this.bound = (int) Math.floor(tau * sigma);

        // Precompute bound + 1 values for Gaussian probabilities, scaled by max
        this.lookupTable = new double[2 * this.bound + 1];
        this.fmax = gaussianPDF(0); // f(0) is the peak of the Gaussian curve

        // Fill the lookup table for integers in range [-bound, bound]
        for (int x = -this.bound; x <= this.bound; x++) {
            this.lookupTable[x + this.bound] = gaussianPDF(x);
        }
    }

    /**
     * Compute the Gaussian probability density function f(x).
     * f(x) = scale * exp(-x^2 / (2 * sigma^2))
     */
    private double gaussianPDF(int x) {
        double scale = 1.0 / (sigma * Math.sqrt(2.0 * Math.PI)); // Normalization factor
        return scale * Math.exp(-x * x / (2.0 * sigma * sigma));
    }

    /**
     * Rejection sampling for a single discrete Gaussian sample mod q.
     */
    public int sample() {
        while (true) {
            // Step 1: Uniformly sample an integer x in range [-bound, bound]
            int x = random.nextInt(2 * this.bound + 1) - this.bound;

            // Step 2: Uniformly sample y in range [0, fmax]
            double y = random.nextDouble() * fmax;

            // Step 3: Check if y <= f(x) from precomputed table
            if (y <= this.lookupTable[x + this.bound]) {
                // Step 4: Return x mod q
                return (x % q + q) % q;
            }

            // Otherwise, repeat
        }
    }

}