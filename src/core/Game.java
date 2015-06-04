package core;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Ginger The Game interface guarantees Markov Decision Process-like
 * functionality for subroutines requesting information about the game.
 * @param <S>
 * @param <Action>
 */
public abstract class Game<S extends State, Action> {

    private LinkedList<S> states;
    final private ArrayList<Player> players_;

    public Game(final ArrayList<Player> players) {
        players_ = players;
    }

    abstract public Player getNextPlayer();

    abstract public State getState(Player p);

    abstract public ArrayList<Action> getActions(Player p);

    abstract public void takeAction(Player p, Action action);

    public boolean isOver() {
        return states.getLast().isTerminal();
    }
}