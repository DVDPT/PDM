package pt.isel.adeetc.meic.pdm.services;

import android.accounts.NetworkErrorException;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import pt.isel.adeetc.meic.pdm.YambaBaseIntentService;
import pt.isel.adeetc.meic.pdm.YambaNavigation;
import pt.isel.adeetc.meic.pdm.extensions.BoundedService;
import winterwell.jtwitter.Twitter;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sorcha
 * Date: 10-05-2012
 * Time: 18:04
 * To change this template use File | Settings | File Templates.
 */
public class StatusUploadService extends YambaBaseIntentService
{

    private static String LOG = "StatusUploadService";
    private static String NAMESERVICE = "ServiceUpload";


    private static LinkedList<String> _status = new LinkedList<String>();

    private BoundedService _boundedImpl = new BoundedService()
    {
        @Override
        protected int handleClientRequest(Message cliengMsg, Message serviceResponse) throws Exception
        {

            Log.d(LOG, "On handleClientRequest.");
            sendStatus(cliengMsg.getData().getString(YambaNavigation.STATUS_SERVICE_MESSAGE_PARAM_NAME));
            return BoundedService.SERVICE_RESPONSE_OK;
        }
    };

    public StatusUploadService()
    {
        super(NAMESERVICE);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        Log.d(LOG, "onBind");
        return _boundedImpl.getBinder();

    }


    private void sendStatus(String message) throws Exception
    {
        Log.d(LOG, "Sending status: " + message);
        Twitter client = getApplicationInstance().getTwitterClient().getTwitter();

        if (!getApplicationInstance().getNetworkState())
        {
            saveStatus(message);
            throw new NetworkErrorException();

        } else
        {
            client.setStatus(message);
        }


    }

    private void saveStatus(String message)
    {
        if (!_status.contains(message))
            _status.add(message);
    }

    private void uploadSavedStatus()
    {
        Log.d(LOG, "uploadSavedStatus");
        LinkedList<String> statusSended = new LinkedList<String>();
        for (String m : _status)
        {
            try
            {
                sendStatus(m);
                statusSended.add(m);
            } catch (Exception e)
            {
                Log.d(LOG, "Error occurred");
                break;
            }

        }
        _status.removeAll(statusSended);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        uploadSavedStatus();
    }


}
