/**************************************************************************************************
 * Program Name   :
 * Author         : Terry Weiss
 * Date           : May 6, 2016
 * Course/Section :
 * Program Description:
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
    /*
     * BEGIN Constructor
     *     Initialize score sheet with an initial size of scores stored
     * END Constructor
     */
    public HighScores() {
        scoreSheet = new ArrayList<>(SCORES_STORED);
    }

    /**
     * Gets the number of scores currently listed
     *
     * @return Number of scores listed
     */
    /*
     * BEGIN Scores
     *     Return score sheet's size
     * END Scores
     */
    public int scores() {
        return scoreSheet.size();
    }

    /**
     * Determines whether there are any scores saved
     *
     * @return True if there are no scores saved
     */
    /*
     * BEGIN Is Empty
     *     Return whether score sheet is empty
     * END Is Empty
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
    /*
     * BEGIN Lowest Score
     *     IF (Score sheet is empty) THEN
     *         Throw no scores exception
     *     END IF
     *     Get lowest score and return its score value
     * END Lowest Score
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
    /*
     * BEGIN Add Score
     *     Initialize added to false
     *     Store size of score sheet as scores
     *     IF (there aren't any scores) THEN
     *         Create and add new Score
     *         Set added to true
     *     ELSE
     *         FOR (each score or until one is added)
     *             IF (score value is higher than current score's score value) THEN
     *                 IF (score sheet is full) THEN
     *                     Remove lowest score
     *                 END IF
     *                 Create and insert new Score at current index
     *                 Set added to true
     *             END IF
     *         END FOR
     *         IF (Score wasn't added and the list isn't full) THEN
     *             Create and append new score sheet
     *             Set added to true
     *         END IF
     *     END IF
     *     Return whether score was added
     * END Add Score
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
    /**
     * BEGIN To String
     *     IF (Score sheet is empty) THEN
     *         Append message that there aren't any high scores
     *     ELSE
     *         Append high scores title
     *         FOR (each score)
     *             Append formatted string with score's name and score value
     *         END FOR
     *     END IF
     *     Return built string
     * END To String
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
    /*
     * BEGIN Load High Scores
     *     TRY
     *         Init object input stream from file input stream from filename
     *         Read and cast file object as a High Score and store it
     *     CATCH (IO Exception)
     *         Create and store new high scores object
     *     FINALLY
     *         Close all streams
     *     END TRY-CATCH
     *     Return new score sheet
     * END Load High Scores
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
    /*
     * START Save High Scores
     *     Set saved to false
     *     IF (high scores isn't null) THEN
     *         TRY
     *             Create object stream writer from file stream writer from filename
     *             Write high score object to file
     *             Set saved to true
     *         CATCH (IO Exception)
     *             Do nothing
     *         FINALLY
     *             Close all streams
     *         END TRY-CATCH
     *     END IF
     *     Return whether scores were saved successfully
     * END Save High Scores
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
        /*
         * BEGIN Constructor
         *     Create IndexOutOfBoundsException using no scores saved message
         * END Constructor
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
    /*
     * BEGIN Main
     *     Warm up RNG with 100 iterations
     *     FOR (10 scores)
     *         Generate a random score
     *         Display score
     *         Add new score
     *         Display all scores so far
     *         Display lowest score to make sure that works
     *     END FOR
     * END Main
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
