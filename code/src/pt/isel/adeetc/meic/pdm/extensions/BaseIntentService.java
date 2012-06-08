package pt.isel.adeetc.meic.pdm.extensions;

import android.app.IntentService;
import android.app.Service;
import pt.isel.adeetc.meic.pdm.common.NavigationMessenger;

/**
 * Created by IntelliJ IDEA.
 * User: Sorcha
 * Date: 07-06-2012
 * Time: 13:31
 * To change this template use File | Settings | File Templates.
 */


public abstract class BaseIntentService<T extends BaseApplication> extends IntentService
{
    public BaseIntentService(String name) {
        super(name);
    }

    public final T getApplicationInstance()
    {
        return (T) getApplication();
    }

    public final NavigationMessenger getNavigationMessenger()
    {

        return getApplicationInstance().getNavigationMessenger();
    }
}
