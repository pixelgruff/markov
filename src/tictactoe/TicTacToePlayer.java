/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import core.Player;

import java.util.Collection;
import java.util.Random;

import core.State;


/**
 *
 * @author Ginger
 */
public class TicTacToePlayer extends Player<TicTacToe, TicTacToeAction> {

    private final Mark mark_;
    
    public Mark getMark()
    {
        return mark_;
    }

    public TicTacToePlayer(Mark mark)
    {
        assert(mark != null);
        mark_ = mark;
    }

    @Override
    public TicTacToeAction chooseAction(State<TicTacToe, TicTacToeAction> state,
            Collection<TicTacToeAction> actions)
    {
        // Could do this better with guava Iterables
        return (TicTacToeAction) actions.toArray()[new Random().nextInt(actions.size())];
    }
}
