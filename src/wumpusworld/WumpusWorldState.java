package wumpusworld;

import java.util.Collection;
import java.util.Collections;

import core.Player;

public class WumpusWorldState
{
    final static int DEFAULT_DUNGEON_HEIGHT = 200;
    final static int DEFAULT_DUNGEON_WIDTH  = 200;

    public static WumpusWorldState generateRandomStartingConfig(final int width, final int height)
    {

        return null;
    }

    public Collection<Percept> getPerceptsForPlayer(final Player player)
    {

        return Collections.emptyList();
    }
}
