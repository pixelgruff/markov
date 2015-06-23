package wumpusworld.entities;

public enum DungeonTileType
{
    EMPTY, PIT;

    public static boolean isPassable(final DungeonTileType tileType)
    {
        switch(tileType)
        {
        case EMPTY:
            return true;
        default:
            return false;
        }
    }

    @Override
    public String toString()
    {
        switch(this)
        {
        case PIT:
            return name();
        default:
            return " ";
        }
    }
}
