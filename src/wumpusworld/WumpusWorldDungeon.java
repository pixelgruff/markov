package wumpusworld;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import utils.ClosedRange;
import utils.Validate;
import utils.Vector2;

/**
 * Immutable, procedurally generated dungeon.
 *
 * Gauranteed to be solvable (at least one path exists between the player and
 * the gold)
 *
 * General layout described:
 * http://www.cis.temple.edu/~giorgio/cis587/readings/wumpus.shtml
 *
 */
public class WumpusWorldDungeon
{
    private static final int DEFAULT_DUNGEON_HEIGHT = 200;
    private static final int DEFAULT_DUNGEON_WIDTH = 200;
    private static final RoomContents EMPTY = null;
    /* Can't have Wumpus World Dungeons that are less than 4 x 4 */
    private static final int MINIMUM_DUNGEON_HEIGHT = 4;
    private static final int MINIMUM_DUNGEON_WIDTH = 4;
    /*
     * SecureRandom simply for high entropy dungeons. If we need to speed things
     * up, ThreadLocalRandom will work in a pinch, but our RandomGen isn't the
     * bottleneck right now, so worry about that later
     */
    private static final Random RGEN = new SecureRandom();

    /**
     * Determines what percentage (within some bounds) of the dungeon should be
     * pits. It's not a hard dungeon without a lot of pits...
     *
     * @return
     */
    private static double pitPercentage()
    {
        /* This range of percentages is chosen arbitrarily */
        final double minPercentage = 0.12;
        final double maxPercentage = 0.37;
        final ClosedRange<Double> validPitPercentages = new ClosedRange<Double>(minPercentage,
                maxPercentage);
        double pitPercentage;
        do
        {
            pitPercentage = RGEN.nextDouble();
        }
        while(!(validPitPercentages.isValueWithin(pitPercentage)));

        return pitPercentage;
    }

    /*
     * Utilizing a map instead of a grid allows us to have possible
     * non-rectangular dungeons
     */
    private final Map<Vector2, RoomContents> dungeon_;
    /*
     * Store the Gold space & the Ladder space as an optimization. If we don't,
     * we have to search through our map for the gold / ladder space *every*
     * time we want to try and place a pit. Sifting through a map for a value is
     * expensive - especially if the map is > 40k elements (default size!)
     */
    private Vector2 goldSpace_ = null;
    private Vector2 ladderSpace_ = null;

    /*
     * Performance optimizations - store the min & max possible X & Y
     * coordinates for this dungeon. This allows some cool stuff like
     * non-rectangular dungeons
     */
    private final int maxXCoordinate_;
    private final int maxYCoordinate_;

    private final int minXCoordinate_;
    private final int minYCoordinate_;

    /**
     * Procedurally constructs a WumpusWorldDungeon of default width & height.
     * Currently this is 200 x 200
     *
     * Note: The constructed Dungeon is guaranteed to be solvable.
     */
    public WumpusWorldDungeon()
    {
        this(DEFAULT_DUNGEON_WIDTH, DEFAULT_DUNGEON_HEIGHT);
    }

    /**
     * Procedurally constructs a WumpusWorldDungeon of the specified width &
     * height. Width and Height must be valid numbers (non-negative, at least as
     * large as the smallest possible dungeon, 4x4)
     *
     * Note: Due to the procedural generation of the dungeon, dungeons larger
     * than 350 x 350 or so will suffer slow generation times.
     *
     * Note: The constructed Dungeon is guaranteed to be solvable
     *
     * @param width
     *            Width of the dungeon
     * @param height
     *            Height of the dungeon
     */
    public WumpusWorldDungeon(final int width, final int height)
    {
        Validate.isTrue(width >= MINIMUM_DUNGEON_WIDTH,
                String.format("Cannot create a dungeon with width < %d", MINIMUM_DUNGEON_WIDTH));
        Validate.isTrue(height >= MINIMUM_DUNGEON_HEIGHT,
                String.format("Cannot create a dungeon with width < %d", MINIMUM_DUNGEON_HEIGHT));

        dungeon_ = new HashMap<Vector2, RoomContents>(DEFAULT_DUNGEON_WIDTH
                * DEFAULT_DUNGEON_HEIGHT);

        /* Fill up the dungeon with null (empty) spaces */
        for(int i = 0; i < width; ++i)
        {
            for(int j = 0; j < height; ++j)
            {
                final Vector2 space = new Vector2(i, j);
                dungeon_.put(space, null);
            }
        }

        /* Determine x & y bounds */
        minXCoordinate_ = minimumXCoordinate();
        maxXCoordinate_ = maximumXCoordinate();
        minYCoordinate_ = minimumYCoordinate();
        maxYCoordinate_ = maximumYCoordinate();

        /* Finally, place everything */
        setupDungeon();
    }

