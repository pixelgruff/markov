package tetris;

import core.Player;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Ginger Test the basics of the Tetris game.
 */
public class TetrisTest {

    public static void main(String[] args) {

        /* Create a game of Tetris */
        final TetrisRules tetrisRules = new TetrisRules();
        final Set<Player> players = new HashSet<Player>();
        final Player human = new Player("Human");
        players.add(human);
        TetrisState tetrisState = tetrisRules.generateInitialState(players);

        /* Map available actions to String representations, and vice-versa */
        final Map<String, TetrisAction> actionMap = new HashMap<>();
        actionMap.put("a", TetrisAction.LEFT);
        actionMap.put("d", TetrisAction.RIGHT);
        actionMap.put("s", TetrisAction.DROP);
        actionMap.put("w", TetrisAction.ROTATE);
        actionMap.put(" ", TetrisAction.WAIT);

        /* Play the game with text output */
        Scanner scan = new Scanner(System.in);
        /* Begin game loop */
        System.out.printf("Starting state: \n%s\n", tetrisState.toString());
        System.out.printf("(%s) $ ", tetrisRules.getAvailableActions(human, tetrisState).toString());
        String input = scan.nextLine();
        while (!tetrisRules.isTerminal(tetrisState) && !input.equalsIgnoreCase("q")) {
            if (!actionMap.containsKey(input)) {
                System.err.println("Command not found!");
            } else if (!tetrisRules.getAvailableActions(human, tetrisState).contains(actionMap.get(input))) {
                System.err.println("Command not valid!");
            } else {
                tetrisState = tetrisRules.transition(tetrisState, actionMap.get(input));
            }

            System.out.printf("Current state: \n%s\n", tetrisState.toString());
            System.out.printf("(%s) $ ", tetrisRules.getAvailableActions(human, tetrisState).toString());
            input = scan.nextLine();
        }
    }
}
