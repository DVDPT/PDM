package pt.isel.adeetc.meic.pdm.controllers;

import android.accounts.NetworkErrorException;
import android.content.Intent;
import pt.isel.adeetc.meic.pdm.R;
import pt.isel.adeetc.meic.pdm.YambaApplication;
import pt.isel.adeetc.meic.pdm.common.*;
import pt.isel.adeetc.meic.pdm.common.db.IDbSet;
import pt.isel.adeetc.meic.pdm.exceptions.Constants;
import pt.isel.adeetc.meic.pdm.exceptions.ShouldNotHappenException;
import pt.isel.adeetc.meic.pdm.services.StatusUploadService;
import pt.isel.adeetc.meic.pdm.services.StatusUploadServiceMessage;
import winterwell.jtwitter.Twitter;

public final class TwitterServiceClient
{
    private static final String LOG = "TwitterServiceClient";

    public final IEvent<Integer> updateStatusCompletedEvent;
    public final IEvent<Iterable<Twitter.ITweet>> getUserTimelineCompletedEvent;

    public final IEventHandler<Boolean> _publisherEvent;

    private final StatusEventHandler _statusEventHandler = new StatusEventHandler();


    private Twitter _twitter;
    private final IDbSet<Twitter.ITweet> _tweetDb;
    private final YambaApplication _app;
    private final TimelineServiceController _timelineController;



    public TwitterServiceClient(IDbSet<Twitter.ITweet> tweetDb, YambaApplication app)
    {
        _tweetDb = tweetDb;
        _app = app;
        updateStatusCompletedEvent = new GenericEvent<Integer>();
        getUserTimelineCompletedEvent = new GenericEvent<Iterable<Twitter.ITweet>>();
        _timelineController = new TimelineServiceController(this, _app.getGeneralPurposeHandler());
        _publisherEvent = new PublisherEventHandler();

        _app.getNetworkEvent().addEventHandler(_publisherEvent);
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


    private class StatusEventHandler implements IEventHandler<Void>, Runnable
    {
        private volatile IEventHandlerArgs<Void> _args;

        @Override
        public void invoke(Object sender, IEventHandlerArgs<Void> statusIEventHandlerArgs)
        {
            _args = statusIEventHandlerArgs;
            _app.getUiHandler().post(this);

        }

        @Override
        public void run()
        {
            int id = R.string.status_tweet_create;

            if (_args.errorOccurred())
            {
                if (_args.getError() instanceof NetworkErrorException)
                {
                    id = R.string.status_tweet_delay;
                } else
                {
                    id = R.string.status_error_insert_newStatus;
                }
            }

            updateStatusCompletedEvent.invoke(this, new GenericEventArgs<Integer>(id, null));

        }
    }

    private class PublisherEventHandler implements IEventHandler<Boolean>
    {

        @Override
        public void invoke(Object sender, IEventHandlerArgs<Boolean> voidIEventHandlerArgs)
        {
            //Intent timelineIntent = new Intent(YambaApplication.getContext(), TimelinePullService.class);
            Intent statusIntent = new Intent(YambaApplication.getContext(), StatusUploadService.class);
            //YambaApplication.getContext().startService(timelineIntent);
            YambaApplication.getContext().startService(statusIntent);
        }
    }
}