    /**
     * Performs a modified A* search to determine if the two spaces are
     * connected. Order of arguments does not particularly matter.
     *
     * Note: Null spaces or spaces outside of the map are never connected.
     *
     * @param source
     *            Space to start the path from
     * @param goal
     *            Space that the path will end, if a path exists
     * @return True if a path can be made, false otherwise
     */
    private boolean areSpacesConnected(final Vector2 source, final Vector2 goal)
    {
        if(!dungeon_.containsKey(source) || !dungeon_.containsKey(goal))
        {
            /*
             * There is no way that two spaces that are not in the map are
             * connected
             */
            return false;
        }
        /*
         * Keep track of the tiles that we've already been to so we don't
         * continue following branches we've already taken
         */
        final Set<Vector2> alreadyDiscoveredTiles = new HashSet<Vector2>();
        alreadyDiscoveredTiles.add(source);
        /*
         * Poor man's A* - Keep a Collection of tiles that we can explore ranked
         * by how close they are to the goal. We pick the
         * closest-space-to-the-goal to explore each iteration
         */
        final NavigableSet<Vector2> tilesToExplore = new TreeSet<Vector2>(
                (first, second) -> Double.compare(first.squaredDistanceBetween(goal),
                        second.squaredDistanceBetween(goal)));
        tilesToExplore.add(source);
        do
        {
            final Vector2 currentSpace = tilesToExplore.pollFirst();
            if(currentSpace.equals(goal))
            {
                return true;
            }

            final Collection<Vector2> cardinalDirections = Vector2.directionalVectors();
            cardinalDirections.stream().map(space -> currentSpace.add(space))
            /* Determine actual movement vector */
            .filter(space -> isSpacePassable(space))
            /*
             * Make sure we don't go anywhere where we've either already been or
             * will go
             */
            .filter(space -> alreadyDiscoveredTiles.add(space))
            /* Queue them up to be explored */
            .forEach(tile -> tilesToExplore.add(tile));
        }
        while(!tilesToExplore.isEmpty());

        return false;
    }

    /**
     * Determines whether or not there exists a path between the specified space
     * and the ladder. This is primarily used when placing pits, to determine if
     * a path exist between the gold and the ladder (if the dungeon is
     * "solvable")
     *
     * @param source
     *            Space to find a path to ladder for
     * @return Whether or not the ladder is reachable from the provided space
     */
    private boolean canReachLadder(final Vector2 source)
    {
        if(!dungeon_.containsValue(RoomContents.LADDER))
        {
            /* We can't reach the ladder if there is no ladder in the dungeon */
            return false;
        }

        return areSpacesConnected(source, ladderSpace());
    }

    /**
     * Determines whether or not there exists a path between the gold and the
     * ladder. This is expensive, but extremely useful, because we can use this
     * to check if an obstacle that was just placed has blocked access to the
     * gold from the Player. If this happens, the dungeon is in an unsolvable
     * state, so we need to be able to check when this occurs.
     *
     * @return True if there exists at least one path from the gold to the
     *         ladder
     */
    private boolean canReachLadderFromGold()
    {
        final Vector2 goldSpace = goldSpace();
        return canReachLadder(goldSpace);
    }

    /**
     * @return The set of RoomContents that are currently populating this
     *         dungeon. No duplicate items.
     */
    private Collection<RoomContents> dungeonContents()
    {
        return dungeon_.values().stream().collect(Collectors.toSet());
    }

