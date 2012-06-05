package pt.isel.adeetc.meic.pdm.services;

import android.content.Intent;
import android.os.Handler;
import pt.isel.adeetc.meic.pdm.YambaApplication;
import pt.isel.adeetc.meic.pdm.YambaNavigation;
import pt.isel.adeetc.meic.pdm.common.*;
import pt.isel.adeetc.meic.pdm.exceptions.Constants;
import winterwell.jtwitter.Twitter;

public final class TwitterServiceClient implements IEventHandler<Iterable<Twitter.ITweet>>
{
    public final IEvent<Twitter.ITweet> updateStatusCompletedEvent;
    public final IEvent<Iterable<Twitter.ITweet>> getUserTimelineCompletedEvent;

    private Iterable<Twitter.ITweet> _statusCache;
    private final StatusEventHandler _statusEventHandler = new StatusEventHandler();

    private Handler _handler;
    private Twitter _twitter;


    public TwitterServiceClient()
    {
        updateStatusCompletedEvent = new GenericEvent<Twitter.ITweet>();
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

        final IEventHandlerArgs<Iterable<Twitter.ITweet>> fdata = data;
        _handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                getUserTimelineCompletedEvent.invoke(this, fdata);
            }
        });
    }

    private class StatusEventHandler implements IEventHandler<Twitter.ITweet>, Runnable
    {
        private IEventHandlerArgs<Twitter.ITweet> _args;

        @Override
        public void invoke(Object sender, IEventHandlerArgs<Twitter.ITweet> statusIEventHandlerArgs)
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

