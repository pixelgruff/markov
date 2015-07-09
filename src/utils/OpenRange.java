package utils;

/**
 * OpenRange is the same concept as a mathematical Open Interval
 * (http://mathworld.wolfram.com/OpenInterval.html)
 *
 * An OpenRange does not include values at it's endpoints. IE,
 * isValueWithin(min) and isValueWithin(max) both return false.
 *
 * @param <T>
 *            Numerical Type of this range. Typically this is Integral types.
 */
public class OpenRange<T extends Number & Comparable<T>> extends Range<T>
{
    public OpenRange(final T min, final T max)
    {
        super(min, max);
        Validate.isTrue(max.compareTo(min) > 0, "Cannot construct an open range with "
                + "the same min and max");
    }

    @Override
    public boolean equals(final Object other)
    {
        return (other instanceof OpenRange) && super.equals(other);
    }

    @Override
    public boolean isValueWithin(final T value)
    {
        return value != null && value.compareTo(min_) > 0 && value.compareTo(max_) < 0;
    }

    @Override
    public String toString()
    {
        return String.format("(%d, %d)", min_, max_);
    }
}
