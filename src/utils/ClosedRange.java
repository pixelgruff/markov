package utils;

public class ClosedRange<T extends Number & Comparable<T>> extends Range<T>
{
    public ClosedRange(final T min, final T max)
    {
        super(min, max);
    }

    @Override
    public boolean isWithin(final T value)
    {
        return value != null && value.compareTo(min_) >= 0 && value.compareTo(max_) <= 0;
    }

}
