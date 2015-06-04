package core;

/**
 *
 * @author Ginger State represents a single state in a <GameType> Game
 * @param <GameType>
 */
public abstract class State<GameType> {

    abstract public boolean isTerminal();

    abstract public double getScore(final Player p);

}
