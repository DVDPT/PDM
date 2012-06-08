package pt.isel.adeetc.meic.pdm.services;

import android.content.Intent;
import android.os.Handler;
import pt.isel.adeetc.meic.pdm.YambaApplication;
import pt.isel.adeetc.meic.pdm.common.GenericEvent;
import pt.isel.adeetc.meic.pdm.common.IEvent;
import pt.isel.adeetc.meic.pdm.common.IEventHandler;
import pt.isel.adeetc.meic.pdm.common.IEventHandlerArgs;
import pt.isel.adeetc.meic.pdm.common.db.IDbSet;
import pt.isel.adeetc.meic.pdm.exceptions.Constants;
import pt.isel.adeetc.meic.pdm.exceptions.ShouldNotHappenException;
import winterwell.jtwitter.Twitter;

public final class TwitterServiceClient
{
    private static final String LOG = "TwitterServiceClient";

    public final IEvent<Integer> updateStatusCompletedEvent;
    public final IEvent<Iterable<Twitter.ITweet>> getUserTimelineCompletedEvent;

    private final StatusEventHandler _statusEventHandler = new StatusEventHandler();

    private Handler _handler;
    private Twitter _twitter;
    private final IDbSet<Twitter.ITweet> _tweetDb;
    private final TimelineServiceController _timelineController;


    public TwitterServiceClient(IDbSet<Twitter.ITweet> tweetDb)
    {
        _tweetDb = tweetDb;
        _handler = new Handler();
        updateStatusCompletedEvent = new GenericEvent<Integer>();
        getUserTimelineCompletedEvent = new GenericEvent<Iterable<Twitter.ITweet>>();
        _timelineController = new TimelineServiceController();
        /*
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
           */
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
        _timelineController.getUserTimelineAsync();
    }

    public Iterable<Twitter.ITweet> getTwitterCachedTimeline()
    {
        return _timelineController.getStatusCache();
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

    IDbSet<Twitter.ITweet> getTweetDbSet()
    {
        return _tweetDb;
    }

    Handler getHandler()
    {
        return _handler;
    }


    private class StatusEventHandler implements IEventHandler<Integer>, Runnable
    {
        private volatile IEventHandlerArgs<Integer> _args;

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

