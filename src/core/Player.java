package core;

import java.util.UUID;

/**
 *
 * @author Ginger The Player class provides a standard interface for generating
 *         unique players and selecting actions.
 */
@SuppressWarnings("rawtypes")
public class Player implements Comparable<Player>
{
    /*
     * We don't actually care about WHAT makes a player unique, only that it is.
     * As such, we can safely hide this UUID / switch to an alternative later,
     * if need be.
     */
    private final UUID id_;
    private final String name_;

    public Player(String name)
    {
        id_ = UUID.randomUUID();
        name_ = name;
    }

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
    
    @Override
    public String toString() { 
        return name_;
    }
}
