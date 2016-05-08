/**************************************************************************************************
 * Program Name   :
 * Author         : Terry Weiss
 * Date           : May 6, 2016
 * Course/Section :
 * Program Description:
 **************************************************************************************************/

package lemonadestand;

import java.io.Serializable;

public class Score implements Serializable {
    private static final long serialVersionUID = 42L;     //Override JVM implementation ID

    public final String name;
    public final double score;

    public Score(String name, double score) {
        this.name   = name;
        this.score  = score;
    }

    @Override
    public String toString() {
        return String.format("Name: %-15s    Score: $%.2f", name, score);
    }
}
