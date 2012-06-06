package pt.isel.adeetc.meic.pdm.services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import pt.isel.adeetc.meic.pdm.YambaBaseService;
import pt.isel.adeetc.meic.pdm.YambaNavigation;
import pt.isel.adeetc.meic.pdm.YambaPreferences;
import pt.isel.adeetc.meic.pdm.common.GenericEventArgs;
import pt.isel.adeetc.meic.pdm.common.IEventHandler;
import pt.isel.adeetc.meic.pdm.common.IterableHelper;
import pt.isel.adeetc.meic.pdm.exceptions.ShouldNotHappenException;
import winterwell.jtwitter.Twitter;

public class TimelinePullService extends YambaBaseService implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private static String LOG = "TimelinePullService";

    private Thread _task;
    private volatile boolean _isCancelled = false;
    IEventHandler<Iterable<Twitter.ITweet>> _callback;

    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent e, int flags, int id)
    {
        super.onStartCommand(e, flags, id);
        launchOperation(e);
        return START_STICKY;
    }

    private void launchOperation(Intent e)
    {
        int paramId = e.getIntExtra(YambaNavigation.timelineServiceParamName, -1);
        if (paramId == -1)
            throw new ShouldNotHappenException("TimelinePullService.onStartCommand : param is -1");

        TimelinePullServiceMessage message = (TimelinePullServiceMessage) getNavigationMessenger().getElement(paramId);

        _callback = message.getCallback();


        if(!getApplicationInstance().isTimelineRefreshedAutomatically())
        {
            new TimelinePullServiceTimerTask().start();
            return;
        }

        if(_task == null)
        {
            _task = new TimelinePullServiceTimerTask();
            _task.start();
        }
        else
        {
            _task.interrupt();
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .unregisterOnSharedPreferenceChangeListener(this);


        cancelTask();
    }

    private void cancelTask()
    {
        if (_task == null)
            return;

        _isCancelled = true;
        _task.interrupt();
        _task = null;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if(key.equals(YambaPreferences.timeLineFetchedAutomaticallyPropName))
            cancelTask();

    }

    private class TimelinePullServiceTimerTask extends Thread
    {

        @Override
        public void run()
        {
            do
            {
                Log.d(LOG,"Running Task");
                Twitter client = getApplicationInstance().getTwitterClient().getTwitter();
                Exception error = null;


                Iterable<Twitter.ITweet> statuses = null;

                try
                {
                    statuses =  IterableHelper.getSuperIterable(client.getUserTimeline(), Twitter.ITweet.class);

                } catch (Exception e)
                {
                    error = e;
                }


                if (_callback == null)
                {
                    ///
                    /// If the service was destroyed.
                    ///
                    if (_isCancelled)
                        return;

                    Log.d(LOG,"Error on task.");
                    throw new ShouldNotHappenException("TimelinePullService.TimelinePullServiceTimerTask.run: callback is null");
                }

                Log.d(LOG,"Calling event handler");
                _callback.invoke(this, new GenericEventArgs<Iterable<Twitter.ITweet>>(statuses, error));


                if (_isCancelled || !getApplicationInstance().isTimelineRefreshedAutomatically())
                    break;

                try
                {
                    Log.d(LOG,"Going to sleep.");
                    Thread.sleep(getApplicationInstance().getTimelineRefreshPeriod() * 60 * 1000);
                } catch (InterruptedException e)
                {
                    if (_isCancelled)
                        break;
                }
            } while (true);

            _isCancelled = false;
        }
    }
}
