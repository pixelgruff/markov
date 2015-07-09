package wumpusworld.entities;

import java.util.Objects;

import utils.Validate;
import utils.Vector2;

public final class Ladder implements DungeonEntity
{
    private final Vector2 position_;

    public Ladder(final Ladder copy)
    {
        Validate.notNull(copy, "Cannot create a Ladder from a null copy");
        position_ = copy.position_;
    }

    public Ladder(final Vector2 position)
    {
        Validate.notNull(position, "Cannot create a Ladder at a null position");
        position_ = position;
    }

    @Override
    public DungeonEntity copy()
    {
        return new Ladder(this);
    }

    @Override
    public boolean equals(final Object other)
    {
        if(other instanceof Ladder)
        {
            final Ladder otherLadder = (Ladder) other;
            return Objects.equals(getPosition(), otherLadder.getPosition());
        }
        return false;
    }

    @Override
    public Vector2 getPosition()
    {
        return position_;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(position_, Ladder.class);
    }

    @Override
    public boolean isPassable()
    {
        return true;
    }

    @Override
    public String shortToString()
    {
        return "L";
    }
}
