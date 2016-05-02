/**************************************************************************************************
 * Program Name   :
 * Author         : Terry Weiss
 * Date           : Apr 21, 2016
 * Course/Section :
 * Program Description:
 **************************************************************************************************/

package lemonadestand;

public class Stand {
    public static enum Resource {
        CUPS, ICE, LEMONS, SUGAR, WATER;
    }


    public static final double STAND_PRICE = 150.00;
    public static final double START_PRICE = 100.00;

    private static final int START_HOUR = 10;
    private static final int END_HOUR   = 19;


    private ResourcePrices resourcePrices;
    private double pricePerCup;

    private int cupsMade, cupsSold;
    private double moneyAtOpen, money;
    private final String location;
    private boolean dayGenerated;

    private Weather weather;

    public Stand(String location, double initialMoney) {
        this.location = location;
        moneyAtOpen   = money = initialMoney;
        pricePerCup   = 0.00;

        generateDay();
    }

    public final void generateDay() {
        if (!dayGenerated) {
            resourcePrices = new ResourcePrices();
            cupsMade = cupsSold = 0;
            weather = Weather.values()[Game.random.nextInt(Weather.values().length)];
            dayGenerated = true;
        }
    }

    public void runDay(double totalMoney) {
        generateDay();

        for (int hour = START_HOUR; hour <= END_HOUR; hour++) {

        }

        dayGenerated = false;
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

    public double money() {
        return money;
    }

    public String location() {
        return location;
    }

    public String weatherForecast() {
        return "The weather in " + location + " will be " + weather + " today.";
    }

    public boolean makeProduct(String product, int quantity) {
        boolean purchased = false;
        double cost = 0.00;

        if (product.equalsIgnoreCase("cups")) {
            cost = quantity * resourcePrices.costPerCup;
        } else if (product.equalsIgnoreCase("signs")) {
            cost = quantity * resourcePrices.signs;
        } else {
            throw new IllegalArgumentException("Illegal purchase:  " + product);
        }

        if (cost <= money) {
            money -= cost;
            purchased = true;
        }

        return purchased;
    }

    public String dailyReport() {
        StringBuilder report = new StringBuilder();

        report.append("Daily Report for ").append(location).append("\n")
              .append(String.format("    You charged $%-4.2f per cup.%n", pricePerCup))
              .append(String.format("    Each cup cost $%-3.02f.%n", resourcePrices.costPerCup))
              .append("    You sold ").append(cupsSold).append(" cups.\n")
              .append(String.format("    You made a net profit of $%-4.2f!%n", netProfit()));

        return report.toString();
    }

    public double netProfit() {
        return money - moneyAtOpen;
    }
}
