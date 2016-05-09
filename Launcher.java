/**************************************************************************************************
 * Program Name   :
 * Author         : Terry Weiss
 * Date           : Apr 22, 2016
 * Course/Section :
 * Program Description:
 **************************************************************************************************/

package lemonadestand;

import java.util.Scanner;

/**
 * The launcher class launches the program and allows the player to continue to play new games until
 * they want to quit. The player is also able to see the high scores list from here. This class
 * handles the flow of the program and should not be instantiated.
 *
 * @author Terry Weiss
 */
public class Launcher {
    private final static Scanner scanner = new Scanner(System.in);  //Scanner object to read input
    private final static String SCORES_FILE = "highscores.sco";     //High scores file

    //Menu option constants
    private final static int INVALID_OPTION = 0;    //Invalid menu option number
    private final static int FIRST_OPTION   = 1;    //First menu option
    private final static int LAST_OPTION    = 3;    //Last menu option
    private final static int NEW_GAME    = 1;       //New game option
    private final static int HIGH_SCORES = 2;       //High scores option
    private final static int QUIT        = 3;       //Quit option


    /**
     * The main method launches the lemonade stand program.
     *
     * @param args Console arguments aren't supported
     */
    /*
     * BEGIN Main
     *     Load and store high scores from scores file
     *     "Warm up" random number generator
     *     DO WHILE (option isn't quit)
     *         Prompt menu option
     *         SWITCH (menu option)
     *             CASE New Game:      Start game with high scores object
     *             CASE High Scores:   Display high scores
     *             CASE Quit:          Save high scores
     *         END SWITCH
     *     END DO-WHILE
     * END Main
     */
    public static void main(String[] args) {
        HighScores highScores;  //High scores list
        int option;             //Menu option selection

        highScores = HighScores.loadHighScores(SCORES_FILE);

        warmupRandomGenerator();

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

    /**
     * Prompts for a menu option. If the player enters an invalid option, they will be re-prompted.
     *
     * @return Menu option selected
     */
    /*
     * START Menu Option
     *
     * END Prompt Menu Option
     */
    /*
     * BEGIN Prompt Menu Option
     *     Display menu
     *     DO WHILE (menu option is out of bounds)
     *         TRY
     *             Prompt for option
     *         CATCH (Invalid number)
     *             Do nothing
     *         END TRY-CATCH
     *
     *         IF (option is out of bounds)
     *             Display option out of bounds
     *         END IF
     *     END DO-WHILE
     *     Return option
     * END Prompt Menu Option
     */
    private static int promptMenuOption() {
        int option = INVALID_OPTION;    //Selected menu option

        System.out.println("          Lemonade Stand");
        System.out.println("    1. Start a new game");
        System.out.println("    2. High Scores");
        System.out.println("    3. Quit");

        do {
            try {
                System.out.print("Enter an option: ");
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                //Option will stay invalid
            }

            if (option < FIRST_OPTION || option > LAST_OPTION) {
                System.out.print("That isn't a valid option. ");
            }
        } while (option < FIRST_OPTION || option > LAST_OPTION);

        return option;
    }

    /**
     * Starts a new game. If the game qualifies for a high score, the player is prompted for their
     * name, and the score is added and saved.
     *
     * @param highScores High scores object
     */
    /**
     * START Start Game
     *     Run game and store score
     *     IF (score is positive AND (high scores list is empty
     *                                  OR this score is higher than lowest)) THEN
     *         Display congrats message
     *         Prompt player's name
     *         Create and add high score to list
     *         Save high scores list
     *     END IF
     * END Start Game
     */
    private static void startGame(HighScores highScores) {
        double score;   //Ending score
        String name;    //Player's name

        score = Game.runGame();

        if (score > 0.00 && (highScores.isEmpty() || score > highScores.lowestScore())) {
            System.out.println("Congratulations!! You got a high score!");
            System.out.print("What's your name? ");
            name = scanner.nextLine();

            highScores.addScore(name, score);
            HighScores.saveHighScores(SCORES_FILE, highScores);
        }
    }

    /**
     * Warms up the random number generator, since the first several iterations are more
     * predictable.
     */
    /**
     * BEGIN Warmup Random Generator
     *     FOR (100 iterations)
     *         Get next random integer
     *     END FOR
     * END Warmup Random Generator
     */
    private static void warmupRandomGenerator() {
        for (int i = 0; i < 100; i++) {
            Game.random.nextInt();
        }
    }

    /**
     * Private empty constructor prevents class from being instantiated
     */
    private Launcher() {
    }
}
