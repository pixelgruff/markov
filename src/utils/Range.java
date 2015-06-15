package utils;

public abstract class Range<T extends Number & Comparable<T>>
{
    protected final T min_;
    protected final T max_;

    protected Range(final T min, final T max)
    {
        Validate.notNull(min, "Cannot create a Range with a null minimum value");
        Validate.notNull(max, "Cannot create a Range with a null maximum value");
        Validate.isTrue(min.compareTo(max) <= 0,
                "Cannot create a Range with a min that is greater than it's max");
        min_ = min;
        max_ = max;
    }

    public abstract boolean isWithin(final T value);
}
