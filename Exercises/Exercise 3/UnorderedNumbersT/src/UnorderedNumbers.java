import java.util.Arrays;
import java.util.Random;

//TODO: remember to find the sum of all the element values and check that all the values are actually ordered correct.

public class UnorderedNumbers {

    static Random random = new Random();

    public static void main(String[] args) {

        NumberArray numberArray = new NumberArray(10000000);

        final long timeStart = System.currentTimeMillis();
        int[] sortedArr = singlePivotQuickSort(numberArray.getUnorderedArr());
        final long timeEnd = System.currentTimeMillis();

        System.out.println();
        numberArray.isArrOrdered(sortedArr);

        System.out.println("Sum of values before = " + numberArray.getSumOfArrayValuesBefore());
        System.out.println("Sum of values after = " + numberArray.getSumOfArrayValues(sortedArr));
        System.out.println("Difference of sums = " + (numberArray.getSumOfArrayValues(sortedArr) - numberArray.getSumOfArrayValuesBefore()));

        System.out.println(timeEnd - timeStart + " ms");

    }

    public static int[] singlePivotQuickSort(int[] arr){
        int arrLength = arr.length;
        int pivot = random.nextInt(arrLength);

        //Rearrange so that all the values smaller are below and bigger are above of pivot

        //Maybe place the pivot on the top or bottom of one of the arrays!
        //Sort according to smaller or larger than pivot
        int j = arrLength;

        for(int i = 0; i < j; i++){
            if(i < pivot){
                if(arr[i] > arr[pivot]){
                    while(arr[--j] > arr[pivot]);
                    if(j == pivot) pivot = i;
                    swapElements(arr, i, j);
                }
            }
            else{
                if (arr[i] < arr[pivot]){
                    swapElements(arr, i, pivot);
                    int tempVal = pivot;
                    pivot = i;
                    i = tempVal;
                }
            }
        }

        int[] smallNumArr = Arrays.copyOfRange(arr, 0, pivot + 1);
        int[] bigNumArr = Arrays.copyOfRange(arr, pivot + 1, arrLength);

        if(arrLength > 2){
            if(pivot > 1) smallNumArr = singlePivotQuickSort(smallNumArr);
            if(arrLength - pivot > 1) bigNumArr = singlePivotQuickSort(bigNumArr);
            arr = joinTwoArrays(smallNumArr, bigNumArr);
        }

        return arr;
    }

    public static int[] joinTwoArrays(int[] arr1, int[] arr2){
        int[] totalArr = new int[arr1.length + arr2.length];
        for(int i = 0; i < totalArr.length; i++){
            if(i < arr1.length) totalArr[i] = arr1[i];
            else totalArr[i] = arr2[i - arr1.length];
        }
        return totalArr;
    }

    public static void swapElements(int[] arr, int index1, int index2){
        int tempVal = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = tempVal;
    }


}

/**
 * This class handles the creation of a random, unordered array of numbers.
 */
class NumberArray{
    private Random random = new Random(System.currentTimeMillis());
    private int[] unorderedArr;
    final private int sumOfArrayValuesBefore;

    public NumberArray(int sizeOfArr){
        createRandomArr(sizeOfArr);
        this.sumOfArrayValuesBefore = getSumOfArrayValues(this.unorderedArr);
    }

    /**
     * This method fills the unordered array with random numbers (0 to 100) with a specified number of indices.
     * @param sizeOfArr The desired length of the array, represented as an int.
     */
    public void createRandomArr(int sizeOfArr){
        this.unorderedArr = new int[sizeOfArr];
        for (int i = 0; i < sizeOfArr; i++) {
            this.unorderedArr[i] = random.nextInt(1000000);
        }
    }

    /**
     * This method checks if the input array is properly sorted from lowest to highest value.
     * @param arr The array to be checked, given as an integer array.
     * @return    If the array has an index bigger than the preceding index it returns {@code false}, else {@code true}.
     */
    public boolean isArrOrdered(int[] arr) {
        for(int i = 0; i < arr.length - 1; i++){
            if(arr[i] > arr[i+1]){
                System.out.println("\nThe array is not sorted and this test has therefore failed.");
                return false;
            }
        }
        System.out.println("\nThe array is sorted and this test was successful!");
        return true;
    }

    /**
     * This method returns the sum of all the elements of the given array.
     * @param arr A given integer array
     * @return Sum of the elements of the given array, represented as an int.
     */
    public int getSumOfArrayValues(int[] arr) {
        int sumOfElements = 0;
        for(int element : arr){
            sumOfElements += element;
        }
        return sumOfElements;
    }

    public int[] getUnorderedArr() {
        return unorderedArr;
    }

    public int getSumOfArrayValuesBefore() {
        return sumOfArrayValuesBefore;
    }
}