    /**
     * Finds *A* space that contains the given RoomContents.
     *
     * If no such contents exist, returns null.
     *
     * @param contents
     *            RoomContents to find a space foor
     * @return *A* Space that contains the contents or null if no such contents
     *         exist in the dungeon.
     */
    private final Vector2 findSpaceFor(final RoomContents contents)
    {
        return dungeon_.entrySet().stream().filter(entry -> entry.getValue() == contents)
                .map(entry -> entry.getKey()).findFirst().orElse(null);
    }

    /**
     * Translates the dungeon to an 2D array representation.
     *
     * Note: Modifying this Array has no impact whatsoever on the internal state
     * of the dungeon.
     *
     * @return The Dungeon as if it were a 2D array
     */
    public RoomContents[][] getDungeonAsArray()
    {
        final int width = width();
        final int height = height();
        final RoomContents[][] dungeon = new RoomContents[width][height];
        dungeon_.entrySet().forEach(spaceToRoomContents ->
        {
            final Vector2 space = spaceToRoomContents.getKey();
            final RoomContents contents = spaceToRoomContents.getValue();
            dungeon[space.getX()][space.getY()] = contents;
        });
        return dungeon;
    }

    /**
     * Returns the dungeon as a Map of Vector2 to RoomContents
     *
     * Note: Modifying this Array has no impact whatsoever on the internal state
     * of the dungeon.
     *
     * @return The Dungeon as if it were a Map of Vector2 to RoomContents
     */
    public Map<Vector2, RoomContents> getDungeonAsMap()
    {
        return new HashMap<Vector2, RoomContents>(dungeon_);
    }

    /**
     * Convenience for caching the gold space if it is not already cached.
     *
     * @return The space on the board that has the gold
     */
    private Vector2 goldSpace()
    {
        if(goldSpace_ == null)
        {
            goldSpace_ = findSpaceFor(RoomContents.GOLD);
        }
        return goldSpace_;
    }

    /**
     *
     * @return Height of the board
     */
    public int height()
    {
        return (maxYCoordinate_ - minYCoordinate_) + 1;
    }

    /**
     * Determines whether or not a space in the dungeon is pass-able.
     *
     * Spaces are not pass-able if they are outside of the dungeon, contain the
     * wumpus, or contain a pit.
     *
     * @param position
     *            Position to check for pass-ability
     * @return True if the player can pass through the space unscathed, false
     *         otherwise
     */
    private boolean isSpacePassable(final Vector2 position)
    {
        return dungeon_.containsKey(position) && RoomContents.isPassable(dungeon_.get(position));
    }

    /**
     * Convenience for caching the ladder space if it is not already cached.
     *
     * @return The space on the board that has the ladder
     */
    private Vector2 ladderSpace()
    {
        if(ladderSpace_ == null)
        {
            ladderSpace_ = findSpaceFor(RoomContents.LADDER);
        }
        return ladderSpace_;
    }

    /**
     * Functionally determines the maximum X coordinate in the dungeon by
     * iterating over every space in the dungeon
     *
     * @return Maximum X coordinate of the dungeon, 0-indexed
     */
    private int maximumXCoordinate()
    {
        return dungeon_.keySet().stream().map(space -> space.getX()).max(Integer::compare).get();
    }

    /**
     * Functionally determines the maximum Y coordinate in the dungeon by
     * iterating over every space in the dungeon
     *
     * @return Maximum Y coordinate of the dungeon, 0-indexed
     */
    private int maximumYCoordinate()
    {
        return dungeon_.keySet().stream().map(space -> space.getY()).max(Integer::compare).get();
    }

    /**
     * Functionally determines the minimum X coordinate in the dungeon by
     * iterating over every space in the dungeon
     *
     * @return Minimum X coordinate of the dungeon, 0-indexed
     */
    private int minimumXCoordinate()
    {
        return dungeon_.keySet().stream().map(space -> space.getX()).min(Integer::compare).get();
    }

    /**
     * Functionally determines the minimum Y coordinate in the dungeon by
     * iterating over every space in the dungeon
     *
     * @return Minimum Y coordinate of the dungeon, 0-indexed
     */
    private int minimumYCoordinate()
    {
        return dungeon_.keySet().stream().map(space -> space.getY()).min(Integer::compare).get();
    }

