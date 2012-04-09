package pt.isel.adeetc.meic.pdm.extensions;

import android.app.Activity;
import android.app.Application;
import pt.isel.adeetc.meic.pdm.common.NavigationMessenger;

public class BaseActivity<T extends Application> extends Activity
{
    private static NavigationMessenger _messenger = new NavigationMessenger();

    @SuppressWarnings("unchecked")
    public final T getApplicationInstance()
    {
        return (T) getApplication();
    }

    //
    //  Should be static, but the code is more intuitive if used as
    //  an instance method @Diogo
    //
    protected final NavigationMessenger getNavigationMessenger()
    {
        return _messenger;
    }


}
