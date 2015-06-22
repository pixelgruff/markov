package wumpusworld;

public enum Percept
{
    BREEZE, BUMP, GLITTER, STENCH;

    public static Percept perceptFor(final DungeonEntity entity)
    {
        if((entity instanceof DungeonTile))
        {
            switch(((DungeonTile) entity).getTileType())
            {
            case PIT:
                return BREEZE;
            case EMPTY:
                return null;
            }
        }
        else if(entity instanceof Wumpus)
        {
            return STENCH;
        }
        else if(entity instanceof Gold)
        {
            return GLITTER;
        }

        return null;
    }
}
