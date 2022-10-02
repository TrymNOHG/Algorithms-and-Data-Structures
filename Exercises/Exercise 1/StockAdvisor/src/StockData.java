import java.util.Random;

/**
 * This class represents interactions with stock data, specifically the finding of optimal buy and sell times of stocks.
 */
public class StockData {

    static Random random = new Random();

    public static void main(String[] args) {
        int[] randomStockChanges = fillArrayWithRandomNumbers(320000000);

        final long startTime = System.currentTimeMillis(); //Using this to calculate time complexity and find BIG O
        optimalStockTrade(randomStockChanges);
        final long endTime = System.currentTimeMillis();


        long timeElapsed = endTime - startTime;

        System.out.println("The time elapsed was " + timeElapsed + " ms");
    }

    /**
     * This method looks through stock change data and chooses the optimal buy and sell time.
     * @param stockChanges The different stock changes, represented as an int array.
     * @return             A string presenting the optimal buy and sell time.
     */
    public static String optimalStockTrade(int[] stockChanges){

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

}
