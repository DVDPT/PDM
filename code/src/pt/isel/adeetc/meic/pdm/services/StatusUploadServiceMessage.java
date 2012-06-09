package pt.isel.adeetc.meic.pdm.services;

import pt.isel.adeetc.meic.pdm.common.IEventHandler;
import pt.isel.adeetc.meic.pdm.common.db.IDbSet;
import winterwell.jtwitter.Twitter;

/**
 * Created by IntelliJ IDEA.
 * User: Sorcha
 * Date: 10-05-2012
 * Time: 18:07
 * To change this template use File | Settings | File Templates.
 */
public class StatusUploadServiceMessage implements IServiceMessage<String,Void>
{
    
    private final IEventHandler<Void> _callBack;
    private String _data;
    private IDbSet<String> _dataBase;

    public StatusUploadServiceMessage(IEventHandler<Void> _callBack, String status) {
        this._callBack = _callBack;
        _data = status;
        
    }
    
    @Override
    public String getData() {
          return _data;
    }

    @Override
    public IEventHandler<Void> getCallback() {
        return _callBack;
    }

    public IDbSet<String> getDatabase(){
        return _dataBase;
    }
}