    /**
     * Places the gold within reach of the player in the dungeon.
     *
     * Note: Only one invocation of this method per Dungeon instance is
     * possible. Multiple invocations will fail with IllegalArgumentExceptions
     */
    private void placeGold()
    {
        Validate.isTrue(
                !dungeon_.containsValue(RoomContents.PIT)
                        && !dungeon_.containsValue(RoomContents.GOLD)
                        && dungeon_.containsValue(RoomContents.LADDER)
                        && dungeon_.containsValue(EMPTY), String.format(
                        "Cannot place gold in a dungeon with pits, with gold, "
                                + "without a ladder, or without empty spaces. "
                                + "This dungeon already contains %s", dungeonContents()));

        Vector2 goldSpace;
        do
        {
            goldSpace = probableSpace();
        }
        /*
         * Make sure the chosen position exists in the map, doesn't have
         * anything on it, and is reachable from the ladder
         */
        while(!dungeon_.containsKey(goldSpace) || dungeon_.get(goldSpace) != EMPTY
                || !canReachLadder(goldSpace));
        /* And cement it in */
        dungeon_.put(goldSpace, RoomContents.GOLD);
    }

    /**
     * Places the ladder randomly in the dungeon.
     *
     * Note: Only one invocation of this method per Dungeon instance is
     * possible. This method can also only be invoked when the dungeon is empty.
     * Multiple invocations, or an invocation when the dungeon is not empty will
     * result in an IllegalArgumentException
     */
    private void placeLadder()
    {
        Validate.isTrue(dungeon_.values().stream().distinct().count() == 1
                && dungeon_.values().contains(EMPTY), String.format(
                "Cannot place a ladder in a pre-populated dungeon or a"
                        + " dungeon without any empty spaces."
                        + " This dungeon already contains %s", dungeonContents()));

        Vector2 ladderSpace;
        do
        {
            ladderSpace = probableSpace();
        }
        /*
         * We've already gauranteed that every space in the dungeon is empty, so
         * there's no need to double check any condition besides that the chosen
         * space actually exists in the dungeon
         */
        while(!dungeon_.containsKey(ladderSpace));

        dungeon_.put(ladderSpace, RoomContents.LADDER);
    }

    /**
     * Places pits procedurally in the dungeon.
     *
     * Some random number of pits are chosen to be placed. This method may be
     * invoked multiple times.
     *
     * Note: Pits are placed in such a way that there always exist at least one
     * path from the player entrance to the gold
     *
     * Note: This method will throw an IllegalArgumentException if there are not
     * any possible locations to place pits, if the dungeon does not have a
     * ladder, or if the dungeon does not have gold.
     */
    private void placePits()
    {
        Validate.isTrue(
                dungeon_.containsValue(RoomContents.LADDER)
                        && dungeon_.containsValue(RoomContents.GOLD)
                        && dungeon_.containsValue(EMPTY), String.format(
                        "Cannot place pits in a dungeon without a ladder, "
                                + "without gold, or without empty spaces."
                                + " This dungeon already contains %s", dungeonContents()));

        final double pitPercentage = pitPercentage();
        final long numPits = Math.round(dungeon_.size() * pitPercentage);
        int numPlacementRetries = 0;
        /*
         * Determine an arbitrary upper-bound on the number of attempts that we
         * make for placing a single pit - if we continually fail, that means
         * the dungeon is too obstacle-saturated
         */
        final int maxPlacementRetries = 50;

        for(int pitCount = 0; pitCount < numPits && numPlacementRetries < maxPlacementRetries;)
        {
            Vector2 pitSpace;
            do
            {
                pitSpace = probableSpace();
            }
            /* Make sure we've picked a valid, empty space */
            while(!dungeon_.containsKey(pitSpace) || dungeon_.get(pitSpace) != EMPTY);

            /* Place the pit so we can evaluate it's "goodness" */
            dungeon_.put(pitSpace, RoomContents.PIT);
            if(canReachLadderFromGold())
            {
                /*
                 * TODO: Make sure that not only can the player reach the gold
                 * from the ladder, but that the wumpus can reach the player
                 */
                ++pitCount;
                numPlacementRetries = 0;
            }
            else
            {
                /* Remove the pit, it's no good! */
                dungeon_.put(pitSpace, EMPTY);
                ++numPlacementRetries;
            }
        }
    }

