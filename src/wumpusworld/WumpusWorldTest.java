package wumpusworld;

import java.util.HashMap;
import java.util.Map;

import wumpusworld.states.PlayerState;
import wumpusworld.states.WumpusWorldState;
import core.Player;
import core.Policy;
import core.Score;
import core.automators.LocalAutomator;
import core.policies.RandomPolicy;

public class WumpusWorldTest
{

    public static void main(final String [] args)
    {
        final Player explorer = new Player("Dungeon Explorer");
        final Policy<WumpusWorldState, WumpusWorldAction> randomPolicy = new RandomPolicy<>();
        final Map<Player, Policy<WumpusWorldState, WumpusWorldAction>> policies = new HashMap<>();
        policies.put(explorer, randomPolicy);

        final WumpusWorldRules rules = new WumpusWorldRules();
        final LocalAutomator<WumpusWorldState, WumpusWorldAction, WumpusWorldRules> wumpusWorldAutomator = new LocalAutomator<>(
                rules, policies);

        /* Note that these players are not added in any guaranteed order */
        final WumpusWorldState endedState = wumpusWorldAutomator.playGameToCompletion();

        final Score score = endedState.getScoreForPlayer(explorer);
        final PlayerState playerState = endedState.states().get(explorer);

        System.out.println(endedState);
        /* 'best' may hold a player that tied for first */
        System.out.println(String.format(
                "Game over. %s ended up with %s points by %s. Total moves: %d", explorer, score,
                playerState, wumpusWorldAutomator.getActionsTaken().size()));
    }
}
