/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import core.Game;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author Ginger
 */
public class TicTacToeGame extends Game<TicTacToe, TicTacToeAction, TicTacToeState, TicTacToePlayer> {

    // X goes first
    private Mark currentMark_ = Mark.X;

    public TicTacToeGame(ArrayList<TicTacToePlayer> players) {
        super(players);

        assert (players.size() == Mark.values().length);
        for(final Mark mark : Mark.values())
        {
            assert (players.stream().anyMatch(player -> (player.getMark() == mark)));
        }
    }

    @Override
    public TicTacToeState getState(TicTacToePlayer p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TicTacToePlayer getNextPlayer()
    {
        // Gross
        return players_.stream()
                .filter(player -> (player.getMark() == (currentMark_ == Mark.X ? Mark.O : Mark.X)))
                .collect(Collectors.toList()).get(0);

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
