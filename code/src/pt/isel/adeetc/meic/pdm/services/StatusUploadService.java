package pt.isel.adeetc.meic.pdm.services;

import android.accounts.NetworkErrorException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import pt.isel.adeetc.meic.pdm.YambaApplication;
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
    private static int MAX_SIZE_CONTENT = 1;


    private static LinkedList<String> _status = new LinkedList<String>();

    private BoundedService _boundedImpl = new BoundedService(((YambaApplication)YambaApplication.getInstance()).getCustomHandlerThread().getLooper())
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
        ContentValues values = new ContentValues(MAX_SIZE_CONTENT);
        values.put(StatusUploadContentProvider.KEY_VALUES_STATUS,message);

        YambaApplication.getContext().getContentResolver().insert(StatusUploadContentProvider.CONTENT_URI,values);
    }

    private void uploadSavedStatus()
    {
        Log.d(LOG, "uploadSavedStatus");
        LinkedList<String> statusSended = new LinkedList<String>();

        Cursor c= YambaApplication.getContext().getContentResolver().query(StatusUploadContentProvider.CONTENT_URI, null, null, null, null);
        String m;
        while(!c.isLast())
        {
            c.moveToNext();
            m=c.getString(0);
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
       YambaApplication.getContext().getContentResolver().delete(StatusUploadContentProvider.CONTENT_URI,null, statusSended.toArray(new String[statusSended.size()]));

    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        uploadSavedStatus();
    }


}
