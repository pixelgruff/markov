package core;

/**
 *
 * @author Ginger State represents a single state in a Game
 */
public interface State<T extends GameType, A extends Action<T>>
{
    abstract public boolean isTerminal();

    abstract public double getScore(final Player<T, A> p);
}
