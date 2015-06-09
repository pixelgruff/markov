package tictactoe;

import core.Score;
import core.State;

import java.util.ArrayList;

import java.util.Collection;

import java.util.LinkedList;
import java.util.List;

import utils.Validate;
import utils.Vector2;

/**
 *
 * @author Ginger
 */
public class TicTacToeState implements State<TicTacToeAction>
{
    private final TicTacToeBoard board_;
    private boolean terminal_;
    private final List<TicTacToeAction> actions_;

    private static final int ITERABLE_PATH_COUNT = 4;
    private static final int INVERSE = -1;

    /*
     * Generate unit vectors to point towards every possible direction that we
     * can "walk" along the TicTacToeBoard. These "paths" are unique lines
     * representing the x axis, the y axis, the line y = x, and the line y = -x
     */
    private static final Collection<Vector2> PATHS = new ArrayList<Vector2>(ITERABLE_PATH_COUNT);
    static
    {
        // Initialize our directions
        for(int dX = -1; dX <= 1; ++dX)
        {
            for(int dY = -1; dY <= 1; ++dY)
            {
                final Vector2 unitVector = new Vector2(dX, dY);
                /*
                 * Only append if we don't already contain our inverse (vector
                 * for the same line)
                 */
                if(!PATHS.contains(unitVector.multiply(INVERSE)))
                {
                    PATHS.add(unitVector);
                }
            }
        }
    }

    public TicTacToeState(final TicTacToeBoard board)
    {
        board_ = new TicTacToeBoard(board);
        terminal_ = false;
        actions_ = new LinkedList<TicTacToeAction>();
    }

    public TicTacToeState(final TicTacToeState copy)
    {
        Validate.notNull(copy, "Cannot create a copy of a null TicTacToeState");
        board_ = new TicTacToeBoard(copy.board_);
        actions_ = new LinkedList<TicTacToeAction>(copy.actions_);
        terminal_ = copy.terminal_;
    }

    @Override
    public boolean isTerminal()
    {
        return terminal_;
    }

    public List<Vector2> getAvailablePositions()
    {
        final List<Vector2> emptySquares = new ArrayList<Vector2>();
        board_.getBoardStateAsMap().entrySet().stream()
                .filter(boardEntry -> boardEntry.getValue() == null)
                .map(boardEntry -> boardEntry.getKey())
                .forEach(position -> emptySquares.add(position));

        return emptySquares;
    }

    public Score applyAction(final TicTacToeAction action)
    {
        Validate.isFalse(terminal_, "Cannot make a move for a state that is already terminal!");

        Validate.notNull(action, "Cannot apply a null action to a TicTacToeBoard");
        final TicTacToeMark mark = action.getMark();
        Validate.notNull(mark, "Cannot apply a null mark to a TicTacToeBoard");
        final Vector2 position = action.getPosition();
        board_.setMarkForPosition(position, mark);
        actions_.add(action);

        final boolean won = isWinningMove(position, mark);
        terminal_ = (actions_.size() == board_.getTotalPossibleMoves()) || won;
        return won ? new Score(1) : new Score(0);
    }

    private boolean isWinningMove(final Vector2 position, final TicTacToeMark mark)
    {
        for(final Vector2 path : PATHS)
        {
            int totalPathLength = 1;
            /*
             * Walk in both directions until we hit the end of the board or we
             * hit a mark that isn't ours
             */
            final Vector2[] directions = { path, path.multiply(INVERSE) };
            for(final Vector2 direction : directions)
            {
                for(Vector2 currentPosition = position.add(direction); board_
                        .isPositionWithinBoard(currentPosition)
                        && board_.getMarkForPosition(currentPosition) == mark; currentPosition = currentPosition
                        .add(direction))
                {
                    ++totalPathLength;
                }
            }
            if(totalPathLength >= board_.getContiguousMovesToWin())
            {
                return true;
            }

        }
        return false;
    }

    @Override
    public String toString()
    {
        return String.format("%s%n%s%n", board_, actions_);
    }
}
