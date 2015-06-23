package wumpusworld.entities;

import java.util.Objects;

import utils.Validate;

public class BowAndArrow extends Item
{
    private boolean hasArrows_ = true;

    private BowAndArrow(final BowAndArrow bowAndArrow, final boolean hasArrows)
    {
        super(bowAndArrow.owner_);
        hasArrows_ = false;
    }

    public BowAndArrow(final DungeonEntity owner)
    {
        super(owner);
    }

    @Override
    public DungeonEntity copy()
    {
        return new BowAndArrow(this);
    }

    @Override
    public boolean equals(final Object other)
    {
        if(other instanceof BowAndArrow)
        {
            return Objects.equals(hasArrows_, ((BowAndArrow) other).hasArrows_)
                    && super.equals(other);
        }
        return false;
    }

    public BowAndArrow fired()
    {
        Validate.isTrue(hasArrows_, "Cannot fire a bow that doesn't have any arrows");
        final boolean noArrows = false;
        return new BowAndArrow(this, noArrows);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(hasArrows_, getPosition());
    }

    @Override
    public boolean isPassable()
    {
        return true;
    }

    @Override
    public String shortToString()
    {
        return "A";
    }

    @Override
    public Item withOwner(final DungeonEntity owner)
    {
        final BowAndArrow copy = new BowAndArrow(owner);
        copy.hasArrows_ = hasArrows_;
        return copy;
    }
}
