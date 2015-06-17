package core.automators;

import java.util.Map;

import core.Automator;
import core.Player;
import core.Policy;
import core.Rules;

public class StatefulAutomator<S, A, R extends Rules<S, A>> implements Automator<S, A, R>
{

    /*
     * TODO: This guy will store a collection of moves and will validate that
     * the API is called in a chained, turn-based fashion
     */

    @Override
    public S advanceUntilPlayerTurn(final R rules, final S initialState, final Player player,
            final Map<Player, Policy<S, A>> policies)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public S advanceSingleAction(final R rules, final S initialState,
            final Map<Player, Policy<S, A>> policies)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public S playGameToCompletion(final R rules, final S initialState,
            final Map<Player, Policy<S, A>> policies)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
