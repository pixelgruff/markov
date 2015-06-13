package tictactoe;

import core.Automator;
import core.Player;
import core.Policy;
import core.policies.RandomPolicy;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ginger Play a game of tic-tac-toe with two policies that simply take
 *         actions at random.
 * 
 *         TODO: Create a proper test suite
 * 
 *         TODO: Remove
 */
public class TicTacToeTest
{
    public static void main(String[] args)
    {
        final Map<Player, Policy<TicTacToeState, TicTacToeAction>> policies = new HashMap<>();
        policies.put(new Player("Player 1"), new RandomPolicy<>());
        policies.put(new Player("Player 2"), new RandomPolicy<>());

        /**
         * Not wild about an Automator returning a "winner"; but we might want
         * it to return all manner of things-- state histories, score mappings,
         * etc.  Food for thought.
         * */
        final Automator<TicTacToeAction, TicTacToeState, TicTacToeRules> ticTacToeAutomator = new Automator<>();
        /* Note that these players are not added in any guaranteed order */
        final TicTacToeState initialState = new TicTacToeState(new ArrayList<Player>(policies.keySet()));
        final Player best = ticTacToeAutomator.play(new TicTacToeRules(), initialState, policies);

        /* 'best' may hold a player that tied for first */
        System.out.println(String.format("Game over. %s (%s) won or tied.", best,
                initialState.getMarkForPlayer(best)));
    }
}
