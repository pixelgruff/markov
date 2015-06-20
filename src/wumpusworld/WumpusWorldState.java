package wumpusworld;

import java.util.Collection;
import java.util.Collections;

import core.Player;

public class WumpusWorldState
{
    private final WumpusWorldDungeon dungeon_;

    public WumpusWorldState()
    {
        this(WumpusWorldDungeon.DEFAULT_DUNGEON_WIDTH, WumpusWorldDungeon.DEFAULT_DUNGEON_HEIGHT);
    }

    public WumpusWorldState(final int width, final int height)
    {
        dungeon_ = new WumpusWorldDungeon(width, height);
    }

    public Collection<Percept> getPerceptsForPlayer(final Player player)
    {
        // TODO: Figure out how to go about proper information hiding
        return Collections.emptyList();
    }
}
