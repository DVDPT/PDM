package pt.isel.adeetc.meic.pdm.services;

import android.content.Intent;
import android.os.IBinder;
import pt.isel.adeetc.meic.pdm.YambaBaseService;
import pt.isel.adeetc.meic.pdm.common.GenericEventArgs;
import pt.isel.adeetc.meic.pdm.common.IEventHandler;
import pt.isel.adeetc.meic.pdm.common.ShouldNotHappenException;
import pt.isel.adeetc.meic.pdm.common.UiHelper;
import winterwell.jtwitter.Twitter;

import java.util.Timer;
import java.util.TimerTask;

public class TimelinePullService extends YambaBaseService
{
    private static String LOG = "TimelinePullService";
    private Timer _timer;
    private TimerTask _task;
    IEventHandler<Iterable<Twitter.Status>> _callback;

    @Override
    public IBinder onBind(Intent intent)
    {

        return null;
    }

    @Override
    public int onStartCommand(Intent e, int flags, int id)
    {
        super.onStartCommand(e,flags,id);
        int paramId = e.getIntExtra("param", -1);
        if (paramId == -1)
            throw new ShouldNotHappenException("TimelinePullService.onStartCommand : param is -1");

        TimelinePullServiceMessage message = (TimelinePullServiceMessage) getNavigationMessenger().getElement(paramId);

        _callback = message.getCallback();

        if (_task == null)
        {

            _task = new TimelinePullServiceTimerTask();
            _timer.scheduleAtFixedRate(_task, 0, getApplicationInstance().getTimelineRefreshPeriod() * 60 * 1000);
        }

        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        _timer = new Timer();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if(_task == null)
            return;

        _task.cancel();
        _timer.cancel();
        _timer = null;
        _task = null;
    }


    private class TimelinePullServiceTimerTask extends TimerTask
    {

        @Override
        public void run()
        {
            Twitter client = getApplicationInstance().getTwitterClient().getTwitter();
            Exception error = null;

            UiHelper.showToast("SISI HAHAHAHAHA");
            Iterable<Twitter.Status> statuses = null;

            try
            {
                statuses = client.getUserTimeline();

            } catch (Exception e)
            {
                error = e;
            }


            if (_callback == null)
            {
                ///
                /// If the service was destroyed.
                ///
                if(_task != null)
                    return;

                throw new ShouldNotHappenException("TimelinePullService.TimelinePullServiceTimerTask.run: callback is null");
            }
            _callback.invoke(this, new GenericEventArgs<Iterable<Twitter.Status>>(statuses, error));
        }
    }
}
