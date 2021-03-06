/**************************************************************************************************
 * Program Name   : Final Project Option #2 - Lemonade Stand
 * Author         : Terry Weiss
 * Date           : Apr 26, 2016
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
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

/**
 * This class runs the entire game. This class is only a collection of functions, and should
 * therefore never be instantiated.
 *
 * @author Terry Weiss
 */
public class Game {
    private static final int MAX_DAYS = 30;     //How many days the game lasts

    protected static final Random random = new Random();            //Random number generator
    private static final Scanner scanner = new Scanner(System.in);  //Input scanner

    /**
     * This method runs the entire game. For the player, there are three "stages," two of which are
     * visible. First, the player must prepare each stand for the upcoming day by buying all the
     * resources and planning what to put where. The player is given the weather forecast for each
     * stand one at a time, and must decide how many cups to make and what price to charge
     * accordingly. Since all the stands are operating concurrently in-game, the player won't get
     * any money until the next day. At the end of the day, a list of reports is displayed to the
     * player so they can see how well (or poorly) each stand did. The player is then also asked if
     * they want to buy a new stand to open the next day. Once the max days are over, or if the
     * player ends early, an overall report will show their end-of-day totals for each day. The
     * method returns their final amount of money as their score. Current stands owned do not add
     * to the score, but they do allow for more customers and more chances of sunny weather, and
     * therefore much more money in-take.
     *
     * @return Final amount of money as a score.
     */
    public static double runGame() {
        final Business business;        //Instance of business for this game

        String userContinues = "y";     //Value user must type to continue
        double dailyProfit;             //Business's profit each day (Not the newspaper)
        double dailyExpenses;           //Business's expenses each day
        double dailyMoney;              //Business's money at last close of business day
        int newSigns;                   //New signs made each day each stand
        int newCups;                    //New cups made each day each stand
        Queue<String> reports;          //Queue of daily reports


        System.out.println("Congratulations on starting your new lemonade business!!");
        System.out.printf("You're beginning your venture with $%3.2f.%n", Business.STARTING_MONEY);
        business = new Business(new Stand(promptStandLocation()));

        reports = new LinkedList<>();
        double[] dailyTotals = new double[MAX_DAYS];
        for (int day = 1; day <= MAX_DAYS && !userContinues.equalsIgnoreCase("n")
                    && business.getMoney() > 0.00 ; day++)
        {
            dailyProfit = 0.00;
            dailyExpenses = 0.00;
            dailyMoney = business.getMoney();

            System.out.println("\nDay " + day + " Prep");
            for (Stand stand : business.locations()) {
                stand.generateDay(dailyMoney - dailyExpenses);

                System.out.println("\n" + stand.weatherForecast());
                System.out.printf("You currently have $%3.2f left for the day.%n",
                                        dailyMoney - dailyExpenses);

                try {
                    newSigns = promptSignPurchase(stand.getSignsMade(), stand.signCost(),
                                                    dailyMoney - dailyExpenses);
                    dailyExpenses += stand.buySigns(newSigns);
                } catch (Stand.TooExpensiveException e) {
                    //Shouldn't happen; Valid quantities are guaranteed by promptResourcePurchase
                    System.out.println("You couldn't afford to buy any signs today.");
                }

                try {
                    newCups = promptCupPurchase(stand.cupCost(), dailyMoney - dailyExpenses);
                    dailyExpenses += stand.buyCups(newCups);
                } catch (Stand.TooExpensiveException e) {
                    //Shouldn't happen; Valid quantities are guaranteed by promptResourcePurchase
                    System.out.println("You couldn't afford to buy any cups today.");
                }

                stand.setCupPrice(promptCupPrice());

                stand.runDay(dailyMoney - dailyExpenses);

                reports.add(stand.dailyReport());
                dailyProfit += stand.netProfit();
            }

            business.addProfit(dailyProfit);
            dailyTotals[day - 1] = business.getMoney();
            while (!reports.isEmpty()) {
                System.out.println("\n" + reports.remove());
            }
            System.out.printf("Current total assets: $%5.02f%n%n", business.getMoney());

            if (business.getMoney() <= 0.00) {
                System.out.println("Oh no, you've gone bankrupt!! Try again!\n");
            } else if (day < MAX_DAYS) {
                System.out.print("Start another day [Y/n]? ");
                userContinues = scanner.nextLine();

                if (!userContinues.equalsIgnoreCase("n")
                        && business.getMoney() >= Business.STAND_PRICE)
                {
                    promptNewStand(business);
                }
            }
        }

        if (dailyTotals[0] > 0.00) {
            System.out.println("Progress Report:");
            for (int day = 0; day < MAX_DAYS && dailyTotals[day] > 0.00; day++) {
                System.out.printf("    Money on day %2d: $%-6.02f%n", day + 1, dailyTotals[day]);
            }
        }

        return business.getMoney();
    }

