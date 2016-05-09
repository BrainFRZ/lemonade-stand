/**************************************************************************************************
 * Program Name   :
 * Author         : Terry Weiss
 * Date           : Apr 21, 2016
 * Course/Section :
 * Program Description:
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
    /*
     * START Run Game
     *     Display initial message and starting money
     *     Prompt for first stand location and create new business
     *     Create new queue of reports
     *     Create new array of daily totals
     *     FOR (Each day until max or until user quits)
     *         Init daily profit and expenses to 0.00
     *         Set daily money to business's money
     *         Display day number
     *         FOR (Each stand)
     *             Generate the stand's day with daily money - daily expenses so far
     *             Display stand's forecast and current money left
     *             TRY
     *                 Prompt number of new signs
     *                 Buy new signs and add to expenses
     *             CATCH (Too expensive)
     *                 Display it's too expensive (guaranteed not to happen)
     *             END TRY-CATCH
     *             TRY
     *                 Prompt number of new cups
     *                 Buy new cups and add to expenses
     *             CATCH (Too expensive)
     *                 Display it's too expensive (guaranteed not to happen)
     *             END TRY-CATCH
     *             Prompt and set cup price
     *             Run day with daily money - daily expenses
     *             Generate and add the report to the report queue
     *             Add stand's net profit to daily profit
     *         END FOR
     *         Add total daily profit to business
     *         Set daily total for this day to business's new money
     *         WHILE (Daily reports isn't empty)
     *             Display and remove first report
     *         END WHILE
     *         Display total assets
     *         IF (player is bankrupt) THEN
     *             Display bankrupt message
     *         ELSE IF (it isn't the last day) THEN
     *             Prompt for another day
     *             IF (user wants another day and has enough money) THEN
     *                 Prompt for another stand
     *             END IF
     *         END IF
     *     END FOR
     *     IF (Daily totals isn't empty) THEN
     *         Display progress report title
     *         FOR (each day until there isn't a negative total)
     *             Display day's total
     *         END FOR
     *     END IF
     *     Return business's final money amount
     * END Run Game
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
    /*
     * BEGIN Prompt Cup Purchase
     * Prompt and return resource purchase for cups with cup cost, max cups and negative quantity
     *     message
     * END Prompt Cup Purchase
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
    /*
     * BEGIN Prompt Sign Purchase
     * Prompt and return resource purchase for signs with sign cost, max signs and negative quantity
     *     message
     * END Prompt Sign Purchase
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
    /*
     * BEGIN Prompt Resource Purchase
     *     Display cost of product
     *     DO WHILE (quantity is negative or more than max allowed)
     *         TRY
     *             Prompt for quantity
     *             IF (input is empty) THEN
     *                 Parse quantity to an integer
     *             ELSE
     *                 Set quantity to 0
     *             END IF
     *
     *             IF (quantity is negative) THEN
     *                 Display error message
     *             ELSE IF (quantity is over max) THEN
     *                 Display can't afford message
     *             END IF
     *         CATCH (Number Format Exception)
     *             Display invalid number message
     *             Set quantity out of bounds
     *         END TRY-CATCH
     *     END DO-WHILE
     *     Return quantity
     * END Prompt Resource Purchase
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
    /*
     * BEGIN Prompt Cup Price
     *     Set cup price to $0.00
     *     DO WHILE (cup price isn't positive)
     *         Prompt for cup price
     *         IF (input is more than 1 character long and starts with $)
     *         TRY
     *             Set cup price to parsed input as a double
     *             IF (cup price isn't positive) THEN
     *                 Display negative price error message
     *             END IF
     *         CATCH (Number format exception)
     *             Display invalid number error message
     *         END TRY-CATCH
     *     END DO-WHILE
     *     Return cup price
     * END Prompt Cup Price
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
    /*
     * BEGIN Prompt New Stand
     *     Set added to true
     *     Prompt whether to buy a new stand
     *     IF (user's response is "y" or "yes") THEN
     *         Set added to true
     *         DO WHILE (added is false)
     *             Prompt for new location name
     *             FOR (each stand in the business locations)
     *                 IF (stand location name matches new location) THEN
     *                     Set added to false
     *                 END IF
     *             END FOR
     *             IF (added) THEN
     *                 Buy new stand at new location
     *             ELSE
     *                 Display error that stand already exists at that location
     *             END IF
     *         END DO-WHILE
     *     END IF
     * END Prompt New Stand
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
    /**
     * BEGIN Prompt Stand Location
     *     Prompt new stand location
     *     WHILE (location is empty)
     *         Reprompt for stand location
     *     END WHILE
     *     Return new location
     * END Prompt Stand Location
     */
    private static String promptStandLocation() {
        System.out.print("Where would you like to start your new stand? ");
        String location = scanner.nextLine();

        while (location.isEmpty()) {
            System.out.print("Please enter a location: ");
            location = scanner.nextLine();
        }

        return location;
    }
}
