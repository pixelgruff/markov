package core;

/**
 *
 * @author Ginger State represents a single state in a Game
 */
public interface State<T extends Action>
{
    public boolean isTerminal();

    public Score applyAction(final T action);
}
