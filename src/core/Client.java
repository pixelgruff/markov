package core;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Ginger Client initializes the Game and Player objects and holds the
 *         primary game loop.
 * @param <S>
 * @param <A>
 */
public class Client<S extends State, A extends Action>
{

    public void play(Game<S, A> game, HashMap<Player, Policy<S, A>> policies)
    {
        while(!game.isOver())
        {
            Player player = game.getCurrentPlayer();
            S state = game.getState(player);
            ArrayList<A> actions = game.getActions(player);
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
