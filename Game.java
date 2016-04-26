/**************************************************************************************************
 * Program Name   :
 * Author         : Terry Weiss
 * Date           : Apr 21, 2016
 * Course/Section :
 * Program Description:
 **************************************************************************************************/

package lemonadestand;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private static final int MAX_DAYS = 30;
    private static final double STARTING_MONEY = 100.00;

    private static final Random generator = new Random();
    private static final Scanner scanner = new Scanner(System.in);

    public static double runGame() {
        String userContinues = "y";
        ArrayList<Stand> locations = new ArrayList<>();
        double totalAssets = STARTING_MONEY;

        System.out.println("Congratulations on starting your new business!!");
        locations.add(new Stand(promptStandLocation(), totalAssets));

        Queue<String> reports = new LinkedList<>();
        double[] dailyTotals = new double[MAX_DAYS];
        for (int day = 1; day <= MAX_DAYS && !userContinues.equalsIgnoreCase("n"); day++) {
            double dailyProfit = 0.00;

            System.out.println("\nDay " + day);
            for (Stand stand : locations) {
                stand.startDay(totalAssets);
                System.out.println("The weather in " + stand.location() + " will be "
                                        + stand.weatherForecast());

                System.out.printf("Each sign will cost $3.2f. ", stand.signPrice());
                int signs = -1;
                do {
                    try {
                        System.out.print("How many signs do you want to make? ");
                        signs = Integer.parseInt(scanner.nextLine());
                        if (signs < 0) {
                            System.out.print("No one wants to buy your signs today. ");
                        }
                    } catch (NumberFormatException e) {
                        System.out.print("That isn't a price! ");
                    }
                } while (signs < 0);


                System.out.printf("Each cup will cost $%3.2f. ", stand.cupPrice());

                double cupPrice = 0.00;
                do {
                    System.out.println("Enter your price per cup: ");
                    try {
                        cupPrice = Double.parseDouble(scanner.nextLine());
                        if (cupPrice <= 0.00) {
                            System.out.print("The price must be positive! ");
                        }
                    } catch (NumberFormatException e) {
                        System.out.print("That isn't a price! ");
                    }
                } while (cupPrice <= 0.00);
                stand.setCupPrice(cupPrice);

                reports.add(stand.dailyReport());
                dailyProfit += stand.netProfit();
            }

            dailyTotals[day] = dailyProfit;
            for (String report : reports) {
                System.out.println(reports.remove());
            }
            System.out.printf("Current total assets: $%5.02f%n%n", totalAssets);

            System.out.print("Start another day [Y/n]? ");
            userContinues = scanner.nextLine();

            if (totalAssets >= Stand.STAND_PRICE) {
                System.out.print("Would you like to purchase another stand [y/N]? ");
                String response = scanner.nextLine();
                if (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("yes")) {
                    locations.add(new Stand(promptStandLocation(), totalAssets));
                }
            }
        }

        if (dailyTotals[0] != 0.00) {
            System.out.println("Progress Report:");
            for (int day = 0; day < MAX_DAYS && dailyTotals[day] != 0.00; day++) {
                System.out.printf("    Money on day %2d: $%-6.02f%n", day, dailyTotals[day]);
            }
        }

        return totalAssets;
    }

    private static double promptCupPrice() {
        double price = 0.00;

        System.out.print("Enter the price for a cup: ");

        do {
            try {
                price = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("That isn't a price! ");
            }
        } while (price <= 0.00);

        return price;
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
