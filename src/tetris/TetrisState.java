package tetris;

import core.Player;
import core.Score;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.ClosedRange;
import utils.Validate;
import utils.Vector2;

/**
 *
 * @author Ginger Represent the Tetris board at a particular moment
 */
public class TetrisState {

    /* Keep track of simulation time */
    private int simTime_;
    /* Board dimensions; would be final but for clashes with Jackson */
    private int width_, height_;
    /* Keep track of player scores */
    private Map<Player, Score> playerScores_;
    /* Keep track of shapes on the board */
    private List<TetrisShape> shapes_;
    /* Flag set if this state is terminal */
    private boolean terminal_ = false;
    /* Range of x- and y-values */
    private ClosedRange xRange_, yRange_;

    /* Default constructor for Jackson */
    public TetrisState() {
    }

    /**
     * Constructor for setting a standard State with no shapes
     *
     * @param players
     * @param width
     * @param height
     */
    public TetrisState(Collection<Player> players, final int width, final int height) {
        simTime_ = 0;
        width_ = width;
        height_ = height;
        shapes_ = new ArrayList<TetrisShape>();

        playerScores_ = new HashMap<Player, Score>(players.size());
        xRange_ = new ClosedRange(0, width - 1);
        yRange_ = new ClosedRange(0, height - 1);
    }

    public TetrisState(final TetrisState copy) {
        simTime_ = copy.simTime_;
        width_ = copy.width_;
        height_ = copy.height_;
        shapes_ = copy.shapes_;

        playerScores_ = new HashMap<Player, Score>(copy.playerScores_);
        xRange_ = new ClosedRange(0, copy.width_ - 1);
        yRange_ = new ClosedRange(0, copy.height_ - 1);
    }

    public boolean isBlockOnBoard(final Vector2 block) {
        Validate.notNull(block, "Block to validate must not be null.");
        return xRange_.isValueWithin(block.getX()) && yRange_.isValueWithin(block.getY());
    }

    public List<TetrisShape> getAllShapes() {
        return shapes_;
    }

    public Score getPlayerScore(final Player player) {
        return playerScores_.getOrDefault(player, new Score());
    }

    public void makeTerminal() {
        terminal_ = true;
    }

    public boolean isTerminal() {
        return terminal_;
    }

    public int getGameTime() {
        return simTime_;
    }
}
