package core.automators;

import java.util.Collection;
import java.util.HashMap;
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
public final class LocalAutomator<S, A, R extends Rules<S, A>> extends Automator<S, A, R>
{
    private final Map<Player, Policy<S, A>> playerPolicies_;

    public LocalAutomator(final R rules, final Map<Player, Policy<S, A>> playerPolicies)
    {
        super(rules, playerPolicies.keySet());
        playerPolicies_ = new HashMap<Player, Policy<S, A>>(playerPolicies);
    }

    @Override
    public S advanceUntilPlayerTurn(final Player player)
    {
        Validate.isTrue(playerPolicies_.containsKey(player), String.format(
                "Cannot advance a game for %s as it is not a known player (%s)", player,
                playerPolicies_.keySet()));
        final Player currentPlayer = rules_.getCurrentPlayer(currentState_);
        while(!Objects.equals(player, currentPlayer) && !rules_.isTerminal(currentState_))
        {
            advanceSingleAction();
        }
        return currentState();
    }

    @Override
    public S advanceSingleAction()
    {
        Validate.isFalse(rules_.isTerminal(currentState_),
                "Cannot advance actions for a terminal state");
        final Player currentPlayer = rules_.getCurrentPlayer(currentState_);
        final Collection<A> availableActions = rules_.getAvailableActions(currentPlayer,
                currentState_);
        final S filteredState = rules_.filterState(currentState_, currentPlayer);
        final Policy<S, A> policy = playerPolicies_.get(currentPlayer);
        final A chosenAction = policy.chooseAction(filteredState, availableActions);
        currentState_ = rules_.transition(currentState_, chosenAction);
        return currentState();
    }

    @Override
    public S playGameToCompletion()
    {
        while(!rules_.isTerminal(currentState_))
        {
            advanceSingleAction();
        }
        return currentState();
    }

    @Override
    public S currentState()
    {
        return rules_.copyState(currentState_);
    }

    @Override
    public S currentStateFilteredForPlayer(final Player player)
    {
        return rules_.filterState(currentState_, player);

    }
}
