package wumpusworld.states;

import java.util.ArrayList;
import java.util.Collection;

import utils.Validate;
import wumpusworld.entities.Percept;
import core.Player;

public class WumpusWorldPlayerState implements WumpusWorldState
{
    final Collection<Percept> percepts_;

    public WumpusWorldPlayerState(final Collection<Percept> percepts)
    {
        Validate.notEmpty(percepts, "Cannot create a WumpusWorldPlayerState without any percepts");
        percepts_ = new ArrayList<Percept>(percepts);
    }

    @Override
    public Collection<Percept> getPerceptsForPlayer(final Player player)
    {
        /*
         * TODO: Take player into consideration. For now, we assume that it's
         * the correct player
         */
        return new ArrayList<Percept>(percepts_);
    }

    @Override
    public boolean isTerminal()
    {
        /* If we have a PlayerState, it's never false :) */
        return false;
    }
}
