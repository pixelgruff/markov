package utils;

public class OpenRange<T extends Number & Comparable<T>> extends Range<T>
{

    public OpenRange(final T min, final T max)
    {
        super(min, max);
    }

    @Override
    public boolean isWithin(final T value)
    {
        if(value == null)
        {
            return false;
        }
        return value.compareTo(min_) > 0 && value.compareTo(max_) < 0;
    }

}
