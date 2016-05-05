package lemonadestand;

public class Customer {
    public final static int BASE_HOURLY_CUSTOMERS = 10;  //Normal number of customers in an hour

    public static int hourlyCustomers(Weather weather, int signsMade) {
        int customers = BASE_HOURLY_CUSTOMERS;

        switch (weather) {
            case RAINY:
                customers -= Game.random.nextInt(3);
            case CLOUDY:
                customers += Game.random.nextInt(5);
            case SUNNY:
                customers += Game.random.nextInt(11);
        }

        customers += customers * (signsMade / 100);

        return customers;
    }

    /**
     * This method determines whether a customer will
     *
     * @param price
     * @param weather
     * @return
     */
    private static boolean willBuy(double price, Weather weather) {
        boolean willBuy = false;

        return willBuy;
    }


    /**
     * This method determines whether a customer considers a cup of lemonade to be too expensive
     * based on the cup's price and the customer's generosity. Cups of lemonade can cost between
     * $0.70 and $1.67 with the average cup costing $1.13. For playability, the average customer
     * will be willing to pay at least $1.50 before they consider it's too expensive. However, to
     * simulate customers' different personalities, they might be stingy, normal or generous. They
     * also will have a random change to the price they're willing to change to pay more if they're
     * generous or less if they're stingy. If the price of the cup isn't more than what they're
     * willing to pay, it's not too expensive.
     *
     * @param price Price of a cup of lemonade
     * @return True if the cup is too expensive
     */
    private static boolean tooExepensive(double price) {
        final double LOWEST_BUY_PRICE = 1.50;   //All customers will pay at least $1.50

        int stinginess;                         //Whether customer is stingy, normal or nice
        double generosityDelta;                 //Delta/Change based on generosity
        double willingToPay;                    //How much the customer is willing to pay

        stinginess = -1 + Game.random.nextInt(3);
        generosityDelta = Game.random.nextInt(50) / 100;   //Up to $0.50 cents delta/change;

        willingToPay = LOWEST_BUY_PRICE + stinginess * generosityDelta;

        return (price > willingToPay);
    }
}
