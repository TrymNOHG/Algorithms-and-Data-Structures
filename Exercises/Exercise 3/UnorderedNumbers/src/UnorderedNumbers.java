import java.util.Random;

public class UnorderedNumbers {

    static final int RANDOM_LIST_BOUND = 1000000;
    static final int DUPLICATE_LIST_BOUND = 100;

    public static void main(String[] args) {

        NumberArray singlePivotArr = new NumberArray(4000000, RANDOM_LIST_BOUND);
        NumberArray dualPivotArr = new NumberArray(4000000, RANDOM_LIST_BOUND);

        NumberArray singlePivotArrDup = new NumberArray(4000000, DUPLICATE_LIST_BOUND);
        NumberArray dualPivotArrDup = new NumberArray(4000000, DUPLICATE_LIST_BOUND);

        //single pivot - random order
        System.out.println("\n--- Random Order Single Pivot ---");
        final int valueBefore = singlePivotArr.getSumOfArrayValuesBefore();
        long startTime = System.nanoTime();
        singlePivotQuickSort(singlePivotArr.getUnorderedArr(), 0, singlePivotArr.getUnorderedArr().length - 1);
        long endTime = System.nanoTime();
        long time = ((endTime - startTime) / 1000000);
        final int valueAfter = singlePivotArr.getSumOfArrayValues();
        int valueDifference = valueAfter - valueBefore;
        System.out.println("The difference in the sum of elements before and after running is : " + valueDifference);
        singlePivotArr.isArrOrdered();
        System.out.println("Time used in single pivot was: " + time + " ms");
        System.out.println("\n \n");

        //dual pivot - random order

        System.out.println("---Random Order Dual Pivot---");
        final int valueBefore2 = dualPivotArr.getSumOfArrayValuesBefore();
        long startTime2 = System.nanoTime();
        dualPivotQuickSort(dualPivotArr.getUnorderedArr(), 0, dualPivotArr.getUnorderedArr().length - 1);
        long endTime2 = System.nanoTime();
        long time2 = ((endTime2 - startTime2) / 1000000);
        final int valueAfter2 = dualPivotArr.getSumOfArrayValues();
        int valueDifference2 = valueAfter2 - valueBefore2;
        System.out.println("The difference in the sum of elements before and after running is : " + valueDifference2);
        dualPivotArr.isArrOrdered();
        System.out.println("Time used in dual pivot was: " + time2 + " ms");
        System.out.println("\n \n");

        //single pivot - sorted
        System.out.println("--- Sorted Single Pivot ---");
        final int valueBefore3 = singlePivotArr.getSumOfArrayValuesBefore();
        long startTime3 = System.nanoTime();
        singlePivotQuickSort(singlePivotArr.getUnorderedArr(), 0, singlePivotArr.getUnorderedArr().length - 1);
        long endTime3 = System.nanoTime();
        long time3 = ((endTime3 - startTime3) / 1000000);
        final int valueAfter3 = singlePivotArr.getSumOfArrayValues();
        int valueDifference3 = valueAfter3 - valueBefore3;
        System.out.println("The difference in the sum of elements before and after running is : " + valueDifference3);
        singlePivotArr.isArrOrdered();
        System.out.println("Time used in single pivot was: " + time3 + " ms");
        System.out.println("\n \n");

        //dual pivot - sorted
        System.out.println("---Sorted Dual Pivot---");
        final int valueBefore4 = dualPivotArr.getSumOfArrayValuesBefore();
        long startTime4 = System.nanoTime();
        dualPivotQuickSort(dualPivotArr.getUnorderedArr(), 0, dualPivotArr.getUnorderedArr().length - 1);
        long endTime4 = System.nanoTime();
        long time4 = ((endTime4 - startTime4) / 1000000);
        final int valueAfter4 = dualPivotArr.getSumOfArrayValues();
        int valueDifference4 = valueAfter4 - valueBefore4;
        System.out.println("The difference in the sum of elements before and after running is : " + valueDifference4);
        dualPivotArr.isArrOrdered();
        System.out.println("Time used in dual pivot was: " + time4 + " ms");
        System.out.println("\n \n");

        //single pivot - duplicate list
        System.out.println("---Duplicate Single Pivot---");
        final int valueBefore5 = singlePivotArrDup.getSumOfArrayValuesBefore();
        long startTime5 = System.nanoTime();
        singlePivotQuickSort(singlePivotArrDup.getUnorderedArr(), 0, singlePivotArrDup.getUnorderedArr().length - 1);
        long endTime5 = System.nanoTime();
        long time5 = ((endTime5 - startTime5) / 1000000);
        final int valueAfter5 = singlePivotArrDup.getSumOfArrayValues();
        int valueDifference5 = valueAfter5 - valueBefore5;
        System.out.println("The difference in the sum of elements before and after running is : " + valueDifference5);
        singlePivotArrDup.isArrOrdered();
        System.out.println("Time used in single pivot was: " + time5 + " ms");
        System.out.println("\n \n");

        //dual pivot - duplicate list
        System.out.println("---Duplicate Dual Pivot---");
        final int valueBefore6 = dualPivotArrDup.getSumOfArrayValuesBefore();
        long startTime6 = System.nanoTime();
        dualPivotQuickSort(dualPivotArrDup.getUnorderedArr(), 0, dualPivotArrDup.getUnorderedArr().length - 1);
        long endTime6 = System.nanoTime();
        long time6 = ((endTime6 - startTime6) / 1000000);
        final int valueAfter6 = dualPivotArrDup.getSumOfArrayValues();
        int valueDifference6 = valueAfter6 - valueBefore6;
        System.out.println("The difference in the sum of elements before and after running is : " + valueDifference6);
        dualPivotArrDup.isArrOrdered();
        System.out.println("Time used in dual pivot was: " + time6 + " ms");
        System.out.println("\n \n");

    }


