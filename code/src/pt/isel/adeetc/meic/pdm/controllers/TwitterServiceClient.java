package pt.isel.adeetc.meic.pdm.controllers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import org.apache.http.auth.AuthenticationException;
import pt.isel.adeetc.meic.pdm.YambaApplication;
import pt.isel.adeetc.meic.pdm.YambaPreferences;
import pt.isel.adeetc.meic.pdm.common.GenericEvent;
import pt.isel.adeetc.meic.pdm.common.IEvent;
import pt.isel.adeetc.meic.pdm.common.IEventHandler;
import pt.isel.adeetc.meic.pdm.common.IEventHandlerArgs;
import pt.isel.adeetc.meic.pdm.common.db.IDbSet;
import pt.isel.adeetc.meic.pdm.exceptions.Constants;
import pt.isel.adeetc.meic.pdm.exceptions.ShouldNotHappenException;
import pt.isel.adeetc.meic.pdm.services.StatusUploadService;
import pt.isel.adeetc.meic.pdm.services.YambaUserInfo;
import winterwell.jtwitter.Twitter;

public final class TwitterServiceClient implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private static final String AUTHENTICATION_ERROR_MESSAGE = "java.io.IOException: Received authentication challenge is null";
    private static final String LOG = "TwitterServiceClient";

    public final IEvent<Integer> updateStatusCompletedEvent;
    public final IEvent<Iterable<Twitter.ITweet>> getUserTimelineCompletedEvent;
    public final IEvent<YambaUserInfo> getUserInfoEvent;

    public final IEventHandler<Boolean> _publisherEvent;

    private Twitter _twitter;
    private final IDbSet<Twitter.ITweet> _tweetDb;
    private final YambaApplication _app;
    private final TimelineServiceController _timelineController;
    private final StatusServiceController _statusController;
    private final UserInfoServiceController _userInfoController;

    private final TimelineContentProviderClient _providerCli;


    public TwitterServiceClient(String userName, String password, String apiRootUrl, IDbSet<Twitter.ITweet> tweetDb, YambaApplication app)
    {
        _tweetDb = tweetDb;
        _app = app;

        updateStatusCompletedEvent = new GenericEvent<Integer>();
        getUserTimelineCompletedEvent = new GenericEvent<Iterable<Twitter.ITweet>>();
        getUserInfoEvent = new GenericEvent<YambaUserInfo>();

        _timelineController = new TimelineServiceController(this, _app.getGeneralPurposeHandler());
        _statusController = new StatusServiceController(_app, this);
        _userInfoController = new UserInfoServiceController(_app, this);
        _publisherEvent = new PublisherEventHandler();
        _providerCli = new TimelineContentProviderClient(_app);

        initialize(userName, password, apiRootUrl);
    }

    private void initialize(String userName, String password, String apiRootUrl)
    {
        configureTwitterClient(userName, password, apiRootUrl);

        if (_app.isTimelineRefreshedAutomatically())
            _timelineController.deployPeriodicAlarm();

        PreferenceManager.getDefaultSharedPreferences(_app).registerOnSharedPreferenceChangeListener(this);

        _app.getNetworkEvent().addEventHandler(_publisherEvent);
    }


    public void updateStatusAsync(String status)
    {
        _statusController.updateStatusAsync(status);
    }

    public void getUserTimelineAsync()
    {
        _timelineController.getUserTimelineAsync();
    }

    public Iterable<Twitter.ITweet> getTwitterCachedTimeline()
    {
        return _timelineController.getStatusCache();
    }

    public void getUserInfoAsync()
    {
        _userInfoController.getUserInfoAsync();
    }

    private void configureTwitterClient(String user, String password, String apiRootUrl)
    {
        Log.d(LOG, String.format("Configuring twitter, user: %s pass:%s", user, password));
        _twitter = new Twitter(user, password);
        _twitter.setAPIRootUrl(apiRootUrl);
    }

    public Twitter getTwitter()
    {
        if (_twitter == null)
            throw new ShouldNotHappenException(Constants.NoUserAndPasswordDefined);
        return _twitter;
    }

    IDbSet<Twitter.ITweet> getTweetDbSet()
    {
        return _tweetDb;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s)
    {
        Log.d(LOG, "onSharedPreferenceChanged: " + s);
        if (s.equals(YambaPreferences.userNamePropName) || s.equals(YambaPreferences.userPasswordPropName) || s.endsWith(YambaPreferences.apiBaseUrl))
        {
            _providerCli.clearAll();
            configureTwitterClient(_app.getUserName(), _app.getPassword(), _app.getApiRootUrl());
        } else if (s.equals(YambaPreferences.timelineRefreshPeriodPropName) || s.equals(YambaPreferences.timeLineFetchedAutomaticallyPropName))
        {
            if (_app.isTimelineRefreshedAutomatically())
            {
                if (s.equals(YambaPreferences.timelineRefreshPeriodPropName))
                {
                    _timelineController.cancelPeriodicAlarm();
                    _timelineController.deployPeriodicAlarm();
                } else
                {
                    _timelineController.deployPeriodicAlarm();
                }
            } else
            {
                _timelineController.cancelPeriodicAlarm();
            }

        }
    }

    private class PublisherEventHandler implements IEventHandler<Boolean>
    {

        @Override
        public void invoke(Object sender, IEventHandlerArgs<Boolean> args)
        {

            try
            {
                if (_app.getNetworkState())
                {
                    Intent statusIntent = new Intent(YambaApplication.getContext(), StatusUploadService.class);
                    YambaApplication.getContext().startService(statusIntent);
                    _timelineController.deployPeriodicAlarm();
                } else
                {

                    _timelineController.cancelPeriodicAlarm();
                }

            } catch (Exception e)
            {
                throw new ShouldNotHappenException(e);
            }
        }
    }

    public Exception checkIfIsAuthenticationExceptionAndReplace(Exception e)
    {
        Exception error = e;
        if (e.getMessage().equals(AUTHENTICATION_ERROR_MESSAGE))
            error = new AuthenticationException();
        return error;
    }
}

