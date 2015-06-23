package utils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents a point in a 2-dimensional Cartesian Coordinate system
 * (https://en.wikipedia.org/wiki/Cartesian_coordinate_system)
 *
 * (x, y)
 *
 */
public final class Vector2
{
    public static Vector2 NORTH = new Vector2(0, 1);
    public static Vector2 NORTHEAST = new Vector2(1, 1);
    public static Vector2 EAST = new Vector2(1, 0);
    public static Vector2 SOUTHEAST = new Vector2(1, -1);
    public static Vector2 SOUTH = new Vector2(0, -1);
    public static Vector2 SOUTHWEST = new Vector2(-1, -1);
    public static Vector2 WEST = new Vector2(-1, 0);
    public static Vector2 NORTHWEST = new Vector2(-1, 1);

    private static final Vector2 [] CARDINAL_DIRECTIONS = { NORTH, EAST, SOUTH, WEST };
    private static final Vector2 [] ALL_DIRECTIONS = { NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH,
            SOUTHWEST, WEST, NORTHWEST };

    public static List<Vector2> cardinalDirections()
    {
        return Arrays.asList(CARDINAL_DIRECTIONS);
    }

    public static Vector2 nextCardinalDirection(final Vector2 direction)
    {
        final int index = cardinalDirections().indexOf(direction);
        if(index >= 0)
        {
            return cardinalDirections().get((index + 1) % cardinalDirections().size());
        }
        return null;
    }

    public static Vector2 previousCardinalDirection(final Vector2 direction)
    {
        final int index = cardinalDirections().indexOf(direction);
        if(index >= 0)
        {
            return cardinalDirections().get((index - 1) % cardinalDirections().size());
        }
        return null;
    }

    /**
     * Returns a List of unit vectors that represent the movement Vectors
     * necessary to transition from a space represented by a Vector2 to all of
     * it's surrounding positions.
     *
     * In other words, adding all of these to a single vector will result in a
     * collection of 8 Vector2s that represent the coordinates that are
     * immediately adjacent to the original vector
     *
     * @return Collection of movement vectors to transition to all adjacent
     *         spaces
     */
    public static List<Vector2> directionalVectors()
    {
        return Arrays.asList(ALL_DIRECTIONS);
    }

    private final int x_;

    private final int y_;

    /**
     * Constructs a Vector2 centered at the origin
     */
    public Vector2()
    {
        this(0, 0);
    }

    /**
     * Constructs a Vector2 with the provided X and Y coordinates
     *
     * @param x
     *            The x coordinate
     * @param y
     *            The y coordinate
     */
    public Vector2(final int x, final int y)
    {
        x_ = x;
        y_ = y;
    }

    /**
     * Creates a copy of the specified Vector2.
     *
     * @param other
     *            Vector to make a copy of.
     */
    public Vector2(final Vector2 other)
    {
        Validate.notNull(other, "Cannot create a copy of a null Vector2");
        x_ = other.x_;
        y_ = other.y_;
    }

    /**
     * Adds the two vectors together, returning the result.
     *
     * Note: Does not modify either Vector2.
     *
     * @param other
     *            Non-null Vector2 to add
     * @return A new Vector2 representing the result of the addition.
     */
    public Vector2 add(final Vector2 other)
    {
        if(other == null)
        {
            return this;
        }
        return new Vector2(x_ + other.x_, y_ + other.y_);
    }

    public double distanceBetween(final Vector2 other)
    {
        if(other == null)
        {
            return getMagnitude();
        }
        return subtract(other).getMagnitude();
    }

    @Override
    public boolean equals(final Object obj)
    {
        if(obj instanceof Vector2)
        {
            final Vector2 other = (Vector2) obj;
            /*
             * We use direct int compares instead of Objects.equals(...) as a
             * speed optimiziation
             */
            return x_ == other.x_ && y_ == other.y_;
        }
        return false;
    }

    public double getMagnitude()
    {
        return Math.sqrt(getMagnitudeSquared());
    }

    public double getMagnitudeSquared()
    {
        return x_ * x_ + y_ * y_;
    }

    /**
     * @return X Coordinate
     */
    public int getX()
    {
        return x_;
    }

    /**
     * @return Y coordinate
     */
    public int getY()
    {
        return y_;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x_, y_);
    }

    /**
     * Performs scalar multiplication on the Vector2
     *
     * @param scalar
     *            Scalar to multiply each component of the Vector2 by
     * @return The result of the multiplication (non-null)
     */
    public Vector2 multiply(final int scalar)
    {
        return new Vector2(x_ * scalar, y_ * scalar);
    }

    /**
     * Multiplies the two vectors together, returning the result.
     *
     * Note: Does not modify either Vector2.
     *
     * @param other
     *            Non-null Vector2 to multiply
     * @return A new Vector2 representing the result of the multiplication.
     */
    public Vector2 multiply(final Vector2 other)
    {
        Validate.notNull(other, "Cannot multiply a null vector");
        return new Vector2(x_ * other.x_, y_ * other.y_);
    }

    public double squaredDistanceBetween(final Vector2 other)
    {
        if(other == null)
        {
            return getMagnitudeSquared();
        }
        return subtract(other).getMagnitudeSquared();
    }

    /**
     * Subtracts the two vectors, returning the result.
     *
     * Note: Does not modify either Vector2.
     *
     * @param other
     *            Non-null Vector2 to subtract
     * @return A new Vector2 representing the result of the subtraction.
     */
    public Vector2 subtract(final Vector2 other)
    {
        if(other == null)
        {
            return this;
        }
        return new Vector2(x_ - other.x_, y_ - other.y_);
    }

    @Override
    public String toString()
    {
        return "(" + x_ + ", " + y_ + ")";
    }
}
