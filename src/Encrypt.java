import org.ejml.data.Matrix;
import org.ejml.simple.SimpleMatrix;
import org.ejml.data.DMatrixRMaj;

import java.util.Random;
import java.util.Vector;

public class Encrypt {

    public static int sampleDiscreteGaussian(int q, double sigma, Random random) {
        double mean = 0.0;

        while (true) {
            // Sample from continuous Gaussian distribution
            double gaussSample = mean + random.nextGaussian() * sigma;

            // Round to nearest integer
            int discreteSample = (int) Math.round(gaussSample);

            // Ensure the value falls within range [0, q]
            if (discreteSample >= 0 && discreteSample < q) {
                return discreteSample;
            }
            // Otherwise, retry
        }
    }

    private static String convertVectorToString(SimpleMatrix vector) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vector.getNumRows(); i++) {
            // Get the value at index i in the column (ASCII code)
            int asciiValue = (int) vector.get(i, 0); // Cast to integer if values are floating-point

            // Convert the ASCII code to a character
            char character = (char) asciiValue;

            // Append the character to the StringBuilder
            sb.append(character);
        }
        sb.toString();
        return sb.toString();
    }

    private static void do_pke(int q, int n, int N, String message) {

//        Matrix a = new DMatrixRMaj(q, n);

        double sigma = 1.0;
       /* """Perform PKE operations Gen/Enc/Dec`.
 Args:
     q (int): Modulus
     n (int): Security parameter
     N (int): Sample size
     sigma (float): sigma
 """ */

        System.out.printf("q=%d n=%d N=%d sigma=%f", q, n, N, sigma);

        //generate private key
        SimpleMatrix vector_s = new SimpleMatrix(n, 1);
        Random random = new Random();
        //set the elements of vector s to integers
        for (int i = 0; i < n; i++) {
            vector_s.set(i, 0, random.nextInt(q)); // Generate a random integer
        }

        //generate public key
        SimpleMatrix A = new SimpleMatrix(N, n);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, random.nextInt(q));
            }
        }

        //Random noise vector e <-- \chi^N


        DiscreteGaussianSampler sampler = new DiscreteGaussianSampler(q, sigma);

        SimpleMatrix vector_e = new SimpleMatrix(N, 1);
        for (int i = 0; i < N; i++) {
            int j = sampler.sample();
            vector_e.set(i, 0, j); // Generate a random integer
        }

        //Compute b = As + e

        System.out.println("vector s");
        System.out.println(vector_s);
        System.out.println("error vector");
        System.out.println(vector_e);
        System.out.println("matrix A");
        System.out.println(A);
        SimpleMatrix temp_vec = A.mult(vector_s);

        //public key
        SimpleMatrix vector_b = temp_vec.plus(vector_e);
        System.out.println("vector b");
        System.out.println(vector_b);

     /* Encrypt the message m \in {0,1} by computing
     r = {0,1}^N // r <-- Z_2^N
     */
        int length = message.length();
        SimpleMatrix vector_message = new SimpleMatrix(length, 1); // Column vector

        for (int i = 0; i < length; i++) {
            vector_message.set(i, 0, (int) message.charAt(i)); // Store ASCII values
        }
        System.out.println("input message vector: " + vector_message);
        SimpleMatrix vector_r = new SimpleMatrix(N, 1);
        for (int i = 0; i < N; i++) {
            //fill with random boolean integers
            vector_r.set(i, 0, random.nextInt(2)
            );
        }
        System.out.println(vector_r);

        //u = A^T*r
        SimpleMatrix vector_u = A.transpose().mult(vector_r);
        System.out.println(vector_u);
        SimpleMatrix qm2 = vector_message.scale(q / 2);
        System.out.println(qm2);
        double dot_product = (vector_b.dot(vector_r));
        SimpleMatrix vector_v = qm2.plus(dot_product);
        for (int i = 0; i < vector_v.numRows(); i++) {
            vector_v.set(i, 0, vector_v.get(i, 0) % q);  // Apply mod q element-wise
        }
        System.out.println("encrypted message vector" + vector_v);


        System.out.println("hello");
        System.out.println(convertVectorToString(vector_v));

        //decryption
     /*
     Decrypt the message m \in {0,1} by computing
     v' = s^T*u
     d = v - v'
     m = roundint(2d/q) mod 2
     */
        double v1 = vector_u.dot(vector_s);
        SimpleMatrix d = vector_v.minus(v1);
        System.out.println("decrypted message: "+convertVectorToString(d));
    }

    public static String encrypt(String input) {
        int q = 256; //modulus
        int n = 4; //secuity parameter
        int N = 7; //LWE sample size

        //chi noise distribution over Z_q

        do_pke(q, n, N, input);

        // For now, just return a placeholder string
        return "This is the encrypted string: " + input;
    }

}
