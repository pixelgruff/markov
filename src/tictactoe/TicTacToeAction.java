package tictactoe;

import core.Action;
import utils.Validate;
import utils.Vector2;

/**
 *
 * @author Ginger Represent a single point on the tic-tac-toe board where a
 *         player might place their mark.
 */
public class TicTacToeAction extends Action
{
    private final Vector2 position_;
    private final TicTacToeMark mark_;

    TicTacToeAction(final Vector2 position, final TicTacToeMark mark)
    {
        Validate.notNull(position, "Cannot create a TicTacToeAction with a null position");
        Validate.notNull(mark, "Cannot create a TicTacToeAction with a null mark");
        position_ = position;
        mark_ = mark;
    }

    public Vector2 getPosition()
    {
        return position_;
    }
    
    public TicTacToeMark getMark()
    {
        return mark_;
    }
}
