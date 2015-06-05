package core;

import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author Ginger The Player class provides a standard interface for generating
 * unique players and selecting actions.
 * @param <A>
 */
public abstract class Player<A> {

    public final UUID id;

    public Player() {
        id = UUID.randomUUID();
    }

    abstract public A chooseAction(final State state, final ArrayList<A> actions);
}
