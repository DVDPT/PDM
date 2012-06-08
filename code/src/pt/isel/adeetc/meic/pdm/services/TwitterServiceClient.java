package pt.isel.adeetc.meic.pdm.services;

import android.accounts.NetworkErrorException;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import com.google.common.collect.Iterables;
import pt.isel.adeetc.meic.pdm.R;
import pt.isel.adeetc.meic.pdm.YambaApplication;
import pt.isel.adeetc.meic.pdm.YambaNavigation;
import pt.isel.adeetc.meic.pdm.common.*;
import pt.isel.adeetc.meic.pdm.common.db.IDbSet;
import pt.isel.adeetc.meic.pdm.exceptions.Constants;
import pt.isel.adeetc.meic.pdm.exceptions.ShouldNotHappenException;
import winterwell.jtwitter.Twitter;

import java.util.LinkedList;

public final class TwitterServiceClient
{
    private static final String LOG = "TwitterServiceClient";

    public final IEvent<Integer> updateStatusCompletedEvent;
    public final IEvent<Iterable<Twitter.ITweet>> getUserTimelineCompletedEvent;

    public final IEventHandler<Boolean> _publisherEvent;

    private Iterable<Twitter.ITweet> _statusCache= new LinkedList<Twitter.ITweet>();
    private final StatusEventHandler _statusEventHandler = new StatusEventHandler();
    private final TimelineServiceEventHandler _timelineServiceEventHandler = new TimelineServiceEventHandler();

    private Handler _handler;
    private Twitter _twitter;
    private final IDbSet<Twitter.ITweet> _tweetDb;



    public TwitterServiceClient(IDbSet<Twitter.ITweet> tweetDb)
    {
        _tweetDb = tweetDb;
        _handler = new Handler();
        updateStatusCompletedEvent = new GenericEvent<Integer>();
        getUserTimelineCompletedEvent = new GenericEvent<Iterable<Twitter.ITweet>>();
        _publisherEvent = new PublisherEventHandler();

        ((YambaApplication)(YambaApplication.getInstance())).getNetworkEvent().addEventHandler(_publisherEvent);



        ThreadPool.QueueUserWorkItem(new Runnable()
        {
            @Override
            public void run()
            {
                Iterable<Twitter.ITweet> status = _tweetDb.getAll();
                Log.d(LOG, "Retrieved saved status.");
                if (_statusCache != null)
                    return;

                synchronized (this)
                {
                    if (_statusCache != null || Iterables.size(status) == 0)
                        return;

                    Log.d(LOG, "Setting on cached.");

                    _statusCache = status;
                }
            }
        });

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
        int id = YambaApplication
                .getInstance()
                .getNavigationMessenger()
                .putElement(new TimelinePullServiceMessage(_timelineServiceEventHandler));

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
                _tweetDb.addAll(IterableHelper.except(_statusCache, oldCacheRef));
            }


            _eventData = data;
            Log.d(LOG, "TimelineServiceEventHandler - scheduling user handler.");
            _handler.post(this);
        }

        @Override
        public void run()
        {
            Log.d(LOG, "TimelineServiceEventHandler - calling user handler.");
            if (_eventData == null)
                throw new ShouldNotHappenException("TwitterServiceClient::TimelineServiceEventHandler._event is null.");

            getUserTimelineCompletedEvent.invoke(this, _eventData);
            _eventData = null;
        }
    }

    private class StatusEventHandler implements IEventHandler<Void>, Runnable
    {
        private volatile IEventHandlerArgs<Void> _args;

        @Override
        public void invoke(Object sender, IEventHandlerArgs<Void> statusIEventHandlerArgs)
        {
            _args = statusIEventHandlerArgs;
            _handler.post(this);

        }

        @Override
        public void run()
        {
            int id = R.string.status_tweet_create;

            if(_args.errorOccurred())
            {
                if(_args.getError() instanceof NetworkErrorException)
                {
                    id =  R.string.status_tweet_delay;
                }
                else
                {
                    id = R.string.status_error_insert_newStatus;
                }
            }

            updateStatusCompletedEvent.invoke(this, new GenericEventArgs<Integer>(id,null));

        }
    }

    private class PublisherEventHandler implements IEventHandler<Boolean>
    {

        @Override
        public void invoke(Object sender, IEventHandlerArgs<Boolean> voidIEventHandlerArgs)
        {
            //Intent timelineIntent = new Intent(YambaApplication.getContext(), TimelinePullService.class);
            Intent statusIntent = new Intent(YambaApplication.getContext(),StatusUploadService.class);
            //YambaApplication.getContext().startService(timelineIntent);
            YambaApplication.getContext().startService(statusIntent);
        }
    }
}

