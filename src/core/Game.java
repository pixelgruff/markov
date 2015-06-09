package core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import utils.Validate;

/**
 *
 * @author Ginger The Game interface guarantees Markov Decision Process-like
 *         functionality for subroutines requesting information about the game.
 * @param <S>
 * @param <A>
 */
public abstract class Game<A extends Action, S extends State<A>>
{

    final protected List<Player> players_;

    public Game(final List<Player> players)
    {
        Validate.notNull(players, "Cannot create a Game with a null player list");
        Validate.isFalse(players.isEmpty(), "Cannot create a Game without any players!");
        // Take a copy - never trust anyone ever
        players_ = new ArrayList<Player>(players);
    }

    abstract public Player getCurrentPlayer();

    abstract public S getState(final Player player);

    abstract public Score getPlayerScore(final Player player);

    abstract public Collection<A> getActions(final Player player);

    abstract public void takeAction(final Player player, final A action);

    abstract public boolean isOver();
}
