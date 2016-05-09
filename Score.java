/**************************************************************************************************
 * Program Name   : Final Project Option #2 - Lemonade Stand
 * Author         : Terry Weiss
 * Date           : May 7, 2016
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

import java.io.Serializable;

/**
 * The Score class holds the name and final score of any player.
 *
 * @author Terry Weiss
 */
final class Score implements Serializable {
    private static final long serialVersionUID = 42L;     //Override JVM implementation ID

    /**
     * Player's name
     */
    protected final String name;

    /**
     * Player's final score
     */
    protected final double score;

    /**
     * Constructs a new score based on the given name and score.
     *
     * @param name
     * @param score
     */
    protected Score(String name, double score) {
        this.name   = name;
        this.score  = score;
    }
}
