package utils;

import java.util.Objects;

/**
 * Represents a point in 2D space
 *
 * (x, y)
 *
 */
public final class Vector2
{

    private final int x_;
    private final int y_;

    /**
     * Creates a new Vector2 with the specified properties
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

    @Override
    public String toString()
    {
        return "(" + x_ + ", " + y_ + ")";
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(obj instanceof Vector2)
        {
            Vector2 other = (Vector2) obj;
            return Objects.equals(x_, other.x_) && Objects.equals(y_, other.y_);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x_, y_);
    }
}
