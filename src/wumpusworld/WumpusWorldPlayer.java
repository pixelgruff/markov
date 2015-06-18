package wumpusworld;

import utils.Validate;
import core.Player;

public class WumpusWorldPlayer extends Player
{
    private boolean hasFiredArrows_ = false;
    private boolean holdingAnObject_ = false;

    public WumpusWorldPlayer(final String name)
    {
        super(name);
    }

    public boolean canFireArrows()
    {
        return !hasFiredArrows_;
    }

    public void fireArrows()
    {
        Validate.isFalse(hasFiredArrows_, "Cannot fire more than one arrow");
        hasFiredArrows_ = true;
    }

    public boolean canGrabAnObject()
    {
        return !holdingAnObject_;
    }

    public void grabAnObject()
    {
        Validate.isFalse(holdingAnObject_, "Cannot grab an object while already holding an object");
        holdingAnObject_ = true;
    }

    public boolean canReleaseAnObject()
    {
        return holdingAnObject_;
    }

    public void releaseAnObject()
    {
        Validate.isTrue(holdingAnObject_, "Cannot release an object that you're not holding");
        holdingAnObject_ = false;
    }
}