    /**
     * Places a single wumpus procedurally in the dungeon.
     *
     * Note: Wumpuses are placed in such a way that there will always exist a
     * path from the player entrance to the gold
     *
     * Note: This method will throw an IllegalArgumentException if there are not
     * any possible locations to place the wumpus, if the dungeon does not have
     * a ladder, or if the dungeon does not have gold.
     */
    private void placeWumpus()
    {
        Validate.isTrue(
                dungeon_.containsValue(RoomContents.LADDER)
                        && dungeon_.containsValue(RoomContents.GOLD)
                        && dungeon_.containsValue(EMPTY), String.format(
                        "Cannot place a wumpus in a dungeon without gold, "
                                + "without a ladder, or without empty spaces."
                                + " This dungeon already contains %s", dungeonContents()));

        Vector2 wumpusSpace;
        final int maxWumpusRetries = 1000;
        for(int numWumpusRetries = 0; numWumpusRetries < maxWumpusRetries; ++numWumpusRetries)
        {
            do
            {
                final int x = probableXCoordinate();
                final int y = probableYCoordinate();
                wumpusSpace = new Vector2(x, y);
            }
            while(!dungeon_.containsKey(wumpusSpace) || dungeon_.get(wumpusSpace) != EMPTY);

            dungeon_.put(wumpusSpace, RoomContents.WUMPUS);
            if(canReachLadderFromGold())
            {
                break;
            }
            else
            {
                dungeon_.put(wumpusSpace, EMPTY);
            }
        }

        if(!dungeon_.containsValue(RoomContents.WUMPUS))
        {
            throw new RuntimeException("Could not place the wumpus!");
        }
    }

    /**
     * Generate a space that has a "pretty good" likelihood of being within the
     * dungeon.
     *
     * @return A space that is likely within the dungeon
     */
    private Vector2 probableSpace()
    {
        final int x = probableXCoordinate();
        final int y = probableYCoordinate();
        return new Vector2(x, y);
    }

    /**
     * Generates an x coordinate that has a "pretty good" likelihood of being
     * within the dungeon
     *
     * @return An x coordinate that is likely within the dungeon
     */
    private int probableXCoordinate()
    {
        final int minX = minXCoordinate_;
        final int maxX = maxXCoordinate_;
        return RGEN.nextInt(maxX + 1 - minX) + minX;
    }

    /**
     * Generates an y coordinate that has a "pretty good" likelihood of being
     * within the dungeon
     *
     * @return An y coordinate that is likely within the dungeon
     */
    private int probableYCoordinate()
    {
        final int minY = minYCoordinate_;
        final int maxY = maxYCoordinate_;
        return RGEN.nextInt(maxY + 1 - minY) + minY;
    }

    /**
     * Places all the necessary dungeon components - ladder, dungeon, wumpus,
     * and pits
     */
    private void setupDungeon()
    {
        /*
         * The order here is of utmost importance. We must place the ladder
         * first - the player needs a way to get in and out. We must place the
         * gold next - the player needs a goal. After that, we can place pits
         * and wumpuses in any order, as the're merely "obstacles" in the
         * players eyes
         */
        placeLadder();
        placeGold();
        placeWumpus();
        placePits();
    }

    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();
        Arrays.asList(getDungeonAsArray()).forEach((row) ->
        {
            Arrays.asList(row).forEach(contents ->
            {
                if(contents == null)
                {
                    builder.append(" ");
                }
                else
                {
                    builder.append(contents.shortToString());
                }

            });
            builder.append(System.lineSeparator());
        });
        return builder.toString();
    }

    /**
     *
     * @return Width of the dungeon
     */
    public int width()
    {
        return (maxXCoordinate_ - minXCoordinate_) + 1;
    }
}
