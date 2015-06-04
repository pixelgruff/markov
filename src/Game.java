package markov;

/**
 *
 * @author Ginger The Game interface guarantees Markov Decision Process-like
 * functionality for subroutines requesting information about the game.
 */
public interface Game {

    public abstract Object getInitialState(Player[] players);

    abstract public Object getState(Player p);

    abstract public Object[] getActions(Player p);

    abstract public void takeAction(Player p, Object action);
    
    abstract public boolean isOver();
}
