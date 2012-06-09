package pt.isel.adeetc.meic.pdm.services;

import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import pt.isel.adeetc.meic.pdm.YambaBaseIntentService;
import pt.isel.adeetc.meic.pdm.common.IterableHelper;
import pt.isel.adeetc.meic.pdm.controllers.TimelineContentProviderClient;
import pt.isel.adeetc.meic.pdm.exceptions.ShouldNotHappenException;
import pt.isel.adeetc.meic.pdm.extensions.BoundedService;
import winterwell.jtwitter.Twitter;

public class TimelinePullService extends YambaBaseIntentService
{
    private static String LOG = "TimelinePullService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }


    private BoundedService _boundedImpl = new BoundedService()
    {
        @Override
        protected int handleClientRequest(Message cliengMsg, Message serviceResponse) throws Exception
        {
            handleRequest(true);
            return BoundedService.SERVICE_RESPONSE_OK;
        }
    };

    private IBinder _binder = _boundedImpl.getBinder();


    public TimelinePullService()
    {
        super(LOG);
    }


    @Override
    public IBinder onBind(Intent intent)
    {

        return _binder;
    }


    @Override
    protected void onHandleIntent(Intent e)
    {
        try
        {
            handleRequest(false);
        } catch (Exception e1)
        {
            throw new ShouldNotHappenException(e1);
        }
    }

    private void handleRequest(boolean isBindedReq) throws Exception
    {
        Log.d(LOG, "On Handle request " + (isBindedReq ? "Binded" : "Started"));
        TimelineContentProviderClient contentProviderClient = new TimelineContentProviderClient(getApplication());
        Exception error = null;
        try
        {
            Iterable<Twitter.ITweet> statuses = IterableHelper.getSuperIterable(getApplicationInstance().getTwitterClient().getTwitter().getUserTimeline(), Twitter.ITweet.class);
            contentProviderClient.addAll(statuses);
        } catch (Exception err)
        {
            error = err;
        }

        if (error != null)
        {
            if (isBindedReq)
                throw error;
        }
    }


}
