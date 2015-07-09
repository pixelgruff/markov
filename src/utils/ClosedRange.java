package utils;

/**
 * ClosedRange is the same concept as a mathematical Closed Interval
 * (http://mathworld.wolfram.com/ClosedInterval.html)
 *
 * A ClosedRange includes values at it's endpoints. IE, isValueWithin(min) and
 * isValueWithin(max) both return true.
 *
 * @param <T>
 *            Numerical Type of this range. Typically this is Integral types.
 */
public class ClosedRange<T extends Number & Comparable<T>> extends Range<T>
{
    public ClosedRange(final T min, final T max)
    {
        super(min, max);
    }

    @Override
    public boolean equals(final Object other)
    {
        return (other instanceof ClosedRange) && super.equals(other);
    }

    @Override
    public boolean isValueWithin(final T value)
    {
        return value != null && value.compareTo(min_) >= 0 && value.compareTo(max_) <= 0;
    }

    @Override
    public String toString()
    {
        return String.format("[%d, %d]", min_, max_);
    }
}
