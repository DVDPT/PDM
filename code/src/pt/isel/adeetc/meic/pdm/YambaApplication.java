package pt.isel.adeetc.meic.pdm;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import pt.isel.adeetc.meic.pdm.extensions.BaseApplication;
import pt.isel.adeetc.meic.pdm.services.TwitterServiceClient;

public class YambaApplication extends BaseApplication
{
    private TwitterServiceClient _client;
    private SharedPreferences _preferences;

    public final String timelineToStatusDetailsParamName = "statusDetailsParam";

    public TwitterServiceClient getTwitterClient()
    {
        if (_client == null || !_client.isValidLogin())
        {
            _client = new TwitterServiceClient();

            _client.configureTwiiterClient(getUserName(), getPassword(), getApiRootUrl());
        }
        return _client;
    }

    public String getUserName()
    {
        _preferences = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        return _preferences.getString("userName","");
       // return "PDM14";
    }

    public String getPassword()
    {
        _preferences = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        return _preferences.getString("userPass","");
        //return "pdm14_";
    }

    public String getApiRootUrl()
    {
        _preferences = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        return _preferences.getString("baseUrl","");
        //return "http://yamba.marakana.com/api";
    }

    
    public String getMaxCharacter(){
        _preferences = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        return _preferences.getString("maxCharacters","0");
        //return  _maxCharacters;
    }

    
    public String getMaxTweets()
    {
        _preferences = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        return _preferences.getString("maxTweets","0");
        //return _maxtweets;
    }
    
}
