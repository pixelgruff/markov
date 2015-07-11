package tetris;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import utils.ClosedRange;
import utils.Validate;
import core.Player;
import core.Score;

/**
 * Represent the Tetris board at a particular moment
 *
 * @author Ginger
 */
public class TetrisState
{

    /* Keep track of simulation time */
    private int simTime_;
    /* Board dimensions; would be final but for clashes with Jackson */
    private int width_, height_;
    /* Keep track of players */
    private Collection<Player> players_;
    /* Keep track of player scores */
    private Map<Player, Score> playerScores_;
    /* Keep track of shapes on the board */
    public Set<TetrisShape> shapes;
    /* Flag set if this state is terminal */
    private boolean terminal_ = false;
    /* Range of x- and y-values */
    private ClosedRange xRange_, yRange_;
    /* Next Tetrimino expected to appear; visible to the player */
    private Tetrimino nextTetrimino_;
    /* Random class for selecting Tetriminos */
    private final Random random_ = new Random();

    /* Default constructor for Jackson */
    public TetrisState()
    {
    }

    /**
     * Constructor for setting a standard State with no shapes.
     * @param players
     * @param width
     * @param height
     */
    public TetrisState(final Set<Player> players, final int width, final int height)
    {
        simTime_ = 0;
        width_ = width;
        height_ = height;
        shapes = new HashSet<TetrisShape>();

        players_ = players;
        playerScores_ = new HashMap<Player, Score>(players.size());
        /* Initialize all scores to 0 */
        players.stream().forEach((player) -> playerScores_.put(player, new Score(0)));
        xRange_ = new ClosedRange(0, width - 1);
        yRange_ = new ClosedRange(0, height - 1);
        nextTetrimino_ = getRandomTetrimino();
    }

    public TetrisState(final TetrisState copy)
    {
        simTime_ = copy.simTime_;
        width_ = copy.width_;
        height_ = copy.height_;

        shapes = new HashSet(copy.shapes);
        players_ = new HashSet(copy.players_);
        playerScores_ = new HashMap<Player, Score>(copy.playerScores_);
        xRange_ = new ClosedRange(0, copy.width_ - 1);
        yRange_ = new ClosedRange(0, copy.height_ - 1);
        nextTetrimino_ = copy.nextTetrimino_;
    }

    public boolean isBlockOnBoard(final TetrisBlock block) {
        Validate.notNull(block, "Block to validate must not be null.");
        return xRange_.isValueWithin(block.getX()) && yRange_.isValueWithin(block.getY());
    }

    public void incrementSimTime() {
        simTime_++;
    }

    public Score getPlayerScore(final Player player)
    {
        return playerScores_.getOrDefault(player, new Score());
    }
    
    /**
     * Look at the next Tetrimino expected to fall.
     * @return 
     */
    public Tetrimino peekTetrimino() {
        return nextTetrimino_;
    }
    
    /**
     * Return the next Tetrimino expected to fall, and calculate a new one .
     * @return
     */
    public Tetrimino popTetrimino() {
        Tetrimino tetrimino = nextTetrimino_;
        nextTetrimino_ = getRandomTetrimino();
        return tetrimino;
    }
    
    /**
     * Increment a player's score
     * @param player
     * @param points 
     */
    public void incrementPlayerScore(final Player player, final int points) {
        Validate.notNull(player, "Player must not be null.");
        Validate.isTrue(points > 0, "Increment to score should exceed 0.");
        Validate.isTrue(playerScores_.containsKey(player), "No such player found.");
        
        playerScores_.put(player, playerScores_.get(player).add(new Score(points)));
    }

    public int getWidth() {
        return width_;
    }

    public int getHeight() {
        return height_;
    }

    public void makeTerminal()
    {
        terminal_ = true;
    }

    public boolean isTerminal()
    {
        return terminal_;
    }

    public int getGameTime()
    {
        return simTime_;
    }
    
    public Collection<Player> getPlayers() {
        return players_;
    }

    @Override
    public String toString() {
        char[][] board = new char[width_][height_];
        for (int i = 0; i < width_; i++) {
            for (int j = 0; j < height_; j++) {
                board[i][j] = '.';
            }
        }
        
        shapes.stream().forEach((shape) -> {
            shape.getAllBlocks().stream().forEach((block) -> {
                board[block.getX()][block.getY()] = 'X';
            });
        });

        StringBuilder builder = new StringBuilder();
        for (int j = height_ - 1; j >= 0; j--) {
            for (int i = 0; i < width_; i++) {
                builder.append(board[i][j]);
            }
            builder.append(System.lineSeparator());
        }
        builder.append("Sim time: ".concat(Integer.toString(simTime_)));
        builder.append(System.lineSeparator());
        builder.append("Next shape: ".concat(nextTetrimino_.toString()));
        builder.append(System.lineSeparator());
        players_.stream().forEach((player) -> {
            builder.append("Player ".concat(player.toString()).concat(": ").concat(playerScores_.getOrDefault(player, new Score(0)).toString()));
            builder.append(System.lineSeparator());
        });
        return builder.toString();
    }

    // TODO: Use the Tetris system of pulling Tetriminos from a bag, rather 
    // than generating them over an even distribution.
    private Tetrimino getRandomTetrimino() {
        /* Randomly select a Tetrimino */
        Tetrimino[] possibleTetriminos = Tetrimino.values();
        return possibleTetriminos[random_.nextInt(possibleTetriminos.length)];
    }
}
