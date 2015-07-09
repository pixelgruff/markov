package tetris;

import java.util.Objects;
import utils.Vector2;

/**
 * Represent a single block of a Tetrimino
 * @author Ginger
 */
public class TetrisBlock {
    /* Flag indicating whether or not this block is safe to move downwards */
    private Vector2 position_;
    
    TetrisBlock(final int x, final int y) {
        position_ = new Vector2(x, y);
    }
    
    TetrisBlock(final TetrisBlock copy) {
        position_ = new Vector2(copy.position_);
    }
    
    public int getX() {
        return position_.getX();
    }
    
    public int getY() {
        return position_.getY();
    }
    
    public TetrisBlock shiftBlockByDelta(final Vector2 delta) {
        TetrisBlock newBlock = new TetrisBlock(this);
        newBlock.position_ = newBlock.position_.add(delta);
        return newBlock;
    }
    
    @Override
    public boolean equals(Object other) {
        if(other instanceof TetrisBlock)
        {
            TetrisBlock otherBlock = (TetrisBlock) other;
            return otherBlock.position_.equals(position_);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.position_);
        return hash;
    }
}
