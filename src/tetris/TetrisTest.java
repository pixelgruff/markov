package tetris;

/**
 *
 * @author Ginger Test the basics of the Tetris game.
 */
public class TetrisTest {
    public static void main(String[] args) {
        /* Test the creation of Tetris shapes */
        TetrisShape t = TetrisShape.getShapeFromTetrimino(Tetrimino.I, new utils.Vector2(3, 10));
        System.out.println(t);
    }
}
