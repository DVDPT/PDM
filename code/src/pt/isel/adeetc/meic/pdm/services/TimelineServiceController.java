package pt.isel.adeetc.meic.pdm.services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import pt.isel.adeetc.meic.pdm.YambaApplication;
import pt.isel.adeetc.meic.pdm.YambaNavigation;
import pt.isel.adeetc.meic.pdm.YambaPreferences;
import pt.isel.adeetc.meic.pdm.common.IEventHandler;
import pt.isel.adeetc.meic.pdm.common.IEventHandlerArgs;
import pt.isel.adeetc.meic.pdm.common.IterableHelper;
import pt.isel.adeetc.meic.pdm.exceptions.ShouldNotHappenException;
import winterwell.jtwitter.Twitter;

import java.util.LinkedList;

final class TimelineServiceController implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private static final String LOG = "TimelineServiceController";
    private Iterable<Twitter.ITweet> _statusCache = new LinkedList<Twitter.ITweet>();
    private final TimelineServiceEventHandler _timelineServiceEventHandler = new TimelineServiceEventHandler();
    private final TwitterServiceClient _twitterFacade;
    private final YambaApplication _app;

    public TimelineServiceController()
    {
        _app = (YambaApplication) YambaApplication.getInstance();
        _twitterFacade = _app.getTwitterClient();
    }

    public Iterable<Twitter.ITweet> getStatusCache()
    {
        return _statusCache;
    }

    public void getUserTimelineAsync()
    {
        if (_app.isTimelineRefreshedAutomatically() && )
        {
            A
        }
        Intent timelineIntent = new Intent(YambaApplication.getContext(), TimelinePullService.class);

        int id = YambaApplication
                .getInstance()
                .getNavigationMessenger()
                .putElement(new TimelinePullServiceMessage(_timelineServiceEventHandler));

        timelineIntent.putExtra(YambaNavigation.timelineServiceParamName, id);
        YambaApplication.getContext().startService(timelineIntent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if (key.equals(YambaPreferences.timeLineFetchedAutomaticallyPropName)) ;
    }

    private final class TimelineServiceEventHandler implements IEventHandler<Iterable<Twitter.ITweet>>, Runnable
    {
        private volatile IEventHandlerArgs<Iterable<Twitter.ITweet>> _eventData = null;

        @Override
        public void invoke(Object sender, IEventHandlerArgs<Iterable<Twitter.ITweet>> data)
        {
            Log.d(LOG, "on timeline handler.");
            if (data.getError() == null)
            {
                Iterable<Twitter.ITweet> oldCacheRef = _statusCache;
                try
                {
                    synchronized (this)
                    {
                        _statusCache = data.getData();
                    }
                } catch (Exception e)
                {
                    throw new ShouldNotHappenException(e);
                }
                Log.d(LOG, "TimelineServiceEventHandler - persisting statuses.");
                _twitterFacade.getTweetDbSet().addAll(IterableHelper.except(_statusCache, oldCacheRef));
            }


            _eventData = data;
            Log.d(LOG, "TimelineServiceEventHandler - scheduling user handler.");
            _twitterFacade.getHandler().post(this);
        }

        @Override
        public void run()
        {
            Log.d(LOG, "TimelineServiceEventHandler - calling user handler.");
            if (_eventData == null)
                throw new ShouldNotHappenException("TwitterServiceClient::TimelineServiceEventHandler._event is null.");

            _twitterFacade.getUserTimelineCompletedEvent.invoke(this, _eventData);
            _eventData = null;
        }
    }
}
