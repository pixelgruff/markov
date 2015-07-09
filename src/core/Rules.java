package core;

import java.util.Collection;
import java.util.Map;

import utils.ClosedRange;

/**
 * Rules holds the transition model and other game-specific logic for
 * identifying and taking appropriate actions.
 *
 * @param <S>
 *            State type for the action
 * @param <A>
 *            State type for the action
 */
public interface Rules<S, A>
{
    /**
     * Provides a deep-copy method for the state. This allows us to generically,
     * without regards to exactly what type the State is, get a copy for it.
     * Useful for Automators, where we can query the state, but don't want to be
     * providing a reference to the actual state of the game. This avoids
     * potentially nefarious actors manipulating game state mid-game.
     *
     * @param state
     *            State to deep copy
     * @return A deep-copy of the state.
     */
    public S copyState(final S state);

    /**
     * Filter a state's information to tailor it to a particular Player's view.
     * This is particularly
     *
     * @param state
     * @param p
     * @return
     */
    public S filterState(final S state, final Player player);

    /**
     * Create the default initial state for this game
     *
     * @param players
     *            Collection of players playing this game
     * @return State created under initial conditions.
     */
    public S generateInitialState(final Collection<Player> players);

    /**
     * Calculate all the valid actions for a particular player at a particular
     * game
     *
     * @param state
     *            Origin State
     * @param player
     *            Player whose actions need to be enumerated
     * @return All valid actions for this player from this state
     */
    public Collection<A> getAvailableActions(final Player player, final S state);

    /**
     * Extract a reference to the current player from the state
     *
     * @param state
     * @return
     */
    public Player getCurrentPlayer(final S state);

    /**
     * Check if a state is terminal, and the simulation should stop
     *
     * @param state
     *            State to check
     * @return Whether or not the state is terminal
     */
    public boolean isTerminal(final S state);

    /**
     * Returns the range of possible player counts (number of players) that this
     * ruleset supports
     *
     * @return A ClosedRange of Integers representing all possible player counts
     */
    public ClosedRange<Integer> numberOfPlayers();

    /**
     * Score a player at a particular game state
     *
     * @param state
     * @param player
     * @return
     */
    public Score score(final S state, final Player player);

    /**
     * Scores for all players for a particular game state
     *
     * @param state
     *            State to get player's scores for
     * @return Mapping of current scores
     */
    public Map<Player, Score> scores(final S state);

    /**
     * Calculate a new state (S') from a state-action pair (S, A) according to
     * the transition model inherent to these game rules
     *
     * @param state
     *            Origin State
     * @param action
     *            Action taken in origin state
     * @return Resultant State
     */
    public S transition(final S state, final A action);
}
