package pt.isel.adeetc.meic.pdm.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import pt.isel.adeetc.meic.pdm.YambaApplication;
import pt.isel.adeetc.meic.pdm.YambaNavigation;
import pt.isel.adeetc.meic.pdm.common.GenericEventArgs;
import pt.isel.adeetc.meic.pdm.common.IterableHelper;
import pt.isel.adeetc.meic.pdm.exceptions.ShouldNotHappenException;
import winterwell.jtwitter.Twitter;

public class TimelinePullService extends IntentService implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private static String LOG = "TimelinePullService";


    public TimelinePullService()
    {
        super(LOG);
    }

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
    protected void onHandleIntent(Intent e)
    {
        int paramId = e.getIntExtra(YambaNavigation.timelineServiceParamName, -1);
        if (paramId == -1)
            throw new ShouldNotHappenException("TimelinePullService.onStartCommand : param is -1");

        TimelinePullServiceMessage message = (TimelinePullServiceMessage) getApplicationInstance().getNavigationMessenger().getElement(paramId);

        Iterable<Twitter.ITweet> statuses = null;
        Exception error = null;


        try
        {
            statuses = IterableHelper.getSuperIterable(getApplicationInstance().getTwitterClient().getTwitter().getUserTimeline(), Twitter.ITweet.class);
        } catch (Exception err)
        {
            error = err;
        }

        message.getCallback().invoke(this, new GenericEventArgs<Iterable<Twitter.ITweet>>(statuses, error));

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


    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
//        if (key.equals(YambaPreferences.timeLineFetchedAutomaticallyPropName))
        //          cancelTask();

    }

    public YambaApplication getApplicationInstance()
    {
        return (YambaApplication) getApplication();
    }

}
