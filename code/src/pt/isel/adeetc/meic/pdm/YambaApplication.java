package pt.isel.adeetc.meic.pdm;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import pt.isel.adeetc.meic.pdm.extensions.BaseApplication;
import pt.isel.adeetc.meic.pdm.services.TwitterServiceClient;

import java.util.prefs.Preferences;

public class YambaApplication extends BaseApplication implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private TwitterServiceClient _client;
    private SharedPreferences _preferences;


    public void onCreate()
    {

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

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
        //return _preferences.getString("userName", "");
        return "PDM14";
    }

    public String getPassword()
    {
        _preferences = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        //return _preferences.getString("userPass", "");
        return "pdm14_";
    }

    public String getApiRootUrl()
    {

        _preferences = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        return _preferences.getString("baseUrl", "http://yamba.marakana.com/api");
        //return "http://yamba.marakana.com/api";
    }


    public String getMaxCharacter()
    {
        _preferences = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        return _preferences.getString("maxCharacters", "0");
        //return  _maxCharacters;
    }


    public String getMaxTweets()
    {
        _preferences = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        return _preferences.getString("maxTweets", "0");
        //return _maxtweets;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s)
    {
        if (s.equals("userPass") || s.equals("userName"))
        {
            getTwitterClient().configureTwiiterClient(getUserName(), getPassword(), getApiRootUrl());
        }
    }
}
