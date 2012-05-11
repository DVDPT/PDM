package pt.isel.adeetc.meic.pdm.services;

import android.content.Intent;
import android.os.IBinder;
import pt.isel.adeetc.meic.pdm.YambaBaseService;
import pt.isel.adeetc.meic.pdm.common.GenericEventArgs;
import pt.isel.adeetc.meic.pdm.common.ShouldNotHappenException;
import winterwell.jtwitter.Twitter;

/**
 * Created by IntelliJ IDEA.
 * User: Sorcha
 * Date: 10-05-2012
 * Time: 18:04
 * To change this template use File | Settings | File Templates.
 */
public class StatusUploadService extends YambaBaseService {

    private static String LOG = "StatusUploadService";

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
        Twitter.Status status = null;

        StatusUploadServiceMessage statusMessage = (StatusUploadServiceMessage) getNavigationMessenger().getElement(paramId);

        Twitter client = getApplicationInstance().getTwitterClient().getTwitter();
        
        try{
            status = client.setStatus(statusMessage.getData());
            
        }catch (Exception ex)
        {
             error = ex;
        }
        statusMessage.getCallback().invoke(this,new GenericEventArgs<Twitter.Status>(status, error));
        return START_STICKY;
    }
}