    /**
     * Prompts for how many cups to purchase given the cost of cups and how much money you
     * currently have available. This method calculates the maximum cups that the player can buy to
     * guarantee {@link promptResourcePurchase} will always return a valid and affordable purchase
     * as long as all argument info is correct.
     *
     * @param cupCost Cost per cup
     * @param money   How much money you have
     * @return        Number of cups purchased
     */
    private static int promptCupPurchase(double cupCost, double money) {
        return promptResourcePurchase(Stand.Product.CUP, cupCost,
                 (int)(money / cupCost), "You can't drink your own product!");
    }

    /**
     * Prompts for how many signs to make given the current number of signs made, cost of signs,
     * and how much money you currently have available. This method calculates the maximum signs
     * that the player can buy to guarantee {@link promptResourcePurchase} will always return a
     * valid and affordable purchase as long as all argument info is correct.
     *
     * @param signsMade  How many signs are already made
     * @param signCost   How much it costs to make a sign
     * @param money      How much money you have
     * @return Number of signs purchased
     */
    private static int promptSignPurchase(int signsMade, double signCost, double money) {
        System.out.println("You currently have " + signsMade + " signs.");
        return promptResourcePurchase(Stand.Product.SIGN, signCost,
                (int)(money / signCost), "No one wants to buy your signs today.");
    }

    /**
     * Prompts for a resource purchase using the given product, product price, max quantity and
     * error message if the quantity is negative.
     *
     * @param product               Product to be made
     * @param price                 Price per product
     * @param max                   Maximum allowed quantity
     * @param negativeErrorMessage  Error message for when the user types in a negative quantity
     * @return Quantity of the product purchased
     */
    private static int promptResourcePurchase(Stand.Product product, double price,
            int max, String negativeErrorMessage)
    {
        String input;       //User's input for quantity
        int quantity = 0;   //Quantity of resource to purchase

        System.out.printf("Each %s costs $%3.2f to make. ", product, price);
        do {
            try {
                System.out.print("How many " + product + "s do you want to make [" +
                        max + " max]? ");
                input = scanner.nextLine();

                if (!input.isEmpty()) {
                    quantity = Integer.parseInt(input);
                } else {
                    quantity = 0;   //Reset to 0 in case it's been looped back with a negative
                }

                if (quantity < 0) {
                    System.out.print(negativeErrorMessage + " ");
                } else if (quantity > max) {
                    System.out.print("You can't afford that many! ");
                }
            } catch (NumberFormatException e) {
                System.out.print("That isn't a quantity! ");
                quantity = -1;
            }
        } while (quantity < 0 || quantity > max);

        return quantity;
    }

    /**
     * Prompts for the price to charge for a cup of lemonade.
     *
     * @return Price to charge per cup
     */
    private static double promptCupPrice() {
        double cupPrice = 0.00;     //Price per cup set
        String input;               //Input for price per cup

        do {
            System.out.print("Enter your price per cup: ");
            input = scanner.nextLine();

            if (input.length() > 1 && input.charAt(0) == '$') {
                input = input.substring(1);
            }

            try {
                cupPrice = Double.parseDouble(input);
                if (cupPrice <= 0.00) {
                    System.out.print("It must be bad if you're paying customers to buy! ");
                }
            } catch (NumberFormatException e) {
                System.out.print("That isn't a price! ");
            }
        } while (cupPrice <= 0.00);

        return cupPrice;
    }

    /**
     * Prompts whether or not to buy a new stand, given the business. If a player enters a name
     * that matches any of the other stands open, they will have to name it something else, since
     * each stand has its own weather and should be located in different areas to prevent naming
     * confusion with the reports. Otherwise, this method will buy a stand in the given business.
     *
     * @param business Business being run from the current Game
     */
    private static void promptNewStand(Business business) {
        boolean added;          //Whether or not the new stand was added
        String newLocation;     //Name of new location

        System.out.print("Would you like to purchase another stand [y/N]? ");
        String response = scanner.nextLine();
        if (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("yes")) {
            added = true;
            do {
                newLocation = promptStandLocation();
                for (Stand stand : business.locations()) {
                    if (stand.location().equalsIgnoreCase(newLocation)) {
                        added = false;
                    }
                }

                if (added) {
                    business.buyStand(new Stand(newLocation));
                } else {
                    System.out.println("You already have a stand in " + newLocation + "!");
                }
            } while (!added);
        }
    }

    /**
     * Prompts the location of a new stand. If the user enters an empty location, they will be
     * prompted for a new location. Therefore, this method guarantees all locations will not be
     * empty.
     *
     * @return Location of the new stand
     */
        System.out.print("Where would you like to start your new stand? ");
        String location = scanner.nextLine();

        while (location.isEmpty()) {
            System.out.print("Please enter a location: ");
            location = scanner.nextLine();
        }

        return location;
    }

    /**
     * This prevents the Game utility class from being instantiated.
     */
    private Game() {
    }
}
