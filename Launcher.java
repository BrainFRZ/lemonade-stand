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
    private final static int NEW_GAME    = 1;
    private final static int HIGH_SCORES = 2;
    private final static int QUIT        = 3;

    public static void main(String[] args) {
        HighScores highScores = HighScores.loadHighScores(SCORES_FILE);
        int option;

        do {
            option = promptMenuOption();

            switch (option) {
                case NEW_GAME:
                    startGame(highScores);
                    break;
                case HIGH_SCORES:
                    System.out.println(highScores);
                    break;
                case QUIT:
                    HighScores.saveHighScores(SCORES_FILE, highScores);
                    break;
            }
        } while (option != QUIT);
    }

    private static int promptMenuOption() {
        int option = INVALID_OPTION;

        System.out.println("          Lemonade Stand");
        System.out.println("    1. Start a new game");
        System.out.println("    2. High Scores");
        System.out.println("    3. Quit");

        do {
            System.out.print("Enter an option: ");

            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                //Option will stay invalid
            }

            if (option < NEW_GAME || option > QUIT) {
                System.out.print("That isn't a valid option. ");
            }
        } while (option < NEW_GAME || option > QUIT);

        return option;
    }

    private static void startGame(HighScores highScores) {
        double score;
        String name;

        score = Game.runGame();

        if (highScores.isEmpty() || score > highScores.lowestScore()) {
            System.out.println("Congratulations!! You got a high score!");
            System.out.print("What's your name? ");
            name = scanner.nextLine();

            highScores.addScore(name, score);
        }

        HighScores.saveHighScores(SCORES_FILE, highScores);
    }
}
