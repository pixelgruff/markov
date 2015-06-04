package core;

/**
 *
 * @author Ginger State represents a single state in a <GameType> Game
 */
public abstract class State {

    abstract public boolean isTerminal();

    abstract public double getScore(final Player p);

}
