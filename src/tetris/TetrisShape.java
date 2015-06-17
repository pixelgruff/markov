package tetris;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import utils.Validate;
import utils.Vector2;

/**
 * @author Ginger Represent a single, connected shape on the Tetris board. The
 *         shape may or may not be a Tetrimino
 */
public class TetrisShape
{

    /* Keep track of every block in this shape */
    private Collection<Vector2> blocks_;

    public TetrisShape(final List<Vector2> blocks)
    {
        Validate.notNull(blocks, "Initial blocks must not be null.");
        Validate.notEmpty(blocks, "Initial blocks must not be empty.");
        blocks_ = blocks;
    }

    /**
     * Convenience constructor that allows building shapes from x- and y- arrays
     *
     * @param xBlocks
     * @param yBlocks
     */
    public TetrisShape(final int[] xBlocks, final int[] yBlocks)
    {
        Validate.isTrue(xBlocks != null && yBlocks != null && xBlocks.length > 0
                && yBlocks.length > 0, "Arrays must both be non-null and of length > 0.");
        Validate.isTrue(xBlocks.length == yBlocks.length, "Arrays must be of equivalent length.");

        blocks_ = new ArrayList<Vector2>();
        for(int i = 0; i < xBlocks.length; i++)
        {
            blocks_.add(new Vector2(xBlocks[i], yBlocks[i]));
        }
    }

    /**
     * Create a TetrisShape from the specified Tetrimino, using the origin
     * coordinate as the bottom-left
     *
     * @param t
     * @param origin
     * @return
     */
    public static TetrisShape getShapeFromTetrimino(final Tetrimino t, final Vector2 origin)
    {
        int[] xBlocks = null;
        int[] yBlocks = null;
        Vector2 center = null;

        /**
         * Each Tetrimino has a unique shape. Centers are defined by the mean of
         * the x- and y-values, rounded down; think of the "center" in two
         * dimensions, snapped to the bottom-left
         */
        switch(t)
        {
        case I:
            xBlocks = new int[] { 0, 1, 2, 3 };
            yBlocks = new int[] { 0, 0, 0, 0 };
            center = new Vector2(1, 0);
            break;
        case J:
            xBlocks = new int[] { 0, 0, 1, 2 };
            yBlocks = new int[] { 1, 0, 0, 0 };
            center = new Vector2(1, 0);
            break;
        case L:
            xBlocks = new int[] { 0, 0, 1, 2 };
            yBlocks = new int[] { 0, 0, 0, 1 };
            center = new Vector2(1, 0);
            break;
        case O:
            xBlocks = new int[] { 0, 0, 1, 1 };
            yBlocks = new int[] { 0, 1, 0, 1 };
            center = new Vector2(0, 0);
            break;
        case S:
            xBlocks = new int[] { 0, 1, 1, 2 };
            yBlocks = new int[] { 0, 0, 1, 1 };
            center = new Vector2(1, 0);
            break;
        case T:
            xBlocks = new int[] { 0, 1, 1, 2 };
            yBlocks = new int[] { 0, 0, 1, 0 };
            center = new Vector2(1, 0);
            break;
        case Z:
            xBlocks = new int[] { 0, 1, 1, 2 };
            yBlocks = new int[] { 1, 1, 0, 0 };
            center = new Vector2(1, 0);
            break;
        default:
            throw new IllegalArgumentException("Tetrimino not found.");
        }
        /* Blocks and center should always have taken on some value */
        Validate.notNull(xBlocks);
        Validate.notNull(yBlocks);
        Validate.notNull(center);
        final TetrisShape shape = new TetrisShape(xBlocks, yBlocks);

        /* Move this shape to the origin point */
        final Vector2 delta = origin.subtract(center);
        shape.shiftShapeByDelta(delta);

        return shape;
    }

    /* Move a shape by some 'delta' described by a Vector2 */
    public void shiftShapeByDelta(final Vector2 delta)
    {
        blocks_ = blocks_.stream().map(position -> position.add(delta))
                .collect(Collectors.toList());
    }

    /* Shift the Shape downward one block */
    public void drop()
    {
        shiftShapeByDelta(new Vector2(0, -1));
    }

    /**
     * Merge two shapes and return the result. Leaves both original shapes
     * unchanged.
     *
     * @param shape1
     * @param shape2
     * @return
     */
    public static TetrisShape merge(final TetrisShape shape1, final TetrisShape shape2)
    {
        Validate.notNull(shape1, "First shape must not be null.");
        Validate.notNull(shape2, "Second shape must not be null.");
        Validate.notEmpty(shape1.blocks_, "First shape must not be null.");
        Validate.notEmpty(shape2.blocks_, "Second shape must not be null.");
        /* Shapes must not contain any overlapping blocks */
        Validate.isFalse(shape1.blocks_.stream().anyMatch(block -> shape2.blocks_.contains(block)));
        /* Merge the blocks */
        return new TetrisShape(Stream.concat(shape1.blocks_.stream(), shape2.blocks_.stream())
                .collect(Collectors.toList()));
    }

    /* Check if any of this shape's blocks are at floor level */
    public boolean isOnFloor()
    {
        return blocks_.stream().anyMatch(block -> block.getY() == 0);
    }

    public List<Vector2> getAllBlocks()
    {
        return new ArrayList<>(blocks_);
    }

    @Override
    public String toString()
    {
        return blocks_.toString();
    }
}
