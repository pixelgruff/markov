package core;

import java.util.Collection;

import java.util.UUID;

/**
 *
 * @author Ginger The Player class provides a standard interface for generating
 *         unique players and selecting actions.
 * @param <T> The Game that this player is For
 */
@SuppressWarnings("rawtypes")
public abstract class Player<T extends GameType, A extends Action<T>> implements Comparable<Player>
{
    /*
     * We don't actually care about WHAT makes a player unique, only that it is.
     * As such, we can safely hide this UUID / switch to an alternative later,
     * if need be.
     */
    protected final UUID id_;

    public Player()
    {
        id_ = UUID.randomUUID();
    }

    abstract public A chooseAction(final State<T, A> state, final Collection<A> actions);

    @Override
    public int compareTo(Player other)
    {
        if(other == null)
        {
            return 1;
        }
        return id_.compareTo(other.id_);
    }

    @Override
    public boolean equals(Object other)
    {
        if(!(other instanceof Player)) {
            return false;
        }
        
        return compareTo((Player) other) == 0;
    }
}
