package tetris;

import core.Player;
import core.Rules;
import core.Score;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import utils.ClosedRange;
import utils.Validate;
import utils.Vector2;

/**
 *
 * @author Ginger Hold the logic for a basic game of Tetris.
 */
public class TetrisRules implements Rules<TetrisState, TetrisAction> {

    private final int DEFAULT_HEIGHT = 25;
    private final int DEFAULT_WIDTH = 10;
    /* Interval at which falling blocks descend, in simtime */
    private final int DESCENT_RATE = 2; 
    
    @Override
    /* Generate an initial state with no shapes */
    public TetrisState generateInitialState(final Collection<Player> players) {
        return new TetrisState(players, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    @Override
    public TetrisState transition(final TetrisState state, final TetrisAction action) {
        /* Validation */
        Validate.notNull(state, "State must not be null.");
        Validate.notNull(action, "Action must not be null.");
        Validate.isFalse(state.isTerminal(),
                "Cannot make a move for a state that is already terminal.");
        
        final TetrisState newState = new TetrisState(state);
        /* If appropriate, move falling pieces */
        if (newState.getGameTime() % DESCENT_RATE == 0) {
            dropTetriminos(newState);
            /* Identify and merge contiguous shapes */
            List<TetrisShape> stackedShapes = findStackedShapes(newState);
            // TODO: MERGE
        }
        /* Identify and mark shapes on the floor */
        
        return newState;
    }

    @Override
    public ClosedRange<Integer> numberOfPlayers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<TetrisAction> getAvailableActions(final Player player, final TetrisState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isTerminal(final TetrisState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Player getCurrentPlayer(final TetrisState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TetrisState filterState(final TetrisState state, Player p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Score score(final TetrisState state, final Player p) {
        return state.getPlayerScore(p);
    }
    
    /**
     * Lower all the 'falling' Tetriminos in the state.
     * Handles collisions without moving pieces into invalid states, but does
     * not trigger terminal conditions, scoring, row removal, merging.
     * @param state 
     */
    private void dropTetriminos(final TetrisState state) {
        /* All shapes should either be on the floor or floating */
        state.getAllShapes().stream()
                .filter((shape) -> (!shape.isOnFloor()))
                .forEach((shape) -> shape.drop()); 
    }
    
    /**
     * Merge all shapes stacked on top of one another; the bottom state should
     * always be on the floor
     * @param state 
     */
    private List<TetrisShape> findStackedShapes(final TetrisState state) {
        List<TetrisShape> stacked = new LinkedList<TetrisShape>();
        for (TetrisShape shape1 : state.getAllShapes()) {
            if (shape1.isOnFloor()) continue;
            for (TetrisShape shape2 : state.getAllShapes()) {
                /* Check if the first shape is above the second shape, and contiguous */
                if (shape2.isOnFloor()) {
                    /* Identify all the x-values this shape contains */
                    List<Integer> xValues = shape1.getAllBlocks().stream()
                            .mapToInt(block -> block.getX())
                            .boxed().collect(Collectors.toList());
                    /* Check each column for stacked shapes */
                    for (Integer xValue : xValues) {
                        if (lowestBlockInShapeAtX(shape1, xValue)
                                .equals(highestBlockInShapeAtX(shape2, xValue)
                                        .add(new Vector2(0, 1))
                                )) {
                            stacked.add(shape1);
                            stacked.add(shape2);
                        }
                    }
                }
            }
        }
        return stacked;
    }
    
    /**
     * Find the lowest block in a shape (minimum y-value)
     * @param shape
     * @param x
     * @return 
     */
    private Vector2 lowestBlockInShapeAtX(final TetrisShape shape, final int x) {
        Validate.notNull(shape, "Shape must not be null.");
        Optional<Vector2> lowest = shape.getAllBlocks().stream()
                .filter(block -> block.getX() == x)
                .min((block1, block2) -> Integer.compare(block1.getY(), block2.getY()));
        Validate.isTrue(lowest.isPresent(), 
                String.format("There should always be some lowest block at X = %d.", x));
        return lowest.get();
    }

    /**
     * Find the highest block in a shape (maximum y-value)
     * @param shape
     * @param x
     * @return 
     */
    private Vector2 highestBlockInShapeAtX(final TetrisShape shape, final int x) {
        Validate.notNull(shape, "Shape must not be null.");
        Optional<Vector2> lowest = shape.getAllBlocks().stream()
                .filter(block -> block.getX() == x)
                .max((block1, block2) -> Integer.compare(block1.getY(), block2.getY()));
        Validate.isTrue(lowest.isPresent(), 
                String.format("There should always be some highest block at X = %d.", x));
        return lowest.get();
    }
    
}
