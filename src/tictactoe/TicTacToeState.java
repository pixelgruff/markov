/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import core.Player;
import core.State;

/**
 *
 * @author Ginger
 */
public class TicTacToeState implements State<TicTacToe, TicTacToeAction> {

    @Override
    public boolean isTerminal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getScore(Player<TicTacToe, TicTacToeAction> p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
