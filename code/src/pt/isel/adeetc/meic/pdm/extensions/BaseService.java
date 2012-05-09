package pt.isel.adeetc.meic.pdm.extensions;

import android.app.Service;
import pt.isel.adeetc.meic.pdm.common.NavigationMessenger;

public abstract class BaseService<T extends BaseApplication> extends Service
{
    @SuppressWarnings("unchecked")
    public final T getApplicationInstance()
    {
        return (T) getApplication();
    }

    public final NavigationMessenger getNavigationMessenger()
    {
        return getApplicationInstance().getNavigationMessenger();
    }
}
