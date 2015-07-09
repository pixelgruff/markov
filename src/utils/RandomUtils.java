package utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils
{
    public static <T> T randomOf(final Collection<T> collection)
    {
        if(collection == null || collection.isEmpty())
        {
            return null;
        }

        final int size = collection.size();
        final int chosenIndex = ThreadLocalRandom.current().nextInt(size);
        final Iterator<T> iterator = collection.iterator();
        for(int index = 0; iterator.hasNext() && index < chosenIndex; ++index)
        {
            iterator.next();
        }

        Validate.isTrue(iterator.hasNext(), "Cannot get a random element from a Collection"
                + " that has a misbehaving iterator");
        return iterator.next();
    }

    public static <T> T randomOf(final List<T> list)
    {
        if(list == null || list.isEmpty())
        {
            return null;
        }

        final int size = list.size();
        final int chosenIndex = ThreadLocalRandom.current().nextInt(size);
        return list.get(chosenIndex);
    }

    @SafeVarargs
    public static <T> T randomOf(final T... params)
    {
        if(params == null || params.length == 0)
        {
            return null;
        }

        final int size = params.length;
        final int chosenIndex = ThreadLocalRandom.current().nextInt(size);
        return params[chosenIndex];
    }
}
