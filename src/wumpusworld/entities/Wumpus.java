package wumpusworld.entities;

import utils.Validate;
import utils.Vector2;

public final class Wumpus implements DungeonEntity
{
    private final Vector2 position_;
    private boolean dead_;

    public Wumpus(final Vector2 position)
    {
        Validate.notNull(position, "Cannot create a Wumpus with a null position");
        position_ = position;
        dead_ = false;
    }

    public Wumpus(final Wumpus copy)
    {
        Validate.notNull(copy, "Cannot create a Wumpus from a null copy");
        position_ = copy.position_;
        dead_ = copy.dead_;
    }

    public void slay()
    {
        dead_ = true;
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

    public boolean isDead()
    {
        return dead_;
    }

    @Override
    public boolean isPassable()
    {
        return !dead_;
    }

    @Override
    public String shortToString()
    {
        return "W";
    }
}
