package pt.isel.adeetc.meic.pdm.services;

import pt.isel.adeetc.meic.pdm.common.IEventHandler;
import winterwell.jtwitter.Twitter;

/**
 * Created by IntelliJ IDEA.
 * User: Sorcha
 * Date: 10-05-2012
 * Time: 18:07
 * To change this template use File | Settings | File Templates.
 */
public class StatusUploadServiceMessage implements IServiceMessage<String,Twitter.ITweet>
{
    
    private final IEventHandler<Twitter.ITweet> _callBack;
    private String _data;

    public StatusUploadServiceMessage(IEventHandler<Twitter.ITweet> _callBack, String status) {
        this._callBack = _callBack;
        _data = status;
        
    }
    
    @Override
    public String getData() {
          return _data;
    }

    @Override
    public IEventHandler<Twitter.ITweet> getCallback() {
        return _callBack;
    }
}
