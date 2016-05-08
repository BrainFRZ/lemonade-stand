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
    public static int SCORES_STORED = 5;
    private static final long serialVersionUID = 42L;

    private ArrayList<Score> scoreSheet;

    public HighScores() {
        scoreSheet = new ArrayList<>(SCORES_STORED + 1);
    }

    public int scores() {
        return scoreSheet.size();
    }

    public boolean isEmpty() {
        return scoreSheet.isEmpty();
    }

    public Score getScore(int index) {
        return scoreSheet.get(index);
    }

    public double lowestScore() {
        return scoreSheet.get(scoreSheet.size() - 1).score;
    }

    public boolean addScore(String name, double score) {
        boolean added = false;
        int scores;

        Score newScore = new Score(name, score);

        scores = scoreSheet.size();
        if (scores == 0) {
            scoreSheet.add(newScore);
            added = true;
        } else {
            for (int i = 0; i < scores && !added; i++) {
                if (newScore.score > scoreSheet.get(i).score) {
                    scoreSheet.add(i, newScore);

                    if (scoreSheet.size() == SCORES_STORED + 1) {
                        scoreSheet.remove(SCORES_STORED);
                    }

                    added = true;
                }
            }

            if (!added && scoreSheet.size() < SCORES_STORED) {
                scoreSheet.add(newScore);
                added = true;
            }
        }

        return added;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();

        if (scoreSheet.isEmpty()) {
            out.append("There currently aren't any high scores.\n");
        } else {
            out.append("            High Scores\n");

            for (Score score : scoreSheet) {
                out.append(score).append("\n");
            }
        }

        return out.toString();
    }

    public static void main(String[] args) {
        double random;
        HighScores scores = new HighScores();

        /*for (int i = 0; i < 10; i++) {
            random = Math.random() * 100;
            System.out.println("Trying " + random);

            scores.addScore("abc", random);

            System.out.println(scores);
            System.out.println(scores.lowestScore());
        }*/

        scores.addScore("Tom", 42.00);
        scores.addScore("Brooke", 9001.00);
        scores.addScore("Terry", 1792.36);
        scores.addScore("Nelson", 151.50);
        scores.addScore("Frank", 248.72);

        HighScores.saveHighScores("highscores.sco", scores);
    }

    public static HighScores loadHighScores(String filename) {
        HighScores scores;
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            fis = new FileInputStream(filename);
            ois = new ObjectInputStream(fis);
            scores = (HighScores)ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            scores = new HighScores();
        } finally {
            try {
                if (fis != null)
                    fis.close();
                if (ois != null)
                    ois.close();
            } catch (IOException e) {
                //Do nothing
            }
        }

        return scores;
    }

    public static boolean saveHighScores(String filename, HighScores highScores)
    {
        boolean loaded = false;

        if (highScores != null) {
            FileOutputStream fos = null;
            ObjectOutputStream oos = null;

            try {
                fos = new FileOutputStream(filename);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(highScores);
                loaded = true;
            } catch (IOException e) {
                //Loaded will stay false
            } finally {
                try {
                    if (oos != null)
                        oos.close();
                    if (fos != null)
                        fos.close();
                } catch (IOException e) {
                    //Do nothing
                }
            }
        }

        return loaded;
    }

}
