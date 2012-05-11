package pt.isel.adeetc.meic.pdm;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by IntelliJ IDEA.
 * User: Sorcha
 * Date: 11-05-2012
 * Time: 0:16
 * To change this template use File | Settings | File Templates.
 */
public class UserInfoActivity extends Activity {

    private TextView _userName;
    private TextView _numberOfTweets;
    private TextView _numberOfFollowers;
    private TextView _numberOfFollowings;
    private ImageView _userImage; 


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);

        
        _userName = (TextView) findViewById(R.id.user_info_name);
        _numberOfTweets = (TextView) findViewById(R.id.user_info_tweets);
        _numberOfFollowers = (TextView) findViewById(R.id.user_info_follwers);
        _numberOfFollowings = (TextView) findViewById(R.id.user_info_follwers);
        _userImage = (ImageView)findViewById(R.id.user_info_image);
    }
    
    
    private void changeUserInfo(String name, int nTweets, int followers, int followings, Uri urlImage){
        
        _userName.setText(name);
        _numberOfTweets.setText(Integer.toString(nTweets));
        _numberOfFollowers.setText(Integer.toString(followers));
        _numberOfFollowings.setText(Integer.toString(followings));
        _userImage.setImageURI(urlImage);
    }
    
    
}