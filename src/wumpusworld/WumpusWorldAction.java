package wumpusworld;

import java.util.Objects;

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

    @Override
    public int hashCode()
    {
        return Objects.hash(player_, action_);
    }

    @Override
    public boolean equals(final Object other)
    {
        if(other instanceof WumpusWorldAction)
        {
            final WumpusWorldAction action = (WumpusWorldAction) other;
            return Objects.equals(player_, action.player_)
                    && Objects.equals(action_, action.action_);
        }
        return false;
    }

    @Override
    public String toString()
    {
        return String.format("s: %s", player_, action_);
    }
}
