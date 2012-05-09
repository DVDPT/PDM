package pt.isel.adeetc.meic.pdm.services;

import android.content.Intent;
import android.os.IBinder;
import pt.isel.adeetc.meic.pdm.YambaBaseService;

import java.util.Timer;
import java.util.TimerTask;

public class TimelinePullService extends YambaBaseService
{
    private static String LOG = "TimelinePullService";
    private Timer _timer;
    private TimerTask _task;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent e, int flags, int id)
    {
        if (_task == null)
        {
            _task = new TimelinePullServiceTimerTask();
            _timer.scheduleAtFixedRate(_task, 0, getApplicationInstance().getTimelineRefreshPeriod() * 60 * 1000);
        }
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        _timer = new Timer();
    }

    @Override
    public void onDestroy()
    {

    }


    private class TimelinePullServiceTimerTask extends TimerTask
    {

        @Override
        public void run()
        {

        }
    }
}
