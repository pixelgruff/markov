package utils;

import java.util.Collection;
import java.util.Map;

/**
 * Poor-man's Apache Validation. Simple Validation for expressions/objects,
 * expand as needed.
 */
public final class Validate
{
    // Internal checker
    private static void failIfTrue(final boolean expression, final String message)
    {
        if(expression)
        {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Validates that the provided assertion is true. Throws an
     * IllegalArgumentException if it is not.
     *
     * @param assertion
     *            Checks the assertion for truthiness
     * @throws IllegalArgumentException
     *             if the assertion is false
     */
    public static void isTrue(final boolean assertion)
    {
        isTrue(assertion, "Validated assertion was false");
    }

    /**
     * Validates that the provided assertion is true. Throws an
     * IllegalArgumentException if it is not.
     *
     * @param assertion
     *            Assertion to check for truthiness
     * @param message
     *            Optional message to add to the IllegalArgumentException, if
     *            thrown
     */
    public static void isTrue(final boolean assertion, final String message)
    {
        failIfTrue(!assertion, message);
    }

    /**
     * Validates that the provided assertion is false. Throws an
     * IllegalArgumentException if it is not.
     *
     * @param assertion
     *            Checks the assertion for truthiness
     * @throws IllegalArgumentException
     *             if the assertion is false
     */
    public static void isFalse(final boolean assertion)
    {
        isTrue(assertion, "Validated assertion was false");
    }

    /**
     * Validates that the provided assertion is false. Throws an
     * IllegalArgumentException if it is not.
     *
     * @param assertion
     *            Assertion to check for truthiness
     * @param message
     *            Optional message to add to the IllegalArgumentException, if
     *            thrown
     */
    public static void isFalse(final boolean assertion, final String message)
    {
        failIfTrue(assertion, message);
    }

    /**
     * Validates that the collection provided is either null or empty. Throws an
     * IllegalArgumentException if it is not.
     *
     * @param collection
     *            Collection to check
     */
    public static <T> void isEmpty(final Collection<T> collection)
    {
        isEmpty(collection, "Collection was not empty");
    }

    /**
     * Validates that the collection provided is either null or empty. Throws an
     * IllegalArgumentException if it is not.
     *
     * @param collection
     *            Collection to check
     * @param message
     *            Optional message to add to the IllegalArgumentException, if
     *            thrown
     */
    public static <T> void isEmpty(final Collection<T> collection, final String message)
    {
        failIfTrue(collection != null && !collection.isEmpty(), message);
    }

    /**
     * Validates that the collection provided is either not null and not empty.
     * Throws an IllegalArgumentException if it is not.
     *
     * @param collection
     *            Collection to check
     */
    public static <T> void notEmpty(final Collection<T> collection)
    {
        notEmpty(collection, "Collection was empty");
    }

    /**
     * Validates that the collection provided is either not null and not empty.
     * Throws an IllegalArgumentException if it is not.
     *
     * @param collection
     *            Collection to check
     * @param message
     *            Optional message to add to the IllegalArgumentException, if
     *            thrown
     */
    public static <T> void notEmpty(final Collection<T> collection, final String message)
    {
        failIfTrue(collection == null || collection.isEmpty(), message);
    }

    /**
     * Validates that the collection provided is either not null and not empty.
     * Throws an IllegalArgumentException if it is not.
     *
     * @param collection
     *            Collection to check
     */
    public static <T, U> void notEmpty(final Map<T, U> map)
    {
        notEmpty(map, "Map was empty");
    }

    /**
     * Validates that the collection provided is either not null and not empty.
     * Throws an IllegalArgumentException if it is not.
     *
     * @param collection
     *            Collection to check
     * @param message
     *            Optional message to add to the IllegalArgumentException, if
     *            thrown
     */
    public static <T, U> void notEmpty(final Map<T, U> map, final String message)
    {
        failIfTrue(map == null || map.isEmpty(), message);
    }

    /**
     * Validates that the object provided is not null. Throws an
     * IllegalArgumentException if it is not.
     *
     * @param value
     *            Value to check for null-ness
     */
    public static void notNull(final Object value)
    {
        notNull(value, "Validated object was null");
    }

    /**
     * Validates that the object provided is not null. Throws an
     * IllegalArgumentException if it is not.
     *
     * @param value
     *            Value to check for null-ness
     * @param message
     *            Optional message to add to the IllegalArgumentException, if
     *            thrown
     */
    public static void notNull(final Object value, final String message)
    {
        failIfTrue(value == null, message);
    }

    /**
     * Checks to see if the provided value is not null.
     *
     * @param value
     *            Value to check for null-ness
     */
    public static void isNull(final Object value)
    {
        isNull(value, "Validated object was null");
    }

    /**
     * Checks to see if the provided value is not null.
     *
     * @param value
     *            Value to check for null-ness
     * @param message
     *            Optional message to add to the IllegalArgumentException, if
     *            thrown
     */
    public static void isNull(final Object value, final String message)
    {
        failIfTrue(value != null, message);
    }

    /**
     * Validates that the value is within the open interval [min, max]. Throws
     * an IllegalArgumentException if it is not. Note: This method should
     * auto-box & unbox pretty much any valid numeric argument
     *
     * @param value
     *            Non-null value to check
     * @param min
     *            Non-null minimum of range
     * @param max
     *            Non-null maximum of range
     * @throws IllegalArgumentException
     *             if value is less than min or larger than max.
     */
    public static <T extends Number & Comparable<T>> void inOpenInterval(final T value,
            final T min, final T max)
    {
        inRange(value, new OpenRange<T>(min, max));
    }

    public static <T extends Number & Comparable<T>> void inRange(final T value,
            final Range<T> range)
    {
        notNull(range, "Cannot determine if a value is within a null range");
        isTrue(range.isValueWithin(value), String.format("%d was not within %s", value, range));
    }
}
