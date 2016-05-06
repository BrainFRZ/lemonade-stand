package lemonadestand;

import java.util.LinkedList;

public class Business {
    public static final double STAND_PRICE    = 150.00;     //Cost of a new stand
    public static final double STARTING_MONEY = 100.00;     //Money you start with in each stand

    private final LinkedList<Stand> locations;

    private double money;

    /**
     * Constructs a new Business. This creates and adds your first stand to the list of locations
     * for free, and also sets up your initial money.
     *
     * @param initialLocation Location name of the first stand
     */
    /*
     * START Constructor
     *     Set money to starting money
     *     Initialize locations as an empty linked list
     *     Create and add new stand based on initial location
     * END Constructor
     */
    public Business(String initialLocation) {
        money = STARTING_MONEY;
        locations = new LinkedList<>();
        locations.add(new Stand(initialLocation, this));
    }

    public double getMoney() {
        return money;
    }

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

    public void addProfit(double profit) {
        money += profit;
    }

    public boolean buyStand(Stand newStand) {
        boolean added = false;

        if (money >= STAND_PRICE) {
            money -= STAND_PRICE;
            locations.add(newStand);
            added = true;
        }

        return added;
    }

    public LinkedList<Stand> locations() {
        return locations;
    }
}
