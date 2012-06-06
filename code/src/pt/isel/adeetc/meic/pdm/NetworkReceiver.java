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
import winterwell.jtwitter.Twitter;

public class NetworkReceiver extends BroadcastReceiver {

    private IEventReceiver<Twitter.ITweet> _updateStatus= new GenericEventReceiver<Twitter.ITweet>();

    @Override
    public void onReceive(Context context, Intent intent)
    {
        boolean isNetworkDown = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,false);
        if(!isNetworkDown)
        {
            _updateStatus.invoke(this,null);
        }
    }
}
