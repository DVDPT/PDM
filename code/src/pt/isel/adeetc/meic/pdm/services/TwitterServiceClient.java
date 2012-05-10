package pt.isel.adeetc.meic.pdm.services;

import android.content.Intent;
import android.os.Handler;
import pt.isel.adeetc.meic.pdm.YambaApplication;
import pt.isel.adeetc.meic.pdm.common.*;
import pt.isel.adeetc.meic.pdm.exceptions.Constants;
import winterwell.jtwitter.Twitter;

import java.security.InvalidParameterException;

public final class TwitterServiceClient implements IEventHandler<Iterable<Twitter.Status>>
{
    public final IEvent<Twitter.Status> updateStatusCompletedEvent;
    public final IEvent<Iterable<Twitter.Status>> getUserTimelineCompletedEvent;

    private Iterable<Twitter.Status> _statusCache;

    private Handler _handler;
    private Twitter _twitter;

    public TwitterServiceClient()
    {
        updateStatusCompletedEvent = new GenericEvent<Twitter.Status>();
        getUserTimelineCompletedEvent = new GenericEvent<Iterable<Twitter.Status>>();
        _handler = new Handler();
    }


    public void updateStatusAsync(String status)
    {
        new UpdateStatusAsyncTask().execute(status);
    }

    @SuppressWarnings({"unchecked"})
    public void getUserTimelineAsync()
    {
        Intent timelineIntent = new Intent(YambaApplication.getContext(), TimelinePullService.class);
        int id = YambaApplication.getInstance().getNavigationMessenger().putElement(new TimelinePullServiceMessage(this));
        timelineIntent.putExtra("param", id);
        YambaApplication.getContext().startService(timelineIntent);
    }

    public Iterable<Twitter.Status> getTwitterCachedTimeline()
    {
        return _statusCache;
    }

    //TODO alterar nome do metodo
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
    public void invoke(Object sender, IEventHandlerArgs<Iterable<Twitter.Status>> data)
    {

        if(data.getError() == null)
        {
            try
            {
                _statusCache = data.getData();
            }
            catch (Exception e)
            {
                throw new ShouldNotHappenException(e);
            }
        }

        final IEventHandlerArgs<Iterable<Twitter.Status>> fdata = data;
        _handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                getUserTimelineCompletedEvent.invoke(this, fdata);
            }
        });
    }

    private class UpdateStatusAsyncTask extends ExtendedAsyncTask<String, Void, Twitter.Status>
    {


        @Override
        protected Twitter.Status doWork(String... params)
        {
            if (params.length == 0)
                throw new InvalidParameterException("status");

            return getTwitter().updateStatus(params[0]);
        }

        @Override
        protected void onPostExecute(Twitter.Status result)
        {
            Exception error = getError();

            updateStatusCompletedEvent.invoke(TwitterServiceClient.this, new GenericEventArgs<Twitter.Status>(result, error));
        }
    }


}


