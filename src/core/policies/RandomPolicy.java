package core.policies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import core.Policy;
import utils.Validate;

public class RandomPolicy<S, A> implements Policy<S, A>
{
    @Override
    public A chooseAction(final S state, final Collection<A> actions)
    {
        /**
         * Action list should never be empty. If the game is "stuck" and a
         * player cannot take any actions, this should be handled in rules
         * either by a) making "pass" an action or b) declaring the game over
         * beforehand
         */
        Validate.notEmpty(actions,
                "Policies should always have at least one action to select from.");
        return new ArrayList<>(actions).get(ThreadLocalRandom.current().nextInt(actions.size()));
    }

    @Override
    public String toString()
    {
        return "Random";
    }
}
