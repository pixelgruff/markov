package core;

import java.util.Map;

public interface Automator<S, A, R extends Rules<S, A>>
{
    public S advanceUntilPlayerTurn(final R rules, final S initialState, final Player player);

    public S advanceSingleAction(final R rules, final S initialState);

    public S playGameToCompletion(final R rules, final S initialState,
            final Map<Player, Policy<S, A>> policies);
}
