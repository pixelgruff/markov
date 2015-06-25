package utils;

import java.util.Iterator;

/**
 * Poor-man's Apache's Iterators
 */
public class Iterators
{

    public static <T> Iterator<T> cycle(final Iterable<T> iterable)
    {
        Validate.notNull(iterable, "Cannot create a cyclic iterator for a null iterable");
        return new Iterator<T>()
        {
            private Iterator<T> iterator_ = iterable.iterator();

            @Override
            public boolean hasNext()
            {
                return iterator_.hasNext() || iterable.iterator().hasNext();
            }

            @Override
            public T next()
            {
                if(iterator_.hasNext())
                {
                    return iterator_.next();
                }
                iterator_ = iterable.iterator();
                return iterator_.next();
            }
        };
    }
}
