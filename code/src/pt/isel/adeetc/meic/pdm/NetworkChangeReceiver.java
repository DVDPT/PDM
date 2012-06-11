package pt.isel.adeetc.meic.pdm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import pt.isel.adeetc.meic.pdm.common.GenericEventArgs;

public class NetworkChangeReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        boolean isNetworkDown = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

        ((YambaApplication) (YambaApplication.getInstance())).getNetworkEvent().invoke(this, new GenericEventArgs<Boolean>(isNetworkDown, null));
    }
}
