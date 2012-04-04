package pt.isel.adeetc.meic.pdm.services;

import android.os.AsyncTask;
import pt.isel.adeetc.meic.pdm.common.GenericEvent;
import pt.isel.adeetc.meic.pdm.common.GenericEventArgs;
import pt.isel.adeetc.meic.pdm.common.ShouldNotHappenException;
import pt.isel.adeetc.meic.pdm.execptions.Constants;
import winterwell.jtwitter.Twitter;

import java.security.InvalidParameterException;

public final class TwitterServiceClient
{
    public final GenericEvent<Twitter.Status> updateStatusCompletedEvent;
    public final GenericEvent<Iterable<Twitter.Status>> getUserTimelineCompletedEvent;

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

    public void setTwitterUsernameAndPassword(String user, String password)
    {
        _twitter = new Twitter(user, password);
    }

    private Twitter getTwitter()
    {
        if (_twitter == null)
            throw new ShouldNotHappenException(Constants.NoUserAndPasswordDefined);
        return _twitter;
    }

    private class UpdateStatusAsyncTask extends AsyncTask<String, Void, Twitter.Status>
    {

        @Override
        protected void onPreExecute()
        {
            _isStatusBeingUpdated = true;
        }

        @Override
        protected Twitter.Status doInBackground(String... strings)
        {
            if (strings.length == 0)
                throw new InvalidParameterException("status");

            return getTwitter().updateStatus(strings[0]);
        }

        @Override
        protected void onPostExecute(Twitter.Status _)
        {
            Twitter.Status result = null;
            Exception error = null;
            _isStatusBeingUpdated = false;
            try
            {
                result = get();
            } catch (Exception e)
            {
                error = e;
            }
            updateStatusCompletedEvent.invoke(TwitterServiceClient.this, new GenericEventArgs<Twitter.Status>(result, error));
        }
    }

    private class GetUserTimelineAsyncTask extends AsyncTask<Void, Void, Iterable<Twitter.Status>>
    {

        @Override
        protected void onPreExecute()
        {
            _isTimelineBeingFetched = true;
        }

        @Override
        protected Iterable<Twitter.Status> doInBackground(Void... voids)
        {
            return getTwitter().getUserTimeline();
        }

        @Override
        protected void onPostExecute(Iterable<Twitter.Status> _)
        {
            _isTimelineBeingFetched = false;
            Iterable<Twitter.Status> result = null;
            Exception error = null;
            _isStatusBeingUpdated = false;
            try
            {
                result = get();
            } catch (Exception e)
            {
                error = e;
            }
            getUserTimelineCompletedEvent
                    .invoke(TwitterServiceClient.this, new GenericEventArgs<Iterable<Twitter.Status>>(result, error));

        }

    }
}
