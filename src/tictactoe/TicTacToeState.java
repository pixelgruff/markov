package tictactoe;

import core.Player;
import core.State;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import utils.Vector2;

/**
 *
 * @author Ginger
 */
public class TicTacToeState implements State {

    private class Board {
        /* width, height, kInARow allow for tic-tac-toe variants, should we 
         * choose to implement them */

        private final int width = 3;
        private final int height = 3;
        private final int kInARow = 3;
        private final Mark[][] positions;

        private Board() {
            positions = new Mark[width][height];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    Mark p = Mark.Empty;
                    positions[i][j] = p;
                }
            }
        }

        private boolean has(final Vector2 position) {
            return (position.getX() >= 0
                    && position.getX() < width
                    && position.getY() >= 0
                    && position.getY() < height);
        }

        private Mark get(final Vector2 position) {
            return positions[position.getX()][position.getY()];
        }

        private void set(final int x, final int y, final Mark mark) {
            positions[x][y] = mark;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            Arrays.asList(positions).stream().forEach((row) -> {
                builder.append(Arrays.deepToString(row).replaceAll("Empty", "?").concat(System.lineSeparator()));
            });
            return builder.toString();
        }
    }

    private final Board board_ = new Board();
    private final HashMap<Player, Double> scores_ = new HashMap<>();
    private boolean isTerminal_;
    private int movesMade_ = 0;

    @Override
    public boolean isTerminal() {
        return isTerminal_;
    }

    @Override
    public double getScore(Player p) {
        return scores_.getOrDefault(p, 0.);
    }

    public ArrayList<Vector2> getAllEmpty() {
        ArrayList<Vector2> empties = new ArrayList<>();
        for (int i = 0; i < board_.width; i++) {
            for (int j = 0; j < board_.height; j++) {
                Vector2 position = new Vector2(i, j);
                if (board_.get(new Vector2(i, j)) == Mark.Empty) {
                    empties.add(position);
                }
            }
        }
        return empties;
    }

    public void mark(final Player player, final Vector2 position, final Mark mark) {
        board_.set(position.getX(), position.getY(), mark);
        // Check if the board is now full
        isTerminal_ = (++movesMade_ == board_.width * board_.height) ? true : isTerminal_;
        // Now we can perform a (relatively) efficient check for winning moves
        if (isWinningMove(position, mark)) {
            isTerminal_ = true;
            scores_.put(player, 1.);
        }
    }

    private boolean isWinningMove(final Vector2 position, final Mark mark) {
        HashMap<Vector2, Integer> walks = new HashMap<>();
        // Look in every direction on a 2D board
        for (int dX = -1; dX <= 1; dX++) {
            for (int dY = -1; dY <= 1; dY++) {
                Vector2 delta = new Vector2(dX, dY);
                walks.put(delta, getWalkLength(position, delta, mark));
            }
        }
        /**
         * Paths can extend in two directions from any position; we map the
         * eight cardinal directions to their four paths (delta and -delta)
         */
        HashMap<Vector2, Integer> paths = new HashMap<>();
        // Map walk lengths to paths
        walks.keySet().stream().forEach((delta) -> {
            Vector2 inverse = delta.multiply(new Vector2(-1, -1));
            /*
             If the inverse is present in the map, append the value of its
             corresponding walk.
             */
            if (paths.containsKey(inverse)) {
                paths.replace(inverse, paths.get(inverse) + walks.get(delta));
            } else {
                paths.put(delta, walks.get(delta));
            }
        });
        // Check if any path lengths constitute a win
        // Match kInARow - 1 because paths do not contain their origin position
        return paths.values().stream().anyMatch(i -> i >= board_.kInARow - 1);
    }

    /* 
     Recursively walk in a single direction until walking off the board or
     encountering a change in mark type
     */
    private int getWalkLength(final Vector2 current, final Vector2 delta, final Mark mark) {
        // Terminate 0-delta cases
        if (delta.getX() == 0 && delta.getY() == 0) {
            return 0;
        }
        // Calculate next location
        Vector2 nextPosition = current.add(delta);
        // Detect board overrun
        if (!board_.has(nextPosition)) {
            return 0;
        }
        // Detect change in mark
        Mark nextMark = board_.get(nextPosition);
        if (nextMark != mark) {
            return 0;
        }
        // Checks out Lou; step to the next position
        return 1 + getWalkLength(nextPosition, delta, mark);
    }

    @Override
    public String toString() {
        return String.format("%s\n%s\n", board_.toString(), scores_.toString());
    }
}
