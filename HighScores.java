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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class HighScores implements Serializable {
    private static final long serialVersionUID = 42L; //Harden against different JVM implementations

    /**
     * Number of scores stored in high score list
     */
    public transient static final int SCORES_STORED = 5;

    private final ArrayList<Score> scoreSheet;        //List of scores


    /**
     * Constructs a new High Score list with the default scores stored.
     */
    public HighScores() {
        scoreSheet = new ArrayList<>(SCORES_STORED);
    }

    /**
     * Gets the number of scores currently listed
     *
     * @return Number of scores listed
     */
    public int scores() {
        return scoreSheet.size();
    }

    /**
     * Determines whether there are any scores saved
     *
     * @return True if there are no scores saved
     */
    public boolean isEmpty() {
        return scoreSheet.isEmpty();
    }

    /**
     * Determines the lowest score value saved. If there are no scores, a NoScoresException will be
     * thrown. The Java Runtime will check this exception implicitly.
     *
     * @throws NoScoresException If there are no scores saved
     * @return Lowest score value
     */
    public double lowestScore() throws NoScoresException {
        if (scoreSheet.isEmpty()) {
            throw new NoScoresException();
        }

        return scoreSheet.get(scoreSheet.size() - 1).score;
    }

    /**
     * Creates a new {@link Score} with the given name and score value and adds it to the list in
     * descending order. If the list is empty, the new score will automatically be added. Otherwise,
     * the method will iterate through the list of stored scores to see if it's higher than any of
     * the currently-stored scores and insert where applicable. If the score still hasn't been
     * added, it will be appended if the list isn't full.
     *
     * @param name   Player's name
     * @param score  Player's score
     * @return True if score was added
     */
    public boolean addScore(String name, double score) {
        boolean added = false;  //Whether score was added
        int scores;             //Number of scores currently stored in list

        scores = scoreSheet.size();
        if (scores == 0) {
            scoreSheet.add(new Score(name, score));
            added = true;
        } else {
            for (int i = 0; i < scores && !added; i++) {
                if (score > scoreSheet.get(i).score) {
                    if (scoreSheet.size() == SCORES_STORED) {
                        scoreSheet.remove(SCORES_STORED - 1);
                    }

                    scoreSheet.add(i, new Score(name, score));
                    added = true;
                }
            }

            if (!added && scoreSheet.size() < SCORES_STORED) {
                scoreSheet.add(new Score(name, score));
                added = true;
            }
        }

        return added;
    }

    /**
     * Creates and returns a String display of the high score sheet. If there are no scores entered,
     * a message will say so instead.
     *
     * @return String representation of the high score list
     */
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();    //Stringbuild to build output string

        if (scoreSheet.isEmpty()) {
            out.append("There currently aren't any high scores.\n");
        } else {
            out.append("            High Scores\n");

            for (Score score : scoreSheet) {
                out.append(String.format("Name: %-15s    Score: $%.2f%n", score.name, score.score));
            }
        }

        return out.toString();
    }

    /**
     * Creates and loads a high score list from the given filename. The file must contain a
     * serialized copy of a previous High Score object. If the file can't be read or
     * doesn't exist, an empty high score list will be created.
     *
     * @param filename Name of file to read from
     * @return New high score loaded, or empty one if unable to read
     */
    public static HighScores loadHighScores(String filename) {
        HighScores scores;              //New high scores object
        ObjectInputStream ois = null;   //Object stream reader

        try {
            ois = new ObjectInputStream(new FileInputStream(filename));
            scores = (HighScores)ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            scores = new HighScores();
        } finally {
            try {
                if (ois != null)
                    ois.close();
            } catch (IOException e) {
                //Do nothing
            }
        }

        return scores;
    }

    /**
     * Saves the given high scores object to the given filename.
     *
     * @param filename    File to save high scores to
     * @param highScores  High scores to save
     * @return Whether high scores were able to be saved
     */
    public static boolean saveHighScores(String filename, HighScores highScores)
    {
        boolean saved = false;          //Whether score was saved
        ObjectOutputStream oos = null;  //Object stream writer

        if (highScores != null) {
            try {
                oos = new ObjectOutputStream(new FileOutputStream(filename));
                oos.writeObject(highScores);
                saved = true;
            } catch (IOException e) {
                //Saved will stay false
            } finally {
                try {
                    if (oos != null)
                        oos.close();
                } catch (IOException e) {
                    //Do nothing
                }
            }
        }

        return saved;
    }


    /**
     * This exception should be thrown whenever there is a check for a score on an empty high score
     * list.
     */
    private class NoScoresException extends IndexOutOfBoundsException {
        /**
         * Constructs the No Scores exception
         */
        private NoScoresException() {
            super("No scores are saved");
        }
    }

    /**
     * This is a demo to create and display a high score using random scores to prove it works
     * easily and visibly.
     *
     * @param args Console arguments aren't supported
     */
    public static void main(String[] args) {
        double random;                              //Random score
        HighScores scores = new HighScores();       //High scores object to be used

        for (int i = 0; i < 100; i++) {
            Game.random.nextInt();
        }

        for (int i = 0; i < 10; i++) {
            random = Math.random() * 100;
            System.out.println("Trying " + random);

            scores.addScore("Somebody", random);

            System.out.print(scores);
            System.out.println("Lowest score: " + scores.lowestScore() + "\n");
        }
    }
}
