import org.ejml.data.Matrix;
import org.ejml.simple.SimpleMatrix;
import org.ejml.data.DMatrixRMaj;

import java.util.Random;
import java.util.Vector;

public class Encrypt {
    private static void do_pke(int q, int n, int N) {

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
        SimpleMatrix A = new SimpleMatrix(n, N);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < N; j++) {
                A.set(i, j, random.nextInt(q));
            }
        }

    //Random noise vector e <-- \chi^N



    System.out.println(vector_s);
    System.out.println(A);
    }
    public static String encrypt(String input) {
        int q = 31; //modulus
        int n = 4; //secuity parameter
        int N = 7; //LWE sample size

        //chi noise distribution over Z_q

        do_pke(q, n, N);

        // For now, just return a placeholder string
        return "This is the encrypted string: " + input;
    }

}
