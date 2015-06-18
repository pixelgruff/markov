package wumpusworld;

import java.util.Collection;

public abstract class WumpusWorldState
{

    // TODO: This should not be abstract
    public abstract Collection<Percepts> getPerceptsForPlayer();
}
