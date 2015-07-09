package tetris;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import utils.Validate;
import utils.Vector2;

/**
 *
 * @author Ginger Represent a single, connected shape on the Tetris board. The
 * shape may or may not be a Tetrimino
 */
public class TetrisShape {

    /* Keep track of every block in this shape */
    private Set<TetrisBlock> blocks_;
    /* Identify a Tetrimino under player control */
    private boolean isControllable_ = true;
    /* Keep track of this shape's center */
    private ShapeCenter center_;
    /* Initialize delta vectors for moving in 2D space */
    private final static List<Vector2> deltaVectors = new ArrayList<Vector2>(4);
    static {
        deltaVectors.add(new Vector2(-1, 0));
        deltaVectors.add(new Vector2(1, 0));
        deltaVectors.add(new Vector2(0, -1));
        deltaVectors.add(new Vector2(0, 1));
    }

    public TetrisShape(final Set<TetrisBlock> blocks) {
        Validate.notNull(blocks, "Initial blocks must not be null.");
        Validate.notEmpty(blocks, "Initial blocks must not be empty.");
        blocks_ = blocks;
    }

    /**
     * Convenience constructor that allows building shapes from x- and y- arrays
     * @param xBlocks
     * @param yBlocks
     * @param center
     */
    public TetrisShape(final int[] xBlocks, final int[] yBlocks, final ShapeCenter center) {
        Validate.isTrue(xBlocks != null && yBlocks != null
                && xBlocks.length > 0 && yBlocks.length > 0,
                "Arrays must both be non-null and of length > 0.");
        Validate.isTrue(xBlocks.length == yBlocks.length,
                "Arrays must be of equivalent length.");
        Validate.notNull(center, "Shape must have a center.");
        
        center_ = center;
        blocks_ = new HashSet<TetrisBlock>();
        for (int i = 0; i < xBlocks.length; i++) {
            blocks_.add(new TetrisBlock(xBlocks[i], yBlocks[i]));
        }
    }

    /**
     * Convenience constructor for deep-copying shapes
     * @param copy 
     */
    public TetrisShape(final TetrisShape copy) {
        Validate.notNull(copy, "Shape to copy must not be null.");
        blocks_ = copy.getAllBlocks();
        isControllable_ = copy.isControllable_;
        center_ = new ShapeCenter(copy.center_);
    }
    
    /**
     * Create a TetrisShape from the specified Tetrimino, using the origin
     * coordinate as the bottom-left
     *
     * @param t
     * @param origin
     * @return
     */
    public static TetrisShape getShapeFromTetrimino(final Tetrimino t, final Vector2 origin) {
        int[] xBlocks = null;
        int[] yBlocks = null;
        ShapeCenter center = null;

        /* Each Tetrimino has a unique shape */
        switch (t) {
            case I:
                xBlocks = new int[]{0, 1, 2, 3};
                yBlocks = new int[]{0, 0, 0, 0};
                center = new ShapeCenter(1.5, -0.5);
                break;
            case J:
                xBlocks = new int[]{0, 0, 1, 2};
                yBlocks = new int[]{1, 0, 0, 0};
                center = new ShapeCenter(1.0, 0.0);
                break;
            case L:
                xBlocks = new int[]{0, 1, 2, 2};
                yBlocks = new int[]{0, 0, 0, 1};
                center = new ShapeCenter(1.0, 0.0);
                break;
            case O:
                xBlocks = new int[]{0, 0, 1, 1};
                yBlocks = new int[]{0, 1, 0, 1};
                center = new ShapeCenter(0.5, 0.5);
                break;
            case S:
                xBlocks = new int[]{0, 1, 1, 2};
                yBlocks = new int[]{0, 0, 1, 1};
                center = new ShapeCenter(1.0, 0.0);
                break;
            case T:
                xBlocks = new int[]{0, 1, 1, 2};
                yBlocks = new int[]{0, 0, 1, 0};
                center = new ShapeCenter(1.0, 0.0);
                break;
            case Z:
                xBlocks = new int[]{0, 1, 1, 2};
                yBlocks = new int[]{1, 1, 0, 0};
                center = new ShapeCenter(1.0, 0.0);
                break;
            default:
                throw new IllegalArgumentException("Tetrimino not found.");
        }
        
        /* Blocks and center should always have taken on some value */
        Validate.notNull(xBlocks);
        Validate.notNull(yBlocks);
        Validate.notNull(center);
        TetrisShape shape = new TetrisShape(xBlocks, yBlocks, center);

        /* Move this shape to the origin point */
        /* Casting the center to an int "snaps" shapes to the bottom-left */
        Vector2 delta = origin.subtract(new Vector2((int) center.getX(), (int) center.getY()));
        shape.shiftShapeByDelta(delta);

        return shape;
    }
    
