/**************************************************************************************************
 * Program Name   :
 * Author         : Terry Weiss
 * Date           : Apr 22, 2016
 * Course/Section :
 * Program Description:
 **************************************************************************************************/

package lemonadestand;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;

public class Launcher {
    private final static String SCORES_FILE = "highscores.sco";

    public static void main(String[] args) {
        String option;
        ArrayList<Double> highScores;

//        System.out.println("          Lemonade Stand");

        Game.runGame();
    }

    private static ArrayList<Double> loadHighScores() {
        ArrayList<Double> scores = new ArrayList<>();

        return scores;
    }
}
