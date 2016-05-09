package lemonadestand;

/**
 * The Customer class is a static calculator to determine whether each customer will be willing to
 * buy a cup of lemonade. Every customer will be willing to pay a different price based on their
 * personality (whether they're stingy or generous) and how they feel about the weather. This class
 * is only a collection of functions, and therefore should not be instantiated.
 *
 * @author Terry Weiss
 */
public class Customer {
    /**
     * This method determines whether a customer considers a cup of lemonade to be too expensive
     * based on the cup's price and the customer's generosity. Cups of lemonade can cost between
     * $0.70 and $1.67 with the average cup costing $1.13. For playability, the average customer
     * will be willing to pay at least $2.25 before they consider it's too expensive.
     * <p>
     * <p>However, to simulate customers' different personalities, they might be stingy, normal or
     * generous. They also will have a random change to the price they're willing to change to pay
     * more if they're generous or less if they're stingy. Weather is also a factor, since someone
     * will be willing to pay more on a hot and sunny day but not as much when it's a cool and
     * cloudy day. Overall, the customer's price range is $1.50 (stingy and cool) to $3.00 (generous
     * and hot).
     * <p>
     * <p>If the price of the cup isn't more than what they're willing to pay, it's not too
     * expensive.
     *
     * @param price Price of a cup of lemonade
     * @param weather Weather at the stand the customer is at
     * @return True if the cup is too expensive
     */
    /*
     * BEGIN Too Expensive
     *     Initialize price they're willing to pay to the lowest buy price ($2.25)
     *     Calculate the stinginess multiplier between -1 and 1
     *     Calculate generosity delta randomly up to 50 cents
     *     Add stinginess multiplier times generosity delta to the price willing to be paid
     *     Add weather modifier to the price willing to be paid
     *     Return if price is more than the customer is willing to pay
     * END Too Expensive
     */
    public static boolean tooExpensive(double price, Weather weather) {
        final double LOWEST_BUY_PRICE = 2.25;       //Average customers will pay $2.25

        int stinginess;                             //Whether customer is stingy, normal or generous
        double generosityDelta;                     //Delta/Change based on generosity
        double willingToPay = LOWEST_BUY_PRICE;     //How much the customer is willing to pay

        stinginess = -1 + Game.random.nextInt(3);
        generosityDelta = Game.random.nextInt(50) / 100.00;   //Up to $0.50 cents delta/change;

        willingToPay += stinginess * generosityDelta;
        willingToPay += weatherModifier(weather);

        return (price > willingToPay);
    }

    /**
     * This method calculates how much money a customer will be willing to pay more or less as
     * a result of the weather. Currently, on a cool and cloudy day a customer will only be willing
     * to pay 25 cents less than normal. On a hot and sunny day, a customer will be willing to pay
     * 25 cents more than normal. Otherwise, the modifier is 0.
     *
     * @param weather The current weather at the stand
     * @return The amount of money a customer will pay more or less because of the weather
     */
    /**
     * BEGIN Weather Modifier
     *     Initialize modifier to 0
     *     IF (it's cool/cloudy) THEN
     *         Set modifier to -$0.25
     *     ELSE IF (it's hot/sunny) THEN
     *         Set modifier to +$0.25
     *     END IF
     * END Weather Modifier
     */
    private static double weatherModifier(Weather weather) {
        double modifier = 0.00;

        if (weather == Weather.CLOUDY) {
            modifier = -0.25;   //Decreases price willing to pay by 25 cents
        } else if (weather == Weather.SUNNY) {
            modifier = +0.25;   //Increases price willing to pay by 25 cents
        }

        return modifier;
    }

    /**
     * This prevents the Customer utility class from being instantiated.
     */
    private Customer() {
    }
}
