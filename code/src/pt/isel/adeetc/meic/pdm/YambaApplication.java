package pt.isel.adeetc.meic.pdm;

import android.app.Application;
import pt.isel.adeetc.meic.pdm.extensions.BaseApplication;
import pt.isel.adeetc.meic.pdm.services.TwitterServiceClient;

public class YambaApplication extends BaseApplication
{
    private TwitterServiceClient _client;
    public final String timelineToStatusDetailsParamName = "statusDetailsParam";

    public TwitterServiceClient getTwitterClient()
    {
        if (_client == null)
        {
            _client = new TwitterServiceClient();
            _client.configureTwiiterClient(getUserName(), getPassword(), getApiRootUrl());
        }
        return _client;
    }

    private String getUserName()
    {
        return "PDM14";
    }

    private String getPassword()
    {
        return "pdm14_";
    }

    private String getApiRootUrl()
    {
        return "http://yamba.marakana.com/api";
    }
}
