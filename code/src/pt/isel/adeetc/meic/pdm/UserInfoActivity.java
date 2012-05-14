package pt.isel.adeetc.meic.pdm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.*;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import pt.isel.adeetc.meic.pdm.services.UserInfoPullService;
import pt.isel.adeetc.meic.pdm.services.YambaUserInfo;

import java.util.LinkedList;


public class UserInfoActivity extends YambaBaseActivity
{

    private TextView _userName;
    private TextView _numberOfTweets;
    private TextView _numberOfFollowers;
    private TextView _numberOfFollowings;
    private ImageView _userImage;

    private Messenger _messenger;


    private Handler _serviceResponseHandler = new Handler()
    {
        public void handleMessage(Message message)
        {
            message.getData().setClassLoader(getClassLoader());
            YambaUserInfo info = (YambaUserInfo) message.getData().get("result");
            changeUserInfo(
                    info.getName(),
                    info.getNrOfTweets(),
                    info.getFollowers(),
                    info.getFollowing(),
                    info.getPhotoUri()
            );
        }
    };

    Messenger _callback = new Messenger(_serviceResponseHandler);


    public void onCreate(Bundle savedInstanceState)
    {
        YambaUserInfo a = new YambaUserInfo();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);


        _userName = (TextView) findViewById(R.id.user_info_name);
        _numberOfTweets = (TextView) findViewById(R.id.user_info_tweets);
        _numberOfFollowers = (TextView) findViewById(R.id.user_info_follwers);
        _numberOfFollowings = (TextView) findViewById(R.id.user_info_follwers);
        _userImage = (ImageView) findViewById(R.id.user_info_image);


    }

    protected void onResume()
    {
        super.onResume();
        Intent serviceIntent = new Intent(this, UserInfoPullService.class);
        bindService(serviceIntent, _userInfoPullServiceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unbindService(_userInfoPullServiceConnection);
    }

    ServiceConnection _userInfoPullServiceConnection = new ServiceConnection()
    {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder)
        {
            _messenger = new Messenger(iBinder);
            try
            {

                Message m = Message.obtain();
                m.replyTo = _callback;
                _messenger.send(m);
            } catch (RemoteException e)
            {
                Log.d("LOG", e.getMessage());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName)
        {
            _messenger = null;
        }
    };


    private void changeUserInfo(String name, int nTweets, int followers, int followings, Uri urlImage)
    {

        _userName.setText(name);
        _numberOfTweets.setText(Integer.toString(nTweets));
        _numberOfFollowers.setText(Integer.toString(followers));
        _numberOfFollowings.setText(Integer.toString(followings));
        _userImage.setImageURI(urlImage);
    }

    protected Iterable<Integer> getActivityDisabledMenuItems()
    {
        LinkedList<Integer> ret = new LinkedList<Integer>();
        ret.add(R.id.menu_user_info);
        return ret;
    }


}