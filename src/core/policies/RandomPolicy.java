package core.policies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import core.Action;
import core.Policy;
import core.State;

public class RandomPolicy<A extends Action, S extends State<A>> implements Policy<A, S>
{
    @Override
    public A chooseAction(S state, Collection<A> actions)
    {
        if(actions.isEmpty())
        {
            return null;
        }
        return new ArrayList<>(actions).get(ThreadLocalRandom.current().nextInt(actions.size()));
    }
}
