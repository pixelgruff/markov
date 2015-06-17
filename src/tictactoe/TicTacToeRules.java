package tictactoe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import utils.ClosedRange;
import utils.Validate;
import utils.Vector2;
import core.Player;
import core.Rules;
import core.Score;

/**
 * @author Ginger Provides operations for updating the game, finding available
 *         moves, and finding a n-in-a-row (3-in-a-row by default).
 */
public class TicTacToeRules implements Rules<TicTacToeState, TicTacToeAction>
{
    /*
     * Number of marks in a row that a Player must have in order to consider
     * having won
     */
    private static final int N_IN_A_ROW = 3;
    private static final int NUMBER_OF_PLAYERS = 2;

    /* The default Mark that starts the game */
    private static final TicTacToeMark MARK_GOES_FIRST = TicTacToeMark.X;

    /*
     * Generate unit vectors to point towards every possible direction that we
     * can "walk" along the TicTacToeState. These "paths" are unique lines
     * representing the x axis, the y axis, the line y = x, and the line y = -x
     */
    private static final int ITERABLE_PATH_COUNT = 4;
    private static final int INVERSE = -1;
    private static final Collection<Vector2> PATHS = new ArrayList<Vector2>(ITERABLE_PATH_COUNT);

    /*
     * Initialize PATHS with all paths (size == ITERABLE_PATH_COUNT). Outside of
     * this, PATHS should never be modified
     */
    static
    {
        for(int dX = -1; dX <= 1; ++dX)
        {
            for(int dY = -1; dY <= 1; ++dY)
            {
                if(dX == 0 && dY == 0)
                {
                    /*
                     * These directions specify actual "movement", which a
                     * 0-direction vector does not have...
                     */
                    continue;
                }

                final Vector2 unitVector = new Vector2(dX, dY);
                /*
                 * Only append if we don't already contain our inverse (vector
                 * for the same line)
                 */
                if(!PATHS.contains(unitVector.multiply(INVERSE)))
                {
                    PATHS.add(unitVector);
                }
            }
        }
    }

    @Override
    public Player getCurrentPlayer(final TicTacToeState state)
    {
        Validate.notNull(state, "Null states have no players.");
        /* Check for the initial state */
        if(state.getCurrentPlayer() == null)
        {
            Validate.isTrue(state.getNumberOfActionsTaken() == 0,
                    "State has a nonzero number of actions in its history, but a null player!");
            state.setCurrentPlayer(state.getPlayer(MARK_GOES_FIRST));
        }
        return state.getCurrentPlayer();
    }

    @Override
    /* Filtering states is easy in a game with perfect information */
    public TicTacToeState filterState(final TicTacToeState state, final Player player)
    {
        return state;
    }

    /* Mark the board and check for a winner */
    @Override
    public TicTacToeState transition(final TicTacToeState state, final TicTacToeAction action)
    {
        Validate.notNull(state, "Cannot apply an action to a null TicTacToeBoard.");
        Validate.notNull(action, "Cannot apply a null action to a TicTacToeBoard.");
        Validate.isFalse(state.isTerminal(),
                "Cannot make a move for a state that is already terminal.");

        /* Copy the old state over */
        final TicTacToeState newState = new TicTacToeState(state);
        /* Mark the board */
        newState.setMarkForPosition(action.getPosition(), action.getMark());
        /* Check to see if this move won the game */
        if(isWinningMove(newState, action))
        {
            /* Update this player's score */
            newState.setScore(newState.getPlayer(action.getMark()), new Score(1.0));
            newState.makeTerminal();
        }
        /* Check to see if this move filled the board */
        if(newState.getNumberOfActionsTaken() >= newState.getTotalPossibleMoves())
        {
            newState.makeTerminal();
        }
        /* Update the "next player"; this only works with X-O games */
        if(newState.isTerminal())
        {
            newState.setCurrentPlayer(null);
        }
        else
        {
            newState.setCurrentPlayer(newState.getPlayer((action.getMark() == TicTacToeMark.X) ? TicTacToeMark.O
                    : TicTacToeMark.X));
        }

        return newState;
    }

    @Override
    /**
     * Find all the unmarked spaces on the board and return them as a list of
     * Actions with the appropriate mark
     */
    public Collection<TicTacToeAction> getAvailableActions(final Player player,
            final TicTacToeState state)
    {
        Validate.notNull(player, "Null players should not be requesting actions.");
        Validate.notNull(state, "Null states have no available actions.");
        Validate.isTrue(!state.isTerminal(), "Terminal states cannot have actions taken on them.");

        /* Grab all positions that don't currently have a mark (null mapping) */
        final Collection<Vector2> emptyPositions = state.getBoardAsMap().keySet().stream()
                .filter((position) -> state.getMarkForPosition(position) == null)
                .collect(Collectors.toList());
        final TicTacToeMark mark = state.getMarkForPlayer(player);
        /* ...and turn them into actions */
        return emptyPositions.stream().map(position -> new TicTacToeAction(position, mark))
                .collect(Collectors.toList());
    }

    @Override
    /**
     * We assume that we have done a good job of marking states as terminal if
     * they ever contain a winning move or fill up all their board positions
     */
    public boolean isTerminal(final TicTacToeState state)
    {
        Validate.notNull(state, "Null states cannot be terminal (or anything).");
        return state.isTerminal();
    }

    @Override
    public Score score(final TicTacToeState state, final Player player)
    {
        Validate.notNull(state, "Null states have no scores.");
        Validate.notNull(player, "Null players have no score.");
        /* Catch null scores from players that have never scored a point */
        return state.getPlayerScore(player);
    }

    private boolean isWinningMove(final TicTacToeState state, final TicTacToeAction action)
    {
        final Vector2 position = action.getPosition();
        for(final Vector2 path : PATHS)
        {
            int totalPathLength = 1;
            /*
             * Walk in both directions until we hit the end of the board or we
             * hit a mark that isn't ours
             */
            final Vector2[] directions = { path, path.multiply(INVERSE) };
            for(final Vector2 direction : directions)
            {
                for(Vector2 currentPosition = position.add(direction); state
                        .isPositionWithinBoard(currentPosition)
                        && state.getMarkForPosition(currentPosition) == action.getMark(); currentPosition = currentPosition
                        .add(direction))
                {
                    ++totalPathLength;
                }
            }
            if(totalPathLength >= N_IN_A_ROW)
            {
                return true;
            }

        }
        return false;
    }

    @Override
    public ClosedRange<Integer> numberOfPlayers()
    {
        return new ClosedRange<Integer>(NUMBER_OF_PLAYERS, NUMBER_OF_PLAYERS);
    }

    @Override
    public TicTacToeState generateInitialState(final Collection<Player> players)
    {
        return new TicTacToeState(players, N_IN_A_ROW);
    }

    @Override
    public Map<Player, Score> scores(final TicTacToeState state)
    {
        Validate.notNull(state, "Null states have no scores.");
        return state.getPlayerScores();
    }
}
