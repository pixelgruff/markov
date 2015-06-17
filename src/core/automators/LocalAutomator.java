package core.automators;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import utils.Validate;
import core.Automator;
import core.Player;
import core.Policy;
import core.Rules;

/**
 * In-memory Automator for playing out games. This should typically be used for
 * testing Policy logic locally against other Policies.
 *
 * @param <S>
 *            State type that the Game supports
 * @param <A>
 *            Action type that the Game supports
 * @param <R>
 *            Rules for the provided State and Actions type
 */
public class LocalAutomator<S, A, R extends Rules<S, A>> implements Automator<S, A, R>
{
    @Override
    public S playGameToCompletion(final R rules, final S initialState,
            final Map<Player, Policy<S, A>> policies)
    {
        Automator.validateArguments(rules, initialState, policies);
        S currentState = initialState;
        while(!rules.isTerminal(currentState))
        {
            currentState = advanceSingleAction(rules, currentState, policies);
        }

        return currentState;
    }

    @Override
    public S advanceUntilPlayerTurn(final R rules, final S initialState, final Player player,
            final Map<Player, Policy<S, A>> policies)
    {
        Automator.validateArguments(rules, initialState, policies);
        Validate.isTrue(policies.keySet().contains(player),
                String.format("Player %s not found within %s", player, policies.keySet()));

        S currentState = initialState;
        while(!Objects.equals(player, rules.getCurrentPlayer(currentState))
                && !rules.isTerminal(currentState))
        {
            currentState = advanceSingleAction(rules, currentState, policies);
        }

        return currentState;
    }

    @Override
    public S advanceSingleAction(final R rules, final S initialState,
            final Map<Player, Policy<S, A>> policies)
    {
        Automator.validateArguments(rules, initialState, policies);
        final Player currentPlayer = rules.getCurrentPlayer(initialState);
        final S knownState = rules.filterState(initialState, currentPlayer);
        final Collection<A> availableActions = rules.getAvailableActions(currentPlayer, knownState);
        final A chosenAction = policies.get(currentPlayer).chooseAction(knownState,
                availableActions);
        /*
         * We can't really transition from a player-filtered state, we need to
         * work off of the actual state of the game
         */
        return rules.transition(initialState, chosenAction);
    }
}
