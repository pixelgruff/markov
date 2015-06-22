package wumpusworld;

import java.util.Objects;

import utils.Validate;
import utils.Vector2;

public final class DungeonTile implements DungeonEntity
{
    private final Vector2 position_;
    private final DungeonTileType tileType_;

    public DungeonTile(final DungeonTileType tileType, final Vector2 position)
    {
        Validate.notNull(tileType, "Cannot create a DungeonTile with a null tile type");
        Validate.notNull(position, "Cannot create a DungeonTile with a null position");
        position_ = position;
        tileType_ = tileType;
    }

    @Override
    public boolean equals(final Object other)
    {
        if(other instanceof DungeonTile)
        {
            final DungeonTile otherTile = (DungeonTile) other;
            return Objects.equals(getPosition(), otherTile.getPosition())
                    && Objects.equals(getTileType(), otherTile.getTileType());
        }
        return false;
    }

    @Override
    public Vector2 getPosition()
    {
        return position_;
    }

    public DungeonTileType getTileType()
    {
        return tileType_;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getPosition(), getTileType());
    }

    @Override
    public boolean isPassable()
    {
        return DungeonTileType.isPassable(tileType_);
    }

    @Override
    public String shortToString()
    {
        return tileType_.toString().substring(0, 1);
    }
}
