package tictactoe;

import core.Automator;
import core.Player;
import core.Policy;
import core.policies.RandomPolicy;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
        final Map<Player, Policy<TicTacToeAction, TicTacToeState>> policies = new HashMap<>();
        policies.put(new Player("Player 1"), new RandomPolicy<>());
        policies.put(new Player("Player 2"), new RandomPolicy<>());

        // Create the game
        final TicTacToeGame game = new TicTacToeGame(policies.keySet().stream()
                .collect(Collectors.toList()));

        final Automator<TicTacToeAction, TicTacToeState, TicTacToeGame> ticTacToeAutomator = new Automator<>();
        final Player winner = ticTacToeAutomator.play(game, policies);

        System.out.println(String.format("Game over. %s (%s) wins!", winner,
                game.getMarkForPlayer(winner)));

        System.out.println(game.getState());
    }
}
