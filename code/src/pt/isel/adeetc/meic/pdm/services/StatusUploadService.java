package pt.isel.adeetc.meic.pdm.services;

import android.content.Intent;
import android.os.IBinder;
import pt.isel.adeetc.meic.pdm.YambaBaseService;
import pt.isel.adeetc.meic.pdm.common.GenericEventArgs;
import pt.isel.adeetc.meic.pdm.common.db.IDbSet;
import pt.isel.adeetc.meic.pdm.exceptions.ShouldNotHappenException;
import winterwell.jtwitter.Twitter;

import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sorcha
 * Date: 10-05-2012
 * Time: 18:04
 * To change this template use File | Settings | File Templates.
 */
public class StatusUploadService extends YambaBaseService {

    private static String LOG = "StatusUploadService";
    
    private static Integer OK = 1;
    private static Integer WAIT = 2;

    private LinkedList<String> _status = new LinkedList<String>();

    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }


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
                statusMessage.getCallback().invoke(this,new GenericEventArgs<Integer>(OK, error));

            }catch (Exception ex)
            {
                error = ex;
            }

        }
        else
        {
             _status.add(statusMessage.getData());
             statusMessage.getCallback().invoke(this,new GenericEventArgs<Integer>(WAIT, error));
        }

        return START_STICKY;
    }
}
