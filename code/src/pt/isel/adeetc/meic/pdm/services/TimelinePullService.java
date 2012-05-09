package pt.isel.adeetc.meic.pdm.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TimelinePullService extends Service
{
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {

    }

    @Override
    public void onDestroy()
    {

    }
}
