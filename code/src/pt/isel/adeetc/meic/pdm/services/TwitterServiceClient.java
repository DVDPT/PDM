package pt.isel.adeetc.meic.pdm.services;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import com.google.common.collect.Iterables;
import pt.isel.adeetc.meic.pdm.NetworkReceiver;
import pt.isel.adeetc.meic.pdm.YambaApplication;
import pt.isel.adeetc.meic.pdm.YambaNavigation;
import pt.isel.adeetc.meic.pdm.common.*;
import pt.isel.adeetc.meic.pdm.common.db.IDbSet;
import pt.isel.adeetc.meic.pdm.exceptions.Constants;
import pt.isel.adeetc.meic.pdm.exceptions.ShouldNotHappenException;
import winterwell.jtwitter.Twitter;

public final class TwitterServiceClient implements IEventHandler<Iterable<Twitter.ITweet>>
{
    private static final String LOG = "TwitterServiceClient";

    public final IEvent<Integer> updateStatusCompletedEvent;
    public final IEvent<Iterable<Twitter.ITweet>> getUserTimelineCompletedEvent;

    private Iterable<Twitter.ITweet> _statusCache;

    private final StatusEventHandler _statusEventHandler = new StatusEventHandler();

    private Handler _handler;
    private Twitter _twitter;
    private final IDbSet<Twitter.ITweet> _tweetDb;


    public TwitterServiceClient(IDbSet<Twitter.ITweet> tweetDb)
    {

        _tweetDb = tweetDb;
        updateStatusCompletedEvent = new GenericEvent<Integer>();
        getUserTimelineCompletedEvent = new GenericEvent<Iterable<Twitter.ITweet>>();
        _handler = new Handler();
    }


    public void updateStatusAsync(String status)
    {
        Intent statusUpload = new Intent(YambaApplication.getContext(), StatusUploadService.class);

        int id = YambaApplication.getInstance().getNavigationMessenger()
                .putElement(new StatusUploadServiceMessage(_statusEventHandler, status));

        statusUpload.putExtra("params", id);

        YambaApplication.getContext().startService(statusUpload);
    }

    @SuppressWarnings({"unchecked"})
    public void getUserTimelineAsync()
    {

        Intent timelineIntent = new Intent(YambaApplication.getContext(), TimelinePullService.class);
        int id = YambaApplication.getInstance().getNavigationMessenger()
                .putElement(new TimelinePullServiceMessage(this));
        timelineIntent.putExtra(YambaNavigation.timelineServiceParamName, id);
        YambaApplication.getContext().startService(timelineIntent);
    }

    public Iterable<Twitter.ITweet> getTwitterCachedTimeline()
    {
        return _statusCache;
    }


    public void configureTwitterClient(String user, String password, String apiRootUrl)
    {
        _twitter = new Twitter(user, password);
        _twitter.setAPIRootUrl(apiRootUrl);
    }

    public Twitter getTwitter()
    {
        if (_twitter == null)
            throw new ShouldNotHappenException(Constants.NoUserAndPasswordDefined);
        return _twitter;
    }


    @Override
    public void invoke(Object sender, IEventHandlerArgs<Iterable<Twitter.ITweet>> data)
    {

        Log.d(LOG, "on timeline handler.");
        if (data.getError() == null)
        {
            try
            {
                _statusCache = data.getData();
            } catch (Exception e)
            {
                throw new ShouldNotHappenException(e);
            }
        }

        _tweetDb.add(Iterables.getFirst(_statusCache, null));
        _statusCache = _tweetDb;

        final IEventHandlerArgs<Iterable<Twitter.ITweet>> fdata = new GenericEventArgs<Iterable<Twitter.ITweet>>(_statusCache, data.getError());
        //final IEventHandlerArgs<Iterable<Twitter.ITweet>> fdata = data;
        Log.d(LOG, "calling user handler.");
        _handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                getUserTimelineCompletedEvent.invoke(this, fdata);
            }
        });
    }

    private class StatusEventHandler implements IEventHandler<Integer>, Runnable
    {
        private IEventHandlerArgs<Integer> _args;

        @Override
        public void invoke(Object sender, IEventHandlerArgs<Integer> statusIEventHandlerArgs)
        {
            _handler.post(this);
            _args = statusIEventHandlerArgs;
        }

        @Override
        public void run()
        {
            updateStatusCompletedEvent.invoke(this, _args);

        }
    }
}

