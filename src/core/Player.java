package core;

import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author Ginger The Player class provides a standard interface for generating
 * unique players and selecting actions.
 * @param <Action>
 */
public abstract class Player<Action> {

    public UUID id;

    public Player() {
        id = UUID.randomUUID();
    }

    abstract public Action chooseAction(final State state, final ArrayList<Action> actions);
}
