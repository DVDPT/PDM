package pt.isel.adeetc.meic.pdm.services;

import android.content.Intent;
import android.os.*;
import android.util.Log;
import pt.isel.adeetc.meic.pdm.YambaBaseIntentService;
import pt.isel.adeetc.meic.pdm.YambaNavigation;
import pt.isel.adeetc.meic.pdm.common.IterableHelper;
import pt.isel.adeetc.meic.pdm.exceptions.ShouldNotHappenException;
import winterwell.jtwitter.Twitter;

public class TimelinePullService extends YambaBaseIntentService
{
    private static String LOG = "TimelinePullService";
    public static final int SERVICE_RESPONSE_OK = 0;
    public static final int SERVICE_RESPONSE_ERROR = 1;


    @Override
    public void onStart(Intent i, int id)
    {
        super.onStart(i, id);

    }

    private void initializeMessenger()
    {
        _messenger = new Messenger(new CustomServiceHandler());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }


    private class CustomServiceHandler extends Handler
    {
        public void handleMessage(Message msg)
        {
            Log.d(LOG, "TimelinePullService.Handler: On handleMessage");
            Exception error = null;
            int result = SERVICE_RESPONSE_OK;

            try
            {
                handleRequest(true);
            } catch (Exception e)
            {
                error = e;
            }

            Message m = Message.obtain();

            if (error != null)
            {
                result = SERVICE_RESPONSE_ERROR;
                m.getData().putSerializable(YambaNavigation.TIMELINE_SERVICE_ERROR_PARAM_NAME, error);
            }

            m.getData().putInt(YambaNavigation.TIMELINE_SERVICE_RESULT_PARAM_NAME, result);

            try
            {
                msg.replyTo.send(m);
            } catch (RemoteException e)
            {
                Log.d(LOG, e.getMessage());
            }
        }
    }

    private Messenger _messenger;

    public TimelinePullService()
    {
        super(LOG);
    }


    @Override
    public IBinder onBind(Intent intent)
    {
        initializeMessenger();
        return _messenger.getBinder();
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

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

}
