package core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import utils.Validate;

/**
 *
 * @author Ginger The Game interface guarantees Markov Decision Process-like
 *         functionality for subroutines requesting information about the game.
 * @param <A>
 *            Action type that the game operates on
 * @param <S>
 *            State type that the game exposes to Players
 */
public abstract class Game<A extends Action, S extends State<A>>
{
    final protected List<Player> players_;

    /**
     * Note: All games require at least one player.
     * 
     * @param players
     *            Ordered list of players that will partake in the game.
     */
    public Game(final List<Player> players)
    {
        Validate.notEmpty(players, "Cannot create a game with a null/empty list of Players");
        // Take a copy - never trust anyone ever
        players_ = new ArrayList<Player>(players);
    }

    public Collection<Player> getPlayers()
    {
        return new ArrayList<Player>(players_);
    }

    public abstract Player getCurrentPlayer();
    
    public abstract Player getCurrentPlayer(final S state);

    public abstract S getState(final Player player);

    public abstract Score getPlayerScore(final Player player);

    public abstract Collection<A> getActions(final Player player);

    public abstract void takeAction(final Player player, final A action);

    public abstract boolean isOver();
    
    public abstract boolean isOver(final S state);

    public S simulate(final Player player, final S currentState)
    {
        
        return null;
    }
}
