package tictactoe;

import java.util.HashMap;
import java.util.Map;

import core.Player;
import core.Policy;
import core.automators.LocalAutomator;
import core.policies.RandomPolicy;

/**
 * @author Ginger Play a game of tic-tac-toe with two policies that simply take
 *         actions at random. TODO: Create a proper test suite TODO: Remove
 */
public class TicTacToeTest
{
    public static void main(final String[] args)
    {
        final Map<Player, Policy<TicTacToeState, TicTacToeAction>> policies = new HashMap<>();
        policies.put(new Player("Player 1"), new RandomPolicy<>());
        policies.put(new Player("Player 2"), new RandomPolicy<>());

        final TicTacToeRules rules = new TicTacToeRules();
        final LocalAutomator<TicTacToeState, TicTacToeAction, TicTacToeRules> ticTacToeAutomator = new LocalAutomator<>(
                rules, policies);

        /* Note that these players are not added in any guaranteed order */
        final TicTacToeState endedState = ticTacToeAutomator.playGameToCompletion();

        final Player winner = policies
                .keySet()
                .stream()
                .max((player1, player2) -> rules.score(endedState, player1).compareTo(
                        rules.score(endedState, player2))).orElse(null);

        System.out.println(endedState);
        /* 'best' may hold a player that tied for first */
        System.out.println(String.format("Game over. %s (%s) won or tied.", winner,
                endedState.getMarkForPlayer(winner)));
    }
}
