package pt.isel.adeetc.meic.pdm.common;

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
        _references.put(hashCode,new WeakReference<Object>(o));
        return hashCode;
    }

    public Object getElement(int id)
    {
        if(!_references.containsKey(id))
            throw new NoSuchElementException(String.format(_noElementFoundFormat,id));
        return _references.get(id);
    }


}
