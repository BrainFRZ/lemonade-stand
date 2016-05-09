/**************************************************************************************************
 * Program Name   : Final Project Option #2 - Lemonade Stand
 * Author         : Terry Weiss
 * Date           : Apr 22, 2016
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

/**
 * This enum holds the different weather forecasts available.
 *
 * @author Terry Weiss
 */
public enum Weather {
    RAINY, SUNNY, CLOUDY;

    /**
     * Generates a string representation describing the forecast.
     *
     * @return String representation of weather
     */
    /*
     * BEGIN To String
     *     Init string to "crazy"
     *     IF (it's rainy) THEN
     *         Set string to "rainy"
     *     ELSE IF (it's sunny)
     *         Set string to "hot and sunny"
     *     ELSE IF (it's cloudy) THEN
     *         Set string to "cloudy and cool"
     *     END IF
     *     Return string
     * END To String
     */
    @Override
    public String toString() {
        String str = "crazy";

        if (this == RAINY) {
            str = "rainy";
        } else if (this == SUNNY) {
            str = "hot and sunny";
        } else if (this == CLOUDY) {
            str = "cloudy and cool";
        }

        return str;
    }
}
