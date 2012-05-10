package pt.isel.adeetc.meic.pdm.extensions;

import android.app.Application;
import android.content.Context;
import pt.isel.adeetc.meic.pdm.common.NavigationMessenger;

public class BaseApplication extends Application
{
    private static BaseApplication _instance;


    private static NavigationMessenger _messenger = new NavigationMessenger();

    public BaseApplication()
    {
        _instance = this;
    }

    public static Context getContext()
    {
        return _instance;
    }

    public static BaseApplication getInstance()
    {
        return _instance;
    }

    //
    //  Should be static, but the code is more intuitive if used as
    //  an instance method @Diogo
    //
    public final NavigationMessenger getNavigationMessenger()
    {
        return _messenger;
    }




}
