package core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Ginger The Game interface guarantees Markov Decision Process-like
 * functionality for subroutines requesting information about the game.
 * @param <S>
 * @param <P>
 * @param <A>
 */
public abstract class Game<T extends GameType, A extends Action<T>, S extends State<T, A>, P extends Player<T, A>>
{
    protected LinkedList<S> states_;
    final protected List<P> players_;

    public Game(final ArrayList<P> players)
    {
        players_ = players;
    }

    abstract public P getNextPlayer();

    abstract public S getState(P p);

    abstract public ArrayList<A> getActions(P p);

    abstract public void takeAction(P p, A action);

    public boolean isOver()
    {
        // Assume we never contain null states
        return states_ != null && !states_.isEmpty() && states_.getLast().isTerminal();
    }
}
