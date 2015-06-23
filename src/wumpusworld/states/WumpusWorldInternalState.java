package wumpusworld.states;

import java.util.Collection;
import java.util.Collections;

import utils.Validate;
import wumpusworld.WumpusWorldDungeon;
import wumpusworld.entities.DungeonExplorer;
import wumpusworld.entities.Percept;
import core.Player;

public class WumpusWorldInternalState implements WumpusWorldState
{
    private final WumpusWorldDungeon dungeon_;

    // private final Map<Player, Collection<Percept>> perceptsForPlayer_;

    public WumpusWorldInternalState(final WumpusWorldDungeon dungeon)
    {
        Validate.notNull(dungeon, "Cannot create a WumpusWorldInternalState with a null dungeon");
        dungeon_ = new WumpusWorldDungeon(dungeon);
    }

    public WumpusWorldDungeon getDungeon()
    {
        return dungeon_;
    }

    // private static void generatePerceptsForPlayer(final WumpusWorldDungeon
    // dungeon, final Player player,

    @Override
    public Collection<Percept> getPerceptsForPlayer(final Player player)
    {
        final DungeonExplorer explorer = dungeon_.getDungeonExplorer(player);
        if(explorer == null)
        {
            return Collections.emptyList();
        }

        // explorer.

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isTerminal()
    {
        // TODO Auto-generated method stub
        return false;
    }

}
