package wumpusworld;

import java.util.Objects;

import utils.Validate;
import utils.Vector2;

public abstract class Item implements DungeonEntity
{
    protected final DungeonEntity owner_;

    protected Item(final DungeonEntity owner)
    {
        Validate.notNull(owner, "Cannot create an Item with a null owner");
        owner_ = owner;
    }

    @Override
    public boolean equals(final Object other)
    {
        if(other instanceof Item)
        {
            final Item otherItem = (Item) other;
            return Objects.equals(getPosition(), otherItem.getPosition())
                    && Objects.equals(getOwner(), otherItem.getOwner());
        }
        return false;
    }

    public DungeonEntity getOwner()
    {
        return owner_;
    }

    @Override
    public Vector2 getPosition()
    {
        return owner_.getPosition();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getPosition(), getOwner());
    }
}
