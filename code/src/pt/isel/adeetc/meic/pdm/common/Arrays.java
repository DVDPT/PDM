package pt.isel.adeetc.meic.pdm.common;

import com.google.common.base.Predicate;

public final class Arrays
{
    private Arrays()
    {
    }

    public static <T> boolean all(T[] arr, Predicate<T> pred)
    {
        for (T t : arr)
        {
            if (!pred.apply(t))
                return false;
        }
        return true;
    }

    public static <T> boolean contains(T[] arr, T value)
    {
        for (T t : arr)
            if (t.equals(value))
                return true;
        return false;
    }
}
