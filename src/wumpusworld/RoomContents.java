package wumpusworld;

import java.util.HashSet;
import java.util.Set;

public enum RoomContents
{
    GOLD, LADDER, PIT, WUMPUS;

    private static final Set<RoomContents> PASSABLE_CONTENTS = new HashSet<RoomContents>();
    static
    {
        PASSABLE_CONTENTS.add(GOLD);
        PASSABLE_CONTENTS.add(LADDER);
        PASSABLE_CONTENTS.add(null);
    }

    public static boolean isPassable(final RoomContents roomContents)
    {
        return PASSABLE_CONTENTS.contains(roomContents);
    }

    public static Percept perceptForRoom(final RoomContents roomContents)
    {
        if(roomContents == null)
        {
            return Percept.BUMP;
        }
        switch(roomContents)
        {
        case PIT:
            return Percept.BREEZE;
        case WUMPUS:
            return Percept.STENCH;
        case GOLD:
            return Percept.GLITTER;
        case LADDER:
        default:
            return null;
        }
    }

    public String shortToString()
    {
        return name().substring(0, 1);
    }
}