    public static void singlePivotQuickSort(int[] t, int v, int h) {
        if (h - v > 2) {
            int delepos = splitt(t, v, h);
            singlePivotQuickSort(t, v, delepos - 1);
            singlePivotQuickSort(t, delepos + 1, h);
        } else median3sort(t, v, h);
    }

    public static int median3sort(int[] t, int v, int h) {
        int m = (v + h) / 2;
        if (t[v] > t[m]) bytt(t, v, m);
        if (t[m] > t[h]) {
            bytt(t, m, h);
            if (t[v] > t[m]) bytt(t, v, m);
        }
        return m;
    }

    public static void bytt(int[] t, int i, int j) {
        int k = t[j];
        t[j] = t[i];
        t[i] = k;
    }

    public static int splitt(int[] t, int v, int h) {
        int iv, ih;
        int m = median3sort(t, v, h);
        int dv = t[m];
        bytt(t, m, h - 1);
        for (iv = v, ih = h - 1; ; ) {
            while (t[++iv] < dv) ;
            while (t[--ih] > dv) ;
            if (iv >= ih) break;
            bytt(t, iv, ih);
        }
        bytt(t, iv, h - 1);
        return iv;
    }

    // Java program to implement
    // dual pivot QuickSort
    static void swap(int[] arr, int i, int j)
    {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * Dual pivot
     * @author Geeks for geeks: <a href="https://www.geeksforgeeks.org/dual-pivot-quicksort/">...</a>
     */
    static void dualPivotQuickSort(int[] arr,
                                   int low, int high)
    {
        if (low < high)
        {

            // piv[] stores left pivot and right pivot.
            // piv[0] means left pivot and
            // piv[1] means right pivot
            int[] piv;
            piv = partition(arr, low, high);

            dualPivotQuickSort(arr, low, piv[0] - 1);
            dualPivotQuickSort(arr, piv[0] + 1, piv[1] - 1);
            dualPivotQuickSort(arr, piv[1] + 1, high);
        }
    }

    static int[] partition(int[] arr, int low, int high)
    {
        /*
            In order to make sure that a sorted array doesn't break the dual pivot algorithm.
            The lowest and highest values are swapped with the 1/3 and 2/3 position elements.
         */
        swap(arr, low, low+(high-low)/3);
        swap(arr, high, high-(high-low)/3);


        if (arr[low] > arr[high])
            swap(arr, low, high);

        // p is the left pivot, and q
        // is the right pivot.
        int j = low + 1;
        int g = high - 1, k = low + 1,
                p = arr[low], q = arr[high];

        while (k <= g)
        {

            // If elements are less than the left pivot
            if (arr[k] < p)
            {
                swap(arr, k, j);
                j++;
            }

            // If elements are greater than or equal
            // to the right pivot
            else if (arr[k] >= q)
            {
                while (arr[g] > q && k < g)
                    g--;

                swap(arr, k, g);
                g--;

                if (arr[k] < p)
                {
                    swap(arr, k, j);
                    j++;
                }
            }
            k++;
        }
        j--;
        g++;

        // Bring pivots to their appropriate positions.
        swap(arr, low, j);
        swap(arr, high, g);

        // Returning the indices of the pivots
        // because we cannot return two elements
        // from a function, we do that using an array.
        return new int[] { j, g };
    }

}

/**
 * This class handles the creation of a random, unordered array of numbers.
 */
class NumberArray{
    private final Random random = new Random(System.currentTimeMillis());
    private int[] unorderedArr;
    final private int sumOfArrayValuesBefore;

    public NumberArray(int sizeOfArr, int randomBound){
        createRandomArr(sizeOfArr, randomBound);
        this.sumOfArrayValuesBefore = getSumOfArrayValues();
    }

    /**
     * This method fills the unordered array with random numbers (0 to 100) with a specified number of indices.
     * @param sizeOfArr The desired length of the array, represented as an int.
     */
    public void createRandomArr(int sizeOfArr, int randomBound){
        this.unorderedArr = new int[sizeOfArr];
        for (int i = 0; i < sizeOfArr; i++) {
            this.unorderedArr[i] = random.nextInt(1000000);
        }
    }

    /**
     * This method checks if the input array is properly sorted from lowest to highest value.
     * @return    If the array has an index bigger than the preceding index it returns {@code false}, else {@code true}.
     */
    public boolean isArrOrdered() {
        for(int i = 0; i < this.unorderedArr.length - 1; i++){
            if(this.unorderedArr[i] > this.unorderedArr[i+1]){
                System.out.println("The array is not sorted and this test has therefore failed.");
                return false;
            }
        }
        System.out.println("The array is sorted and this test was successful!");
        return true;
    }

    /**
     * This method returns the sum of all the elements of the object array.
     * @return Sum of the elements of the given array, represented as an int.
     */
    public int getSumOfArrayValues() {
        int sumOfElements = 0;
        for(int element : this.unorderedArr){
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
