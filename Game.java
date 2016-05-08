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
        double dailyProfit;     //Business's profit each day (Not the newspaper)
        double dailyExpenses;   //Business's expenses each day
        double dailyMoney;      //Business's money at last close of business day
        int newSigns;           //New signs made each day each stand
        int newCups;            //New cups made each day each stand


        System.out.println("Congratulations on starting your new lemonade business!!");
        System.out.printf("You're beginning your venture with $%3.2f.%n", Business.STARTING_MONEY);
        business = new Business(new Stand(promptStandLocation()));

        Queue<String> reports = new LinkedList<>();
        double[] dailyTotals = new double[MAX_DAYS];
        for (int day = 1; day <= MAX_DAYS && !userContinues.equalsIgnoreCase("n"); day++) {
            dailyProfit = 0.00;
            dailyExpenses = 0.00;
            dailyMoney = business.getMoney();

            System.out.println("\nDay " + day + " Prep");
            for (Stand stand : business.locations()) {
                stand.generateDay(dailyMoney - dailyExpenses);

                System.out.println("\n" + stand.weatherForecast());
                System.out.printf("You currently have $%3.2f left for the day.%n",
                                        dailyMoney - dailyExpenses);

                newSigns = promptSignPurchase(stand, dailyMoney - dailyExpenses);
                dailyExpenses += stand.buySigns(newSigns);

                newCups = promptCupPurchase(stand.cupCost(), dailyMoney - dailyExpenses);
                dailyExpenses += stand.buyCups(newCups);

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

    private static int promptCupPurchase(double cupCost, double money) {
        return promptResourcePurchase(Stand.Product.CUP, cupCost,
                 (int)(money / cupCost), "You can't drink your own product!");
    }

    private static int promptSignPurchase(Stand stand, double money) {
        System.out.println("You currently have " + stand.getSignsMade() + " signs.");
        return promptResourcePurchase(Stand.Product.SIGN, stand.signPrice(),
                (int)(money / stand.signPrice()), "No one wants to buy your signs today.");
    }

    private static int promptResourcePurchase(Stand.Product product, double price,
            int max, String negativeErrorMessage)
    {
        String input;
        int quantity = 0;

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
            }
        } while (quantity < 0 || quantity > max);

        return quantity;
    }

    private static double promptCupPrice() {
        double cupPrice = 0.00;
        String input;

        do {
            System.out.print("Enter your price per cup: ");
            try {
                input = scanner.nextLine();

                if (input.length() > 1 && input.charAt(0) == '$') {
                    input = input.substring(1);
                }

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
