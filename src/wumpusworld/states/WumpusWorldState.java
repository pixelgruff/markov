package wumpusworld.states;

import java.util.Collection;

import wumpusworld.entities.Percept;
import core.Player;

public interface WumpusWorldState
{
    public Collection<Percept> getPerceptsForPlayer(final Player player);

    public boolean isTerminal();
}
