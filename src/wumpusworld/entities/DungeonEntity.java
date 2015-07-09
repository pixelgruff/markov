package wumpusworld.entities;

import utils.Vector2;

public interface DungeonEntity
{
    public DungeonEntity copy();

    public Vector2 getPosition();

    public boolean isPassable();

    public String shortToString();
}
