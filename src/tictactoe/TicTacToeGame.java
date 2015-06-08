/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import core.Game;
import core.Player;

import java.util.ArrayList;
import java.util.HashMap;
import utils.Vector2;

/**
 *
 * @author Ginger
 */
public class TicTacToeGame extends Game<TicTacToeState, TicTacToeAction> {

    // X goes first
    private Mark currentMark_ = Mark.X;
    private final HashMap<Player, Mark> marks_ = new HashMap();
    private final TicTacToeState state_ = new TicTacToeState();

    public TicTacToeGame(ArrayList<Player> players) {
        super(players);

        // Tic-tac-toe only has two players
        assert (players.size() == 2);
        // Is there a cleaner way to do this?
        // marks_ = new HashMap(players, Marks.values())
        marks_.put(players.get(0), Mark.X);
        marks_.put(players.get(1), Mark.O);
    }

    @Override
    public TicTacToeState getState(Player p) {
        // Players have access to all in-game information
        return state_;
    }

    @Override
    public Player getCurrentPlayer() {
        assert(!isOver());
        // Gross?
        return players_.stream()
                .filter(player -> marks_.get(player) == currentMark_)
                .findFirst().get();
    }

    @Override
    public ArrayList<TicTacToeAction> getActions(Player p) {
        // Actions in tic-tac-toe are not player-specific
        ArrayList<Vector2> empties = state_.getAllEmpty();
        /*
        This definitely feels like it's made to be done functionally, but I ran
        into problems ¯\_(ツ)_/¯	
        Behold a hybrid approach
        */
        ArrayList<TicTacToeAction> actions = new ArrayList<>();
        empties.stream().forEach(v -> actions.add(new TicTacToeAction(v.getX(), v.getY())));
        
        return actions;
    }

    @Override
    public void takeAction(Player player, TicTacToeAction action) {
        // Log or save the old state? 

        // Verify this is the correct player
        assert (marks_.get(player) == currentMark_);
        // Verify this action is in the action space for this player
        assert (getActions(player).contains(action));
        // Mark the board
        state_.mark(player, action.position, marks_.get(player));
        // Swap the current mark
        currentMark_ = (currentMark_ == Mark.X) ? Mark.O : Mark.X;
    }

    @Override
    public boolean isOver() {
        return state_.isTerminal();
    }

}
