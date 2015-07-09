package utils;

import java.util.Objects;

/**
 * Ranges are the same concept as mathematical intervals
 * (https://en.wikipedia.org/wiki/Interval_%28mathematics%29)
 *
 * Essentially, they represent some a continuum of values between some starting
 * position and some ending position. Whether or not the Range includes the
 * starting and/or ending positions is up to the implementation
 *
 * @param <T>
 *            Numerical Type of this range. Typically this is Integral types.
 */
public abstract class Range<T extends Number & Comparable<T>>
{
    protected final T max_;
    protected final T min_;

    /**
     * Constructs a new range object. Ranges cannot have null minimum or maximum
     * values. Additionally, the minimum value of a Range cannot be greater than
     * its maximum value.
     *
     * @param min
     *            Minimum value that this range supports
     * @param max
     *            Maximum value that this range supports
     */
    protected Range(final T min, final T max)
    {
        Validate.notNull(min, "Cannot create a Range with a null minimum value");
        Validate.notNull(max, "Cannot create a Range with a null maximum value");
        Validate.isTrue(min.compareTo(max) <= 0,
                "Cannot create a Range with a min that is greater than its max");
        min_ = min;
        max_ = max;
    }

    @Override
    public boolean equals(final Object other)
    {
        if(other instanceof Range)
        {
            @SuppressWarnings("rawtypes")
            final Range range = (Range) other;
            return Objects.equals(min_, range.min_) && Objects.equals(max_, range.max_);
        }
        return false;
    }

    public T getMaximumBounds()
    {
        return max_;
    }

    public T getMinimumBounds()
    {
        return min_;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(min_, max_);
    }

    /**
     * Determines if a value is within the range
     *
     * @param value
     *            An arbitrary value to check
     * @return True if the value is within the range, False otherwise
     */
    public abstract boolean isValueWithin(final T value);
}
