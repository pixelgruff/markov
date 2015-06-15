package tetris;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import utils.Validate;
import utils.Vector2;

/**
 *
 * @author Ginger Represent a single, connected shape on the Tetris board.
 * The shape may or may not be a Tetrimino 
 */
public class TetrisShape {
    private ArrayList<Vector2> blocks_;
    
    public TetrisShape(final ArrayList<Vector2> blocks) {
        Validate.notNull(blocks, "Initial blocks must not be null.");
        Validate.notEmpty(blocks, "Initial blocks must not be empty.");
        blocks_ = blocks;
    }
    
    public TetrisShape(final int[] xBlocks, final int[] yBlocks) {
        Validate.isTrue(xBlocks != null && yBlocks != null
                && xBlocks.length > 0 && yBlocks.length > 0,
                "Arrays must both be non-null and of length > 0.");
        Validate.isTrue(xBlocks.length == yBlocks.length,
                "Arrays must be of equivalent length.");
        
        blocks_ = new ArrayList<Vector2>();
        for (int i = 0; i < xBlocks.length; i++) {
            blocks_.add(new Vector2(xBlocks[i], yBlocks[i]));
        }
    }
    
    /* Move a shape by some 'delta' described by a Vector2 */
    public void shiftShapeByDelta(final Vector2 delta) {
        List newBlocks = blocks_.stream()
                .map(position -> position.add(delta))
                .collect(Collectors.toList());
        
//        ArrayList<Vector2> newBlocks = new ArrayList<Vector2>();
//        blocks_.stream().forEach((position) -> newBlocks.add(position.add(delta)));
//        blocks_ = newBlocks;
    }
    
    /**
     * Create a TetrisShape from the specified Tetrimino, using the origin
     * coordinate as the bottom-left 
     * @param t
     * @param origin
     * @return 
     */
    public static TetrisShape getShapeFromTetrimino(final Tetrimino t, final Vector2 origin) {
        int[] xBlocks = null;
        int[] yBlocks = null;
        Vector2 center = null;
        
        /** 
         * Each Tetrimino has a unique shape.  Centers are defined by the 
         * mean of the x- and y-values, rounded down; think of the "center" in
         * two dimensions, snapped to the bottom-left
         * */
        switch(t) {
            case I:
                xBlocks = new int[]{ 0, 1, 2, 3 };
                yBlocks = new int[]{ 0, 0, 0, 0 };
                center = new Vector2(1, 0);
                break;
            case J:
                xBlocks = new int[]{ 0, 0, 1, 2 };
                yBlocks = new int[]{ 1, 0, 0, 0 };
                center = new Vector2(1, 0);
                break;
            case L:
                xBlocks = new int[]{ 0, 0, 1, 2 };
                yBlocks = new int[]{ 0, 0, 0, 1 };
                center = new Vector2(1, 0);
                break;
            case O:
                xBlocks = new int[]{ 0, 0, 1, 1 };
                yBlocks = new int[]{ 0, 1, 0, 1 };
                center = new Vector2(0, 0);
                break;
            case S:
                xBlocks = new int[]{ 0, 1, 1, 2 };
                yBlocks = new int[]{ 0, 0, 1, 1 };
                center = new Vector2(1, 0);
                break;
            case T:
                xBlocks = new int[]{ 0, 1, 1, 2 };
                yBlocks = new int[]{ 0, 0, 1, 0 };
                center = new Vector2(1, 0);
                break;
            case Z:
                xBlocks = new int[]{ 0, 1, 1, 2 };
                yBlocks = new int[]{ 1, 1, 0, 0 };
                center = new Vector2(1, 0);
                break;
        }
        /* Blocks and center should always have taken on some value */
        Validate.notNull(xBlocks);
        Validate.notNull(yBlocks);
        Validate.notNull(center);
        TetrisShape shape = new TetrisShape(xBlocks, yBlocks);
        
        /* Move this shape to the origin point */
        Vector2 delta = origin.subtract(center);
        shape.shiftShapeByDelta(delta);
        
        return shape;
    }
    
    @Override
    public String toString() {
        return blocks_.toString();
    }
}
