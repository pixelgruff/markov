package wumpusworld.states;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import utils.RandomUtils;
import utils.Validate;
import utils.Vector2;
import wumpusworld.WumpusWorldDungeon;
import wumpusworld.entities.DungeonExplorer;
import wumpusworld.entities.Gold;
import wumpusworld.entities.Percept;
import core.Player;

public class WumpusWorldInternalState implements WumpusWorldState
{
    private final WumpusWorldDungeon dungeon_;

    private final PlayerState playerState_;

    public WumpusWorldInternalState(final WumpusWorldDungeon dungeon, final PlayerState playerState)
    {
        Validate.notNull(dungeon, "Cannot create a WumpusWorldInternalState with a null dungeon");
        dungeon_ = new WumpusWorldDungeon(dungeon);
        playerState_ = playerState;
    }

    public Player currentPlayer()
    {
        /*
         * There should only be one player, but let's shake things up in case
         * there isn't TODO: FIGURE OUT PLAYERS PROPERLY
         */
        return RandomUtils.randomOf(dungeon_.getPlayers());
    }

    public WumpusWorldDungeon getDungeon()
    {
        return dungeon_;
    }

    @Override
    public Collection<Percept> getPerceptsForPlayer(final Player player)
    {
        if(playerState_ == null || playerState_.isTerminal())
        {
            /* Should never happen */
            return Collections.emptyList();
        }

        final DungeonExplorer explorer = dungeon_.getDungeonExplorer(player);
        if(explorer == null)
        {
            return Collections.emptyList();
        }

        /* Assumes there is only one player. TODO: Fix this */
        final Vector2 explorerPosition = explorer.getPosition();

        final Set<Percept> percepts = Vector2.cardinalDirections().stream()
        /* Direction -> what dungeon tiles are adjacent */
        .map(direction -> explorerPosition.add(direction))
        /* Into everything on those tiles */
        .flatMap(position -> dungeon_.getEntitiesOnSpace(position).stream())
        /*
         * Ignore gold, because gold you have to be standing on, not be adjacent
         * to
         */
        .filter(entity -> !(entity instanceof Gold))
        /* Into the percepts for those things */
        .map(entity -> Percept.perceptFor(entity))
        /* Only stuff that has percepts */
        .filter(percept -> percept != null).collect(Collectors.toSet());

        /* lol */
        if(playerState_ == PlayerState.PLAYER_RAN_INTO_WALL)
        {
            percepts.add(Percept.BUMP);
        }

        /* Do a gold check on current space */
        dungeon_.getEntitiesOnSpace(explorerPosition).forEach(entity ->
        {
            if(entity instanceof Gold)
            {
                percepts.add(Percept.perceptFor(entity));
            }
        });

        return percepts;
    }

    public PlayerState getPlayerState()
    {
        return playerState_;
    }

    @Override
    public boolean isTerminal()
    {
        return playerState_ != null && playerState_.isTerminal();
    }
}
