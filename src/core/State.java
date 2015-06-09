package core;

/**
 *
 * @author Ginger State represents a single state in a Game
 */
public interface State<T extends Action>
{
    abstract public boolean isTerminal();

    abstract public Score applyAction(final T action);
}
