/**************************************************************************************************
 * Program Name   :
 * Author         : Terry Weiss
 * Date           : Apr 21, 2016
 * Course/Section :
 * Program Description:
 **************************************************************************************************/

package lemonadestand;

public class Stand {
    public enum Product {
        CUP, SIGN;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }


    //Public info for all stands
    public static final int BASE_HOURLY_CUSTOMERS = 10;     //Normal number of customers in an hour

    //Static resource prices for all stands
    private static final double[] LEMONS_PRICES = {1.0, 0.5, 0.33};     //Possible lemon prices
    private static final double[] SUGAR_PRICES = {0.5, 0.25};           //Possible sugar prices
    private static final double[] ICE_PRICES = {0.1, 0.05};             //Possible ice prices
    private static final double WATER_PRICE = 0.05;                     //Price of water
    private static final double CUPS_PRICE = 0.02;                      //Price of cups
    private static final double SIGN_PRICE = 0.25;                      //Price of signs

    //Hours all stands are open
    private static final int START_HOUR = 10;       //Hour stands start (10AM)
    private static final int CLOSE_HOUR = 19;       //Hour stands close (7PM)


    private ResourcePrices resourcePrices;
    private double pricePerCup;
    private double openingMoney;
    private double money;

    private int cupsMade, dailyCustomers, cupsSold, signsMade;
    private final String location;
    private boolean dayGenerated;
    private Weather weather;

    public Stand(String location) {
        this.location = location;
        pricePerCup   = 0.00;
    }

    public final void generateDay(double openingBalance) {
        if (!dayGenerated) {
            weather        = Weather.values()[Game.random.nextInt(Weather.values().length)];
            resourcePrices = new ResourcePrices();
            cupsSold       = cupsMade = 0;
            dailyCustomers = 0;
            money          = openingMoney   = openingBalance;
            dayGenerated   = true;
        }
    }

    public void runDay(double totalMoney) {
        if (!dayGenerated) {
            generateDay(totalMoney);
        }

        for (int hour = START_HOUR; hour <= CLOSE_HOUR; hour++) {
            int hourlyCustomers = hourlyCustomers();
            dailyCustomers += hourlyCustomers;
            for (int customer = 0; customer < hourlyCustomers && cupsSold < cupsMade; customer++) {
                if (!Customer.tooExpensive(pricePerCup, weather)) {
                    cupsSold++;
                }
            }
        }
        money += cupsSold * pricePerCup;

        dayGenerated = false;
    }

    public int hourlyCustomers() {
        int customers = BASE_HOURLY_CUSTOMERS;

        if (weather == Weather.RAINY) {
            customers -= Game.random.nextInt(3);
        } else if (weather == Weather.CLOUDY) {
            customers += Game.random.nextInt(5);
        } else if (weather == Weather.SUNNY) {
            customers += Game.random.nextInt(11);
        }

        customers += (int)(customers * (signsMade / 100.00));

        return customers;
    }

    public void setCupPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Negative price:  " + price);
        }

        pricePerCup = price;
    }

    public double cupPrice() {
        return pricePerCup;
    }

    public double cupCost() {
        return resourcePrices.costPerCup;
    }

    public double signPrice() {
        return resourcePrices.signs;
    }

    public int getSignsMade() {
        return signsMade;
    }

    public String location() {
        return location;
    }

    public String weatherForecast() {
        return "The weather in " + location + " will be " + weather + " today.";
    }

    public double buyCups(int quantity) {
        final double CANT_AFFORD = -1.0;
        double cost;

        cost = quantity * resourcePrices.costPerCup;

        if (cost <= money) {
            money -= cost;
            cupsMade += quantity;
        } else {
            cost = CANT_AFFORD;
        }

        return cost;
    }

    public double buySigns(int quantity) {
        final double CANT_AFFORD = -1.0;
        double cost;

        cost = quantity * resourcePrices.signs;

        if (cost <= money) {
            money -= cost;
            signsMade += quantity;
        } else {
            cost = CANT_AFFORD;
        }

        return cost;
    }

    public String dailyReport() {
        String report = "Daily Report for " + location + "\n";

        report += String.format("    Each cup cost $%-3.02f.", resourcePrices.costPerCup)
                        + String.format("  You charged $%-4.2f per cup.%n", pricePerCup)
                + "    You had " + dailyCustomers + " potential customers."
                        + "  You sold " + cupsSold + " of " + cupsMade + " cups.\n"
                + String.format("    You made a net profit of $%.2f!%n", netProfit());

        return report;
    }

    public double netProfit() {
        return money - openingMoney;
    }

    /**
     * This class holds all the daily prices and is unique each day for each stand. Since this
     * container is more of a struct than a class, all fields are public so they don't restrict
     * the class's privacy modifier.
     */
    private class ResourcePrices {
        public final double lemons;         //Cost of lemons
        public final double sugar;          //Cost of sugar
        public final double ice;            //Cost of ice
        public final double water;          //Cost of water
        public final double cups;           //Cost of cups
        public final double signs;          //Cost of signs
        public final double costPerCup;     //Overall cost per cup of lemonade

        /**
         * Randomly generates the resource prices for the day, and the overall cost per cup.
         */
        private ResourcePrices() {
            lemons = LEMONS_PRICES[Game.random.nextInt(LEMONS_PRICES.length)];
            sugar = SUGAR_PRICES[Game.random.nextInt(SUGAR_PRICES.length)];
            ice = ICE_PRICES[Game.random.nextInt(ICE_PRICES.length)];
            water = WATER_PRICE;
            cups = CUPS_PRICE;
            signs = SIGN_PRICE;
            costPerCup = lemons + sugar + ice + water + cups;
        }
    }
}
