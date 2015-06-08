package tictactoe;

import core.Client;
import core.Player;
import core.Policy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Ginger Play a game of tic-tac-toe with two policies that simply take
 * actions at random.
 */
public class RandomTicTacToe {
    // A random policy for testing
    private static final Policy<TicTacToeState, TicTacToeAction> randomPolicy_ = 
            (TicTacToeState state, ArrayList<TicTacToeAction> actions) -> 
                    actions.get(new Random().nextInt(actions.size()));
    
    private static final HashMap<Player, Policy> policies_ = new HashMap<>();
    
    public static void main(String[] args) {
        // Add both players
        policies_.put(new Player("Player 1"), randomPolicy_);
        policies_.put(new Player("Player 2"), randomPolicy_);
        // Create the game
        TicTacToeGame tictactoe = new TicTacToeGame(new ArrayList<>(policies_.keySet()));
        new Client().play(tictactoe, policies_);
        
        System.out.println("Game over.");
        System.out.println(tictactoe.getState((Player)policies_.keySet().toArray()[0]));
    }
}
