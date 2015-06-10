package core;


/**
 * Actions are simple Object wrappers.
 * 
 * If at all possible, implementations should be immutable. That is, after
 * object construction, their properties should not be able to be modified by
 * any public method.
 * 
 * Generally, Actions should describe the "diff" in state that a Player 
 * would make by taking one. For example, in Chess, a ChessAction could 
 * be defined as something like:
 * 
 * class ChessTile 
 * {
 *      public ChessPiece getPiece();
 *      public Vector2 getPosition();
 * }
 * 
 * class ChessAction implements Action
 * {
 *      public ChessTile currentTile();
 *      public ChessTile potentialTile(); 
 * }
 * 
 *
 */
public interface Action
{

}
