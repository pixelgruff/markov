package utils;

import java.util.ArrayList;
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
    public static List<Vector2> cardinalDirections()
    {
        final int totalDirections = 4;
        final List<Vector2> cardinalDirections = new ArrayList<Vector2>(totalDirections);
        /* TODO: Clean up, this is messy */
        for(int dX = -1; dX <= 1; ++dX)
        {
            for(int dY = -1; dY <= 1; ++dY)
            {
                if(dY == 0 || dX == 0)
                {
                    continue;
                }
                final Vector2 cardinalDirection = new Vector2(dX, dY);
                cardinalDirections.add(cardinalDirection);
            }

        }
        return cardinalDirections;
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
        final int totalDirections = 8;
        final List<Vector2> directionalVectors = new ArrayList<Vector2>(totalDirections);
        /* TODO: Clean up, this is messy */
        for(int dX = -1; dX <= 1; ++dX)
        {
            for(int dY = -1; dY <= 1; ++dY)
            {
                if(dX == 0 && dY == 0)
                {
                    /*
                     * These directions specify actual "movement", which a
                     * 0-direction vector does not have...
                     */
                    continue;
                }

                final Vector2 directionalVector = new Vector2(dX, dY);
                directionalVectors.add(directionalVector);
            }
        }
        return directionalVectors;
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
        Validate.notNull(other, "Cannot add a null vector");
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
        Validate.notNull(other, "Cannot subtract a null vector");
        return new Vector2(x_ - other.x_, y_ - other.y_);
    }

    @Override
    public String toString()
    {
        return "(" + x_ + ", " + y_ + ")";
    }
}
