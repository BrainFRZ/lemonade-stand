/**************************************************************************************************
 * Program Name   :
 * Author         : Terry Weiss
 * Date           : Apr 22, 2016
 * Course/Section :
 * Program Description:
 **************************************************************************************************/

package lemonadestand;

import java.util.Scanner;

public class Launcher {
    private final static Scanner scanner = new Scanner(System.in);
    private final static String SCORES_FILE = "highscores.sco";
    private final static int INVALID_OPTION = 0;
    private final static int NEW_GAME = 1;
    private final static int HELP     = 2;
    private final static int QUIT     = 3;

    public static void main(String[] args) {
        HighScores highScores = HighScores.loadHighScores(SCORES_FILE);
        String name;
        boolean gameOver;
        double score;

        do {
            score = Game.runGame();

            if (highScores != null) {
                if (score > highScores.lowestScore()) {
                    System.out.println("Congratulations!! You got a high score!");
                    System.out.print("What's your name? ");
                    name = scanner.nextLine();

                    highScores.addScore(name, score);
                }

                HighScores.saveHighScores(SCORES_FILE, highScores);
            }

            gameOver = promptGameOver();

            if (highScores != null) {
                HighScores.saveHighScores(SCORES_FILE, highScores);
            }

        } while (!gameOver);
    }

    private static boolean promptGameOver() {
        String continues;

        System.out.print("Would you like to play another game? ");
        continues = scanner.nextLine();

        return (continues.equalsIgnoreCase("n") || continues.equalsIgnoreCase("no"));
    }
}
