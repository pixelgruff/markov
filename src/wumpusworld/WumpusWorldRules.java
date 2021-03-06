package wumpusworld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import utils.ClosedRange;
import utils.Validate;
import utils.Vector2;
import wumpusworld.entities.DungeonExplorer;
import wumpusworld.entities.DungeonTile;
import wumpusworld.entities.DungeonTileType;
import wumpusworld.entities.Gold;
import wumpusworld.entities.Item;
import wumpusworld.entities.Wumpus;
import wumpusworld.states.PlayerState;
import wumpusworld.states.WumpusWorldInternalState;
import wumpusworld.states.WumpusWorldPlayerState;
import wumpusworld.states.WumpusWorldState;
import core.Player;
import core.Rules;
import core.Score;
import java.util.Set;

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
        Validate.notNull(state, "Cannot copy a null WumpusWorldState!");
        return state.copy();
    }

    @Override
    public WumpusWorldState filterState(final WumpusWorldState state, final Player player)
    {
        Validate.notNull(state, "Cannot filter a null WumpusWorldState");
        final Collection<Percept> perceptsForPlayer = state.getPerceptsForPlayer(player);
        return new WumpusWorldPlayerState(player, perceptsForPlayer);
    }

    @Override
    public WumpusWorldState generateInitialState(final Set<Player> players)
    {
        Validate.notEmpty(players, "Cannot create a WumpusWorldDungeon" + " without any players");
        Validate.isTrue(numberOfPlayers().isValueWithin(players.size()),
                String.format("Cannot create a WumpusWorldDungeon with "
                        + "%d players. Valid amounts: ", players.size(), numberOfPlayers()));

        final WumpusWorldInternalState internalStartingState = new WumpusWorldInternalState(players);
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
        Validate.notNull(state, "Cannot determine the current player for a null WumpusWorldState");
        return state.getCurrentPlayer();
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
    public Score score(final WumpusWorldState state, final Player player)
    {
        return state.getScoreForPlayer(player);
    }

    @Override
    public Map<Player, Score> scores(final WumpusWorldState state)
    {
        return state.scores();
    }

    @Override
    public WumpusWorldState transition(final WumpusWorldState state, final WumpusWorldAction action)
    {
        Validate.isTrue(state instanceof WumpusWorldInternalState,
                "Can not transition without a proper reference to our internal state. "
                        + "Did you accidentally pass in a filtered state?");
        Validate.notNull(action, "Cannot transition states with a null action");
        final Player player = action.getPlayer();
        Validate.isTrue(Objects.equals(state.getCurrentPlayer(), player),
                "Cannot transition states for an " + "action made with an invalid player");
        Validate.isTrue(getAvailableActions(player, state).contains(action),
                "Cannot take an invalid action");

        final WumpusWorldInternalState internalState = (WumpusWorldInternalState) state;
        /* Make sure we don't modify the state that was passed in */
        final WumpusWorldInternalState ourState = new WumpusWorldInternalState(internalState);
        final WumpusWorldDungeon dungeon = ourState.getDungeon();

        final DungeonExplorer explorer = dungeon.getDungeonExplorer(player);
        Validate.notNull(explorer, "Cannot transition a null DungeonExplorer");
        final Vector2 position = explorer.getPosition();

        PlayerState playerState = PlayerState.PLAYER_OK;
        final Collection<Item> itemsDropped = new ArrayList<Item>();
        switch(action.getAction())
        {
        case TURN_RIGHT:
            explorer.turnRight();
            break;
        case TURN_LEFT:
            explorer.turnLeft();
            break;
        case RELEASE:
            final Item releasedItem = explorer.release();
            if(releasedItem != null)
            {
                itemsDropped.add(releasedItem);
            }
            break;
        case GRAB:
        {
            final Item item = dungeon.getEntitiesOnSpace(position).stream()
                    .filter(entity -> entity instanceof Item).map(entity -> (Item) entity)
                    .findFirst().orElse(null);
            if(item != null)
            {
                explorer.grab(item);
            }
            break;
        }
        case CLIMB:
            if(Objects.equals(dungeon.ladderSpace(), position)
                    && explorer.getItems().stream().anyMatch(item -> item instanceof Gold))
            {
                playerState = PlayerState.PLAYER_ESCAPED_WITH_GOLD;
            }
            break;
        case MOVE_FORWARD:
        {
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
                            if(entity instanceof Wumpus && !((Wumpus) entity).isDead())
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
                        }).filter(status -> status != null).findFirst()
                        .orElse(PlayerState.PLAYER_OK);
            }
            else
            {
                playerState = PlayerState.PLAYER_RAN_INTO_WALL;
            }
            break;
        }
        case FIRE_ARROW:
        {
            final Vector2 direction = explorer.getDirection();
            for(Vector2 currentSpace = position.add(direction); dungeon.contains(currentSpace); currentSpace = currentSpace
                    .add(direction))
            {
                dungeon.getEntitiesOnSpace(currentSpace).forEach(entity ->
                {
                    if(entity instanceof Wumpus)
                    {
                        ((Wumpus) entity).slay();
                        /* How do we deal with freshly "slayed" wumpuses ..? */
                    }
                });
            }
            break;
        }
        default:
            throw new RuntimeException("Unexpected action: " + action.getAction());
        }

        final WumpusWorldInternalState nextState = new WumpusWorldInternalState(ourState);
        nextState.setPlayerState(player, playerState);
        /* Add any items dropped back into the dungeon */
        itemsDropped.forEach(item -> nextState.getDungeon().putItem(item));
        return nextState;
    }
}
