/**************************************************************************************************
 * Program Name   :
 * Author         : Terry Weiss
 * Date           : May 6, 2016
 * Course/Section :
 * Program Description:
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
