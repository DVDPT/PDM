package pt.isel.adeetc.meic.pdm.common;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public final class IterableHelper
{

    private IterableHelper()
    {
    }

    public static <T, E extends T> Iterable<T> getSuperIterable(Iterable<E> iter, Class<T> superClass)
    {
        return Iterables.transform(iter, new Function<E, T>()
        {
            @Override
            public T apply(E e)
            {
                return e;
            }
        });
    }

    public static <T> Iterable<T> except(Iterable<T> first, Iterable<T> second)
    {
        final Iterable<? extends T> secondIter = second;
        return Iterables.filter(first, new Predicate<T>()
        {
            @Override
            public boolean apply(T o)
            {
                return !Iterables.contains(secondIter, o);
            }
        });
    }
}
