package core;
import java.util.Collection;

/**
 *
 * @author Ginger Policies map states to actions; users will extend this class
 *         to implement their AI.
 * @param <S>
 *            State type for the action
 * @param <A>
 *            Action type for the game
 */
public interface Policy<S extends State, A extends Action>
{
    public A chooseAction(S state, Collection<A> actions);
}
