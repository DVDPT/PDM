package pt.isel.adeetc.meic.pdm.extensions;

import android.app.Activity;
import pt.isel.adeetc.meic.pdm.common.NavigationMessenger;

public class BaseActivity<T extends BaseApplication> extends Activity
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
