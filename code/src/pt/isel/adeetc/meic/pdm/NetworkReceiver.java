package pt.isel.adeetc.meic.pdm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import pt.isel.adeetc.meic.pdm.common.GenericEventArgs;
import pt.isel.adeetc.meic.pdm.common.GenericEventReceiver;
import pt.isel.adeetc.meic.pdm.common.IEventHandler;
import pt.isel.adeetc.meic.pdm.common.IEventReceiver;
import pt.isel.adeetc.meic.pdm.services.StatusUploadService;
import winterwell.jtwitter.Twitter;

public class NetworkReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
       /* final String action = intent.getAction();
        if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
            if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false))
            {
                context.startService(new Intent(context, StatusUploadService.class));
            } else {

            }
        }  */
        final String action = intent.getAction();
        boolean isNetworkDown = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,false);
        if(!isNetworkDown)
        {
            IEventReceiver r = (IEventReceiver) context;
            ((IEventReceiver) context).invoke(context,null);
        }
        else
        {

        }
    }
}
