/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import core.Game;
import core.Player;
import core.Score;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import utils.Validate;

/**
 *
 * @author Ginger
 */
public class TicTacToeGame extends Game<TicTacToeAction, TicTacToeState>
{
    // X goes first
    private TicTacToeMark currentMark_ = TicTacToeMark.X;
    private final Map<Player, TicTacToeMark> playersToMarks_;
    private final TicTacToeState state_;
    private final Map<Player, Score> scores_;

    public TicTacToeGame(final List<Player> players)
    {
        super(players);
        
        final TicTacToeBoard board = new TicTacToeBoard();
        state_ = new TicTacToeState(board);
        scores_ = new HashMap<Player, Score>();

        Validate.isTrue(players.size() == TicTacToeMark.values().length,
                "Cannot create a TicTacToeGame with " + players.size() + " players");

        playersToMarks_ = new HashMap<Player, TicTacToeMark>(players.size());
        for(int i = 0; i < players.size(); ++i)
        {
            playersToMarks_.put(players.get(i), TicTacToeMark.values()[i]);
        }
    }

    @Override
    public TicTacToeState getState(final Player player)
    {
        /*
         * Players have access to all in-game information (but by copy. Those
         * nefarious players, you never know what they might do)
         */
        return getState();
    }
    
    public TicTacToeState getState()
    {
        return new TicTacToeState(state_);
    }
    
    public TicTacToeMark getMarkForPlayer(final Player player)
    {
        return playersToMarks_.get(player);
    }
    
    @Override
    public Score getPlayerScore(final Player player)
    {
        return scores_.getOrDefault(player, new Score(0));
    }

    @Override
    public Player getCurrentPlayer()
    {
        if(isOver())
        {
            return null;
        }

        /* Holy crap why doesn't java have a bimap http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/BiMap.html */
        return playersToMarks_.entrySet().stream()
                .filter(playerToMark -> playerToMark.getValue() == currentMark_).findFirst()
                .map(playerToMark -> playerToMark.getKey()).orElse(null);
    }

    @Override
    public Collection<TicTacToeAction> getActions(final Player player)
    {
        // Don't return any actions if the game is over
        return isOver() ? Collections.emptyList() : state_.getAvailablePositions().stream()
                .map(position -> new TicTacToeAction(position, getMarkForPlayer(player)))
                .collect(Collectors.toList());
    }

    @Override
    public void takeAction(final Player player, final TicTacToeAction action)
    {
        // Log or save the old state?

        Validate.isTrue(playersToMarks_.get(player) == currentMark_, "Cannot take an action for " + player + "; it's not your turn!");
        Validate.isTrue(getActions(player).contains(action), "Cannot take action " + action + "; it simply isn't possible");
        // Mark the board
        final Score scoreOfAction = state_.applyAction(action);
        final Score previousScore = getPlayerScore(player);
        scores_.put(player, scoreOfAction.add(previousScore));

        // Swap the current mark
        currentMark_ = (currentMark_ == TicTacToeMark.X) ? TicTacToeMark.O : TicTacToeMark.X;
    }

    @Override
    public boolean isOver()
    {
        return state_.isTerminal();
    }
}
