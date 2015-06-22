package wumpusworld;

import utils.Vector2;

public interface DungeonEntity
{
    public Vector2 getPosition();

    public boolean isPassable();

    public String shortToString();
}
