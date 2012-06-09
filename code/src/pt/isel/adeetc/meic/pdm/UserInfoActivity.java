package pt.isel.adeetc.meic.pdm;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import pt.isel.adeetc.meic.pdm.common.IEventHandler;
import pt.isel.adeetc.meic.pdm.common.IEventHandlerArgs;
import pt.isel.adeetc.meic.pdm.common.UiHelper;
import pt.isel.adeetc.meic.pdm.services.YambaUserInfo;

import java.util.LinkedList;


public class UserInfoActivity extends YambaBaseActivity implements IEventHandler<YambaUserInfo>
{
    private TextView _userName;
    private TextView _numberOfTweets;
    private TextView _numberOfFollowers;
    private TextView _numberOfFollowings;
    private ImageView _userImage;

    private ProgressDialog _dialog;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        _userName = (TextView) findViewById(R.id.user_info_name);
        _numberOfTweets = (TextView) findViewById(R.id.user_info_tweets);
        _numberOfFollowers = (TextView) findViewById(R.id.user_info_followers);
        _numberOfFollowings = (TextView) findViewById(R.id.user_info_following);
        _userImage = (ImageView) findViewById(R.id.user_info_image);


    }

    protected void onResume()
    {
        super.onResume();
        getApplicationInstance().getTwitterClient().getUserInfoEvent.setEventHandler(this);
        getApplicationInstance().getTwitterClient().getUserInfoAsync();
        _dialog = ProgressDialog.show(this, getString(R.string.loading), getString(R.string.user_info_activity));

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        getApplicationInstance().getTwitterClient().getUserInfoEvent.removeEventHandler();
    }

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


    @Override
    public void invoke(Object sender, IEventHandlerArgs<YambaUserInfo> args)
    {
        if(_dialog != null)
            _dialog.dismiss();
        try
        {
            YambaUserInfo info = args.getData();
            changeUserInfo(
                    info.getName(),
                    info.getNrOfTweets(),
                    info.getFollowers(),
                    info.getFollowing(),
                    info.getPhotoUri()
            );
        } catch (Exception e)
        {
            UiHelper.showToast(R.string.user_info_error);
        }
    }
}