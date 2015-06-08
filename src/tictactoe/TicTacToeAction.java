package tictactoe;

import core.Action;
import utils.Vector2;

/**
 *
 * @author Ginger Represent a single point on the tic-tac-toe board where a 
 * player might place their mark.
 */
public class TicTacToeAction extends Action {

    public Vector2 position;

    TicTacToeAction(final int x, final int y) {
        position = new Vector2(x, y);
    }

    TicTacToeAction(final Vector2 position) {
        this.position = position;
    }
}
