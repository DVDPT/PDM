package pt.isel.adeetc.meic.pdm.services;

import android.accounts.NetworkErrorException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import pt.isel.adeetc.meic.pdm.YambaApplication;
import pt.isel.adeetc.meic.pdm.YambaBaseIntentService;
import pt.isel.adeetc.meic.pdm.common.GenericEventArgs;
import pt.isel.adeetc.meic.pdm.common.IEventHandler;
import pt.isel.adeetc.meic.pdm.exceptions.ShouldNotHappenException;
import winterwell.jtwitter.Twitter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sorcha
 * Date: 10-05-2012
 * Time: 18:04
 * To change this template use File | Settings | File Templates.
 */
public class StatusUploadService extends YambaBaseIntentService {

    private static String LOG = "StatusUploadService";
    private static String NAMESERVICE = "ServiceUpload";
    private static int MAX_SIZE_CONTENT = 1;


    private static LinkedList<String> _status = new LinkedList<String>();

    public StatusUploadService() {
        super(NAMESERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }


    private void sendStatus(String message, IEventHandler<Void> handler) throws Exception {
        Exception error = null;
        Twitter client = getApplicationInstance().getTwitterClient().getTwitter();

        if (!getApplicationInstance().getNetworkState())
        {
            saveStatus(message);
            error = new NetworkErrorException();

        } else {
            try {

                client.setStatus(message);

            } catch (Exception e) {
                error = e;
            }
        }

        if(handler != null)
            handler.invoke(this, new GenericEventArgs<Void>(null, error));
        else if(error != null)
            throw error;
    }


    private void saveStatus(String message)
    {
        ContentValues values = new ContentValues(MAX_SIZE_CONTENT);
        values.put(StatusUploadContentProvider.KEY_VALUES_STATUS,message);
        YambaApplication.getContext().getContentResolver().insert(StatusUploadContentProvider.CONTENT_URI,values);
    }

    private void uploadSavedStatus()
    {
        LinkedList<String> statusSended = new LinkedList<String>();
        Cursor c = YambaApplication.getContext().getContentResolver().query(StatusUploadContentProvider.CONTENT_URI, null,null,null,null);
        String message;
        while(c.isLast())
        {
            c.moveToNext();
            message=c.getString(0);
            try {
                sendStatus(message, null);
                statusSended.add(message);
            } catch (Exception e) {
                break;
            }
        }
        YambaApplication.getContext().getContentResolver().delete(StatusUploadContentProvider.CONTENT_URI,null, (String[]) statusSended.toArray());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int paramId = intent.getIntExtra("params", -1);
        if (paramId == -1) {
            uploadSavedStatus();
            return;
        }

        StatusUploadServiceMessage statusMessage = (StatusUploadServiceMessage) getNavigationMessenger().getElement(paramId);



        try {
            sendStatus(statusMessage.getData(), statusMessage.getCallback());
        } catch (Exception e) {
            throw new ShouldNotHappenException(e);
        }

    }

    /*
   @Override
   public int onStartCommand(Intent e, int flags, int id)
   {
       super.onStartCommand(e,flags,id);
       int paramId = e.getIntExtra("params", -1);
       if (paramId == -1)
           throw new ShouldNotHappenException("StatusUploadService.onStartCommand : param is -1");

       Exception error = null;
       Twitter.ITweet status = null;
       StatusUploadServiceMessage statusMessage = (StatusUploadServiceMessage) getNavigationMessenger().getElement(paramId);


       if(getApplicationInstance().getNetworkState())
       {
           Twitter client = getApplicationInstance().getTwitterClient().getTwitter();

           try{
               status = client.setStatus(statusMessage.getData());
               statusMessage.getCallback().notifyUiOfChanges(this,new GenericEventArgs<Integer>(SENDED, error));

           }catch (Exception ex)
           {
               error = ex;
           }

       }
       else
       {
            _status.add(statusMessage.getData());
            statusMessage.getCallback().notifyUiOfChanges(this,new GenericEventArgs<Integer>(DELAYDED, error));
       }

       return START_STICKY;
   } */
}
