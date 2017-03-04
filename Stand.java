/**************************************************************************************************
 * Program Name   : Final Project Option #2 - Lemonade Stand
 * Author         : Terry Weiss
 * Date           : Apr 21, 2016
 * Course/Section : CSC 112-801 Fundamentals of Computing II
 * Program Description: The Lemonade Stand program runs a simulation for 30 days to make as much
 *     money as possible. The player starts with one stand and $100, and can buy as many stands as
 *     they'd like. Each day starts with weather reports where it can be rainy, cloudy, or sunny.
 *     The weather can be different for each stand. Each stand also generates the price per cup of
 *     lemonade based on the random daily price of each ingredient. The player may also make signs
 *     that add 1% of the customers per sign made. Each stand has its own pool of signs, since they
 *     are in different locations. The player is given the weather report and cost per cup, and is
 *     then prompted for how many cups and signs they'd like to make.
 *
 * The stands then generate how many customers per hour visit based on the weather, and whether each
 *     customer wants to buy the lemonade based on the price, the weather, and their generosity or
 *     stinginess. As each stand is simulated, its profits and expenses are added to the daily
 *     total. Once the day is over, a report will be displayed for each stand's results, and the
 *     daily profits will be added to the business's money pool.
 *
 * The game also records the player's final amount of money as a score, and a high score is
 *     maintained holding the top 5 scores. If the player's score qualifies, they will be prompted
 *     for their name, and the score will be inserted into the high scores list. The high scores
 *     list is saved to a file in serialized state and persists between games.
 **************************************************************************************************/

package lemonadestand;

/**
 * The Stand class handles the state and behavior to each individual lemonade stand. Each stand
 * is designed to be added to a Business, although it is possible to have one by itself if its
 * overall money is stored externally. The Stand class only keeps track of the money its made daily.
 *
 * @author Terry Weiss
 */
public class Stand {

    /**
     * This enum lists the different products the player must buy for the stand. For the sake of
     * use in strings, all enums should be singular so the caller has the option to pluralize if
     * desired.
     */
    public enum Product {
        CUP, SIGN;

        /**
         * Displays the enum value's name in lowercase.
         *
         * @return Name of the enum value in lowercase
         */
        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }


    //Public info for all stands
    public static final int BASE_HOURLY_CUSTOMERS = 10;     //Normal number of customers in an hour
    public static final int START_HOUR = 10;                //Hour stands open (10AM)
    public static final int CLOSE_HOUR = 19;                //Hour stands close (7PM)

    //Static resource prices for all stands
    private static final double[] LEMONS_PRICES = {1.0, 0.5, 0.33};     //Possible lemon prices
    private static final double[] SUGAR_PRICES = {0.5, 0.25};           //Possible sugar prices
    private static final double[] ICE_PRICES = {0.1, 0.05};             //Possible ice prices
    private static final double WATER_PRICE = 0.05;                     //Price of water
    private static final double CUPS_PRICE = 0.02;                      //Price of cups
    private static final double SIGN_PRICE = 0.25;                      //Price of signs

    private final String location;          //Locatin of the stand

    //Money-related values
    private ResourcePrices resourcePrices;  //Price of all resources
    private double pricePerCup;             //Price to make a cup
    private double openingMoney;            //Money you start with at the beginning of the day
    private double money;                   //Money currently available
    private int signsMade;                  //How many signs have been made (doesn't reset)

    //Daily stats
    private Weather weather;            //Today's weather forecast
    private int cupsMade;               //How many cups should be made
    private int dailyCustomers;         //How many potential customers throughout the day
    private int cupsSold;               //How many cups sold throughout the day
    private boolean dayGenerated;       //Whether the day's stats have been generated


    /**
     * Creates the new stand at the given location and sets the initial price per cup to $0.00.
     *
     * @param location Location of the stand
     */
    public Stand(String location) {
        this.location = location;
        pricePerCup   = 0.00;
    }

    /**
     * Generates the weather and all the daily prices randomly if the day hasn't already been.
     * generated. This method also resets the daily stats, and sets the money back to the opening
     * balance given. If the day has already been generated, everything is ignored
     *
     * @param openingBalance Initial balance to start the day
     * @return True if new day was generated
     */
    public final boolean generateDay(double openingBalance) {
        boolean generatingNewDay;    //Whether new day should be generated

        generatingNewDay = !dayGenerated; //Only generate new day if not already generated

        if (generatingNewDay) {
            weather        = Weather.values()[Game.random.nextInt(Weather.values().length)];
            resourcePrices = new ResourcePrices();
            cupsSold       = cupsMade = 0;
            dailyCustomers = 0;
            money          = openingMoney   = openingBalance;
            dayGenerated   = true;
        }

        return generatingNewDay;
    }

