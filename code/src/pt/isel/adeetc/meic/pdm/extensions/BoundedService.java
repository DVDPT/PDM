package pt.isel.adeetc.meic.pdm.extensions;

import android.os.*;
import android.util.Log;

public abstract class BoundedService
{
    private static final String LOG = "BoundedService";

    public static final int SERVICE_RESPONSE_OK = 0;
    public static final int SERVICE_RESPONSE_ERROR = 1;
    public static final String SERVICE_STATUS_RESPONSE_PARAM_NAME = "SERVICE_STATUS_RESPONSE_PARAM_NAME";
    public static final String SERVICE_ERROR_RESPONSE_PARAM_NAME = "SERVICE_ERROR_RESPONSE_PARAM_NAME";

    private class BoundedServiceHandler extends Handler
    {
        public void handleMessage(Message msg)
        {
            Exception error = null;
            Message serviceResponse = Message.obtain();
            int result = BoundedService.SERVICE_RESPONSE_OK;
            try
            {
                result = handleClientRequest(msg, serviceResponse);
            } catch (Exception e)
            {
                error = e;
            }
            if (error != null)
            {
                result = BoundedService.SERVICE_RESPONSE_ERROR;
                serviceResponse.getData().putSerializable(SERVICE_ERROR_RESPONSE_PARAM_NAME, error);
            }

            serviceResponse.getData().putInt(SERVICE_STATUS_RESPONSE_PARAM_NAME, result);

            try
            {
                msg.replyTo.send(serviceResponse);
            } catch (RemoteException e)
            {
                Log.d(LOG, e.getMessage());
            }
        }
    }

    protected abstract int handleClientRequest(Message cliengMsg, Message serviceResponse) throws Exception;

    private Messenger _serviceMessenger;
    private Handler _serviceHandler;

    public Messenger getServiceMessenger()
    {
        if (_serviceMessenger != null)
        {
            createNewMessengerInstance();
        }

        return _serviceMessenger;
    }

    public IBinder getBinder()
    {
        return createNewMessengerInstance().getBinder();
    }

    private Messenger createNewMessengerInstance()
    {
        return _serviceMessenger = new Messenger(_serviceHandler = new BoundedServiceHandler());
    }

    public Handler getServiceHandler()
    {
        return _serviceHandler;
    }

}
