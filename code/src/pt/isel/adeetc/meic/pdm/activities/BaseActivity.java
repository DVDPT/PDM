package pt.isel.adeetc.meic.pdm.activities;

import android.app.Activity;
import android.app.Application;

public class BaseActivity<T extends Application> extends Activity
{
    @SuppressWarnings("unchecked")
    public T getApplicationInstance()
    {
        return (T) getApplication();
    }
}
