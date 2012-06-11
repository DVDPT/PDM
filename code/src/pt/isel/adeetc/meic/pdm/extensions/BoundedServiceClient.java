package pt.isel.adeetc.meic.pdm.extensions;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.*;
import android.util.Log;

public abstract class BoundedServiceClient
{

    static String LOG = "BoundedServiceClient";

    ///
    /// Boolean variable to know if the call should call the service
    /// when a connectio is available.
    ///
    private boolean _callServiceWhenConnected;

    ///
    /// The client messenger, used to attend the service responses.
    ///
    private Messenger _serviceMessenger;

    ///
    /// Client handler to dispatch server responses.
    ///
    private Handler _clientHandler = new Handler()
    {
        public void handleMessage(Message message)
        {
            //message.getData().setClassLoader(getClassLoader());
            onServiceResponse(message);
        }
    };

    ///
    /// The service messenger, used to call the service.
    ///
    private Messenger _clientCallbackMessenger = new Messenger(_clientHandler);


    ///
    /// The server connection to be notified when the server is connected or disconnected.
    ///
    private ServiceConnection _servinceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder)
        {
            _serviceMessenger = new Messenger(iBinder);

            if (callServiceWhenConnected())
            {
                callService();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName)
        {
            Log.d(LOG, "onServiceDisconnected");
            _serviceMessenger = null;
        }
    };

    public void unbind(Context ctx)
    {
        if(!isServiceConnected())
            return;

        ctx.unbindService(_servinceConnection);
        _serviceMessenger = null;
    }

    public void callService()
    {
        Message m = Message.obtain();
        fillServiceRequest(m);
        try
        {
            m.replyTo = _clientCallbackMessenger;
            _serviceMessenger.send(m);
        } catch (RemoteException e)
        {
            Log.d(LOG, e.getMessage());
        }
    }

    protected abstract void fillServiceRequest(Message obtain);

    protected abstract void onServiceResponse(Message message);

    public boolean callServiceWhenConnected()
    {
        return _callServiceWhenConnected;
    }

    public void setCallServiceWhenConnected(boolean callService)
    {
        _callServiceWhenConnected = callService;
    }

    public Messenger getServiceMessenger()
    {
        return _serviceMessenger;
    }

    public Messenger getClientCallbackMessenger()
    {
        return _clientCallbackMessenger;
    }

    public ServiceConnection getServiceConnection()
    {
        return _servinceConnection;
    }

    public boolean isServiceConnected()
    {
        return _serviceMessenger != null;
    }


}
