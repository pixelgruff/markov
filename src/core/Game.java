package core;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ginger The Game interface guarantees Markov Decision Process-like
 * functionality for subroutines requesting information about the game.
 * @param <S>
 * @param <A>
 */
public abstract class Game<S extends State, A extends Action> {

    final protected List<Player> players_;

    public Game(final ArrayList<Player> players) {
        players_ = players;
    }

    abstract public Player getCurrentPlayer();

    abstract public S getState(Player p);

    abstract public ArrayList<A> getActions(Player p);

    abstract public void takeAction(Player p, A action);

    abstract public boolean isOver();
}
