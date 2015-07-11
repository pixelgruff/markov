package tetris;

import core.Automator;
import core.Player;
import core.Policy;
import core.automators.LocalAutomator;
import core.policies.PrintPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ginger Test the basics of the Tetris game.
 */
public class TetrisTest {

    public static void main(String[] args) {

        /* Map available actions to String representations */
        final Map<String, TetrisAction> actionMap = new HashMap<>();
        actionMap.put("a", TetrisAction.LEFT);
        actionMap.put("d", TetrisAction.RIGHT);
        actionMap.put("s", TetrisAction.DROP);
        actionMap.put("w", TetrisAction.ROTATE);
        actionMap.put(" ", TetrisAction.WAIT);
        /* Create policy */
        Policy stdOutPolicy = new PrintPolicy(actionMap);
        /* Map a human player to this policy */
        Map<Player, Policy> policyMap = new HashMap<>();
        policyMap.put(new Player("Human"), stdOutPolicy);
        /* Create the automator and run the game */
        Automator automator = new LocalAutomator(new TetrisRules(), policyMap);
        automator.playGameToCompletion();
    }
}
