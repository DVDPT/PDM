package pt.isel.adeetc.meic.pdm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * Created by IntelliJ IDEA.
 * User: Sorcha
 * Date: 05-06-2012
 * Time: 19:56
 * To change this template use File | Settings | File Templates.
 */
public class WifiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        
        if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION))
        {
            NetworkInfo info = (NetworkInfo)intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (info.getState().equals(NetworkInfo.State.CONNECTED))
            {

            }
        }
    }
}
