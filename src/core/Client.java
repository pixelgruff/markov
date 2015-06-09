package core;

import java.util.Collection;
import java.util.Map;

/**
 *
 * @author Ginger Client initializes the Game and Player objects and holds the
 *         primary game loop.
 * @param <S>
 * @param <A>
 */
public class Client<A extends Action, S extends State<A>, G extends Game<A, S>>
{

    public void play(G game, Map<Player, Policy<A, S>> policies)
    {
        while(!game.isOver())
        {
            Player player = game.getCurrentPlayer();
            S state = game.getState(player);
            Collection<A> actions = game.getActions(player);
            A action = policies.get(player).chooseAction(state, actions);
            /*
             * TODO: Nefarious players could return an illegal action from
             * chooseAction(). How and where should we verify that the action
             * returned is a member of the set 'actions'?
             */
            
            game.takeAction(player, action);

        }
    }
}
