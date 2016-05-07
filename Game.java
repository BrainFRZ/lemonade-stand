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

public class Game {
    private static final int MAX_DAYS = 30;

    protected static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);

    public static double runGame() {
        String userContinues = "y";
        Business business;

        System.out.println("Congratulations on starting your new lemonade business!!");
        System.out.printf("You're beginning your venture with $%3.2f.%n", Business.STARTING_MONEY);
        business = new Business(new Stand(promptStandLocation()));

        Queue<String> reports = new LinkedList<>();
        double[] dailyTotals = new double[MAX_DAYS];
        for (int day = 1; day <= MAX_DAYS && !userContinues.equalsIgnoreCase("n"); day++) {
            double dailyProfit = 0.00;
            double dailyExpenses = 0.00;
            double dailyMoney = business.getMoney();

            System.out.println("\nDay " + day + " Prep");
            for (Stand stand : business.locations()) {
                stand.generateDay(dailyMoney - dailyExpenses);

                System.out.println(stand.weatherForecast());
                System.out.printf("You currently have $%3.2f left for the day.%n",
                                        dailyMoney - dailyExpenses);

                System.out.println("You currently have " + stand.getSignsMade() + " signs.");
                dailyExpenses += promptResourcePurchase(stand, dailyMoney - dailyExpenses,
                                "sign", stand.signPrice(), "No one wants to buy your signs today.");

                dailyExpenses += promptResourcePurchase(stand, dailyMoney - dailyExpenses,
                                    "cup", stand.cupCost(), "You can't drink your own product!");

                stand.setCupPrice(promptCupPrice());

                stand.runDay(dailyMoney - dailyExpenses);

                reports.add(stand.dailyReport());
                dailyProfit += stand.netProfit();
            }

            business.addProfit(dailyProfit);
            dailyTotals[day - 1] = business.getMoney();
            while (!reports.isEmpty()) {
                System.out.println(reports.remove());
            }
            System.out.printf("Current total assets: $%5.02f%n%n", business.getMoney());

            if (day < MAX_DAYS) {
                System.out.print("Start another day [Y/n]? ");
                userContinues = scanner.nextLine();

                if (!userContinues.equalsIgnoreCase("n")
                        && business.getMoney() >= Business.STAND_PRICE)
                {
                    System.out.print("Would you like to purchase another stand [y/N]? ");
                    String response = scanner.nextLine();
                    if (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("yes")) {
                        business.buyStand(new Stand(promptStandLocation()));
                    }
                }
            }
        }

        if (dailyTotals[0] != 0.00) {
            System.out.println("Progress Report:");
            for (int day = 0; day < MAX_DAYS && dailyTotals[day] != 0.00; day++) {
                System.out.printf("    Money on day %2d: $%-6.02f%n", day + 1, dailyTotals[day]);
            }
        }

        return business.getMoney();
    }

    private static double promptResourcePurchase(Stand stand, double currentMoney,
            String resourceName, double price, String negativeErrorMessage)
    {
        final double CANT_AFFORD = -1.0;

        String input;
        int quantity = 0;
        double resourceCost = CANT_AFFORD;

        System.out.printf("Each %s costs $%3.2f to make. ", resourceName, price);
        do {
            try {
                System.out.print("How many " + resourceName + "s do you want to make [" +
                        (int)(currentMoney / price) + " max]? ");
                input = scanner.nextLine();

                if (!input.isEmpty()) {
                    quantity = Integer.parseInt(input);
                }
            } catch (NumberFormatException e) {
                System.out.print("That isn't a quantity! ");
            }

            if (quantity < 0) {
                System.out.print(negativeErrorMessage + " ");
            } else {
                resourceCost = stand.makeProduct(resourceName, quantity);

                if (resourceCost == CANT_AFFORD) {
                    System.out.print("You can't afford that many! ");
                }
            }
        } while (quantity < 0 || resourceCost == CANT_AFFORD);

        return resourceCost;
    }

    private static double promptCupPrice() {
        double cupPrice = 0.00;
        String input;

        do {
            System.out.print("Enter your price per cup: ");
            try {
                input = scanner.nextLine();

                if (input.charAt(0) == '$' && input.length() > 1) {
                    input = input.substring(1);
                }

                cupPrice = Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("That isn't a price! ");
            }

            if (cupPrice <= 0.00) {
                System.out.print("It must be bad if you're paying customers to buy! ");
            }
        } while (cupPrice <= 0.00);

        return cupPrice;
    }

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
