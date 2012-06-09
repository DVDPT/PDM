package pt.isel.adeetc.meic.pdm.services;

import android.content.Intent;
import android.os.*;
import android.util.Log;
import pt.isel.adeetc.meic.pdm.YambaBaseService;
import pt.isel.adeetc.meic.pdm.YambaNavigation;
import winterwell.jtwitter.Twitter;

public class UserInfoPullService extends YambaBaseService
{


    Handler userInfoHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            Log.d("LOG", "UserInfoPullService.UserInfoHandler: On handleMessage");

            Twitter t = getApplicationInstance().getTwitterClient().getTwitter();
            Twitter.User user = t.getUser(getApplicationInstance().getUserName());
            YambaUserInfo userInfo = new YambaUserInfo(user);


            Message m = Message.obtain();

            m.getData().putParcelable(YambaNavigation.USER_INFO_SERVICE_PARAM_NAME, userInfo);

            try
            {
                msg.replyTo.send(m);
            } catch (RemoteException e)
            {
                Log.d("LOG", e.getMessage());
            }
        }
    };

    private Messenger _messenger = new Messenger(userInfoHandler);

    @Override
    public IBinder onBind(Intent intent)
    {
        Log.d("LOG", "UserInfoPullService.onBind: onBind method");

        return _messenger.getBinder();
    }
}
