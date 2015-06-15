package utils;

import java.util.Objects;

public class OpenRange<T extends Number & Comparable<T>> extends Range<T>
{
    public OpenRange(final T min, final T max)
    {
        super(min, max);
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

    @Override
    public boolean equals(final Object other)
    {
        if(!(other instanceof OpenRange))
        {
            return false;
        }

        @SuppressWarnings("rawtypes")
        final OpenRange range = (OpenRange) other;
        return Objects.equals(min_, range.min_) && Objects.equals(max_, range.max_);
    }
}
