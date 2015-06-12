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
 */
public class Automator<A extends Action, S extends State<A>, G extends Game<A, S>>
{
    public Player play(final G game, final Map<Player, Policy<A, S>> policies)
    {
        while(!game.isOver())
        {
            final Player player = game.getCurrentPlayer();
            final S state = game.getState(player);
            final Collection<A> actions = game.getActions(player);
            final A action = policies.get(player).chooseAction(state, actions);
            game.takeAction(player, action);
        }

        /*
         * TODO: Handle the case of a draw. This assumes that one player has a
         * higher score than all of the others.
         */
        return policies
                .keySet()
                .stream()
                .max((player1, player2) -> game.getPlayerScore(player1).compareTo(
                        game.getPlayerScore(player2))).orElse(null);
    }
}