    /**
     * Simulates customer purchases hour-by-hour to find how many cups are sold, and adds up the
     * total profit at the end of the day. If the day has not already been generated, this method
     * will also generate a new day. This is useful when you don't need to use the info from the
     * day's stats.
     *
     * @param totalMoney Money you have at the beginning of the day for this stand.
     */
    public void runDay(double totalMoney) {
        int hourlyCustomers;    //How many potential customers will arrive during the given hour

        if (!dayGenerated) {
            generateDay(totalMoney);
        }

        for (int hour = START_HOUR; hour <= CLOSE_HOUR; hour++) {
            hourlyCustomers = hourlyCustomers();
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

    /**
     * Calculates how many customers will arrive each hour based on the weather. Also each sign
     * made adds 1% to the customers per hour. All deltas are in compliance with project specs.
     *
     * @return How many customers visit in an hour
     */
    public int hourlyCustomers() {
        int customers = BASE_HOURLY_CUSTOMERS;  //How many customers in the hour

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

    /**
     * Sets the price per cup. If the price isn't positive, an illegal argument exception is thrown.
     *
     * @param price Price per cup
     */
    public void setCupPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Negative price:  " + price);
        }

        pricePerCup = price;
    }

    /**
     * Gets the price per cup.
     *
     * @return Price per cup
     */
    public double cupPrice() {
        return pricePerCup;
    }

    /**
     * Gets the cost to make each cup.
     *
     * @return Cost per cup
     */
    public double cupCost() {
        return resourcePrices.costPerCup;
    }

    /**
     * Gets the cost to make each sign.
     *
     * @return Cost per sign
     */
    public double signCost() {
        return resourcePrices.signs;
    }

    /**
     * Gets how many signs have been made. Once a sign is made, it lasts for the lifetime of the
     * stand, but more signs can be added each day.
     *
     * @return How many signs have been made at this stand
     */
    public int getSignsMade() {
        return signsMade;
    }

    /**
     * Gets the stand's location. The location can never change for the lifetime of the stand.
     *
     * @return Stand's location
     */
    public String location() {
        return location;
    }

    /**
     * Generates a weather forecast message including the stand's location and weather.
     *
     * @return Stand's location
     */
    public String weatherForecast() {
        return "The weather in " + location + " will be " + weather + " today.";
    }

    /**
     * Buys the given quantity of cups if you have enough money. If you don't have enough money,
     * a TooExpensive exception will be thrown.
     *
     * @param quantity Number of cups to buy
     * @throws TooExpensiveException If the stand can't afford the purchase
     * @return Cost for the quantity of cups
     */
    public double buyCups(int quantity) throws TooExpensiveException {
        double cost;

        cost = quantity * resourcePrices.costPerCup;

        if (cost <= money) {
            money -= cost;
            cupsMade += quantity;
        } else {
            throw new TooExpensiveException(cost);
        }

        return cost;
    }

    /**
     * Makes the given quantity of signs if you have enough money. If you don't have enough money,
     * a TooExpensive exception will be thrown.
     *
     * @param quantity Number of signs to make
     * @throws TooExpensiveException If the stand can't afford the purchase
     * @return Cost for the quantity of signs
     */
    public double buySigns(int quantity) throws TooExpensiveException {
        double cost;

        cost = quantity * resourcePrices.signs;

        if (cost <= money) {
            money -= cost;
            signsMade += quantity;
        } else {
            throw new TooExpensiveException(cost);
        }

        return cost;
    }

    /**
     * Generates a daily report using the stand's location, cost per cup, price per cup, daily
     * customers, cups sold, cups made and net profit.
     *
     * @return Daily report message
     */
    public String dailyReport() {
        String report = "Daily Report for " + location + "\n";

        report += String.format("    Each cup cost $%-3.02f.", resourcePrices.costPerCup)
                        + String.format("  You charged $%-4.2f per cup.%n", pricePerCup)
                + "    You had " + dailyCustomers + " potential customers."
                        + "  You sold " + cupsSold + " of " + cupsMade + " cups.\n"
                + String.format("    You made a net profit of $%.2f!%n", netProfit());

        return report;
    }

    /**
     * Calculates the profit for the day.
     *
     * @return Today's profit for this stand
     */
    public double netProfit() {
        return money - openingMoney;
    }

    /**
     * This class holds all the daily prices and is unique each day for each stand randomly choosing
     * from the available prices specified by Stand.
     */
    private final class ResourcePrices {
        private final double lemons;         //Cost of lemons
        private final double sugar;          //Cost of sugar
        private final double ice;            //Cost of ice
        private final double water;          //Cost of water
        private final double cups;           //Cost of cups
        private final double signs;          //Cost of signs
        private final double costPerCup;     //Overall cost per cup of lemonade

        /**
         * Randomly generates the resource prices for the day, and the overall cost per cup.
         */
        /**
         * BEGIN Constructor
         *     Calculate and store price of lemons
         *     Calculate and store price of sugar
         *     Calculate and store price of ice
         *     Calculate and store price of water
         *     Calculate and store price of cups
         *     Calculate and store price of signs
         *     Add all prices and store cost per cup
         * END Constructor
         */
        private ResourcePrices() {
            lemons = LEMONS_PRICES[Game.random.nextInt(LEMONS_PRICES.length)];
            sugar  = SUGAR_PRICES[Game.random.nextInt(SUGAR_PRICES.length)];
            ice    = ICE_PRICES[Game.random.nextInt(ICE_PRICES.length)];
            water  = WATER_PRICE;
            cups   = CUPS_PRICE;
            signs  = SIGN_PRICE;
            costPerCup = lemons + sugar + ice + water + cups;
        }
    }

    /**
     * The TooExpensive exception should be thrown whenever a purchase will cost more than what
     * a Stand currently can afford.
     */
    public class TooExpensiveException extends Exception {
        /**
         * Constructs a new TooExpensive exception giving the cost and how much money the stand has.
         *
         * @param cost Cost that was too expensive
         */
        public TooExpensiveException(double cost) {
            super(String.format("$%.2f is too expensive. Stand has $%.2f.", cost, money));
        }
    }
}
