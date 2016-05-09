/**************************************************************************************************
 * Program Name   : Final Project Option #2 - Lemonade Stand
 * Author         : Terry Weiss
 * Date           : May 5, 2016
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

import java.util.LinkedList;

/**
 * The business class holds the locations of all the Stands in the current game and also keeps
 * track of how much money is held between all of them.
 *
 * @author Terry Weiss
 */
public class Business {
    public static final double STAND_PRICE    = 150.00;     //Cost of a new stand
    public static final double STARTING_MONEY = 100.00;     //Money you start with in each stand

    private final LinkedList<Stand> locations;  //Linked list of the Stands/business locations

    private double money;   //How much money the business since last close of business day

    /**
     * Constructs a new Business. This creates and adds your first stand to the list of locations
     * for free, and also sets up your initial money.
     *
     * @param initialStand The first stand to be added (given for free)
     */
    /*
     * START Constructor
     *     Set money to starting money
     *     Initialize locations as an empty linked list
     *     Add initial stand
     * END Constructor
     */
    public Business(Stand initialStand) {
        money = STARTING_MONEY;
        locations = new LinkedList<>();
        locations.add(initialStand);
    }

    /**
     * Gets how much money the business has.
     *
     * @return Business's money
     */
    /*
     * BEGIN Get Money
     *     Return amount of money
     * END Get Money
     */
    public double getMoney() {
        return money;
    }

    /**
     * The spend method takes the given expense away from the business's money if it has enough
     * money. If the expense is negative, an IllegalArgumentException will be thrown.
     *
     * @param expense Amount of the expense
     * @throws IllegalArgumentException If expense is negative
     * @return Whether the expense was spent
     */
    /*
     * START Spend
     *     Set spent to false
     *     IF (Expense is negative) THEN
     *         Throw illegal argument exception
     *     ELSE IF (expense isn't more than money available) THEN
     *         Subtract expense from money
     *         Set spent to true
     *     END IF
     *     Return whether spent
     * END Spend
     */
    public boolean spend(double expense) {
        boolean spent = false;

        if (expense < 0) {
            throw new IllegalArgumentException("Negative expenses aren't allowed:" + expense);
        } else if (money >= expense) {
            money -= expense;
            spent = true;
        }

        return spent;
    }

    /**
     * Adds the specified profit to the business's money pool.
     *
     * @param profit Amount of profit
     */
    /*
     * START Add Profit
     *     Add profit to money
     * END ADD Profit
     */
    public void addProfit(double profit) {
        money += profit;
    }

    /**
     * Buys a new stand if there's enough money, and adds the stand to the list of locations.
     *
     * @param newStand New Stand to be added
     * @return Whether stand was able to be added
     */
    /*
     * START Buy Stand
     *     Set added to false
     *     IF (Money is at least cost of a new stand) THEN
     *         Subtract stand price from money
     *         Add new stand to locations list
     *         Set added to true
     *     END IF
     *     Return whether added
     * END Buy Stand
     */
    public boolean buyStand(Stand newStand) {
        boolean added = false;

        if (money >= STAND_PRICE) {
            money -= STAND_PRICE;
            locations.add(newStand);
            added = true;
        }

        return added;
    }

    /**
     * Gets the list of locations as a linked list.
     *
     * @return Linked List of locations
     */
    protected LinkedList<Stand> locations() {
        return locations;
    }
}
