package pt.isel.adeetc.meic.pdm.extensions;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application
{
    private static BaseApplication _instance;

    public BaseApplication()
    {
        _instance = this;
    }

    public static Context getContext()
    {
        return _instance;
    }


}
