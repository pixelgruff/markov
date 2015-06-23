package wumpusworld.entities;

import java.util.Objects;

public final class Gold extends Item
{
    public Gold(final DungeonEntity owner)
    {
        super(owner);
    }

    public Gold(final Gold copy)
    {
        this(copy.getOwner());
    }

    @Override
    public DungeonEntity copy()
    {
        return new Gold(this);
    }

    @Override
    public boolean equals(final Object other)
    {
        if(other instanceof Gold)
        {
            return super.equals(other);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), Gold.class);
    }

    public boolean isCarriedByPlayer()
    {
        return(owner_ instanceof DungeonExplorer);
    }

    @Override
    public boolean isPassable()
    {
        return true;
    }

    @Override
    public String shortToString()
    {
        return "G";
    }

    public double value()
    {
        /* This gold is worth 1 million dollars */
        return 1000000;
    }

    @Override
    public Item withOwner(final DungeonEntity owner)
    {
        return new Gold(owner);
    }
}
