package tictactoe;

import java.util.Objects;

import utils.Validate;
import utils.Vector2;

/**
 *
 * @author Ginger Represent a single point on the tic-tac-toe board where a
 *         player might place their mark.
 */
public class TicTacToeAction
{
    private final Vector2 position_;
    private final TicTacToeMark mark_;

    /*
     * We need default constructors in order to serialize our data via Jackson.
     */
    @SuppressWarnings("unused")
    private TicTacToeAction()
    {
        position_ = null;
        mark_ = null;
    }

    public TicTacToeAction(final Vector2 position, final TicTacToeMark mark)
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

    @Override
    public boolean equals(final Object other)
    {
        if(!(other instanceof TicTacToeAction))
        {
            return false;
        }

        final TicTacToeAction action = (TicTacToeAction) other;
        return Objects.equals(position_, action.position_) && Objects.equals(mark_, action.mark_);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(position_, mark_);
    }

    @Override
    public String toString()
    {
        return String.format("%s:%s", mark_, position_);
    }
}
