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
public class Automator<A, S, R extends Rules<S, A>>
{
    public Player play(final R rules, S state, final Map<Player, Policy<S, A>> policies)
    {
        while(!rules.isTerminal(state))
        {
            final Player player = rules.getCurrentPlayer(state);
            // TODO: State cannot be set to a filtered state; we'll bleed
            // information over time!
            final S knownState = rules.filterState(state, player);
            final Collection<A> actions = rules.getAvailableActions(player, state);
            final A action = policies.get(player).chooseAction(knownState, actions);
            state = rules.transition(state, action);
        }
        System.out.println(state);

        /*
         * TODO: Handle the case of a draw. This assumes that one player has a
         * higher score than all of the others.
         */
        final S terminalState = state;
        return policies
                .keySet()
                .stream()
                .max((player1, player2) -> rules.score(terminalState, player1).compareTo(
                        rules.score(terminalState, player2))).orElse(null);
    }
}
