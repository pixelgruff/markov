package wumpusworld.states;

import java.util.Collection;

import wumpusworld.entities.Percept;
import core.Player;
import core.Score;

public interface WumpusWorldState
{
    public Collection<Percept> getPerceptsForPlayer(final Player player);

    public boolean isTerminal();

    public WumpusWorldState copy();

    public Player getCurrentPlayer();

    public Score getScoreForPlayer(final Player player);
}
