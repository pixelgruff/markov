package wumpusworld.states;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import utils.Validate;
import wumpusworld.Percept;
import core.Player;
import core.Score;

public class WumpusWorldPlayerState implements WumpusWorldState
{
    final Collection<Percept> percepts_;
    final Player player_;

    public WumpusWorldPlayerState(final Player player, final Collection<Percept> percepts)
    {
        Validate.notNull(percepts, "Cannot create a WumpusWorldPlayerState with null percepts");
        Validate.notNull(player, "Cannot create a WumpusWorldPlayerState for a null player");
        percepts_ = new ArrayList<Percept>(percepts);
        player_ = player;
    }

    public WumpusWorldPlayerState(final WumpusWorldPlayerState copy)
    {
        Validate.notNull(copy, "Cannot create a copy of a null WumpusWorldState");
        percepts_ = new ArrayList<>(copy.percepts_);
        player_ = copy.player_;
    }

    @Override
    public Collection<Percept> getPerceptsForPlayer(final Player player)
    {
        if(!Objects.equals(player, player_))
        {
            return Collections.emptySet();
        }
        return new HashSet<Percept>(percepts_);
    }

    @Override
    public boolean isTerminal()
    {
        /* If we have a PlayerState, it's never false :) */
        return false;
    }

    @Override
    public WumpusWorldState copy()
    {
        return new WumpusWorldPlayerState(this);
    }

    @Override
    public Player getCurrentPlayer()
    {
        return player_;
    }

    @Override
    public Map<Player, PlayerState> states()
    {
        return Collections.emptyMap();
    }

    @Override
    public Score getScoreForPlayer(final Player player)
    {
        return new Score();
    }

    @Override
    public Map<Player, Score> scores()
    {
        return Collections.emptyMap();
    }
}
