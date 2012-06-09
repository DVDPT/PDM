package pt.isel.adeetc.meic.pdm.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import pt.isel.adeetc.meic.pdm.YambaApplication;
import pt.isel.adeetc.meic.pdm.YambaNavigation;
import pt.isel.adeetc.meic.pdm.common.GenericEventArgs;
import pt.isel.adeetc.meic.pdm.common.IEventHandler;
import pt.isel.adeetc.meic.pdm.common.IEventHandlerArgs;
import pt.isel.adeetc.meic.pdm.exceptions.ShouldNotHappenException;
import pt.isel.adeetc.meic.pdm.extensions.BoundedService;
import pt.isel.adeetc.meic.pdm.extensions.BoundedServiceClient;
import pt.isel.adeetc.meic.pdm.services.UserInfoPullService;
import pt.isel.adeetc.meic.pdm.services.YambaUserInfo;

public class UserInfoServiceController extends BoundedServiceClient implements IEventHandler<IEventHandler<YambaUserInfo>>
{
    private static final String LOG = "UserInfoServiceController";

    private final YambaApplication _app;
    private final TwitterServiceClient _twitterFacade;


    public UserInfoServiceController(YambaApplication app, TwitterServiceClient cli)
    {
        _app = app;
        _twitterFacade = cli;

        cli.getUserInfoEvent.setOnEventHandlerChanged(this);
        setCallServiceWhenConnected(true);
    }

    public void getUserInfoAsync()
    {
        if (!isServiceConnected())
        {
            _app.bindService(getIntent(), getServiceConnection(), Context.BIND_AUTO_CREATE);
            return;
        }
        callService();
    }

    @Override
    public void invoke(Object sender, IEventHandlerArgs<IEventHandler<YambaUserInfo>> args)
    {
        try
        {
            if (args.getData() == null)
            {
                Log.d(LOG, "Unbinding");
                unbind(_app);
            }
        } catch (Exception e)
        {
            throw new ShouldNotHappenException(e);
        }
    }

    @Override
    protected void fillServiceRequest(Message obtain)
    {
    }

    @Override
    protected void onServiceResponse(Message message)
    {
        Log.d(LOG, "onServiceResponse");
        Exception error = null;
        ///
        /// ??????????????????????????????????????
        ///
        message.getData().setClassLoader(_app.getClassLoader());

        int val = message.getData().getInt(YambaNavigation.USER_INFO_SERVICE_RESULT_PARAM_NAME);

        if (val == BoundedService.SERVICE_RESPONSE_ERROR)
            error = (Exception) message.getData().getSerializable(YambaNavigation.USER_INFO_SERVICE_ERROR_PARAM_NAME);


        YambaUserInfo info = (YambaUserInfo) message.getData().get(YambaNavigation.USER_INFO_SERVICE_RESPONSE_PARAM_NAME);
        final IEventHandlerArgs<YambaUserInfo> data = new GenericEventArgs<YambaUserInfo>(info, error);
        _app.getUiHandler().post(new Runnable()
        {
            @Override
            public void run()
            {
                _twitterFacade.getUserInfoEvent.invoke(UserInfoServiceController.this, data);
            }
        });

    }

    public Intent getIntent()
    {
        return new Intent(_app, UserInfoPullService.class);
    }
}
