package markov;

/**
 *
 * @author Ginger Client initializes the Game and Player objects and holds the
 * primary game loop.
 */
public class Client {

    public static void play(Game game, Player[] players) {
        while (!game.isOver()) {
            for (Player p : players) {
                Object state = game.getState(p);
                Object[] actions = game.getActions(p);
                Object action = p.chooseAction(state, actions);
                /* 
                TODO: Nefarious players could return an illegal action from 
                chooseAction().  How and where should we verify that the action
                returned is a member of the set 'actions'? 
                */
                game.takeAction(p, action);
            }
        }
    }
}
