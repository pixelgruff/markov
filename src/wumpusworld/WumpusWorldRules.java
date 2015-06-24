package wumpusworld;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import utils.ClosedRange;
import utils.Validate;
import utils.Vector2;
import wumpusworld.entities.DungeonExplorer;
import wumpusworld.entities.DungeonTile;
import wumpusworld.entities.DungeonTileType;
import wumpusworld.entities.Item;
import wumpusworld.entities.Wumpus;
import wumpusworld.states.PlayerState;
import wumpusworld.states.WumpusWorldInternalState;
import wumpusworld.states.WumpusWorldState;
import core.Player;
import core.Rules;
import core.Score;

/*
 * Full details found:
 * http://www.cis.temple.edu/~giorgio/cis587/readings/wumpus.shtml
 */
public class WumpusWorldRules implements Rules<WumpusWorldState, WumpusWorldAction>
{
    private static final int NUMBER_OF_PLAYERS = 1;

    @Override
    public WumpusWorldState copyState(final WumpusWorldState state)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public WumpusWorldState filterState(final WumpusWorldState state, final Player p)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public WumpusWorldState generateInitialState(final Collection<Player> players)
    {
        Validate.notEmpty(players, "Cannot create a WumpusWorldDungeon" + " without any players");
        Validate.isTrue(numberOfPlayers().isValueWithin(players.size()),
                String.format("Cannot create a WumpusWorldDungeon with "
                        + "%d players. Valid amounts: ", players.size(), numberOfPlayers()));

        final Player player = players.stream().findFirst().get();

        final WumpusWorldDungeon dungeon = new WumpusWorldDungeon();
        dungeon.addDungeonExplorerForPlayer(player);

        final WumpusWorldInternalState internalStartingState = new WumpusWorldInternalState(dungeon);
        return internalStartingState;
    }

    @Override
    public Collection<WumpusWorldAction> getAvailableActions(final Player player,
            final WumpusWorldState state)
    {
        Validate.notNull(state, "Cannot determine available actions for a null WumpusWorldState");
        if(isTerminal(state))
        {
            return Collections.emptyList();
        }

        return Arrays.asList(WumpusWorldAction.Action.values()).stream()
                .map(action -> new WumpusWorldAction(player, action)).collect(Collectors.toList());
    }

    @Override
    public Player getCurrentPlayer(final WumpusWorldState state)
    {
        return state.
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isTerminal(final WumpusWorldState state)
    {
        Validate.notNull(state, "Cannot determine if a null WumpusWorldState is terminal");
        return state.isTerminal();
    }

    @Override
    public ClosedRange<Integer> numberOfPlayers()
    {
        return new ClosedRange<Integer>(NUMBER_OF_PLAYERS, NUMBER_OF_PLAYERS);
    }

    @Override
    public Score score(final WumpusWorldState state, final Player p)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<Player, Score> scores(final WumpusWorldState state)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public WumpusWorldState transition(final WumpusWorldState state, final WumpusWorldAction action)
    {
        if(!(state instanceof WumpusWorldInternalState))
        {
            throw new RuntimeException("Can not transition without a proper "
                    + "reference to our internal state. "
                    + "Did you accidentally pass in a filtered state?");
        }

        final WumpusWorldInternalState internalState = (WumpusWorldInternalState) state;
        final WumpusWorldDungeon dungeon = internalState.getDungeon();
        final DungeonExplorer explorer = dungeon.getDungeonExplorer(action.getPlayer());
        Validate.notNull(explorer, "Cannot transition a null DungeonExplorer");
        final Vector2 position = explorer.getPosition();

        // TODO: Validate player

        PlayerState playerState = null;

        switch(action.getAction())
        {
        case TURN_RIGHT:
            explorer.turnRight();
            break;
        case TURN_LEFT:
            explorer.turnLeft();
            break;
        case RELEASE:
            explorer.release();
            // TODO: Figure out how to add the item back into the dungeon
        case GRAB:
            final Item item = dungeon.getEntitiesOnSpace(position).stream()
                    .filter(entity -> entity instanceof Item).map(entity -> (Item) entity)
                    .findFirst().orElse(null);
            if(item != null)
            {
                explorer.grab(item);
            }
            break;
        case MOVE_FORWARD:
            final Vector2 direction = explorer.getDirection();
            final Vector2 resultPosition = position.add(direction);
            if(dungeon.contains(resultPosition))
            {
                explorer.moveForward();
                playerState = dungeon
                        .getEntitiesOnSpace(resultPosition)
                        .stream()
                        .map(entity ->
                        {
                            if(entity instanceof Wumpus)
                            {
                                return PlayerState.PLAYER_EATEN;
                            }
                            else if(entity instanceof DungeonTile
                                    && ((DungeonTile) entity).getTileType() == DungeonTileType.PIT)
                            {
                                return PlayerState.PLAYER_FELL_TO_DEATH;
                            }
                            else
                            {
                                return null;
                            }
                        }).filter(status -> status != null).findFirst().orElse(null);
                // TODO: REST OF THIS LOGIC
            }
            else
            {
                playerState = PlayerState.PLAYER_RAN_INTO_WALL;
                // TODO: REST OF THIS LOGIC
            }

        }

        // TODO Auto-generated method stub
        return null;
    }

}
