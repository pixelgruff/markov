package markov;

import java.util.UUID;

/**
 *
 * @author Ginger The Player class provides a standard interface for generating
 * unique players and selecting actions.
 */
public abstract class Player {

    public UUID id;

    public Player() {
        id = UUID.randomUUID();
    }

    abstract public Object chooseAction(Object state, Object[] actions);
}
