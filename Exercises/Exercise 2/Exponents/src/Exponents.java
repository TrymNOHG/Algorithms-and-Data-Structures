/**
 * This class contains algorithms for calculating the power of a given number. The class includes 3 different ways
 * of attaining the result after finding the exponent, with varying time complexities.
 * @author Trym Hamer Gudvangen, Leon Egeberg Hesthaug, og Eirik Elvestad
 */
public class Exponents {

    public static void main(String[] args) {

        final long startTime = System.nanoTime();
        float result = exponentCalc1(1.001f,8000);
        final long endTime = System.nanoTime();

        final long startTime2 = System.nanoTime();
        float resultAlt = exponentCalc2(1.001f,8400);
        final long endTime2 = System.nanoTime();

        final long startTime3 = System.nanoTime();
        double resultJava = exponentCalcJava(1.001f,8000);
        final long endTime3 = System.nanoTime();

        long totalTime = endTime - startTime;
        long totalTime2 = endTime2 - startTime2;
        long totalTime3 = endTime3 - startTime3;

        System.out.println("Result from exercise 2.1-1: " + result);
        System.out.println("Time in microseconds: " + totalTime/1000f + "\n");

        System.out.println("Result from exercise 2.2-3: " + resultAlt);
        System.out.println("Time taken in microseconds: " + totalTime2/1000f + "\n");

        System.out.println("Result from Java's exponent algorithm: " + resultJava);
        System.out.println("Time taken in microseconds: " + totalTime3/1000f);

    }

    /**
     * This method returns the result of a number x with exponent n.
     * If n is not equal to 0, a recursive call is made with n-1 as the new exponent.
     *
     * @param x Base number as float
     * @param n Exponent as int
     * @return  Calculation result as float
     */
    public static float exponentCalc1(float x, int n){
        if (n == 0) return 1;
        else if (n > 0) return x * exponentCalc1(x, n - 1);
        else return -1;
    }

    /**
     * This method returns the result of a number x with exponent n.
     * If n is not equal to 0, a recursive call is made with x times (n-1)/2 as the new exponent and x*x as base if it
     * is an odd exponent. If n is an even exponent, a recursive call is made with n/2 as the new exponent and x*x.
     * @param x Base number as float
     * @param n Exponent as int
     * @return  Calculation result as float
     */
    public static float exponentCalc2(float x, int n){
        if (n == 0) return 1;
        else if (n & 1 == 1) return x * exponentCalc2(x*x, (n - 1)/2);
        else return exponentCalc2(x*x, n/2);
    }

    /**
     * This method uses the built-in java exponent method {@link Math#pow(double, double)}.
     * @param x Base number as float
     * @param n Exponent as int
     * @return  Calculation result as float
     */
    public static double exponentCalcJava(float x, int n){
        return Math.pow(x, n);
    }
}