    /**
     * Determine whether or not a shape has any block at a particular set of coordinates
     * @param x
     * @param y
     * @return 
     */
    public boolean hasBlockAtCoords(final int x, final int y) {
        return blocks_.stream().anyMatch((block) -> block.getX() == x && block.getY() == y);
    }
    
    /**
     * Return a version of this shape, turned clockwise; 
     * the original shape is unchanged.
     * @return 
     */
    public TetrisShape getRotatedClockwise() {
        TetrisShape newShape = new TetrisShape(this);
        /** 
         * Clockwise math works as follows:
         * Calculate the delta between every block and the center of the shape.
         * Then replace that block with a block at coordinates centered around
         * the shape's center, offset by the calculated deltas.
         * Rotation is accomplished by using the y-delta to specify the new 
         * block's x-coordinate, and vice-versa.
         * This is a trivial case of https://en.wikipedia.org/wiki/Rotation_matrix,
         * where cos(theta) == 0 and sin(theta) == 1 and -1
         * (This did not make it easier to understand)
         */
        newShape.blocks_ = newShape.getAllBlocks().stream()
                .map((block) -> {
                    final double deltaX = newShape.center_.getX() - block.getX();
                    final double deltaY = newShape.center_.getY() - block.getY();
                    return new TetrisBlock(
                            (int) Math.round(newShape.center_.getX() - deltaY), 
                            (int) Math.round(newShape.center_.getY() + deltaX));
                }).collect(Collectors.toSet());
        return newShape;
    }
    
    /**
     * Return a version of this shape, shifted left; 
     * the original shape is unchanged.
     * @return 
     */
    public TetrisShape getShiftedLeft() {
        TetrisShape newShape = new TetrisShape(this);
        newShape.shiftShapeByDelta(new Vector2(-1, 0));
        return newShape;
    }
    
    /**
     * Return a version of this shape, shifted right; 
     * the original shape is unchanged.
     * @return 
     */
    public TetrisShape getShiftedRight() {
        TetrisShape newShape = new TetrisShape(this);
        newShape.shiftShapeByDelta(new Vector2(1, 0));
        return newShape;
    }

    /**
     * Return a version of this shape, shifted downward; 
     * the original shape is unchanged.
     * @return 
     */
    public TetrisShape getDescended() {
        TetrisShape newShape = new TetrisShape(this);
        newShape.shiftShapeByDelta(new Vector2(0, -1));
        return newShape;
    }
    
    /**
     * Return a version of this shape, shifted downward by a prescribed delta;
     * the original shape is unchanged.
     * @param delta
     * @return 
     */
    public TetrisShape getDroppedByDelta(final int delta) {
        Validate.isTrue(delta >= 0, "Shape should not be 'dropped' by negative distances.");
        
        TetrisShape newShape = new TetrisShape(this);
        newShape.shiftShapeByDelta(new Vector2(0, -delta));
        return newShape;
    }
    
    /**
     * Identify the shape under user control.
     * @return 
     */
    public boolean isControllable() {
        return isControllable_;
    }
    
    /**
     * Flag a shape as no longer under user control.
     */
    public void makeUncontrollable() {
        isControllable_ = false;
    }
    
