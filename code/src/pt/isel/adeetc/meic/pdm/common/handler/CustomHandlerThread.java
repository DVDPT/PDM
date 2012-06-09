package pt.isel.adeetc.meic.pdm.common.handler;

import android.os.Handler;
import android.os.Looper;
import pt.isel.adeetc.meic.pdm.exceptions.ShouldNotHappenException;

public class CustomHandlerThread extends Thread
{
    private Handler _handler;

    public CustomHandlerThread()
    {

        start();
    }

    public void run()
    {
        setName("CustomHandlerThread");
        Looper.prepare();

        synchronized (this)
        {
            _handler = new Handler();
            notify();
        }

        Looper.loop();
    }

    public Handler getHandler()
    {
        if (_handler != null)
            return _handler;
        synchronized (this)
        {
            if (_handler != null)
                return _handler;

            try
            {
                wait();
            } catch (InterruptedException e)
            {
                throw new ShouldNotHappenException(e);
            }
        }
        return _handler;
    }

}
