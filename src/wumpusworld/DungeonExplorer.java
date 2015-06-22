package wumpusworld;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import utils.Validate;
import utils.Vector2;

public class DungeonExplorer implements DungeonEntity
{
    private final Collection<Item> items_;

    private final Vector2 position_;

    public DungeonExplorer(final Vector2 position)
    {
        Validate.notNull(position, "Cannot create a DungeonExplorer with a null position");
        position_ = position;
        items_ = new ArrayList<>();
    }

    @Override
    public boolean equals(final Object other)
    {
        if(other instanceof DungeonExplorer)
        {
            final DungeonExplorer otherExplorer = (DungeonExplorer) other;
            return Objects.equals(getPosition(), otherExplorer.getPosition())
                    && Objects.equals(getItems(), otherExplorer.getItems());
        }
        return false;
    }

    public Collection<Item> getItems()
    {
        return new ArrayList<>(items_);
    }

    @Override
    public Vector2 getPosition()
    {
        return position_;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(position_, items_);
    }

    @Override
    public boolean isPassable()
    {
        return false;
    }

    @Override
    public String shortToString()
    {
        return "E";
    }
}
