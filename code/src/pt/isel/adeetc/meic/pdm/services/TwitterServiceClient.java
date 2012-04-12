package pt.isel.adeetc.meic.pdm.services;

import pt.isel.adeetc.meic.pdm.common.*;
import pt.isel.adeetc.meic.pdm.exceptions.Constants;
import winterwell.jtwitter.Twitter;

import java.security.InvalidParameterException;

public final class TwitterServiceClient
{
    public final IEvent<Twitter.Status> updateStatusCompletedEvent;
    public final IEvent<Iterable<Twitter.Status>> getUserTimelineCompletedEvent;

    private volatile boolean _isStatusBeingUpdated;
    private volatile boolean _isTimelineBeingFetched;

    private Twitter _twitter;

    public TwitterServiceClient()
    {
        updateStatusCompletedEvent = new GenericEvent<Twitter.Status>();
        getUserTimelineCompletedEvent = new GenericEvent<Iterable<Twitter.Status>>();
    }

    public boolean isStatusBeingUpdated()
    {
        return _isStatusBeingUpdated;
    }

    public boolean isTimelineBeingFetched()
    {
        return _isTimelineBeingFetched;
    }

    public void updateStatusAsync(String status)
    {
        new UpdateStatusAsyncTask().execute(status);
    }

    @SuppressWarnings({"unchecked"})
    public void getUserTimelineAsync()
    {
        new GetUserTimelineAsyncTask().execute();
    }

    //TODO alterar nome do metodo
    public void configureTwiiterClient(String user, String password, String apiRootUrl)
    {
        _twitter = new Twitter(user, password);
        _twitter.setAPIRootUrl(apiRootUrl);
    }

    private Twitter getTwitter()
    {
        if (_twitter == null)
            throw new ShouldNotHappenException(Constants.NoUserAndPasswordDefined);
        return _twitter;
    }

    private class UpdateStatusAsyncTask extends ExtendedAsyncTask<String, Void, Twitter.Status>
    {

        @Override
        protected void onPreExecute()
        {
            _isStatusBeingUpdated = true;
        }

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
            _isStatusBeingUpdated = false;
            updateStatusCompletedEvent.invoke(TwitterServiceClient.this, new GenericEventArgs<Twitter.Status>(result, error));
        }
    }

    private class GetUserTimelineAsyncTask extends ExtendedAsyncTask<Void, Void, Iterable<Twitter.Status>>
    {

        @Override
        protected void onPreExecute()
        {
            _isTimelineBeingFetched = true;
        }

        @Override
        protected Iterable<Twitter.Status> doWork(Void... params)
        {
            return getTwitter().getUserTimeline();
        }

        @Override
        protected void onPostExecute(Iterable<Twitter.Status> result)
        {
            _isTimelineBeingFetched = false;
            Exception error = getError();
            getUserTimelineCompletedEvent
                    .invoke(TwitterServiceClient.this, new GenericEventArgs<Iterable<Twitter.Status>>(result, error));

        }

    }
}


