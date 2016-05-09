/**************************************************************************************************
 * Program Name   : Final Project Option #2 - Lemonade Stand
 * Author         : Terry Weiss
 * Date           : Apr 26, 2016
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
