/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import core.Game;
import java.util.ArrayList;

/**
 *
 * @author Ginger
 */
public class TicTacToeGame extends Game<TicTacToe, TicTacToeAction, TicTacToeState, TicTacToePlayer> {

    // X goes first
    private Mark currentMark_ = Mark.X;

    public TicTacToeGame(ArrayList<TicTacToePlayer> players) {
        super(players);

        assert (players.size() == 2);
        // We expect a very finite set of players
        assert ((players.get(1).mark_ == Mark.X && players.get(2).mark_ == Mark.O)
                || (players.get(1).mark_ == Mark.O && players.get(2).mark_ == Mark.X));
    }

    @Override
    public TicTacToeState getState(TicTacToePlayer p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TicTacToePlayer getNextPlayer() {
        for (TicTacToePlayer p : players_) {
            if (p.mark_ == currentMark_) {
                currentMark_ = (currentMark_ == Mark.X) ? Mark.O : Mark.X;
                return p;
            }
        }
        return null;
    }

    @Override
    public ArrayList<TicTacToeAction> getActions(TicTacToePlayer p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void takeAction(TicTacToePlayer p, TicTacToeAction action) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
