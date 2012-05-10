package pt.isel.adeetc.meic.pdm;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import pt.isel.adeetc.meic.pdm.extensions.BaseApplication;
import pt.isel.adeetc.meic.pdm.services.TwitterServiceClient;

public class YambaApplication extends BaseApplication implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private TwitterServiceClient _client;
    private SharedPreferences _preferences;


    public void onCreate()
    {

        _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        _preferences.registerOnSharedPreferenceChangeListener(this);
    }

    public final String timelineToStatusDetailsParamName = "statusDetailsParam";

    public TwitterServiceClient getTwitterClient()
    {
        if (_client == null)
        {
            _client = new TwitterServiceClient();

            _client.configureTwitterClient(getUserName(), getPassword(), getApiRootUrl());
        }
        return _client;
    }

    public String getUserName()
    {

        //return _preferences.getString("userName", "");
        return "PDM14";
    }

    public String getPassword()
    {

        //return _preferences.getString("userPass", "");
        return "pdmstudent";
    }

    public String getApiRootUrl()
    {


        //return _preferences.getString("baseUrl", "http://yamba.marakana.com/api");
        return "http://yamba.marakana.com/api";
    }


    public String getMaxCharacter()
    {

        return _preferences.getString("maxCharacters", "0");

    }


    public int getStatusMaxCharactersShowedInTimeline()
    {
        return new Integer(_preferences.getString("maxCharsInTimeline", "100"));
    }

    public int getMaxTweets()
    {
        return new Integer(_preferences.getString("maxTweets", "12"));

    }

    public boolean isTimelineRefreshedAutomatically()
    {
        return _preferences.getBoolean("timelineFetchedAutomaticallly",false);
    }

    public int getTimelineRefreshPeriod()
    {
        return _preferences.getInt("timelineRefreshPeriod",1);
    }

    public int getNumberOfStatusPreserved()
    {
        return _preferences.getInt("numberOfStatusPreserved",10);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s)
    {
        if (s.equals("userPass") || s.equals("userName"))
        {
            getTwitterClient().configureTwitterClient(getUserName(), getPassword(), getApiRootUrl());
        }
    }


}
