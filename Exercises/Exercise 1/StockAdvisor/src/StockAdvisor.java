import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StockAdvisor {

//    static int[] stockChanges = {-1, 3, -9, 2, 2, -1, 2, -1, -5};
    static int[] stockChanges = {1, 3, -4, 5, 5, -15, 14};
    static Random random = new Random();


    public static void main(String[] args) {
        int[] randomStockChanges = fillArrayWithRandomNumbers(10000000);
//        stockCheck(stockChanges);
//        stockCheck2(stockChanges);

        final long startTime = System.currentTimeMillis(); //Using this to calculate time complexity and find BIG O
        stockCheck3(randomStockChanges);
//        optimalGain(randomStockChanges);
        final long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;
        System.out.println("The time elapsed was " + timeElapsed + " ms");
    }

    /**
     * Change the return value
     */
    public static void stockCheck(int[] stockChanges){
        int[] bestIndices = {0, 0};
        List<Integer> stockValueList = new ArrayList<>();
        //Adding the starting value, so the changes are relative to this start position
        stockValueList.add(0);

        //TODO: Change stockValueList name!!!
        //This for loop creates an array of all the different point the stock market was at.
        for(int i = 0; i < stockChanges.length; i++){
            int currentStockValue = stockValueList.get(i);
            stockValueList.add(currentStockValue + stockChanges[i]);
        }

        //Remove start value of 0.
        stockValueList.remove(0);

        //In order to find the best point to buy and then sell, the biggest difference has to be found in this array.
        for (int i = 1; i < stockValueList.size(); i++){
            for (int j = stockValueList.size() - 1; j > i; j--){
                //Check if the difference is better
                if(stockValueList.get(j) - stockValueList.get(i) > stockValueList.get(bestIndices[1]) - stockValueList.get(bestIndices[0])){
                    bestIndices[0] = i;
                    bestIndices[1] = j;
                }
            }

        }

        System.out.println("The best point to buy at is: " + bestIndices[0] + " " +
                "and the best point to sell at is: " + bestIndices[1]);

    }


    /**
     * Change the return value
     */
    public static void stockCheck2(int[] stockChanges){
        int[] bestIndices = {0, 0};

        int bestRelativeDifference = 0;
        //In order to find the best point to buy and then sell, the biggest difference has to be found in this array.
        for (int i = 0; i < stockChanges.length - 1; i++){
            int relativeDifference = 0;
            for (int j = i + 1; j < stockChanges.length; j++){
                relativeDifference += stockChanges[j];
                //Check if the difference is better
                if(relativeDifference > bestRelativeDifference){
                    bestRelativeDifference = relativeDifference;
                    bestIndices[0] = i;
                    bestIndices[1] = j;
                }
            }
        }

        System.out.println("The best point to buy at is: " + bestIndices[0] + " " +
                "and the best point to sell at is: " + bestIndices[1]);

    }

    /**
     * Change the return value
     */
    public static void stockCheck3(int[] stockChanges){
        int stockChangesSize = stockChanges.length;

        //Adding the starting value, so the changes are relative to this start position
        int[] stockValueList = new int[stockChangesSize + 1];
        stockValueList[0] = 0;
        for(int i = 0; i< stockChangesSize; i++){
            stockValueList[i+1] = stockValueList[i] + stockChanges[i];
        }

        int[] storageArray = {0,0,0};

        //Create an array which has 3 different values: temp of the lowest buy index and current indices of best difference
        //In order to find the best point to buy and then sell, the biggest difference has to be found in this array.
        for (int i = 0; i < stockChangesSize; i++){
            //This if-statement checks if a new minimum stock value has been found, if so change the stored index.
            if(stockValueList[i] < stockValueList[storageArray[0]]){
                storageArray[0] = i;
            }

            //This if-statement checks if the difference between the current lowest value and the current stock value
            //is the largest difference found so far.
            if(stockValueList[i] - stockValueList[storageArray[0]] > stockValueList[storageArray[2]] - stockValueList[storageArray[1]]){
                storageArray[2] = i;
                storageArray[1] = storageArray[0];
            }
        }

        System.out.println("The best point to buy at is: " + storageArray[1] +
                " and the best point to sell at is: " + storageArray[2]);

    }

    /**
     * This method fills an array with random numbers (-10 to 10) with a specified number of indices.
     * @param length The desired length of the array, represented as an int.
     * @return       The array with random numbers, represented as an int array.
     */
    public static int[] fillArrayWithRandomNumbers(int length){
        int[] arrayToBeFilled = new int[length];
        for (int i = 0; i < length; i++) {
            arrayToBeFilled[i] = random.nextInt(21) - 10;
        }
        return arrayToBeFilled;
    }

    public static String optimalGain(int[] stockChanges){

        //Translates day to day stock changes into stock prices
        int[] stockPrice = new int[stockChanges.length + 1];
        stockPrice[0] = 0;
        for(int i = 0; i<stockChanges.length; i++){
            stockPrice[i+1] = stockPrice[i] + stockChanges[i];
        }

        int min1 = 0;
        int dif1;
        int dif2 = 0;
        int min2 = 0;
        int minIdx1 = 0;
        int maxIdx2 = 0;
        int minIdx2 = 0;
        int stockLength = stockPrice.length;
        //Moves through stock prices comparing differences between different lows and highs. Returns stock prices/days which produced the largest difference.
        for (int i = 0; i< stockLength; i++){

            if(stockPrice[i]<min1){
                min1 = stockPrice[i];
                minIdx1 = i;
            }

            dif1 = stockPrice[i] - min1;

            if(dif1>dif2){
                dif2 = dif1;
                min2 = min1;
                minIdx2 = minIdx1;
                maxIdx2 = i;
            }
        }
        String result = "The best time to buy is when the stock price is :" + min2 + "\n" + "The best time to sell is when the stock price is : " + stockPrice[maxIdx2] + "\nBuy Day: " + minIdx2 + "\nSell day: " + maxIdx2;
        return result;
    }


}
