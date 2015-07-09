package tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import utils.Validate;
import utils.Vector2;
import core.Player;
import core.Score;

public class TicTacToeState
{
    private int width_;
    private int height_;
    /*
     * With a map of positions to TicTacToeMarks representing the board, we can
     * have arbitrary TicTacToe boards (non-rectangular). Currently there are no
     * constructors to support this, but the fact remains - it is possible.
     */
    private Map<Vector2, TicTacToeMark> board_;
    /* Keep track of which player uses which mark */
    private Map<Player, TicTacToeMark> playerMarks_;
    /* Keep track of player scores */
    private Map<Player, Score> playerScores_;
    /* Keep track of the current player */
    private Player currentPlayer_;
    /* Keep track of the number of marks placed on the board */
    private int marksPlaced_ = 0;
    private boolean terminal_ = false;

    /*
     * We need default constructors in order to serialize our data via Jackson.
     * Unfortunately, that either means we insert a lot of fake data, or make
     * the member variables non-final. Either way is sad :(
     */
    @SuppressWarnings("unused")
    private TicTacToeState()
    {
    }

    /**
     * Generates a TicTacToeBoard of the desired size. This TicTacToeBoard has a
     * width of $dimension, height of $dimensions, and requires $dimension
     * moves-in-a-row to win. The dimension is checked for validity (must be a
     * value greater than 0)
     *
     * @param players
     *            The Player objects who will be "playing" the game
     * @param dimension
     *            The dimension of the board.
     */
    public TicTacToeState(final Collection<Player> players, final int dimension)
    {
        this(players, dimension, dimension);
    }

    /**
     * Generates a TicTacToeBoard of $width width, $height height
     *
     * @param players
     *            The Player objects who will be "playing" the game
     * @param width
     *            Width of the board. Must be greater than 0.
     * @param height
     *            Height of the board. Must be greater than 0.
     */
    public TicTacToeState(final Collection<Player> players, final int width, final int height)
    {
        Validate.isTrue(height > 0, "Cannot start a game with a negative/zero width!");
        Validate.isTrue(width > 0, "Cannot start a game with a negative/zero height");
        Validate.notEmpty(players, "Cannot start a game with no players.");

        width_ = width;
        height_ = height;

        board_ = new HashMap<Vector2, TicTacToeMark>(width * height);
        initializeBoard();

        /* Assign marks to players in the order they appear */
        playerMarks_ = new HashMap<Player, TicTacToeMark>(players.size());
        /*
         * Don't initialize player scores to anything - scores will be assigned
         * if and when a score should be. We leave the getScore() option to
         * determine what exactly a null score should be
         */
        playerScores_ = new HashMap<Player, Score>(players.size());
        Validate.isTrue(TicTacToeMark.values().length == players.size(), String.format(
                "There must be exactly one player for each mark!%n" + "Got: %d players, %d marks.",
                players.size(), TicTacToeMark.values().length));

        /*
         * Java needs a bidirectional Map so much. Need to determine an
         * assignment of players to marks.
         */
        final List<Player> orderedPlayers = new ArrayList<>(players);
        for(int i = 0; i < orderedPlayers.size(); i++)
        {
            playerMarks_.put(orderedPlayers.get(i), TicTacToeMark.values()[i]);
        }
        /*
         * The rules should determine what player goes first, so we initialize
         * currentPlayer_ to null
         */
        currentPlayer_ = null;
    }

    /**
     * Generates a mutable copy of the provided board. Taking a copy of another
     * board will divorce the two states completely; modifying one board will
     * have no effect on the other.
     *
     * @param copy
     *            TicTacToeBoard to copy. Must not be null.
     */
    public TicTacToeState(final TicTacToeState copy)
    {
        Validate.notNull(copy, "Cannot copy a null TicTacToeBoard!");
        width_ = copy.width_;
        height_ = copy.height_;
        marksPlaced_ = copy.marksPlaced_;

        board_ = new HashMap<Vector2, TicTacToeMark>(copy.board_);
        playerMarks_ = new HashMap<Player, TicTacToeMark>(copy.playerMarks_);
        playerScores_ = new HashMap<Player, Score>(copy.playerScores_);
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

    public TicTacToeMark getMarkForPlayer(final Player player)
    {
        Validate.isTrue(playerMarks_.containsKey(player),
                String.format("No known marks for player %s", player));
        return playerMarks_.get(player);
    }

    /* Find the player who corresponds with a particular mark on the game board */
    /*
     * TODO?: Implement a bidirectional map like in the thirty different
     * excellent libraries listed on StackOverFlow?
     */
    public Player getPlayer(final TicTacToeMark mark)
    {
        Validate.notNull(mark, "Null marks have no players.");
        Validate.isTrue(playerMarks_.values().contains(mark),
                "Mark not found in all the marks this state knows about.");

        final List<Entry<Player, TicTacToeMark>> matchingPlayers = playerMarks_.entrySet().stream()
                .filter((entry) -> entry.getValue() == mark).collect(Collectors.toList());

        Validate.notEmpty(matchingPlayers);
        /*
         * Validate the one-to-one mapping of players to marks. This is an
         * assumption we make about the way the game will be played!
         */
        Validate.isTrue(matchingPlayers.size() == 1,
                "Found more than one player who places this mark.");
        return matchingPlayers.get(0).getKey();
    }

    public void setScore(final Player player, final Score score)
    {
        playerScores_.put(player, score);
    }

    public Score getPlayerScore(final Player player)
    {
        return playerScores_.getOrDefault(player, new Score());
    }

    public Map<Player, Score> getPlayerScores()
    {
        return new HashMap<Player, Score>(playerScores_);
    }

    public void makeTerminal()
    {
        terminal_ = true;
    }

    public boolean isTerminal()
    {
        return terminal_;
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

    /* Get the number of actions taken to have arrived at this state */
    public int getNumberOfActionsTaken()
    {
        return marksPlaced_;
    }

    public Player getCurrentPlayer()
    {
        return currentPlayer_;
    }

    public void setCurrentPlayer(final Player player)
    {
        // TODO: Validate ?
        currentPlayer_ = player;
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
        ++marksPlaced_;
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
        playerMarks_.keySet().forEach(
                (player) ->
                {
                    builder.append(player.toString());
                    builder.append(String.format(" (%s) %s %s",
                            getMarkForPlayer(player).toString(), getPlayerScore(player),
                            /*
                             * Special string if player != null and player is
                             * current
                             */
                            (currentPlayer_ != null && currentPlayer_.equals(player) ? " (current)"
                                    : "")));
                    builder.append(System.lineSeparator());
                });
        return builder.toString();
    }
}
