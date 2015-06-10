package tictactoe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import utils.Validate;
import utils.Vector2;

public class TicTacToeBoard
{
    private static final int DEFAULT_TIC_TAC_TOE_SIZE = 3;

    private final int width_;
    private final int height_;
    private final int nInARow_;
    /*
     * With a map of positions to TicTacToeMarks representing the board, we can
     * have arbitrary TicTacToe boards (non-rectangular). Currently there are no
     * constructors to support this, but the fact remains - it is possible.
     */
    private final Map<Vector2, TicTacToeMark> board_;

    /**
     * Generates a TicTacToe board of default size (3x3) for your pleasure.
     */
    public TicTacToeBoard()
    {
        this(DEFAULT_TIC_TAC_TOE_SIZE);
    }

    /**
     * Generates a TicTacToeBoard of the desired size. This TicTacToeBoard has a
     * width of $dimension, height of $dimensions, and requires $dimension
     * moves-in-a-row to win.
     * 
     * The dimension is checked for validity (must be a value greater than 0)
     * 
     * @param dimension
     *            The dimension of the board.
     */
    public TicTacToeBoard(final int dimension)
    {
        this(dimension, dimension, dimension);
    }

    /**
     * Generates a TicTacToeBoard of $width width, $height height that requires
     * $nInARow moves-in-a-row to win
     * 
     * @param width
     *            Width of the board. Must be greater than 0.
     * @param height
     *            Height of the board. Must be greater than 0.
     * @param nInARow
     *            Number of moves-in-a-row to win. Must be greater than 0 and
     *            less than or equal to Math.max(height, width)
     */
    public TicTacToeBoard(final int width, final int height, final int nInARow)
    {
        Validate.isTrue(width > 0, "Cannot create a TicTacToeBoard with a negative/zero width!");
        Validate.isTrue(height > 0, "Cannot create a TicTacToeBoard with a negative/zero height");
        Validate.isTrue(nInARow <= Math.max(height, width),
                "Cannot have a TicTacToeBoard where the win condition is larger than the board!");
        Validate.isTrue(nInARow > 0,
                "Cannot create a TicTacToeBoard with a negative/zero win condition!");

        width_ = width;
        height_ = height;
        nInARow_ = nInARow;

        board_ = new HashMap<Vector2, TicTacToeMark>(width * height);
        initializeBoard();
    }

    /**
     * Generates a mutable copy of the provided board. Taking a copy of another
     * board will divorce the two states completely; modifying one board will
     * have no effect on the other.
     * 
     * @param copy
     *            TicTacToeBoard to copy. Must not be null.
     */
    public TicTacToeBoard(final TicTacToeBoard copy)
    {
        Validate.notNull(copy, "Cannot copy a null TicTacToeBoard!");
        width_ = copy.width_;
        height_ = copy.height_;
        nInARow_ = copy.nInARow_;

        board_ = new HashMap<Vector2, TicTacToeMark>(copy.board_);
    }

    private void initializeBoard()
    {
        for(int i = 0; i < width_; ++i)
        {
            for(int j = 0; j < height_; ++j)
            {
                final Vector2 position = new Vector2(i, j);
                board_.put(position, null);
            }
        }
    }

    public int getWidth()
    {
        return width_;
    }

    public int getHeight()
    {
        return height_;
    }
    
    public int getTotalPossibleMoves()
    {
        return board_.size();
    }
    
    public int getContiguousMovesToWin()
    {
        return nInARow_;
    }   

    public Map<Vector2, TicTacToeMark> getBoardAsMap()
    {
        return new HashMap<Vector2, TicTacToeMark>(board_);
    }

    public TicTacToeMark[][] getBoardAsArray()
    {
        final TicTacToeMark[][] board = new TicTacToeMark[width_][height_];
        board_.entrySet().forEach(positionToMark ->
        {
            final Vector2 position = positionToMark.getKey();
            final TicTacToeMark mark = positionToMark.getValue();
            board[position.getX()][position.getY()] = mark;
        });
        return board;
    }

    public boolean isPositionWithinBoard(final Vector2 position)
    {
        return board_.containsKey(position);
    }

    public TicTacToeMark getMarkForPosition(final Vector2 position)
    {
        return board_.get(position);
    }

    public void setMarkForPosition(final int x, final int y, final TicTacToeMark mark)
    {
        final Vector2 position = new Vector2(x, y);
        setMarkForPosition(position, mark);
    }

    public void setMarkForPosition(final Vector2 position, final TicTacToeMark mark)
    {
        Validate.isTrue(isPositionWithinBoard(position),
                "Cannot set a mark for a position that isn't within the board!");
        Validate.isNull(getMarkForPosition(position),
                "Cannot assign a mark to a position that has already been marked!");
        Validate.notNull(mark, "Cannot assign a null mark to a TicTacToeBoard!");
        board_.put(position, mark);
    }

    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();
        // TODO: Does this work / look pretty?
        Arrays.asList(getBoardAsArray())
                .stream()
                .forEach(
                        (row) ->
                        {
                            builder.append(Arrays.deepToString(row).replaceAll("null", "?")
                                    .concat(System.lineSeparator()));
                        });
        return builder.toString();
    }
}
