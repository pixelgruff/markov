/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import core.Player;
import java.util.Random;
import core.State;
import java.util.ArrayList;

/**
 *
 * @author Ginger
 */
public class TTTPlayer extends Player<TTTAction> {

    public TTTGame.Mark mark_;

    public TTTPlayer(TTTGame.Mark mark) {
        mark_ = mark;
    }

    @Override
    public TTTAction chooseAction(State state, ArrayList<TTTAction> actions) {
        // Return a random action
        return actions.get(new Random().nextInt(actions.size()));
    }

}
