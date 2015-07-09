package tetris;

import core.Player;
import core.Rules;
import core.Score;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import utils.ClosedRange;
import utils.Validate;
import utils.Vector2;

/**
 *
 * @author Ginger Hold the logic for a basic game of Tetris.
 */
public class TetrisRules implements Rules<TetrisState, TetrisAction> {

    private final int POINTS_PER_ROW = 100;
    private final int DEFAULT_HEIGHT = 22;
    private final int DEFAULT_WIDTH = 10;
    /* Interval at which falling blocks descend, in simtime */
    private final int DESCENT_RATE = 2;
    /* Origin for spawning new Tetriminos */
    private final Vector2 tetriminoOrigin = new Vector2(DEFAULT_WIDTH / 2 - 1, DEFAULT_HEIGHT - 2);

    @Override
    /* Generate an initial state with no shapes */
    public TetrisState generateInitialState(final Collection<Player> players) {
        Validate.isTrue(players.size() == 1, "Exactly one person plays Tetris!");
        final TetrisState state = new TetrisState(players, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        /* Add the first Tetrimino */
        state.shapes.add(TetrisShape.getShapeFromTetrimino(state.popTetrimino(), tetriminoOrigin));
        return state;
    }

    @Override
    public TetrisState transition(final TetrisState state, final TetrisAction action) {
        /* Validation */
        Validate.notNull(state, "State must not be null.");
        Validate.notNull(action, "Action must not be null.");
        Validate.isFalse(state.isTerminal(),
                "Cannot make a move for a state that is already terminal.");
        System.out.printf("Taking action: %s\n", action.toString());

        TetrisState newState = new TetrisState(state);
        newState.incrementSimTime();

        /* Apply the user action to the currently controlled Tetrimino */
        newState = applyMove(newState, action);

        /* If appropriate, move falling pieces or spawn a new Tetrimino */
        /* Updates occur either on a timed delay, or after the user has dropped
         their Tetrimino, forcing an expedient update */
        if (newState.getGameTime() % DESCENT_RATE == 0
                || newState.shapes.stream().allMatch((shape) -> !shape.isControllable())) {
            /* Identify shapes that are falling */
            Set<TetrisShape> fallingShapes = getFallingShapes(newState);

            /* If no shapes are falling, add a Tetrimino; otherwise drop falling shapes */
            final Set<TetrisShape> newShapes = (fallingShapes.isEmpty())
                    ? new HashSet<TetrisShape>(Arrays.asList(
                                    TetrisShape.getShapeFromTetrimino(newState.popTetrimino(), tetriminoOrigin)
                            ))
                    /* Drop falling shapes */
                    : applyDescent(fallingShapes);
            Validate.notEmpty(newShapes,
                    "An update should always create either a new Tetrimino or a newly-descended Tetrimino.");

            /* Enumerate stationary shapes (perhaps blocks that have just landed!) */
            Set<TetrisShape> stationaryShapes = newState.shapes.stream()
                    .filter((shape) -> !fallingShapes.contains(shape))
                    .collect(Collectors.toSet());
            /* Ensure stationary shapes cannot be controlled by players */
            stationaryShapes.stream().forEach((shape) -> shape.makeUncontrollable());

            /* Update shapes in the state */
            newState.shapes = Stream.concat(
                    newShapes.stream(),
                    stationaryShapes.stream()
            ).collect(Collectors.toSet());
        }

        /* Check for completed rows, removing and scoring appropriately; 
         then collect the resulting state */
        return applyRowRemoval(newState);
    }

    @Override
    public ClosedRange<Integer> numberOfPlayers() {
        return new ClosedRange(1, 1);
    }

    @Override
    public Collection<TetrisAction> getAvailableActions(final Player player, final TetrisState state) {
        Validate.notNull(player, "Player must not be null.");
        Validate.notNull(state, "State must not be null.");
        Validate.isFalse(state.isTerminal(), "No actions available for a terminal state.");

        /* Apply each transformation in turn */
        return Arrays.asList(TetrisAction.values()).stream()
                /* Retain actions that result in valid states */
                .filter((action) -> {
                    return isStateValid(applyMove(state, action));
                }).collect(Collectors.toList());
    }

    @Override
    public boolean isTerminal(final TetrisState state) {
        /* If any blocks have come to rest in the two "hidden" rows at the top
         of the board, we consider the player to have lost */
        return (state.shapes.stream()
                .anyMatch((shape) -> (shape.getAllBlocks().stream()
                        .anyMatch((block) -> (block.getY() >= DEFAULT_HEIGHT - 2
                                && !shape.isControllable())
                        ))));
    }

    @Override
    public Player getCurrentPlayer(final TetrisState state) {
        List<Player> players = new ArrayList(state.getPlayers());
        Validate.isTrue(players.size() == 1, "Exactly one person plays Tetris.");
        return players.get(0);
    }

    @Override
    public TetrisState filterState(final TetrisState state, Player p) {
        /* No filtering necessary for Tetris */
        return state;
    }

    @Override
    public Score score(final TetrisState state, final Player p) {
        return state.getPlayerScore(p);
    }

    /**
     * Verify a state contains only valid shapes.
     * @param state
     * @return 
     */
    private boolean isStateValid(final TetrisState state) {
        TetrisShape[][] board = new TetrisShape[state.getWidth()][state.getHeight()];

        /* Store references to this shape, at the positions the shape's blocks */
        for (TetrisShape shape : state.shapes) {
            for (TetrisBlock block : shape.getAllBlocks()) {
                /* Check for invalid blocks */
                if (!state.isBlockOnBoard(block)) {
                    return false;
                }
                /* Check for collisions */
                if (board[block.getX()][block.getY()] != null) {
                    return false;
                } else {
                    board[block.getX()][block.getY()] = shape;
                }
            }
        }
        /* If all states could be placed on the board without incident, we 
         declare this state valid */
        return true;
    }

    /**
     * Apply the row-removal and scoring algorithm to a Tetris state
     *
     * @param state
     * @return
     */
    private TetrisState applyRowRemoval(final TetrisState state) {
        Validate.notNull(state, "Cannot remove rows from a null state.");

        /* Extract shapes not under user control, as shapes still in motion 
         cannot be included in row removal */
        final TetrisState scorableState = new TetrisState(state);
        scorableState.shapes = state.shapes.stream()
                .filter((shape) -> !shape.isControllable())
                .collect(Collectors.toSet());

        /* Idenfity full rows */
        List<Integer> fullRows = findFullRows(scorableState);

        while (!fullRows.isEmpty()) {
            /* Remove full rows, drop all shapes and save the result to the new state */
            scorableState.shapes = getShapesWithRowsRemoved(scorableState.shapes, fullRows);
            scorableState.shapes = applyGravityToState(scorableState).shapes;

            /* Update the player's score based on rows removed */
            int score = POINTS_PER_ROW * fullRows.size();
            scorableState.incrementPlayerScore(getCurrentPlayer(state), score);

            /* Rebuild the list of full rows */
            fullRows = findFullRows(scorableState);
        }
        
        /* Add the user-controllable shapes back into the state */
        scorableState.shapes.addAll(state.shapes.stream()
                .filter((shape) -> shape.isControllable())
                .collect(Collectors.toSet()));
        return scorableState;
    }

    /**
     * Return a copy of these shapes with any blocks in a set of rows removed,
     * and any disconnected shapes split.
     *
     * @param shapes
     * @param rows
     * @return
     */
    private Set<TetrisShape> getShapesWithRowsRemoved(final Set<TetrisShape> shapes, final List<Integer> rows) {
        return new HashSet<>(shapes).stream()
                /* Map this stream to a stream of the same shapes, with the blocks
                 at getY() in row filtered out */
                .map((shape) -> {
                    final TetrisShape newShape = new TetrisShape(
                            shape.getAllBlocks().stream()
                            .filter((block) -> !rows.contains(block.getY()))
                            .collect(Collectors.toSet())
                    );
                    return newShape;
                    /* Then map the result, allowing completely empty shapes to
                     expire and shapes with disconnected pieces to split */
                }).flatMap((shape) -> shape.splitShape().stream())
                .collect(Collectors.toSet());
    }

    /**
     * Identify which rows of a TetrisState's board are "full", meaning they
     * have values at all x-coordinates (columns) for a given y-coordinate (row)
     *
     * @param board
     * @return
     */
    private List<Integer> findFullRows(final TetrisState state) {
        Validate.notNull(state, "State must not be null.");
        /* Translate the state to a 2D board array for easier comparisons */
        final TetrisShape[][] board = getBoardFromState(state);
        Validate.isTrue(board.length > 0 && board[0].length > 0, "Board must contain rows and columns.");
        final int width = board.length;
        final int height = board[0].length;

        List<Integer> fullRows = new ArrayList<Integer>();

        /* Iterate numerically (yuk) because we need to check board(ii, :),
         which precludes efficiently streaming the board data since the relevant
         values are not contiguous */
        for (int row = 0; row < height; row++) {
            boolean hasFoundEmpty = false;
            for (int column = 0; column < width; column++) {
                if (board[column][row] == null) {
                    hasFoundEmpty = true;
                    break;
                }
            }
            if (!hasFoundEmpty) {
                fullRows.add(row);
            }
        }
        return fullRows;
    }

    /**
     * Apply a movement to the user-controllable Tetrimino on the board.
     *
     * @param state
     * @param action
     * @return A new state, with only the user-controllable shape transformed.
     */
    private TetrisState applyMove(final TetrisState state, final TetrisAction action) {
        Validate.notNull(state, "Cannot apply moves to a null state.");
        Validate.notNull(action, "Cannot apply null actions.");
        /* Identify and remove the user-controllable shape */
        final TetrisState newState = new TetrisState(state);
        final List<TetrisShape> controllableShapes = newState.shapes.stream()
                .filter((shape) -> shape.isControllable())
                /* Could use findAny(), but this provides a convenient place to
                 check our assumptions */
                .collect(Collectors.toList());
        Validate.notEmpty(controllableShapes, "No user-controllable shape found.");
        Validate.isTrue(controllableShapes.size() == 1, "Multiple user-controllable shapes found.");

        /* Extract the shape for convenient transformation */
        newState.shapes.removeAll(controllableShapes);
        final TetrisShape shape = controllableShapes.get(0);

        /* Transform the shape according to the action */
        final TetrisShape newShape;
        switch (action) {
            case LEFT:
                newShape = shape.getShiftedLeft();
                break;
            case RIGHT:
                newShape = shape.getShiftedRight();
                break;
            case ROTATE:
                /* Only clockwise rotation is supported */
                newShape = shape.getRotatedClockwise();
                break;
            case DROP:
                newShape = applyDropToShape(state, shape);
                newShape.makeUncontrollable();
                break;
            default:
                newShape = null;
        }

        /* Add the shape back into the state */
        Validate.notNull(newShape, "Shape user actions should never create a null shape on the board.");
        newState.shapes.add(newShape);

        return newState;
    }

    /**
     * Get a version of this shape, dropped as far as possible given the
     * provided state. The original state and shape are not modified.
     *
     * @param shape
     * @return
     */
    private TetrisShape applyDropToShape(final TetrisState state, final TetrisShape shape) {
        Validate.notEmpty(shape.getAllBlocks(), "Cannot drop an empty shape.");
        /* Find the distance between this shape's blocks and the ground, and
         move a safe distance downard */
        final TetrisShape[][] board = getBoardFromState(state);
        final int safeDistance = shape.getAllBlocks().stream()
                /* Measure from each block down to the bottom of the
                 board or another block */
                .map((block) -> {
                    final int column = block.getX();
                    int row = block.getY();
                    /* Iterate downwards, looking for the edge of the 
                     board or another block */
                    while (row > 0
                    && (board[column][row - 1] == null
                    || board[column][row - 1] == shape)) {
                        row--;
                    }
                    return block.getY() - row;
                }).min(Integer::compareTo).get();

        return shape.getDroppedByDelta(safeDistance);
    }

    /**
     * Move all shapes down a single block. Does not handle row removal.
     *
     * @param shapes Shapes to be moved.
     * @return All shapes, moved downwards by a single block.
     */
    private Set<TetrisShape> applyDescent(final Set<TetrisShape> shapes) {
        /* Drop shapes that have not landed */
        return shapes.stream()
                .map(shape -> shape.getDescended())
                .collect(Collectors.toSet());
    }

    /**
     * Return a copy of this state, with all states dropped to the bottom of the
     * board.
     *
     * @param shapes
     * @return
     */
    private TetrisState applyGravityToState(final TetrisState state) {
        Validate.notNull(state, "State must not be null.");
        
        // TODO: There must be a way to sort the shapes so that the lowest
        // uninhibited shapes can drop first, instead of this hideous while 
        // loop... but I'm not going to try to find it right now
        final TetrisState newState = new TetrisState(state);
        Set<TetrisShape> oldShapes = null;
        while (!newState.shapes.equals(oldShapes)) {
            oldShapes = newState.shapes;
            newState.shapes = newState.shapes.stream()
                .map((shape) -> applyDropToShape(newState, shape))
                .collect(Collectors.toSet());
        }
        
        return newState;
    }

    /**
     * Manufacture a 2D board from a state's shapes.
     *
     * @param shapes
     * @return
     */
    private TetrisShape[][] getBoardFromState(final TetrisState state) {
        TetrisShape[][] board = new TetrisShape[state.getWidth()][state.getHeight()];

        /* Store references to this shape, at the positions the shape's blocks */
        state.shapes.stream().forEach((shape) -> {
            shape.getAllBlocks().stream().forEach((block) -> {
                /* Check for collisions */
                Validate.isNull(board[block.getX()][block.getY()],
                        "Block collision during update; no two shapes should share a block!");
                board[block.getX()][block.getY()] = shape;
            });
        });

        return board;
    }

    /**
     * Identify every shape on the Tetris board that would not collide if moved,
     * and can be allowed to fall
     *
     * @param board
     * @param width
     * @param height
     * @return
     */
    private Set<TetrisShape> getFallingShapes(final TetrisState state) {
        Validate.notNull(state, "Cannot find falling shapes in a null state.");

        /* Project the shapes onto a 2D board to simplify comparisons in 2D space */
        TetrisShape[][] board = getBoardFromState(state);
        Validate.isTrue(board.length > 0 && board[0].length > 0, "Board must contain rows and columns.");

        /* Return all shapes that are "falling", meaning... */
        return state.shapes.stream()
                /* For every shape... */
                .filter((shape) -> shape.getAllBlocks().stream()
                        /* For every block in the shape... */
                        .allMatch((block) -> {
                            /* That block must not be on the bottom row... */
                            if (block.getY() > 0) {
                                /* And the block below it must either be
                                 a part of the same shape, or empty */
                                TetrisShape belowShape = board[block.getX()][block.getY() - 1];
                                if (belowShape == shape || belowShape == null) {
                                    return true;
                                }
                            }
                            return false;
                        })
                ).collect(Collectors.toSet());
    }
}
