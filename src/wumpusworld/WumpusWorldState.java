package wumpusworld;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import utils.Validate;
import utils.Vector2;
import core.Player;

public class WumpusWorldState
{
    private static final Random RGEN = new SecureRandom();

    private final WumpusWorldDungeon dungeon_;

    private final Vector2 playerDirection_;

    private final Vector2 playerPosition_;

    public WumpusWorldState()
    {
        this(WumpusWorldDungeon.DEFAULT_DUNGEON_WIDTH, WumpusWorldDungeon.DEFAULT_DUNGEON_HEIGHT);
    }

    public WumpusWorldState(final int width, final int height)
    {
        dungeon_ = new WumpusWorldDungeon(width, height);
        /* The player starts at the ladder (entrance) */
        playerPosition_ = dungeon_.findSpaceFor(RoomContents.LADDER);
        final List<Vector2> cardinalDirections = Vector2.cardinalDirections();
        /* Arbitrary initial direction */
        playerDirection_ = cardinalDirections.get(RGEN.nextInt(cardinalDirections.size()));
    }

    public WumpusWorldState(final WumpusWorldState copy)
    {
        Validate.notNull(copy, "Cannot create a WumpusWorldState from a null copy");
        dungeon_ = copy.dungeon_;
        playerDirection_ = copy.playerDirection_;
        playerPosition_ = copy.playerPosition_;
    }

    public WumpusWorldDungeon getDungeon()
    {
        /* Dungeons are immutable, returning our own reference is ok */
        return dungeon_;
    }

    public Collection<Percept> getPerceptsForPlayer(final Player player)
    {
        final Set<Percept> percepts = new HashSet<Percept>(Percept.values().length);
        Vector2.cardinalDirections().forEach(direction ->
        {
            final Vector2 adjacentSpace = playerPosition_.add(direction);
            final RoomContents contents = dungeon_.contentsForSpace(adjacentSpace);
            final Percept perceptForContents = RoomContents.perceptForRoom(contents);
            if(perceptForContents != null)
            {
                percepts.add(perceptForContents);
            }
        });
        return percepts;
    }

    public Vector2 getPlayerDirection()
    {
        return playerDirection_;
    }

    public Vector2 getPlayerPosition()
    {
        /* Vector2s are immutable, returnign our own reference is ok */
        return playerPosition_;
    }

    public WumpusWorldState withPlayerAt(final Vector2 position)
    {
        new WumpusWorldState(this);
        // copy.pl
        // return new WumpusWorldState(getDungeon(), position,
        // getPlayerDirection());
        return null;
    }
}
