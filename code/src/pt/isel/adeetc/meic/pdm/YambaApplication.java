package pt.isel.adeetc.meic.pdm;

import android.app.Application;
import pt.isel.adeetc.meic.pdm.extensions.BaseApplication;
import pt.isel.adeetc.meic.pdm.services.TwitterServiceClient;

public class YambaApplication extends BaseApplication
{
    private TwitterServiceClient _client;
    private String _userName;
    private String _userPass;
    private String _serviceUrl;
    private int _maxCharacters;
    private int _maxtweets;
    
    
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
    
    private void setUserName(String name){
        _userName = name;
    }

    private String getPassword()
    {
        return "pdm14_";
    }

    
    private void setPassword(String pass){
        _userPass = pass;
    }
    private String getApiRootUrl()
    {
        return "http://yamba.marakana.com/api";
    }
    
    private void setApiRootUrl(String url){
        _serviceUrl = url;
    }
    
    private int getMaxCharacter(){
        return  _maxCharacters;
    }
    
    private void setMaxCharacter(int max){
        _maxCharacters=max;
    }
    
    private int getMaxTweets(){
        return _maxtweets;
    }

    private void setMaxTweets(int max){
        _maxtweets=max;
    }
    
}
