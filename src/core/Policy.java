package core;

import java.util.ArrayList;

/**
 *
 * @author Ginger Policies map states to actions; users will extend this class
 * to implement their AI.
 * @param <S>
 * @param <A>
 */
public interface Policy<S extends State, A extends Action> {

    public abstract A chooseAction(S state, ArrayList<A> actions);
}
