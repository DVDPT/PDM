package pt.isel.adeetc.meic.pdm;

import android.app.Application;
import pt.isel.adeetc.meic.pdm.services.TwitterServiceClient;

public class YambaApplication extends Application
{
    private TwitterServiceClient _client;


    public TwitterServiceClient getTwitterClient()
    {
        if (_client == null)
        {
            _client = new TwitterServiceClient();
            _client.configureTwiiterClient(getUserName(),getPassword(),getApiRootUrl());
        }
        return _client;
    }

    private String getUserName()
    {
        return "PDM14";
    }

    private String getPassword()
    {
        return "SOFIDIDI";
    }

    private String getApiRootUrl()
    {
        return "http://yamba.marakana.com/api";
    }
}
