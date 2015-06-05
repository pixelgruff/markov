package core;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Ginger The Game interface guarantees Markov Decision Process-like
 * functionality for subroutines requesting information about the game.
 * @param <S>
 * @param <P>
 * @param <A>
 */
public abstract class Game<S extends State, P extends Player, A> {

    private LinkedList<S> states;
    final protected ArrayList<P> players_;

    public Game(final ArrayList<P> players) {
        players_ = players;
    }

    abstract public P getNextPlayer();

    abstract public S getState(P p);

    abstract public ArrayList<A> getActions(P p);

    abstract public void takeAction(P p, A action);

    public boolean isOver() {
        return states.getLast().isTerminal();
    }
}
