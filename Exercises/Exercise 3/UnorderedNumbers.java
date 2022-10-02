import java.util.Random;

public class UnorderedNumbers {

    public static void main(String[] args) {
        Random rand = new Random();
        int[] numbers = new int[1000000];

        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = rand.nextInt(1000000);
        }

        int[] numbers2 = new int[1000000];

        for (int i = 0; i < numbers2.length; i++) {
            numbers2[i] = rand.nextInt(1000000);
        }

        //dual pivot

        int before = adder(numbers);
        long startTime = System.nanoTime();
        dualPivotQuickSort(numbers, 0, numbers.length - 1);
        long endTime = System.nanoTime();
        long time = ((endTime - startTime) / 1000000);
        int after = adder(numbers);
        int result = before - after;
        System.out.println("Forskjellen på liste før kjøring, og liste etter kjøring er: " + result);
        System.out.println("Listen er sortert?: " + sortedChecker(numbers));
        System.out.println("Milli second used in single pivot was: " + time);
        System.out.println("\n \n");


        //single pivot
        int before2 = adder(numbers);
        long startTime2 = System.nanoTime();
        quickSort2(numbers2,0, numbers2.length - 1);
        long endTime2 = System.nanoTime();
        long time2 = ((endTime2 - startTime2) / 1000000);
        int after2 = adder(numbers);
        int result2 = before2 - after2;
        System.out.println("Forskjellen på liste før kjøring, og liste etter kjøring er: " + result2);
        System.out.println("Listen er sortert?: " + sortedChecker(numbers2));
        System.out.println("Milli second used in dual pivot was: " + time2);

    }


    public static void quickSort2(int[] t, int v, int h) {
        if (h - v > 2) {
            int delepos = splitt(t, v, h);
            quickSort2(t, v, delepos - 1);
            quickSort2(t, delepos + 1, h);
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
     * @param arr
     * @param low
     * @param high
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

        public static int adder(int[] array){
            int sum = 0;
            for(int i = 0; i <= array.length - 1; i++){
                sum += array[i];
            }

        return sum;
        }

        public static boolean sortedChecker(int[] array){
            for(int n = 0; n <= array.length - 2; n++){
                if(array[n + 1] >= array[n]){
                    return true;
                }
                else {
                    break;
                }

            }

            return false;
        }

    }
