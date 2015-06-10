package core;
import java.util.Collection;

/**
 *
 * @author Ginger Policies map states to actions; users will extend this class
 *         to implement their AI.
 * @param <A>
 *            Action type for the game
 * @param <S>
 *            State type for the action
 */
public interface Policy<A extends Action, S extends State<A>>
{
    public A chooseAction(S state, Collection<A> actions);
}
