package wumpusworld;

import java.security.SecureRandom;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import utils.ClosedRange;
import utils.Validate;
import utils.Vector2;

public class WumpusWorldDungeon
{
    private static final int DEFAULT_DUNGEON_HEIGHT = 200;
    private static final int DEFAULT_DUNGEON_WIDTH = 200;
    private static final RoomContents EMPTY = null;
    private static final int MINIMUM_DUNGEON_HEIGHT = 4;
    private static final int MINIMUM_DUNGEON_WIDTH = 4;
    private static final Random RGEN = new SecureRandom();

    private static double pitPercentage()
    {
        /*
         * TODO: These values are crazy small because our search method is
         * god-awful. Once we fix our pathfinding, crank these values up to
         * 20-40%
         */
        final double minPercentage = 0.01;
        final double maxPercentage = 0.05;
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

    private final Map<Vector2, RoomContents> dungeon_;

    public WumpusWorldDungeon()
    {
        this(DEFAULT_DUNGEON_WIDTH, DEFAULT_DUNGEON_HEIGHT);
    }

    public WumpusWorldDungeon(final int width, final int height)
    {
        Validate.isTrue(width >= MINIMUM_DUNGEON_WIDTH,
                String.format("Cannot create a dungeon with width < %d", MINIMUM_DUNGEON_WIDTH));
        Validate.isTrue(height >= MINIMUM_DUNGEON_HEIGHT,
                String.format("Cannot create a dungeon with width < %d", MINIMUM_DUNGEON_HEIGHT));

        dungeon_ = new HashMap<Vector2, RoomContents>(DEFAULT_DUNGEON_WIDTH
                * DEFAULT_DUNGEON_HEIGHT);
        for(int i = 0; i < width; ++i)
        {
            for(int j = 0; j < height; ++j)
            {
                final Vector2 space = new Vector2(i, j);
                /* Fill up the dungeon with null (empty) spaces */
                dungeon_.put(space, null);
            }
        }

        setupDungeon();
    }

    private boolean canReachLadder(final Vector2 space)
    {
        if(!dungeon_.containsKey(space))
        {
            return false;
        }

        /* Breadth-first search this bad boy */
        final Set<Vector2> exhaustedSpacesOnBoard = new HashSet<Vector2>();
        final Queue<Vector2> possibleExplorationTiles = new ArrayDeque<Vector2>();

        /*
         * TODO: This search method blowwwwwwwwwwwwwwwwwwwwwws get some A* in
         * here
         */
        Vector2 currentSpace = space;
        do
        {
            final RoomContents contents = dungeon_.get(currentSpace);
            if(contents == RoomContents.LADDER)
            {
                return true;
            }
            /* Explore outwards */
            final Collection<Vector2> movableDirections = Vector2.directionalVectors();
            final Set<Vector2> reachableSpaces = new HashSet<Vector2>(movableDirections.size());
            final Vector2 centerSpace = currentSpace;
            movableDirections.forEach(direction ->
            {
                final Vector2 newSpaceToExplore = centerSpace.add(direction);
                if(dungeon_.containsKey(newSpaceToExplore)
                        && !exhaustedSpacesOnBoard.contains(newSpaceToExplore))
                {
                    final RoomContents roomContents = dungeon_.get(newSpaceToExplore);
                    if(RoomContents.isPassable(roomContents))
                    {
                        reachableSpaces.add(newSpaceToExplore);
                    }
                }
            });

            reachableSpaces.forEach(tile -> possibleExplorationTiles.add(tile));
            if(possibleExplorationTiles.isEmpty())
            {
                return false;
            }
            exhaustedSpacesOnBoard.add(currentSpace);
            exhaustedSpacesOnBoard.addAll(reachableSpaces);

            currentSpace = possibleExplorationTiles.remove();

        }
        while(!possibleExplorationTiles.isEmpty());

        return false;
    }

    private boolean canReachLadderFromGold()
    {
        final Vector2 goldSpace = dungeon_.entrySet().stream()
                .filter(entry -> entry.getValue() == RoomContents.GOLD)
                .map(entry -> entry.getKey()).findFirst().get();
        return canReachLadder(goldSpace);
    }

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

    public int height()
    {
        return (maximumYCoordinate() - minimumYCoordinate()) + 1;
    }

    private int maximumXCoordinate()
    {
        return dungeon_.keySet().stream().map(space -> space.getX()).max(Integer::compare).get();
    }

    private int maximumYCoordinate()
    {
        return dungeon_.keySet().stream().map(space -> space.getY()).max(Integer::compare).get();
    }

    private int minimumXCoordinate()
    {
        return dungeon_.keySet().stream().map(space -> space.getX()).min(Integer::compare).get();
    }

    private int minimumYCoordinate()
    {
        return dungeon_.keySet().stream().map(space -> space.getY()).min(Integer::compare).get();
    }

    private void placeGold()
    {
        Validate.isTrue(
                !dungeon_.containsValue(RoomContents.PIT)
                        && !dungeon_.containsValue(RoomContents.GOLD)
                        && dungeon_.containsValue(RoomContents.LADDER)
                        && dungeon_.containsValue(EMPTY), String.format(
                        "Cannot place gold in a dungeon with pits, with gold, "
                                + "without a ladder, or without empty spaces. "
                                + "This dungeon already contains %s", dungeon_.values()));

        Vector2 goldSpace;
        do
        {
            /*
             * Attempt to find a likely candidate for the ladder, placed
             * randomly
             */
            final int x = probableXCoordinate();
            final int y = probableYCoordinate();
            goldSpace = new Vector2(x, y);
        }
        while(!dungeon_.containsKey(goldSpace) || dungeon_.get(goldSpace) != EMPTY
                || !canReachLadder(goldSpace));
        dungeon_.put(goldSpace, RoomContents.GOLD);
    }

    private void placeLadder()
    {
        Validate.isTrue(dungeon_.values().stream().distinct().count() == 1
                && dungeon_.values().contains(EMPTY), String.format(
                "Cannot place a ladder in a pre-populated dungeon or a"
                        + " dungeon without any empty spaces."
                        + " This dungeon already contains %s", dungeon_.values()));

        Vector2 ladderSpace;
        do
        {
            /*
             * Attempt to find a likely candidate for the ladder, placed
             * randomly
             */
            final int x = probableXCoordinate();
            final int y = probableYCoordinate();
            ladderSpace = new Vector2(x, y);
        }
        while(!dungeon_.containsKey(ladderSpace));
        dungeon_.put(ladderSpace, RoomContents.LADDER);
    }

    private void placePits()
    {
        Validate.isTrue(
                !dungeon_.containsValue(RoomContents.PIT)
                        && dungeon_.containsValue(RoomContents.LADDER)
                        && dungeon_.containsValue(EMPTY), String.format(
                        "Cannot place pits in a dungeon with pits, "
                                + "without a ladder, or without empty spaces."
                                + " This dungeon already contains %s", dungeon_.values()));

        final double pitPercentage = pitPercentage();
        final long numPits = Math.round(dungeon_.size() * pitPercentage);
        int numPlacementRetries = 0;
        final int maxPlacementRetries = 50;

        for(int pitCount = 0; pitCount < numPits && numPlacementRetries < maxPlacementRetries;)
        {
            Vector2 pitSpace;
            do
            {
                final int x = probableXCoordinate();
                final int y = probableYCoordinate();
                pitSpace = new Vector2(x, y);
            }
            while(!dungeon_.containsKey(pitSpace) || dungeon_.get(pitSpace) != EMPTY);

            dungeon_.put(pitSpace, RoomContents.PIT);
            if(canReachLadderFromGold())
            {
                ++pitCount;
                numPlacementRetries = 0;
                System.out.println("Placed " + pitCount + " pits out of " + numPits);
            }
            else
            {
                /* Remove the pit, it's no good! */
                dungeon_.put(pitSpace, EMPTY);
                ++numPlacementRetries;
            }
        }
    }

    private void placeWumpus()
    {
        Validate.isTrue(
                !dungeon_.containsValue(RoomContents.WUMPUS)
                        && dungeon_.containsValue(RoomContents.LADDER)
                        && dungeon_.containsValue(EMPTY), String.format(
                        "Cannot place a wumpus in a dungeon with a wumpus, "
                                + "without a ladder, or without empty spaces."
                                + " This dungeon already contains %s", dungeon_.values()));

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

    private int probableXCoordinate()
    {
        final int minX = minimumXCoordinate();
        final int maxX = maximumXCoordinate();
        return RGEN.nextInt(maxX + 1 - minX) + minX;
    }

    private int probableYCoordinate()
    {
        final int minY = minimumYCoordinate();
        final int maxY = maximumYCoordinate();
        return RGEN.nextInt(maxY + 1 - minY) + minY;
    }

    private void setupDungeon()
    {
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
                    builder.append("?");
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

    public int width()
    {
        return (maximumYCoordinate() - minimumYCoordinate()) + 1;
    }
}
