
package tetris;

import utils.Vector2;

/**
 * A simple class to hold the (sometimes non-integer) center point for a TetrisShape.
 * @author Ginger
 */
public class ShapeCenter {
    private final double x_, y_;
    
    ShapeCenter(final double x, final double y) {
        x_ = x;
        y_ = y;
    }
    
    /**
     * Convenience constructor for deep-copying centers
     * @param copy 
     */
    ShapeCenter(final ShapeCenter copy) {
        x_ = copy.x_;
        y_ = copy.y_;
    }
    
    /**
     * Returns a new instance of this ShapeCenter, shifted by a given delta.
     * The original object is not changed.
     * @param delta
     * @return 
     */
    public ShapeCenter shiftCenterByDelta(final Vector2 delta) {
        return new ShapeCenter(x_ + delta.getX(), y_ + delta.getY());
    }
    
    public double getX() {
        return x_;
    }
    
    public double getY() {
        return y_;
    }
}
