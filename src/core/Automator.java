package core;

import java.util.Collection;
import java.util.List;

import utils.ClosedRange;
import utils.Validate;

public abstract class Automator<S, A, R extends Rules<S, A>>
{
    protected final R rules_;
    protected S currentState_;

    protected Automator(final R rules, final Collection<Player> players)
    {
        Validate.notNull(rules, "Cannot create an Automator with a null Rule set");
        Validate.notEmpty(players, "Cannot create an Automator with a null/empty player collection");
        final ClosedRange<Integer> possiblePlayerCounts = rules.numberOfPlayers();
        Validate.isTrue(
                possiblePlayerCounts.isValueWithin(players.size()),
                String.format("Cannot create a game for %s, %d is not within %s", players,
                        players.size(), possiblePlayerCounts));
        rules_ = rules;

        final S initialState = rules.generateInitialState(players);
        currentState_ = initialState;
    }

    public abstract S advanceUntilPlayerTurn(final Player player);

    public abstract S advanceSingleAction();

    public abstract S playGameToCompletion();

    public abstract S currentState();

    public abstract S currentStateFilteredForPlayer(final Player player);
    
    public abstract List<A> getActionsTaken();
}
