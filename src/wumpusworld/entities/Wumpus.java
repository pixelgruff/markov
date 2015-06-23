package wumpusworld.entities;

import utils.Validate;
import utils.Vector2;

public final class Wumpus implements DungeonEntity
{
    private final Vector2 position_;

    public Wumpus(final Vector2 position)
    {
        Validate.notNull(position, "Cannot create a Wumpus with a null position");
        position_ = position;
    }

    public Wumpus(final Wumpus copy)
    {
        Validate.notNull(copy, "Cannot create a Wumpus from a null copy");
        position_ = copy.position_;
    }

    @Override
    public DungeonEntity copy()
    {
        return new Wumpus(this);
    }

    @Override
    public Vector2 getPosition()
    {
        return position_;
    }

    @Override
    public boolean isPassable()
    {
        return false;
    }

    @Override
    public String shortToString()
    {
        return "W";
    }
}
