package core;

import java.util.Map;

import utils.Validate;

/**
 * An Automator is a helper for playing out Games.
 *
 * @param <S>
 *            State type that the Game supports
 * @param <A>
 *            Action type that the Game supports
 * @param <R>
 *            Rules for the provided State and Actions type
 */
public interface Automator<S, A, R extends Rules<S, A>>
{
    /**
     * "Plays" the game using the provided rules, initial state, and policy
     * mapping until it is the specified player's turn. Note: This is not
     * guaranteed to terminate depending on the implementation of the specified
     * rules.
     *
     * @param rules
     *            Game Rules to use
     * @param initialState
     *            A valid gamestate to start from
     * @param player
     *            Player in question
     * @param policies
     *            Policies for all players
     * @return The first valid state where the current turn is that of the
     *         provided Player's
     */
    public S advanceUntilPlayerTurn(final R rules, final S initialState, final Player player,
            final Map<Player, Policy<S, A>> policies);

    /**
     * "Plays" the game using the provided rules, initial state, and policy
     * mapping for a single action. This is akin to advancing the gamestate one
     * "tick" or "frame", returning the very next State.
     *
     * @param rules
     *            Game Rules to use
     * @param initialState
     *            A valid gamestate to start from
     * @param policies
     *            Policies for all players
     * @return The State directly resulting from the current player making a
     *         single action.
     */
    public S advanceSingleAction(final R rules, final S initialState,
            final Map<Player, Policy<S, A>> policies);

    /**
     * "Plays" the game using the provided rules, initial state, and policy
     * mapping for an entire game. This will advance the gamestate to the first
     * state that is terminal.
     *
     * @param rules
     *            Game Rules to use
     * @param initialState
     *            A valid gamestate to start from. This should typically be the
     *            state returned by rules.generateInitialState()
     * @param policies
     *            Policies for all players
     * @return The State that resulted from a full duration game. This state
     *         should be terminal.
     */
    public S playGameToCompletion(final R rules, final S initialState,
            final Map<Player, Policy<S, A>> policies);

    /* Helper for generically validating arguments for all Automator functions */
    public static <S, A, R extends Rules<S, A>> void validateArguments(final R rules,
            final S initialState, final Map<Player, Policy<S, A>> policies)
    {
        Validate.notNull(rules, "Cannot interact with a null Rules instance");
        Validate.notNull(initialState, "Cannot interact with a null initial state");
        Validate.notEmpty(policies, "Cannot interact with a null/empty policy map");
    }
}
