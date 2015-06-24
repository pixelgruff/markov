package wumpusworld;

import utils.Validate;
import core.Player;

public class WumpusWorldAction
{
    public enum Action
    {
        TURN_LEFT, TURN_RIGHT, MOVE_FORWARD, FIRE_ARROW, GRAB, RELEASE, CLIMB
    }

    private final Player player_;
    private final Action action_;

    public WumpusWorldAction(final Player player, final Action action)
    {
        Validate.notNull(player, "Cannot create a WumpusWorldAction for a null player");
        Validate.notNull(action, "Cannot create a WumpusWorldAction with a null action");
        player_ = player;
        action_ = action;
    }

    public Player getPlayer()
    {
        return player_;
    }

    public Action getAction()
    {
        return action_;
    }
}
