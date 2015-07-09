package tetris;

import core.Player;
import core.Score;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import utils.Vector2;

/**
 *
 * @author Ginger Test the basics of the Tetris game.
 */
public class TetrisTest {

    public static void main(String[] args) {

        /* Test the creation of Tetris shapes */
        final TetrisRules tetrisRules = new TetrisRules();
        final ArrayList<Player> players = new ArrayList<Player>();
        players.add(new Player("Human"));

        TetrisState tetrisState = tetrisRules.generateInitialState(players);
        System.out.println(tetrisState);
        System.out.println("");

        /* Add some easy shapes so we can score some sweet points */
        TetrisShape cheatingShape = TetrisShape.getShapeFromTetrimino(Tetrimino.I, new Vector2(1, 0));
        cheatingShape.makeUncontrollable();
        tetrisState.shapes.add(cheatingShape);
        
       Map<Player, Score> scores = tetrisRules.scores(tetrisState);
        
        for (int i = 0; i < 50; i++) {
            if (!tetrisRules.isTerminal(tetrisState)) {
                Collection<TetrisAction> actions = tetrisRules.getAvailableActions(players.get(0), tetrisState);
                final TetrisAction action = new ArrayList<>(actions).get(ThreadLocalRandom.current().nextInt(actions.size()));
                tetrisState = tetrisRules.transition(tetrisState, action);
                System.out.println(tetrisState);
                System.out.println("");
            }
        }
    }
}
