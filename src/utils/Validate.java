package utils;

/**
 * Poor-man's Apache Validation.
 * 
 * Simple Validation for expressions/objects, expand as needed.
 *
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
     * Checks to see if the provided assertion is true.
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
     * Checks to see if the provided assertion is true.
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
     * Checks to see if the provided value is null.
     * 
     * @param value
     *            Value to check for null-ness
     */
    public static void notNull(final Object value)
    {
        notNull(value, "Validated object was null");
    }

    /**
     * Checks to see if the provided value is null.
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
     * Checks to see if value is within the open interval [min, max]
     * 
     * Note: This method should auto-box & unbox pretty much any valid numeric
     * argument
     * 
     * @param value
     *            Non-null value to check
     * @param min
     *            Non-null minimum of range
     * @param max
     *            Non-null maximum of range
     * 
     * @throws IllegalArgumentException
     *             if value is less than min or larger than max.
     */
    public static void inOpenInterval(final Number value, final Number min, final Number max)
    {
        notNull(value, "value cannot be null");
        notNull(min, "min cannot be null");
        notNull(max, "max cannot be null");
        failIfTrue(
                value.doubleValue() < min.doubleValue() || value.doubleValue() > max.doubleValue(),
                String.format("%d was not within [%d, %d]", value, min, max));
    }
}
