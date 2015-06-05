package core;

import java.util.ArrayList;

/**
 *
 * @author Ginger Client initializes the Game and Player objects and holds the
 * primary game loop.
 * @param <S>
 * @param <P>
 * @param <A>
 */
public class Client<S extends State, P extends Player<A>, A> {

    public void play(Game<S, P, A> game) {
        while (!game.isOver()) {
            P player = game.getNextPlayer();
            S state = game.getState(player);
            ArrayList<A> actions = game.getActions(player);
            A action = player.chooseAction(state, actions);
            /* 
             TODO: Nefarious players could return an illegal action from 
             chooseAction().  How and where should we verify that the action
             returned is a member of the set 'actions'? 
             */
            game.takeAction(player, action);

        }
    }
}
