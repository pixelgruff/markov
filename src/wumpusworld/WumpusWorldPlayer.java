package wumpusworld;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import utils.Validate;
import utils.Vector2;

/**
 * Mutable WumpusWorldPlayer
 *
 * @author wallstop
 *
 */
public final class WumpusWorldPlayer
{
    private static final Random RGEN = new SecureRandom();

    private final Vector2 direction_;
    private boolean hasFiredArrows_ = false;

    private boolean holdingAnObject_ = false;
    private Vector2 position_;

    public WumpusWorldPlayer(final Vector2 position)
    {
        direction_ = randomCardinalDirection();
        // position_
    }

    public boolean canFireArrows()
    {
        return !hasFiredArrows_;
    }

    public boolean canGrabAnObject()
    {
        return !holdingAnObject_;
    }

    public boolean canReleaseAnObject()
    {
        return holdingAnObject_;
    }

    public void fireArrows()
    {
        Validate.isFalse(hasFiredArrows_, "Cannot fire more than one arrow");
        hasFiredArrows_ = true;
    }

    public void grabAnObject()
    {
        Validate.isFalse(holdingAnObject_, "Cannot grab an object while already holding an object");
        holdingAnObject_ = true;
    }

    /* TODO: Move somewhere else */
    private Vector2 randomCardinalDirection()
    {
        final List<Vector2> cardinalDirections = Vector2.cardinalDirections();
        return cardinalDirections.get(RGEN.nextInt(cardinalDirections.size()));
    }

    public void releaseAnObject()
    {
        Validate.isTrue(holdingAnObject_, "Cannot release an object that you're not holding");
        holdingAnObject_ = false;
    }
}
