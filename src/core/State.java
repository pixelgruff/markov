package core;

/**
 *
 * @author Ginger State represents a single state in a Game
 */
public interface State
{
    abstract public boolean isTerminal();

    abstract public double getScore(final Player p);
}
