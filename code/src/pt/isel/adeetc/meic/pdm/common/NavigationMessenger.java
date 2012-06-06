package pt.isel.adeetc.meic.pdm.common;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.NoSuchElementException;

public final class NavigationMessenger
{
    private final String _noElementFoundFormat = "No element has found with id %d";
    final private HashMap<Integer, WeakReference<Object>> _references;

    public NavigationMessenger()
    {
        _references = new HashMap<Integer, WeakReference<Object>>();
    }


    public int putElement(Object o)
    {
        int hashCode = o.hashCode();
        _references.put(hashCode, new WeakReference<Object>(o));
        return hashCode;
    }

    public Object getElement(int id)
    {
        Object ret = getElementAndPreserve(id);
        _references.get(id).clear();
        _references.remove(id);
        return ret;
    }

    public Object getElementAndPreserve(int id)
    {
        if (!_references.containsKey(id))
            throw new NoSuchElementException(String.format(_noElementFoundFormat, id));

        Object obj = _references.get(id).get();

        if (obj == null)
            Log.v("NavigationMessenger", String.format("Returning null object with id %d", id));
        return obj;
    }

    public Object remove(int id)
    {
        return getElement(id);
    }

}
