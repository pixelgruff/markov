package core;

import java.util.Collection;
import java.util.Map;

/**
 * TODO: Turn this into an interface with some nice, hefty functions to
 * manipulate game state. This way, we can implement an in-memory Automator
 * (offline) and have the game server itself be an automator.
 *
 * @author Ginger Client initializes the Game and Player objects and holds the
 *         primary game loop.
 * @param <S>
 * @param <A>
 * @param <R>
 */
public class InMemoryAutomator<S, A, R extends Rules<S, A>> implements Automator<S, A, R>
{
    @Override
    public S playGameToCompletion(final R rules, final S initialState,
            final Map<Player, Policy<S, A>> policies)
    {
        S currentState = initialState;
        while(!rules.isTerminal(currentState))
        {
            final Player player = rules.getCurrentPlayer(currentState);
            final S knownState = rules.filterState(currentState, player);
            final Collection<A> actions = rules.getAvailableActions(player, currentState);
            final A action = policies.get(player).chooseAction(knownState, actions);
            currentState = rules.transition(currentState, action);
        }

        return currentState;
    }

    @Override
    public S advanceUntilPlayerTurn(final R rules, final S initialState, final Player player)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public S advanceSingleAction(final R rules, final S initialState)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
