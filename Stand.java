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

    private int cupsMade, cupsSold, signsMade;
    private final String location;
    private final Business business;
    private boolean dayGenerated;
    private Weather weather;

    public Stand(String location, Business business) {
        this.location = location;
        this.business = business;
        pricePerCup   = 0.00;

        generateDay();
    }

    public final void generateDay() {
        if (!dayGenerated) {
            weather        = Weather.values()[Game.random.nextInt(Weather.values().length)];
            resourcePrices = new ResourcePrices();
            cupsMade       = cupsSold = 0;
            dayGenerated   = true;
        }
    }

    public void runDay(double totalMoney) {
        generateDay();

        for (int hour = START_HOUR; hour <= CLOSE_HOUR; hour++) {
            int hourlyCustomers = hourlyCustomers();
            for (int customer = 0; customer < hourlyCustomers; customer++) {
                if (!Customer.tooExpensive(pricePerCup, weather)) {
                    cupsSold++;
                }
            }
        }

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

        if (cost <= business.getMoney()) {
            business.spend(cost);
            purchased = true;

            if (product.equalsIgnoreCase("cups")) {
                cupsMade += quantity;
            } else {
                signsMade += quantity;
            }
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
        return cupsSold * pricePerCup;
    }

    private class ResourcePrices {
        public double lemons;
        public double sugar;
        public double ice;
        public double water;
        public double cups;
        public double signs;
        public double costPerCup;

        public ResourcePrices() {
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
