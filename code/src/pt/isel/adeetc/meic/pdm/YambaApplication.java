package pt.isel.adeetc.meic.pdm;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import pt.isel.adeetc.meic.pdm.common.IEventHandler;
import pt.isel.adeetc.meic.pdm.common.IEventHandlerArgs;
import pt.isel.adeetc.meic.pdm.common.IEventReceives;
import pt.isel.adeetc.meic.pdm.common.db.IDbSet;
import pt.isel.adeetc.meic.pdm.exceptions.ShouldNotHappenException;
import pt.isel.adeetc.meic.pdm.extensions.BaseApplication;
import pt.isel.adeetc.meic.pdm.services.TimelineContentProviderClient;
import pt.isel.adeetc.meic.pdm.services.TwitterServiceClient;
import winterwell.jtwitter.Twitter;

import java.util.LinkedList;

public class YambaApplication extends BaseApplication implements SharedPreferences.OnSharedPreferenceChangeListener, IEventReceives<Boolean>
{
    private TwitterServiceClient _client;
    private SharedPreferences _preferences;
    private IDbSet<Twitter.ITweet> _tweetDb;
    private boolean _networkState;
    private LinkedList<IEventHandler> _handlers;
    private BroadcastReceiver _netReceive;


    @Override
    public void onCreate()
    {
        super.onCreate();
        _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        _preferences.registerOnSharedPreferenceChangeListener(this);
        _handlers = new LinkedList<IEventHandler>();

        //StatusDatabaseDataSource tweetDb = new StatusDatabaseDataSource(getContext());
        //tweetDb.open();
        _tweetDb = new TimelineContentProviderClient(getContext());
        _netReceive =  new NetworkReceiver();
        registerReceiver(_netReceive, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
        //_tweetDb.close();
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

    public void setNetworkState(boolean newState)
    {
        _networkState=newState;
    }

    public boolean getNetworkState()
    {
        return _networkState;
    }

    @Override
    public void invoke(Object sender, IEventHandlerArgs<Boolean> arg) {

        try {
            _networkState = arg.getData();
            for(IEventHandler ev : _handlers)
            {
                ev.invoke(sender, arg);
            }
        } catch (Exception e) {
           throw new ShouldNotHappenException();
        }

    }

    @Override
    public void removeEventHandler(IEventHandler event)
    {
        _handlers.remove(event);
    }

    @Override
    public void addEventHandler(IEventHandler event) {

        _handlers.add(event);
    }

    @Override
    public void onEventHandlerChanged(IEventHandler event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
