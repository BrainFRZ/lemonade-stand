/**************************************************************************************************
 * Program Name   :
 * Author         : Terry Weiss
 * Date           : May 6, 2016
 * Course/Section :
 * Program Description:
 **************************************************************************************************/

package lemonadestand;

public class Score {
    public final String name;
    public final double score;

    public Score(String name, double score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public String toString() {
        return String.format("Name: %-15s  Score: $%.2f", name, score);
    }
}
