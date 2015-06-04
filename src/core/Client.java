package core;

import java.util.ArrayList;

/**
 *
 * @author Ginger Client initializes the Game and Player objects and holds the
 * primary game loop.
 * @param <S>
 * @param <Action>
 */
public class Client<S extends State, Action> {

    public void play(Game<State, Action> game) {
        while (!game.isOver()) {
            Player<Action> player = game.getNextPlayer();
            State state = game.getState(player);
            ArrayList<Action> actions = game.getActions(player);
            Action action = player.chooseAction(state, actions);
            /* 
             TODO: Nefarious players could return an illegal action from 
             chooseAction().  How and where should we verify that the action
             returned is a member of the set 'actions'? 
             */
            game.takeAction(player, action);

        }
    }
}
