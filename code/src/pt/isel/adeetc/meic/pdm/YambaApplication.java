package pt.isel.adeetc.meic.pdm;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.preference.PreferenceManager;
import android.util.Log;
import pt.isel.adeetc.meic.pdm.common.GenericMultipleEvent;
import pt.isel.adeetc.meic.pdm.common.IMultipleEvent;
import pt.isel.adeetc.meic.pdm.common.db.IDbSet;
import pt.isel.adeetc.meic.pdm.controllers.TimelineContentProviderClient;
import pt.isel.adeetc.meic.pdm.controllers.TwitterServiceClient;
import pt.isel.adeetc.meic.pdm.extensions.BaseApplication;
import pt.isel.adeetc.meic.pdm.services.IEmailSender;
import pt.isel.adeetc.meic.pdm.services.SimpleEmailSender;
import winterwell.jtwitter.Twitter;

public class YambaApplication extends BaseApplication
{
    private TwitterServiceClient _client;
    private SharedPreferences _preferences;
    private IDbSet<Twitter.ITweet> _tweetDb;
    private IEmailSender _emailSender;
    private boolean _networkState;
    private Handler _uiHandler, _asyncHandler;
    private HandlerThread _customThread;


    private IMultipleEvent<Boolean> _networkEvent;

    @Override
    public void onCreate()
    {
        super.onCreate();
        _preferences = PreferenceManager.getDefaultSharedPreferences(this);

        _networkEvent = new GenericMultipleEvent<Boolean>();

        //StatusDatabaseDataSource tweetDb = new StatusDatabaseDataSource(getContext());
        //tweetDb.open();
        _tweetDb = new TimelineContentProviderClient(getContext());
        _uiHandler = new Handler();

        _customThread = new HandlerThread("Custom Handler Thread");
        _customThread.start();

        _asyncHandler = new Handler(_customThread.getLooper());
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
            _client = new TwitterServiceClient(getUserName(), getPassword(), getApiRootUrl(), _tweetDb, this);
        }
        return _client;
    }

    public IEmailSender getEmailSender()
    {
        if (_emailSender == null)
        {
            _emailSender = new SimpleEmailSender(getContext());
        }
        return _emailSender;
    }

    public String getUserName()
    {

        return _preferences.getString(YambaPreferences.userNamePropName, "");
        //eturn "PDM14";
    }

    public String getPassword()
    {

        return _preferences.getString(YambaPreferences.userPasswordPropName, "");
        //return "pdm14_";
    }

    public String getApiRootUrl()
    {


        return _preferences.getString(YambaPreferences.apiBaseUrl, "http://yamba.marakana.com/api");
        //return "http://yamba.marakana.com/api";
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

    public boolean getNetworkState()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return wifiNetwork.isAvailable();

    }

    public IMultipleEvent<Boolean> getNetworkEvent()
    {
        return _networkEvent;
    }


    public Handler getUiHandler()
    {
        return _uiHandler;
    }

    public void initialize()
    {
        getTwitterClient();
    }

    public Handler getGeneralPurposeHandler()
    {
        return _asyncHandler;
    }


    public HandlerThread getCustomHandlerThread()
    {
        Log.d("YambaApplication","getCustomHandlerThread - " + (_customThread == null));
        return _customThread;
    }


}
