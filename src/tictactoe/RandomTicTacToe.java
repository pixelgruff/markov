package tictactoe;

import core.Client;
import core.Player;
import core.Policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 *
 * @author Ginger Play a game of tic-tac-toe with two policies that simply take
 *         actions at random.
 */
public class RandomTicTacToe
{
    // A random policy for testing
    private static final Policy<TicTacToeAction, TicTacToeState> RANDOM_POLICY = (
            TicTacToeState state, Collection<TicTacToeAction> actions) -> new ArrayList<>(actions)
            .get(new Random().nextInt(actions.size()));

    public static void main(String[] args)
    {
        final Map<Player, Policy<TicTacToeAction, TicTacToeState>> policies = new HashMap<Player, Policy<TicTacToeAction, TicTacToeState>>();
        policies.put(new Player("Player 1"), RANDOM_POLICY);
        policies.put(new Player("Player 2"), RANDOM_POLICY);
        // Create the game
        final TicTacToeGame tictactoe = new TicTacToeGame(policies.keySet().stream()
                .collect(Collectors.toList()));
        new Client<TicTacToeAction, TicTacToeState, TicTacToeGame>().play(tictactoe, policies);

        System.out.println("Game over.");
        // wat
        System.out.println(tictactoe.getState((Player) policies.keySet().toArray()[0]));
    }
}