    /* Move a shape by some 'delta' described by a Vector2 */
    private void shiftShapeByDelta(final Vector2 delta) {
        blocks_ = blocks_.stream()
                .map(block -> block.shiftBlockByDelta(delta))
                .collect(Collectors.toSet());
        /* Shifting a center (double precision) by a Vector2 (integer precision)
        means we need to think carefully about any comparisons and equality checks
        we do.  :-(  Currently the only concern (moving Tetriminos to valid
        positions) is handled by rounding and then casting to Int before sending
        a block somewhere.  Is there a more elegant way to handle this problem? */
        center_ = center_.shiftCenterByDelta(delta);
    }

    public Set<TetrisBlock> getAllBlocks() {
        return blocks_;
    }

    @Override
    public String toString() {
        return blocks_.toString();
    }
    
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof TetrisShape))
        {
            return false;
        }

        @SuppressWarnings("rawtypes")
        final TetrisShape otherShape = (TetrisShape) other;
        return otherShape.blocks_.equals(blocks_);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.blocks_);
        return hash;
    }
    
    /**
     * Convert a shape, potentially empty or disconnected, into a set of 
     * connected shapes.
     * @return 
     */
    public Set<TetrisShape> splitShape() {
        return getAllAdjacentBlocks(blocks_).stream()
                .map((neighborhood) -> {
                    TetrisShape shape = new TetrisShape(neighborhood);
                    shape.isControllable_ = isControllable_;
                    /* Centers can no longer be safely assigned to split shapes */
                    shape.center_ = null;
                    return shape;
                }).collect(Collectors.toSet());
    }
    
    /**
     * Map a shape's blocks to (potentially) multiple sets of adjacent blocks.
     * @param blocks
     * @return 
     */
    private Set<Set<TetrisBlock>> getAllAdjacentBlocks(final Set<TetrisBlock> blocks) {
        Validate.notNull(blocks, "Cannot find adjacent blocks for a null set.");
        Validate.notEmpty(blocks, "Cannot find adjacent blocks for an empty set.");
        
        /* Copy the set to avoid hidden changes to the argument */
        final Set<TetrisBlock> currentBlocks = new HashSet<TetrisBlock>(blocks);
        /* Prepare to save all the neighborhoods */
        final Set<Set<TetrisBlock>> blockNeighborhoods = new HashSet<>();
        
        while (!currentBlocks.isEmpty()) {
            /* Get an arbitrary starting block to begin searching */
            final TetrisBlock startingBlock = currentBlocks.stream().findAny().get();
            final Set<TetrisBlock> neighborhood = getAllConnected(currentBlocks, startingBlock);
            /* Save this neighborhood of blocks (a contiguous shape) */
            blockNeighborhoods.add(neighborhood);
            /* Remove these blocks from the search space */
            currentBlocks.removeAll(neighborhood);
        }
        
        return blockNeighborhoods;
    }
    
    /**
     * Recursive DFS-like collection search, returning all blocks adjacent to a 
     * given block.
     * @param allBlocks
     * @param currentBlock
     * @return 
     */
    private Set<TetrisBlock> getAllConnected(final Set<TetrisBlock> allBlocks, final TetrisBlock currentBlock) {
        final HashSet<TetrisBlock> connected = new HashSet<TetrisBlock>();
        connected.add(currentBlock);
        deltaVectors.stream()
                /* For each cardinal direction... */
                .forEach((delta) -> {
                    /* Find any block one step in this direction... */
                    Optional<TetrisBlock> matchingBlock = allBlocks.stream()
                    .filter((block) -> block.getX() == block.getX() + delta.getX()
                            && block.getY() == block.getY() + delta.getY())
                    .findAny();
                    /* If you found one, add him, and ask him to look for any neighbors to add as well. */
                    if (matchingBlock.isPresent()) {
                        connected.addAll(getAllConnected(allBlocks, matchingBlock.get()));
                    }
                });
        return connected;
    }
}
