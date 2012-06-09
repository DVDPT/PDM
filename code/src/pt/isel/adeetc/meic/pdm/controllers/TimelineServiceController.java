package pt.isel.adeetc.meic.pdm.controllers;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import pt.isel.adeetc.meic.pdm.YambaApplication;
import pt.isel.adeetc.meic.pdm.YambaNavigation;
import pt.isel.adeetc.meic.pdm.YambaPreferences;
import pt.isel.adeetc.meic.pdm.common.GenericEventArgs;
import pt.isel.adeetc.meic.pdm.common.IEventHandler;
import pt.isel.adeetc.meic.pdm.common.IEventHandlerArgs;
import pt.isel.adeetc.meic.pdm.exceptions.ShouldNotHappenException;
import pt.isel.adeetc.meic.pdm.extensions.BoundedServiceClient;
import pt.isel.adeetc.meic.pdm.services.TimelineContentProvider;
import pt.isel.adeetc.meic.pdm.services.TimelinePullService;
import winterwell.jtwitter.Twitter;

import java.util.LinkedList;

final class TimelineServiceController extends ContentObserver implements SharedPreferences.OnSharedPreferenceChangeListener, IEventHandler<IEventHandler<Iterable<Twitter.ITweet>>>
{
    private static final String LOG = "TimelineServiceController";

    private Iterable<Twitter.ITweet> _statusCache = new LinkedList<Twitter.ITweet>();

    private final TwitterServiceClient _twitterFacade;

    private final YambaApplication _app;


    private AlarmManager _alarmManager;
    private PendingIntent _periodicIntent;

    private volatile boolean _boundedRequestActive;


    public TimelineServiceController(TwitterServiceClient cli, Handler handler)
    {
        super(handler);
        _app = (YambaApplication) YambaApplication.getInstance();
        _twitterFacade = cli;
        _app.getContentResolver().registerContentObserver(TimelineContentProvider.CONTENT_URI, true, this);
        _boundedServiceClient.setCallServiceWhenConnected(true);
        _twitterFacade.getUserTimelineCompletedEvent.setOnEventHandlerChanged(this);


        initialize();
    }

    private void initialize()
    {
        onChange(false);
        if (_app.isTimelineRefreshedAutomatically() && !isAlarmDeployed())
            deployPeriodicAlarm();
    }

    public Iterable<Twitter.ITweet> getStatusCache()
    {
        return _statusCache;
    }

    @Override
    public void onChange(boolean ignore)
    {
        Log.d(LOG, "On Content Observer");

        if (_boundedRequestActive)
            return;

        _statusCache = _twitterFacade.getTweetDbSet().getAll();
        notifyUiOfChanges(_statusCache, null);

    }

    public void getUserTimelineAsync()
    {
        if (_app.isTimelineRefreshedAutomatically() && !isAlarmDeployed())
            deployPeriodicAlarm();

        Log.d(LOG, "Getting timeline");
        _boundedRequestActive = true;
        if (!_boundedServiceClient.isServiceConnected())
        {
            Log.d(LOG, "Binding Timeline Service");
            _app.bindService(getServiceIntent(), _boundedServiceClient.getServiceConnection(), Context.BIND_AUTO_CREATE);
            return;
        }

        _boundedServiceClient.callService();


    }

    private Intent getServiceIntent()
    {
        return new Intent(_app, TimelinePullService.class);

    }

    private void deployPeriodicAlarm()
    {
        int refreshPeriod = _app.getTimelineRefreshPeriod() * 60 * 1000;
        Log.d(LOG, "Deploying alarm");
        _alarmManager = (AlarmManager) _app.getSystemService(Application.ALARM_SERVICE);

        _periodicIntent = PendingIntent.getService(_app, 0, getServiceIntent(), 0);
        _alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, refreshPeriod, refreshPeriod, _periodicIntent);
    }

    private boolean isAlarmDeployed()
    {
        return _alarmManager != null;
    }

    private void cancelPeriodicAlarm()
    {
        if (_alarmManager == null)
            throw new ShouldNotHappenException("Alarm manager is null");

        _alarmManager.cancel(_periodicIntent);
        _periodicIntent = null;
        _alarmManager = null;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if (key.equals(YambaPreferences.timeLineFetchedAutomaticallyPropName))
        {
            if (_app.isTimelineRefreshedAutomatically())
            {
                if (!isAlarmDeployed())
                    deployPeriodicAlarm();
            } else
            {
                cancelPeriodicAlarm();
            }
        }
    }

    public void notifyUiOfChanges(Iterable<Twitter.ITweet> statuses, Exception error)
    {
        Log.d(LOG, "on timeline handler.");
        final GenericEventArgs<Iterable<Twitter.ITweet>> data = new GenericEventArgs<Iterable<Twitter.ITweet>>(statuses, error);
        Log.d(LOG, "TimelineServiceEventHandler - scheduling user handler.");
        _app.getUiHandler().post(new Runnable()
        {
            @Override
            public void run()
            {
                Log.d(LOG, "TimelineServiceEventHandler - calling user handler.");
                _twitterFacade.getUserTimelineCompletedEvent.invoke(this, data);
            }
        });
    }

    @Override
    public void invoke(Object sender, IEventHandlerArgs<IEventHandler<Iterable<Twitter.ITweet>>> args)
    {
        try
        {
            if (args.getData() == null && _boundedServiceClient.isServiceConnected())
            {
                Log.d(LOG, "Unbinding Timeline service");
                _boundedServiceClient.unbind(_app);
            }
        } catch (Exception e)
        {
            throw new ShouldNotHappenException(e);
        }
    }

    private final BoundedServiceClient _boundedServiceClient = new BoundedServiceClient()
    {
        @Override
        protected void fillServiceRequest(Message message)
        {
        }

        @Override
        protected void onServiceResponse(Message message)
        {

            Log.d(LOG, "onServiceResponse");
            Exception error = null;
            int val = message.getData().getInt(YambaNavigation.TIMELINE_SERVICE_RESULT_PARAM_NAME);

            if (val == TimelinePullService.SERVICE_RESPONSE_ERROR)
                error = (Exception) message.getData().getSerializable(YambaNavigation.TIMELINE_SERVICE_ERROR_PARAM_NAME);
            else
                _statusCache = _twitterFacade.getTweetDbSet().getAll();

            notifyUiOfChanges(_statusCache, error);
            _boundedRequestActive = false;
        }
    };
}
