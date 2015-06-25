package wumpusworld.states;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import utils.Iterators;
import utils.Validate;
import utils.Vector2;
import wumpusworld.WumpusWorldDungeon;
import wumpusworld.entities.DungeonExplorer;
import wumpusworld.entities.Gold;
import wumpusworld.entities.Percept;
import core.Player;

public class WumpusWorldInternalState implements WumpusWorldState
{
    private final Player currentPlayer_;
    private final WumpusWorldDungeon dungeon_;
    private final Collection<Player> players_;
    private final Map<Player, PlayerState> playerStates_;

    public WumpusWorldInternalState(final Collection<Player> players)
    {
        Validate.notEmpty(players, "Cannot create a WumpusWorldInternalState "
                + "for a null/empty collection of players");
        Validate.isFalse(players.contains(null), "Cannot create a WumpusWorldInternalState with a "
                + "Collection that contains a null player");
        players_ = new ArrayList<>(players);
        dungeon_ = new WumpusWorldDungeon();
        playerStates_ = new HashMap<>();

        players_.forEach(player ->
        {
            dungeon_.addDungeonExplorerForPlayer(player);
            playerStates_.put(player, PlayerState.PLAYER_OK);
        });
        currentPlayer_ = players_.iterator().next();
    }

    public WumpusWorldInternalState(final WumpusWorldInternalState copy)
    {
        Validate.notNull(copy, "Cannot create a copy of a null WumpusWorldInternalState");
        dungeon_ = new WumpusWorldDungeon(copy.dungeon_);
        playerStates_ = new HashMap<>(copy.playerStates_);
        players_ = new ArrayList<>(copy.players_);
        currentPlayer_ = copy.currentPlayer_;
    }

    public Player currentPlayer()
    {
        return currentPlayer_;
    }

    public WumpusWorldDungeon getDungeon()
    {
        return dungeon_;
    }

    public Player getNextPlayer()
    {
        /* TODO: Move these validation checks... higher */
        Validate.isTrue(players_.contains(currentPlayer_), "Cannot determine next player - "
                + "invalid current player");
        Validate.notEmpty(players_, "Cannot determine next player - no players!");

        final Iterator<Player> cyclicIterator = Iterators.cycle(players_);
        /*
         * Cycle iterator until we're at our current position (what if there are
         * duplicate players?)
         */
        Player player;
        do
        {
            player = cyclicIterator.next();
        }
        while(!Objects.equals(currentPlayer_, player));

        return cyclicIterator.next();
    }

    @Override
    public Collection<Percept> getPerceptsForPlayer(final Player player)
    {
        if(!Objects.equals(currentPlayer_, player))
        {
            return Collections.emptyList();
        }

        final PlayerState playerState = playerStates_.get(player);
        if(playerState == null || playerState.isTerminal())
        {
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
        if(playerState == PlayerState.PLAYER_RAN_INTO_WALL)
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

    public PlayerState getPlayerState(final Player player)
    {
        return playerStates_.get(player);
    }

    @Override
    public boolean isTerminal()
    {
        return playerStates_.values().stream().allMatch(playerState -> playerState.isTerminal());
    }

    public void setPlayerState(final Player player, final PlayerState state)
    {
        Validate.isTrue(Objects.equals(currentPlayer_, player),
                "Cannot set the state of a player who is " + "not currently active");
        Validate.notNull(state, "Cannot assign " + player + " a null state");
        playerStates_.put(player, state);
        if(state.isTerminal())
        {
            players_.remove(player);
        }
    }
}
