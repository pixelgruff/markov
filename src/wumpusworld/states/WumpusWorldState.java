package wumpusworld.states;

import java.util.Collection;
import java.util.Map;

import wumpusworld.Percept;
import core.Player;
import core.Score;

public interface WumpusWorldState
{
    public Collection<Percept> getPerceptsForPlayer(final Player player);

    public boolean isTerminal();

    public WumpusWorldState copy();

    public Player getCurrentPlayer();

    public Score getScoreForPlayer(final Player player);

    public Map<Player, Score> scores();

    public Map<Player, PlayerState> states();
}
