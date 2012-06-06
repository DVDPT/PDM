package pt.isel.adeetc.meic.pdm;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import pt.isel.adeetc.meic.pdm.db.YambaStatusDatabase;
import pt.isel.adeetc.meic.pdm.extensions.BaseApplication;
import pt.isel.adeetc.meic.pdm.services.TwitterServiceClient;

public class YambaApplication extends BaseApplication implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private TwitterServiceClient _client;
    private SharedPreferences _preferences;
    private YambaStatusDatabase _tweetDb;
    private boolean isNetwortUp;


    @Override
    public void onCreate()
    {
        super.onCreate();
        _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        _preferences.registerOnSharedPreferenceChangeListener(this);
        _tweetDb = new YambaStatusDatabase(getContext());
        _tweetDb.open();
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
        _tweetDb.close();
    }


    public TwitterServiceClient getTwitterClient()
    {
        if (_client == null)
        {
            _client = new TwitterServiceClient(_tweetDb);
            _client.configureTwitterClient(getUserName(), getPassword(), getApiRootUrl());
        }
        return _client;
    }

    public String getUserName()
    {

        //return _preferences.getString(YambaPreferences.userNamePropName, "");
        return "PDM14";
    }

    public String getPassword()
    {

        //return _preferences.getString(YambaPreferences.userPasswordPropName, "");
        return "pdm14_";
    }

    public String getApiRootUrl()
    {


        //return _preferences.getString(YambaPreferences.apiBaseUrl, "http://yamba.marakana.com/api");
        return "http://yamba.marakana.com/api";
    }


    public String getMaxCharacter()
    {

        return _preferences.getString(YambaPreferences.maxCharactersOnStatus, "100");

    }


    public int getStatusMaxCharactersShowedInTimeline()
    {
        return new Integer(_preferences.getString(YambaPreferences.maxCharactersOnStatusTimeline, "100"));
    }

    public int getMaxTweets()
    {
        return new Integer(_preferences.getString(YambaPreferences.maxStatusShownOnTimeline, "12"));

    }

    public boolean isTimelineRefreshedAutomatically()
    {
        return _preferences.getBoolean(YambaPreferences.timeLineFetchedAutomaticallyPropName, false);

    }

    public int getTimelineRefreshPeriod()
    {
        return new Integer(_preferences.getString(YambaPreferences.timelineRefreshPeriodPropName, "1"));
    }

    public int getNumberOfStatusPreserved()
    {
        return new Integer(_preferences.getString(YambaPreferences.numberOfStatusPreservedPropName, "10"));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s)
    {
        if (s.equals(YambaPreferences.userNamePropName) || s.equals(YambaPreferences.userPasswordPropName))
        {
            getTwitterClient().configureTwitterClient(getUserName(), getPassword(), getApiRootUrl());
        }
    }

    public void changeNetworkState(boolean state){
        isNetwortUp = state;
    }

    public boolean getNetworkState()
    {
        return isNetwortUp;
    }


}
